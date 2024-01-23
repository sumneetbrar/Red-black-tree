/** 
 * This program analyzes the a text that is given to it, counting every
 * individual word. It uses a red-black tree to store the infomation in an
 * ordered manner. It is able to report on them, and give basic statistics about
 * the tree used.
 *
 * It relies on a RedBlackTree object, that you must write. The API is as given
 * in the assignment.
 *
 * @author Adam A. Smith
 * @version 2023.10.21
 */

import java.io.*;
import java.util.*;

public class WordFreqs2 {
	public static void main(String[] args) {
		// make sure we have a file
		if (args.length == 0) {
			System.err.println("Please enter a file name!");
			System.exit(1);
		}

		// make the rb-tree, and query on it
		try {
			RedBlackTree<String, Integer> tree = makeRedBlackTree(args[0]);
			queryUser(tree);
		}
		catch (IOException e) {
			System.err.println("Couldn't open file \"" +args[0]+ "\".");
			System.exit(1);
		}
	}

	// load a file & make a RedBlackTree from its words
	private static RedBlackTree<String, Integer> makeRedBlackTree(String filename) throws IOException {
		RedBlackTree<String, Integer> tree = new RedBlackTree<>();
		
		// set up Scanner that tokenizes based on non-(letters, numbers, underscore, apostrophe)
		Scanner scanner = new Scanner(new File(filename));
		scanner.useDelimiter("[^A-Za-z0-9'_]+");

		// read, token by token
		while (scanner.hasNext()) {
			String token = scanner.next().toLowerCase();

			// remove leading/trailing apostrophes
			if (token.startsWith("'")) token = token.replaceAll("^'+", "");
			if (token.endsWith("'")) token = token.replaceAll("'+$", "");

			// skip if it was just apostrophes
			if (token.length() == 0) continue;

			// add to table
			if (tree.containsKey(token)) {
				int freq = tree.get(token) + 1;
				tree.put(token, freq);
			}
			else tree.put(token, 1);
		}

		// close out & return
		scanner.close();
		return tree;
	}

