import java.util.Iterator;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;

/**
 * COMP 2503 Winter 2020 Assignment 3 March 23, 2020
 * 
 * BST Class to instantiate binary search tree (BST). Provides logic for how to
 * add, delete and find a Node into the BST. This class extends the comparable
 * interface and implements the iterable interface.
 * 
 * @author Salim Manji
 *
 */

public class BST<T extends Comparable<T>> implements Iterable<T>, Visit<T> {

	private BSTNode root;
	private int size;
	private Comparator<T> comparator;
	private Deque<T> itDeque;

	public BST() {
		size = 0;
		root = null;
		comparator = null;
	}

	/**
	 * Overloaded constructor to create a new BST with a comparator object.
	 * 
	 * @param c comparator to use.
	 */
	public BST(Comparator<T> c) {
		size = 0;
		root = null;
		comparator = c;
	}

	public int size() {
		return size;
	}

	@Override
	/**
	 * Call to construct a new iterator object.
	 */
	public Iterator<T> iterator() {
		return new BSTIterator();
	}

	/**
	 * Call to find the object with the smallest value.
	 * 
	 * @return the object with the smallest value.
	 */
	public T min() {
		BSTNode min = minFinder(root);
		return min.getData();
	}

	/**
	 * Call to find a specific object in a BST.
	 * 
	 * @param toFind the object to find.
	 * @return the BSTNode containing the object to find.
	 */
	public T find(T toFind) {
		BSTNode nodeToFind = new BSTNode(toFind);
		T foundToken = null;

		BSTNode found = find(root, nodeToFind);
		if (found != null) {
			foundToken = found.getData();
		}

		return foundToken;
	}

	/**
	 * Call to add an object to a BST.
	 * 
	 * @param toAdd the object to add to the BST.
	 */
	public void add(T toAdd) {
		size++;
		BSTNode nodeToAdd = new BSTNode(toAdd);
		if (root == null) {
			root = nodeToAdd;
		} else {
			add(root, nodeToAdd);
		}
	}

	/**
	 * Call to delete a specific object from the list.
	 * 
	 * @param toDelete the object to delete.
	 */
	public void delete(T toDelete) {
		BSTNode nodeToDelete = new BSTNode(toDelete);
		delete(root, nodeToDelete);
		size--;
	}

	/**
	 * Call to determine the height of a tree.
	 * 
	 * @return an integer describing the tree height.
	 */
	public int height() {
		return height(root);
	}

	/**
	 * Finds a specific node in a tree.
	 * 
	 * @param toFind the data to find.
	 * @param root   the node to examine.
	 * @return if the node is found, that node is returned, else a null value is
	 *         returned.
	 */
	private BSTNode find(BSTNode root, BSTNode toFind) {
		int position = 0;
		if (root == null)
			return null;

		if (comparator == null) {
			position = getPositionNoComp(toFind, root);
		} else {
			position = getPosition(toFind, root);
		}

		if (position == 0) {
			return root;
		} else if (position < 0) {
			return find(root.getLeft(), toFind);
		} else {
			return find(root.getRight(), toFind);
		}
	}

	/**
	 * Detailed instructions on how to add a node to a BST based on a comparator
	 * value.
	 * 
	 * @param root  current node being compared against.
	 * @param toAdd node to add to the BST.
	 */
	private void add(BSTNode root, BSTNode toAdd) {
		int position = 0;

		if (comparator == null) {
			position = getPositionNoComp(toAdd, root);
		} else {
			position = getPosition(toAdd, root);
		}

		if (position < 0) {
			if (root.getLeft() == null) {
				root.setLeft(toAdd);
			} else {
				add(root.getLeft(), toAdd);
			}
		} else {
			if (root.getRight() == null) {
				root.setRight(toAdd);
			} else {
				add(root.getRight(), toAdd);
			}
		}
	}

	/**
	 * Determines the height of a tree.
	 * 
	 * @param root the node to examine.
	 * @return an int describing the height of a BST.
	 */
	private int height(BSTNode root) {

		if (root == null) {
			return -1;
		} else {
			int lHeight = height(root.getLeft());
			int rHeight = height(root.getRight());
			return 1 + Math.max(lHeight, rHeight);
		}
	}

	/**
	 * Deletes a given node, once found.
	 * 
	 * @param root     the node to examine.
	 * @param toDelete the node to delete.
	 * @return
	 */
	private BSTNode delete(BSTNode root, BSTNode toDelete) {
		int position = 0;

		if (root == null)
			return root;

		if (comparator == null) {
			position = getPositionNoComp(toDelete, root);
		} else {
			position = getPosition(toDelete, root);
		}

		if (position < 0) {
			root.setLeft(delete(root.getLeft(), toDelete));
		} else if (position > 0) {
			root.setRight(delete(root.getRight(), toDelete));
		} else {

			if (root.isLeaf()) {
				root = null;
			} else if (root.hasOneChild()) {
				if (root.hasOnlyLeft()) {
					return root.getLeft();
				} else if (root.hasOnlyRight()) {
					return root.getRight();
				}
			} else {
				BSTNode min = minFinder(root.getRight());
				root.setData(min.getData());
				root.setRight(delete(root.getRight(), min));
			}
		}
		return root;
	}

