import java.util.Comparator;

/**
 * COMP 2503 Winter 2020 Assignment 3 March 23, 2020
 * 
 * Comparator class to order wordsByLength. When called, returns an int to determine
 * sort order, first by longest length of word, then alphabetically by
 * word.
 * 
 * @author Salim Manji
 *
 **/

public class LengthDesc implements Comparator<Token>{

	/**
	 * Compares two Token objects to determine sort order, first by lengths 
	 * of the words, then by alphabetical ordering.
	 * 
	 * @param Two objects of type Token.
	 * @return +1 if token1 is greater than token2, -1 if token1 is less than
	 *         token2.
	 * 
	 */
	public int compare(Token token1, Token token2) {
		int result = token2.getLength() - token1.getLength();

		if (result == 0)
			result = token1.getWord().compareTo(token2.getWord());

		return result;
	}

}
