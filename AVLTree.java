public class AVLTree<T extends Comparable<? super T>> extends BST<T> {
	
   protected int height;
	
	public AVLTree() {
		super();
		height = -1;
	}
	
	public AVLTree(BSTNode<T> root) {
		super(root);
		height = -1;
	}
	
	public int getHeight() {
		return getHeight(root);
	}
	
	private int getHeight(BSTNode<T> node) {
      if(node == null)
         return -1;
      else
         return 1 + Math.max(getHeight(node.left), getHeight(node.right));
   }
	
   private AVLTree<T> getLeftAVL() {
      AVLTree<T> leftsubtree = new AVLTree<T>(root.left);
      return leftsubtree;
   }

   private AVLTree<T> getRightAVL() {
      AVLTree<T> rightsubtree = new AVLTree<T>(root.right);
      return rightsubtree;
    }
    
	protected int getBalanceFactor() {
      if(isEmpty())
         return 0;
      else
         return getRightAVL().getHeight() - getLeftAVL().getHeight();
    }
    
    public void insertAVL(T el)  {
      super.insert(el);
      this.balance();
    }
    
    public void deleteAVL(T el) {
        if (isEmpty()) {
            System.out.println("The tree is empty. Cannot delete.");
            return;
        }

        // Perform the standard deletion operation
        super.deleteByCopying(el);

        // Rebalanced the tree after deletion
        balance();
    }
    
    protected void balance()
    {
      if(!isEmpty())
      {
         getLeftAVL().balance();
    	   getRightAVL().balance();

         adjustHeight();
        
         int balanceFactor = getBalanceFactor();
        
         if(balanceFactor == -2) {
			   System.out.println("Balancing node with el: "+root.el);
            if(getLeftAVL().getBalanceFactor() < 0)
			      rotateRight();
            else
               rotateLeftRight();
         }
		
         else if(balanceFactor == 2) {
            System.out.println("Balancing node with el: "+root.el);
            if(getRightAVL().getBalanceFactor() > 0)
               rotateLeft();
            else
               rotateRightLeft();
         }
      }
   }
    
   protected void adjustHeight()
   {
      if(isEmpty())
         height = -1;
      else
         height = 1 + Math.max(getLeftAVL().getHeight(), getRightAVL().getHeight());   
   }
    
   protected void rotateRight() {
       System.out.println("RIGHT ROTATION");

       // Store a reference to the right subtree
       BSTNode<T> tempNode = root.right;

       // Update the root's right child to be its left child
       root.right = root.left;

       // Update the root's left child to be the left child's right child
       root.left = root.right.left;

       // Update the left child's right child to be the right subtree
       root.right.left = root.right.right;

       // Set the right subtree to the original left child
       root.right.right = tempNode;

       // Swap the element values between the root and its new right child
       T val = root.el;
       root.el = root.right.el;
       root.right.el = val;

       // Adjust the height of the right subtree
       getRightAVL().adjustHeight();

       // Adjust the overall height of the tree
       adjustHeight();
   }

    
   protected void rotateLeft() {
 	   //System.out.println("LEFT ROTATION");
      BSTNode<T> tempNode = root.left;
      root.left = root.right;
      root.right = root.left.right;
      root.left.right = root.left.left;
      root.left.left = tempNode;
            
      T val = (T) root.el;
      root.el = root.left.el;
      root.left.el = val;
            
      getLeftAVL().adjustHeight();
      adjustHeight();
	}
	
	protected void rotateLeftRight()
   {
       System.out.println("Double Rotation (Left-Right)...");

       // First, perform a left rotation on the left subtree
       getLeftAVL().rotateLeft();
       getLeftAVL().adjustHeight();

       // Then, perform a right rotation on the tree itself
       rotateRight();
       adjustHeight();
  }

   protected void rotateRightLeft()
   {
		System.out.println("Double Rotation...");
      getRightAVL().rotateRight();
      getRightAVL().adjustHeight();
      this.rotateLeft();
      this.adjustHeight();
   }
    
}