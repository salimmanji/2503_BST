import java.util.Comparator;

/**
 * COMP 2503 Winter 2020 Assignment 3 March 23, 2020
 * 
 * Comparator class to order wordsByFreqDesc. When called, returns an int to
 * determine sort order, first by lowest frequency to highest, then
 * alphabetically by word.
 * 
 * @author Salim Manji
 *
 **/

public class FreqDesc implements Comparator<Token> {

	/**
	 * Compares two Token objects to determine sort order, first by highest
	 * frequency of the word count, then by alphabetical ordering. 
	 * 
	 * @param Two objects of type Token.
	 * @return +1 if token1 is greater than token2, -1 if token1 is less than
	 *         token2.
	 * 
	 */
	public int compare(Token token1, Token token2) {
		int result = token2.getFrequency() - token1.getFrequency();

		if (result == 0)
			result = token1.getWord().compareTo(token2.getWord());

		return result;
	}
	
}