	// enter a loop to query the user for searches & commands
	private static void queryUser(RedBlackTree<String, Integer> tree) {
		Scanner scanner = new Scanner(System.in);

		System.out.println("The text contains " +tree.size()+ " unique words.");
		System.out.println("Please enter a word to query, or \"!help\" for help, or \"!exit\" to exit.");
		System.out.print("> ");

		while (scanner.hasNextLine()) {
			String query = scanner.nextLine().trim();

			// do nothing on empty string
			if (query.length() == 0);

			// special commands starting with !
			else if (query.startsWith("!")) {
				// quit on "!quit" or "!exit"
				if (query.equals("!quit") || query.equals("!exit")) break;

				// print stats on the backing table
				else if (query.equals("!stats")) {
					int numRedNodes = tree.countRedNodes();
					System.out.println("Tree table statistics:");
					System.out.println("\tSize (n): " +tree.size());
					System.out.println("\tHeight: " +tree.calcHeight() + " (" +tree.calcBlackHeight()+ " black)");
					System.out.printf("\tAvg node depth: %1.3f\n", tree.calcAverageDepth());
					System.out.printf("\t# red nodes: %d (%1.1f%%)\n", numRedNodes, 100.0*numRedNodes/tree.size());
					System.out.println("\tRoot key: \"" + tree.getRootKey() +"\"");
				}

				// help menu
				else if (query.equals("!help")) printHelpMenu();

				// unknown special command
				else {
					System.out.println("I didn't recognize that. Try \"!help\"?");
				}
			}

			// size or select
			else if (query.startsWith("#")) {
				if (query.length() == 1) System.out.println("The text contains " +tree.size()+ " unique words.");
				else {
					int rank = parseInt(query.substring(1));
					String word = tree.select(rank);
					if (word == null) System.out.println("There is no such word. Try a number from 0-" +(tree.size()-1) +".");
					else System.out.println("Word #" +rank+ " is \"" +word+ "\".");
				}
			}

			// get rank
			else if (query.startsWith("&")) {
				String word = query.substring(1);
				int rank = tree.findRank(word);
				if (rank == -1) System.out.println("\""+ word+ "\" is not in the text.");
				else System.out.println("\""+ word+ "\" is word #" +rank+ ".");
			}

			// predecessor / first
			else if (query.startsWith("<")) {
				if (query.length() == 1) System.out.println("The first word (alphabetically) is \"" +tree.findFirstKey() +"\".");
				else {
					query = query.substring(1);
					String predecessor = tree.findPredecessor(query);
					if (predecessor == null) System.out.println("Nothing comes before \"" +query+ "\" alphabetically.");
					else System.out.println("\"" +predecessor+ "\" comes before \"" +query+ "\" alphabetically.");
				}
			}
			
			// ceilings & lasts
			else if (query.startsWith(">")) {
				if (query.length() == 1) System.out.println("The last word (alphabetically) is \"" +tree.findLastKey() +"\".");
				else {
					query = query.substring(1);
					String successor = tree.findSuccessor(query);
					if (successor == null) System.out.println("Nothing comes after \"" +query+ "\" alphabetically.");
					else System.out.println("\"" +successor+ "\" comes after \"" +query+ "\" alphabetically.");
				}
			}

			// rank
			
			// deletion
			else if (query.startsWith("-")) {
				String wordToDelete = query.substring(1);
				if (wordToDelete.length() == 0) System.out.println("What word do you want to delete? Please try again.");
				else {
					Integer value = tree.delete(wordToDelete);
					if (value == null) System.out.println("\""+ wordToDelete+ "\" was not present.");
					else if (value == 1) System.out.println("1 entry of \"" +wordToDelete+ "\" has been deleted.");
					else System.out.println(value + " entries of \"" +wordToDelete+ "\" have been deleted.");
				}
			}

			// add a word or increase its frequency
			else if (query.startsWith("+")) {
				String newWord = query.substring(1);
				if (newWord.length() == 0) System.out.println("What word do you want to add? Please try again.");
				else if (tree.containsKey(newWord)) {
					int freq = tree.get(newWord) + 1;
					tree.put(newWord, freq);
					System.out.println("\"" +newWord+ "\" now appears " +freq+ "×.");
				}
				else {
					tree.put(newWord, 1);
					System.out.println("\"" +newWord+ "\" now appears 1×.");
				}
			}

			// regular query
			else {
				Integer freq = tree.get(query);
				if (freq == null || freq == 0) System.out.println("\"" +query+ "\" is not in the text.");
				else System.out.println("\"" +query+ "\" appears " +freq+ "× in the text.");
			}
			System.out.print("> ");
		}

		System.out.println("Goodbye!");
	}

	// just prints a help menu
	private static void printHelpMenu() {
		System.out.println("\tword\t\tprints word frequency");
		System.out.println("\t-word\t\tdeletes word");
		System.out.println("\t+word\t\tadds word");
		System.out.println("\t<\t\tgets the first word");
		System.out.println("\t>\t\tgets the last word");
		System.out.println("\t<word\t\tfinds the predecessor of word");
		System.out.println("\t>word\t\tfinds the successor of word");
		System.out.println("\t#\t\tprints the tree size");
		System.out.println("\t#number\t\tprints the word with rank number");
		System.out.println("\t&word\t\tprints the rank of word");
		System.out.println("\t!help\t\tprints this help menu");
		System.out.println("\t!stats\t\tprints in-depth stats on the table");
		System.out.println("\t!exit\t\texits the program");
	}

	// parse int, but return -1 if invalid
	private static int parseInt(String string) {
		try {
			return Integer.parseInt(string);
		}
		catch (NumberFormatException ex) {
			return -1;
		}
	}
}