	/**
	 * Call to the inOrder method to instruct an iterator on how to traverse a list,
	 * and what to do for each node.
	 * 
	 * @param root            the node being visited.
	 * @param iteratorVisitor instructions on how to visit a node.
	 */
	public void traverse(BSTNode root, Visit<T> iteratorVisitor) {
		inOrder(root, iteratorVisitor);
	}

	@Override
	/**
	 * Implementation of the visit abstract class; instructions on what to do when a
	 * node is visited by the iterator.
	 */
	public void visit(T t) {
		return;
	}

	/**
	 * Finds the minimum node in a BST.
	 * 
	 * @param root the node to examine.
	 * @return the minimum node.
	 */
	private BSTNode minFinder(BSTNode root) {
		if (root.getLeft() != null) {
			root = minFinder(root.getLeft());
		}
		return root;
	}

	/**
	 * Instructions on how to traverse a given BST in-order.
	 * 
	 * @param root    the current node from which to traverse the list.
	 * @param visitor a specific set of instructions (of the Visit class) to
	 *                complete whenever a node is being visited.
	 */
	private void inOrder(BSTNode root, Visit<T> visitor) {
		if (root != null) {
			inOrder(root.getLeft(), visitor);
			visitor.visit(root.getData());
			inOrder(root.getRight(), visitor);
		}
	}

	/**
	 * Assists in determining where to add and how to find a Node in the BST.
	 * 
	 * @param toAdd      The new Node to add when an BST is instantiated using the
	 *                   overloaded constructor.
	 * @param toPosition Node to determine the position of.
	 * @param root       current Node to compare to.
	 * @return An int that alerts the add/find methods if the iterator needs to
	 *         iterate further through the BST.
	 */
	private int getPosition(BSTNode toPosition, BSTNode root) {
		return comparator.compare(toPosition.getData(), root.getData());
	}

	/**
	 * Assists in determining where to add and how to find a Node in the BST.
	 * 
	 * @param toAdd      The new Node to add when an BST is instantiated using the
	 *                   default constructor.
	 * @param toPosition Node to determine the position of.
	 * @param root       current Node to compare to.
	 * @return An int that alerts the add/find methods if the iterator needs to
	 *         iterate further through the BST.
	 * 
	 */
	private int getPositionNoComp(BSTNode toPosition, BSTNode root) {
		return toPosition.getData().compareTo(root.getData());
	}

	private class BSTIterator implements Iterator<T> {
		public BSTIterator() {
			itDeque = new ArrayDeque<T>();
			traverse(root, new IteratorVisit());
		}

		public boolean hasNext() {
			return itDeque.size() > 0;
		}

		public T next() {
			return itDeque.pollFirst();
		}

	}

	private class IteratorVisit implements Visit<T> {
		/**
		 * Implementation of the Visit abstract class for use when iterating a BST.
		 */
		@Override
		public void visit(T t) {
			if (t != null)
				itDeque.addLast(t);
		}

	}

	private class BSTNode implements Comparable<BSTNode> {
		/**
		 * Private class for BSTNodes.
		 */

		private T data;
		private BSTNode left;
		private BSTNode right;

		public BSTNode(T newToken) {
			setLeft(null);
			setRight(null);
			setData(newToken);
		}

		public T getData() {
			return data;
		}

		public void setData(T newToken) {
			data = newToken;
		}

		public void setLeft(BSTNode left) {
			this.left = left;
		}

		public void setRight(BSTNode right) {
			this.right = right;
		}

		public BSTNode getLeft() {
			return left;
		}

		public BSTNode getRight() {
			return right;
		}

		/**
		 * Determines if a BSTNode is a leaf (has no children)
		 * 
		 * @return true if BSTNode is a leaf, else false.
		 */
		public boolean isLeaf() {
			return !hasLeft() && !hasRight();
		}

		/**
		 * Determines if a node has only one child.
		 * 
		 * @return true if only one child, false if node has 0 or 2 children.
		 */
		public boolean hasOneChild() {
			return (hasOnlyLeft() || hasOnlyRight());
		}

		/**
		 * Determines if BSTNode has only a left child.
		 * 
		 * @return true if the node only has a left child, false otherwise.
		 */
		public boolean hasOnlyLeft() {
			return hasLeft() && !hasRight();
		}

		/**
		 * Determines if a node only has a right child.
		 * 
		 * @return true if the node only has a right child, false otherwise.
		 */
		public boolean hasOnlyRight() {
			return !hasLeft() & hasRight();
		}

		/**
		 * Determines where to add the new BSTNode in the tree.
		 * 
		 * @param other BSTNode to compare against.
		 * @return An int that alerts the caller if the iterator needs to move left or
		 *         right down the tree.
		 */
		public int compareTo(BSTNode other) {
			return this.getData().compareTo(other.getData());
		}

		/**
		 * Determines if the node has a left child.
		 * 
		 * @return true if node has a left child, false otherwise.
		 */
		public boolean hasLeft() {
			return left != null;
		}

		/**
		 * Determines if the node has a right child.
		 * 
		 * @return true if node has a right child, false otherwise.
		 */
		public boolean hasRight() {
			return right != null;
		}
	}

}
