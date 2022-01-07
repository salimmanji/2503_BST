
import java.util.Scanner;
import java.util.Iterator;

/**
 * COMP 2503 Winter 2020 Assignment 3 March 23, 2020
 * 
 * This program reads a text file and compiles a binary search tree of Tokens
 * that contain the word read in and the frequency of each word's occurrence. If
 * the word added is included in the list of stop words, it is deleted. Two
 * additional lists are populated based on alternative ordering (by descending
 * frequency and by word length), then printed.
 * 
 * @author Salim Manji
 */

public class A3 {
	private static final int NUM_WORDS_TO_PRINT = 10;

	private BST<Token> wordsByNaturalOrder = new BST<Token>();
	private BST<Token> wordsByFreqDesc = new BST<Token>(new FreqDesc());
	private BST<Token> wordsByLength = new BST<Token>(new LengthDesc());

	private String[] stopWords = { "a", "about", "all", "am", "an", "and", "any", "are", "as", "at", "be", "been",
			"but", "by", "can", "cannot", "could", "did", "do", "does", "else", "for", "from", "get", "got", "had",
			"has", "have", "he", "her", "hers", "him", "his", "how", "i", "if", "in", "into", "is", "it", "its", "like",
			"more", "me", "my", "no", "now", "not", "of", "on", "one", "or", "our", "out", "said", "say", "says", "she",
			"so", "some", "than", "that", "thats", "the", "their", "them", "then", "there", "these", "they", "this",
			"to", "too", "us", "upon", "was", "we", "were", "what", "with", "when", "where", "which", "while", "who",
			"whom", "why", "will", "you", "your", "up", "down", "left", "right", "man", "woman", "would", "should",
			"dont", "after", "before", "im", "men" };

	private int totalWordCount = 0;
	private int stopWordCount = 0;

	private Scanner input = new Scanner(System.in);

	public static void main(String[] args) {
		A3 a3 = new A3();
		a3.run();
	}

	/**
	 * Runs the program.
	 */
	private void run() {
		readFile();
		removeStop();
		createFreqLists();
		printResults();
	}

	/**
	 * Instructions to determine what information to output to a .txt file.
	 */
	private void printResults() {
		int numUniqueWords = wordsByNaturalOrder.size();
		int loopEnd = Math.min(NUM_WORDS_TO_PRINT, numUniqueWords);

		System.out.println("Total Words: " + totalWordCount);
		System.out.println("Unique Words: " + wordsByNaturalOrder.size());
		System.out.println("Stop Words: " + stopWordCount);
		System.out.println();
		System.out.println("10 Most Frequent");
		printQuery(wordsByFreqDesc, loopEnd);
		System.out.println();

		System.out.println("10 Longest");
		printQuery(wordsByLength, loopEnd);
		System.out.println();
		try {
			System.out.println("The longest word is " + wordsByLength.min());
		} catch (NullPointerException e) {
			System.out.println("The longest word is null");
		}
		
		try {
			System.out.println("The average word length is " + avgLength());
		} catch (Exception e) {
			System.out.println("The average word length is 0");
		}
		
		System.out.println();

		System.out.println("All");
		printQuery(wordsByNaturalOrder, numUniqueWords);
		System.out.println();

		System.out.println("Alphabetic Tree: (Optimum Height: " + optHeight(wordsByNaturalOrder.size())
				+ ") (Actual Height: " + wordsByNaturalOrder.height() + ")");
		System.out.println("Frequency Tree: (Optimum Height: " + optHeight(wordsByFreqDesc.size())
				+ ") (Actual Height: " + wordsByFreqDesc.height() + ")");
		System.out.println("Length Tree: (Optimum Height: " + optHeight(wordsByLength.size()) + ") (Actual Height: "
				+ wordsByLength.height() + ")");
	}

	/*
	 * Reads the file and add words to the tree.
	 */
	private void readFile() {
		Scanner input = new Scanner(System.in);
		boolean stopWordStatus = false;

		while (input.hasNext()) {
			String currentWord = getWord(input);

			if (isString(currentWord)) {
				totalWordCount++;
				if (isStopWord(currentWord)) {
					stopWordStatus = true;
				}
				Token currentToken = new Token(currentWord, stopWordStatus);
				if (!isUnique(currentToken)) {
					increaseTally(currentToken);
				} else {
					if (currentToken.getStop()) {
						stopWordCount++;
					}
					wordsByNaturalOrder.add(currentToken);
				}
			}
			stopWordStatus = false;
		}
		input.close();
	}

	/**
	 * Retrieves the boolean from a Token that flags stop word status.
	 * 
	 * @param toCheck is the Token to check the status of.
	 * @return true if the boolean held by the Token is set to true, else false.
	 */
	private boolean isStop(Token toCheck) {
		return toCheck.getStop();
	}

	/**
	 * Pulls the next word scanned from the input.txt file.
	 * 
	 * @return The word to be scanned.
	 */
	private String getWord(Scanner input) {
		return input.next().trim().toLowerCase().replaceAll("[^a-z]", "");
	}

	/**
	 * Populates the wordsByFreqDesc and wordsByLength trees.
	 */
	private void createFreqLists() {
		populateFreq(wordsByFreqDesc);
		populate(wordsByLength);
	}

