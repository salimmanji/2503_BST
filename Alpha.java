import java.util.Comparator;

/**
 * COMP 2503 Winter 2020 Assignment 3 March 23, 2020
 * 
 * Comparator class to order wordlist. When called, returns which word appears
 * first alphabetically.
 * 
 * @author Salim Manji
 * @param <T> Generic type to compare.
 *
 **/

public class Alpha implements Comparator<Token> {

	/**
	 * Compares two Token objects to determine sort order according to alphabetical
	 * ordering.
	 * 
	 * @param Two objects of type Token.
	 * @return +1 if token1 is greater than token2, -1 if token1 is less than
	 *         token2.
	 * 
	 */
	public int compare(Token token1, Token token2) {

		return token1.getWord().compareTo(token2.getWord());
	}

}
