/**
 * COMP 2503 Winter 2020 Assignment 3 March 23, 2020
 * 
 *  Creates an object that implements the Visit method.
 *  The traversal methods in BST take an object of 
 *  type Visit and invoke the visit() method. 
 * 
 * @author Salim Manji
 *
 **/

public interface Visit<T> {
	
   public void visit(T t);
   
}