	/**
	 * Cycles through the wordsByNaturalOrder BST and adds any word with a frequency
	 * greater than 2.
	 * 
	 * @param toPopulate the BST to populate.
	 */
	private void populateFreq(BST<Token> toPopulate) {
		if (wordsByNaturalOrder.size() > 0) {
			Iterator<Token> it = wordsByNaturalOrder.iterator();
			do {
				Token toAdd = it.next();
				if (toAdd.hasMinFreq()) {
					toPopulate.add(toAdd);
				}
			} while (it.hasNext());
		}
	}


	/**
	 * Using the wordsByNaturalOrder tree, an iterator cycles through each node in
	 * the tree and adds it to the list toPopulate.
	 * 
	 * @param toPopulate is the list to be populated.
	 */
	private void populate(BST<Token> toPopulate) {
		if (wordsByNaturalOrder.size() > 0) {
			Iterator<Token> it = wordsByNaturalOrder.iterator();
			do {
				toPopulate.add(it.next());
			} while (it.hasNext());
		}
	}

	/**
	 * Finds the average length of words stored by the wordsByNaturalOrder tree by
	 * dividing the total characters of all words in the BST by the total number of
	 * words.
	 * 
	 * @return the average length of all words in the BST.
	 */
	private int avgLength() {
		int words = 0;
		int chars = 0;
		if (wordsByNaturalOrder.size() > 0) {
			Iterator<Token> it = wordsByNaturalOrder.iterator();
			do {
				Token toCount = it.next();
				words++;
				chars += toCount.getLength();
			} while (it.hasNext());
		}
		return chars / words;
	}


	/**
	 * When called, this method removes all the stop words from the
	 * wordsByNaturalOrder tree.
	 */
	private void removeStop() {
		Iterator<Token> it = wordsByNaturalOrder.iterator();

		while (it.hasNext()) {
			Token curr = it.next();
			if (isStop(curr)) {
				wordsByNaturalOrder.delete(curr);
			}
		}
	}

	/**
	 * Calculate the optimal height for a tree of size n. Truncated to an integer
	 * value.
	 * 
	 * @param n the size of the current list to determine optimal height.
	 * @return the optimal height of a list with n nodes.
	 */
	private int optHeight(int n) {
		double h = Math.log(n + 1) / Math.log(2) - 1;

		if (Math.round(h) < h)
			return (int) Math.round(h) + 1;
		else
			return (int) Math.round(h);
	}

	/**
	 * Determines if the String read in by the file reader contains a value, or if
	 * it is empty.
	 * 
	 * @param curr is the current word being read in by the file reader that needs
	 *             to be checked.
	 * @return True if length is greater than 0, False if 0 chars or less.
	 * 
	 */
	private boolean isString(String curr) {
		return (curr.length() > 0);
	}

	/**
	 * Cycles through the array of stopWords to determine if the word read in is a
	 * stop word.
	 * 
	 * @param curr is the current word being read in by the file reader that needs
	 *             to be checked.
	 * @return True if curr is on the list of stopWords, False if not on list of
	 *         stopWords.
	 * 
	 */
	private boolean isStopWord(String curr) {
		boolean stopWordFound = false;

		for (String word : stopWords) {
			if (word.equals(curr)) {
				stopWordFound = true;
				break;
			}
		}
		return stopWordFound;
	}

	/**
	 * Confirms the wordList size is greater than zero to avoid a null pointer
	 * exception, then cycles through the SLL to output each word and the occurrence
	 * of that word to the output.txt file.
	 * 
	 * @param endIndex is the size of the SLL if less than 10 unique words are in
	 *                 wordList, 10 if the SLL more than 10 unique words are in SLL
	 *                 or the size of wordList when printing all (10 has been
	 *                 initialized to constant "NUM_WORDS_TO_PRINT").
	 */
	private void printQuery(BST<Token> tree, int endIndex) {
		if (endIndex > 0)
			printList(tree, endIndex);
	}

	/**
	 * Iterates through the tree and outputs the desired toString of each object to
	 * the .txt file.
	 * 
	 * @param tree     is the tree to iterate through.
	 * @param endIndex Signals when to stop iterating through the tree.
	 */
	private void printList(BST<Token> tree, int endIndex) {
		int counter = 0;
		Iterator<Token> it = tree.iterator();
		while (it.hasNext() && counter < endIndex) {
			System.out.println(it.next());
			counter++;
		}
	}

	/**
	 * Determines whether the current word has already been read in by the file
	 * reader and added to the SLL wordList.
	 * 
	 * @param toCheck is the current word being read in by the file reader that
	 *                needs to be checked.
	 * @return True if the word has not yet been read in and is unique, False if it
	 *         has already been read in and is not unique.
	 * 
	 */
	private boolean isUnique(Token toCheck) {
		return (wordsByNaturalOrder.find(toCheck) == null);
	}

	/**
	 * When called, this method searches the singly linked list (SLL) wordList for
	 * the token containing the current string, then determines the index of that
	 * token and increases the frequency counter held by the object.
	 * 
	 * @param toIncrease is the Token created with the current word read by the
	 *                   scanner.
	 * 
	 */
	private void increaseTally(Token toIncrease) {
		Token curr = wordsByNaturalOrder.find(toIncrease);
		if (curr != null)
			curr.increaseFrequency();
	}

}
