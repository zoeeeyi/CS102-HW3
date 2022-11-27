//This code was adopted from Data Structures and Algorithms in Java / Edition 2 by Robert Lafore
// tree.java
// demonstrates binary search tree

//HW3 QUESTION: provide the implementation of the methods below + TEST all your methods in the main by using the menu in the main (see main method)
//Make sure your code works (either compiled in command line (terminal) or in Eclipse. 


import java.io.*;
import java.util.*;               // for Stack class if needed
////////////////////////////////////////////////////////////////
class Node
{
   public int iData;              // data item (key)
   public double dData;           // data item
   public Node leftChild;         // this node's left child
   public Node rightChild;        // this node's right child
}  // end class Node  

class Tree
{
   //#region Tree Variables
   private Node root;             // first node of tree
   public Node getRoot() {return root;}
   //#endregion

   //#region Tree Constructor
   public Tree()                  // constructor
   { root = null; }            // no nodes in tree yet
   //#endregion
   
   //#region Tree Methods
   public Node find(int key)      // find node with given key
   {                           // (assumes non-empty tree)
      Node current = root;               // start at root
      while(current.iData != key)        // while no match,
         {
            if(key < current.iData) current = current.leftChild; // go left
            else current = current.rightChild; //go right
            if(current == null) return null; //no result
         }
      return current;     
   }

   public void insert(int id, double dd) //this method inserts a node of (id and dd) into the tree. (We are consider a BINARY SEARCH TREE by iData)
   {
      Node newNode = new Node();    // make new node
      newNode.iData = id;           
      newNode.dData = dd;

      if(root==null) root = newNode;
      else 
      {
         Node current = root;
         Node parent;
         while(true)
         {
            parent = current;
            if(id < current.iData)  // go left?
            {
               current = current.leftChild;
               if(current == null)
               {                 // insert on left
                  parent.leftChild = newNode;
                  return;
               }
            }  // end if go left
            else                    // or go right?
            {
               current = current.rightChild;
               if(current == null)  // if end of the line
               {                 // insert on right
                  parent.rightChild = newNode;
                  return;
               }
            }  // end else go right
         }  // end while
      }  // end else not root
   }

   public void traverse(int traverseType) //this method is full implemented see below 
   {
      switch(traverseType)
      {
         case 1: System.out.print("\nPreorder traversal: ");
            preOrder(root);
            break;
         case 2: System.out.print("\nInorder traversal:  ");
            inOrder(root);
            break;
         case 3: System.out.print("\nPostorder traversal: ");
            postOrder(root);
            break;
      }
      System.out.println();
   }

   private void preOrder(Node localRoot) //implement preOrder traversal
   {
      if(localRoot != null)
      {
         System.out.print(localRoot.iData + " ");
         preOrder(localRoot.leftChild);
         preOrder(localRoot.rightChild);
      }
   }
   
   private void inOrder(Node localRoot) //implement in Order traversal
   {
      if(localRoot != null)
      {
         inOrder(localRoot.leftChild);
         System.out.print(localRoot.iData + " ");
         inOrder(localRoot.rightChild);
      }
   }

   private void postOrder(Node localRoot) //implement postOrder traversal
   {
      if(localRoot != null)
      {
         postOrder(localRoot.leftChild);
         postOrder(localRoot.rightChild);
         System.out.print(localRoot.iData + " ");
      }
   }

   private boolean BSTchecker(Node localRoot, int low, int high)
   {
      if(localRoot == null) return true;
      
      //check min max constraint
      if(((localRoot.iData > low) && (localRoot.iData < high))) return (BSTchecker(localRoot.leftChild, low, localRoot.iData) && BSTchecker(localRoot.rightChild, localRoot.iData, high));
      else return false;
   }

   public void isBST(Node localRoot)
   {
      if(BSTchecker(localRoot, Integer.MIN_VALUE, Integer.MAX_VALUE)) System.out.println("It's BST.");
      else System.out.println("It's not BST.");
   }

   private Node getSuccessor(Node delNode)
   {
      Node successorParent = delNode;
      Node successor = delNode;
      Node current = delNode.rightChild;   // go to right child
      while(current != null)               // until no more
      {                                 // left children,
         successorParent = successor;
         successor = current;
         current = current.leftChild;      // go to left child
      }
      // if successor not
      if(successor != delNode.rightChild)  // right child,
      {                                 // make connections
         successorParent.leftChild = successor.rightChild;
         successor.rightChild = delNode.rightChild;
      }
      return successor;
   }

   public boolean delete(int key)
   {
      Node current = root;
      Node parent = root;
      
      //#region Find Node
      // search for node, same algorithm as Find(*). The only extra thing is we check if it's left child here.
      boolean isLeftChild = true;
      while(current.iData != key)
      {
         parent = current;
         if(key < current.iData)         // go left?
         {
            isLeftChild = true;
            current = current.leftChild;
         }
         else                            // or go right?
         {
            isLeftChild = false;
            current = current.rightChild;
         }
         if(current == null) return false;
      }
      //#endregion

      // if no children, delete it
      if(current.leftChild == null && current.rightChild == null)
      {
         if(current == root) root = null;                 // tree is empty
         else if(isLeftChild) parent.leftChild = null;     // disconnect
         else parent.rightChild = null;
      }
      //if no right child, replace with left subtree
      else if (current.rightChild==null)
      {
         if(current == root)
            root = current.leftChild;
         else if(isLeftChild)
            parent.leftChild = current.leftChild;
         else
            parent.rightChild = current.leftChild;
      }
      // if no left child, replace with right subtree
      else if(current.leftChild==null)
      {
         if(current == root)
            root = current.rightChild;
         else if(isLeftChild)
            parent.leftChild = current.rightChild;
         else
            parent.rightChild = current.rightChild;
      }
      // two children, replace with inorder successor
      else
      {
         // get successor of node to delete (current)
         Node successor = getSuccessor(current);

         // connect parent of current to successor instead
         if(current == root) root = successor;
         else if(isLeftChild) parent.leftChild = successor;
         else parent.rightChild = successor;

         // connect successor to current's left child
         successor.leftChild = current.leftChild;
      }
      // (successor cannot have a left child)
      return true;
   }

   public void displayTreeLevels() // this method will display the nodes at each level in the tree.
   {
      Stack globalStack = new Stack();
      globalStack.push(root);
      int nBlanks = 32;
      boolean isRowEmpty = false;
      System.out.println("......................................................");
      while(isRowEmpty==false)
      {
         Stack localStack = new Stack();
         isRowEmpty = true;

         for(int j=0; j<nBlanks; j++) System.out.print(' ');

         while(globalStack.isEmpty()==false)
         {
            Node temp = (Node)globalStack.pop();
            if(temp != null)
            {
               System.out.print(temp.iData);
               localStack.push(temp.leftChild);
               localStack.push(temp.rightChild);

               if(temp.leftChild != null || temp.rightChild != null) isRowEmpty = false;
            }
            else
            {
               System.out.print("--");
               localStack.push(null);
               localStack.push(null);
            }
            for(int j=0; j<nBlanks*2-2; j++) System.out.print(' ');
         }
         System.out.println();
         nBlanks /= 2;
         while(localStack.isEmpty()==false) globalStack.push(localStack.pop());
      }  // end while isRowEmpty is false
      System.out.println("......................................................");
   }

   public void displaymyChilds(int id, double dd) //given a node who idata is id and dd is ddata display it childen in the following way:
   {
      Node parent = find(id);
      if (parent.dData != dd) return;

      //Display children
      if (parent.leftChild == null && parent.rightChild == null){
         System.out.println("Do not have children");
         return;
      }

      //Left child: idata:  dData: 
      if (parent.leftChild != null) System.out.println("Left child: " + "iData: " + parent.leftChild.iData + ", dData: " + parent.leftChild.dData);
      else System.out.println("Do not have left child");

      //Right child: idata: dData:
      if (parent.rightChild != null) System.out.println("Right child: " + "iData: " + parent.rightChild.iData + ", dData: " + parent.rightChild.dData);
      else System.out.println("Do not have right child");
   }

   public void displayLeaves(Node localRoot) //this method will display all the leaves (iData and dData) of all the leaves)
   {
      if (localRoot == null) return;
   
      if (localRoot.leftChild == null && localRoot.rightChild == null)
      {
         System.out.println("Leaf: " + "iData: " + localRoot.iData + ", dData: " + localRoot.dData);
         return;
      }
      
      if (localRoot.leftChild != null) displayLeaves(localRoot.leftChild);
      if (localRoot.rightChild != null) displayLeaves(localRoot.rightChild);
   }
}  // end class Tree


////////////////////////////////////////////////////////////////
class HW3Trees
{
   public static void main(String[] args) throws IOException
   {
      int value;
      Tree theTree = new Tree();

      //#region Create a new tree
      theTree.insert(50, 1.5);
      theTree.insert(25, 1.2);
      theTree.insert(75, 1.7);
      theTree.insert(12, 1.5);
      theTree.insert(37, 1.2);
      theTree.insert(43, 1.7);
      theTree.insert(30, 1.5);
      theTree.insert(33, 1.2);
      theTree.insert(87, 1.7);
      theTree.insert(93, 1.5);
      theTree.insert(97, 1.5);  
      //#endregion

      //#region Menu
      //1. Traverse
      theTree.traverse(1);
      theTree.traverse(2);
      theTree.traverse(3);

      //2. isBST
      theTree.isBST(theTree.getRoot());

      //3. Delete
      theTree.delete(12);

      //4. Display Tree by levels
      theTree.displayTreeLevels();

      //5. Display my childs
      theTree.displaymyChilds(37, 1.2);

      //6. Insert a new Node
      theTree.insert(100, 0.5);

      //7. Display all the leaves
      theTree.displayLeaves(theTree.getRoot());
      //#endregion

   }// end class TreeApp
}