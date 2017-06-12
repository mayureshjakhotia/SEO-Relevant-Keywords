import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.jsoup.nodes.Document;

/*
 * Parses the fetched document, removes stop words, identifies candidate words, removes 
 * special characters, returns relevant results according to maximum count along with the 
 * frequency of occurrence of all the words appearing in the HTML Document.
 * Implements Singleton Design Pattern.
 * Extends Parser interface.
 * 
 */
public class WordsParser implements Parser {
	
	private Document doc;
	private HashSet<String> stopWords = new HashSet<>();
	private HashMap<String, Integer> candidateFrequency = new HashMap<>();
	private Scanner scanStopWordsFile;
	private Scanner scanDocument;
	private static WordsParser wordsParser = null; 

	/*
	 * Private constructor to avoid instantiation of class using new keyword
	 */
	private WordsParser(){}
	
	/*
	 * Returns a new instance or an existing one if already created
	 */	
	public static WordsParser getWordsParser() {
		if(wordsParser == null) {
			wordsParser = new WordsParser();
        }
		return wordsParser;
	}
	
	/*
	 * Sets the fetched Document by HTMLParser
	 */
	public void setDocument (Document doc) {
		this.doc = doc;
	}
	
	/*
	 * Parses the stop words and candidate words
	 */
	public void parse() {
		parseStopWords();
		parseCandidateWords();		
	}

	/*
	 * Scans the stop words from stopwords.txt and saves them in to a HashSet
	 */
	private void parseStopWords() {
		scanStopWordsFile = new Scanner(getClass().getResourceAsStream("stopwords.txt"));

		while (scanStopWordsFile.hasNext()) {
			String newWord = removeSpecialCharacters(scanStopWordsFile.next().trim().toLowerCase());
			
			if (newWord != null || !newWord.isEmpty()) {
				stopWords.add(newWord);
			}
		}		
	}
	
	/*
	 * Parses the candidate words and stores the word along with the frequency into a HashMap
	 * candidateFrequency. Also, checks if the word is valid and removes special characters
	 * before inserting in to the HashMap.
	 * 
	 */
	private void parseCandidateWords() {
		scanDocument = new Scanner(doc.text());

		while (scanDocument.hasNext()) {
			String word = scanDocument.next();
			String temp = removeSpecialCharacters(word);

			// Checking if the word is not a candidate word
			if (!stopWords.contains(temp.toLowerCase())) {
				// Word length should be greater than 1 to avoid single letter words
				if (isValidWord(temp) && temp.length()>1) {
					int frequency = 0;
					if (!candidateFrequency.containsKey(temp.toLowerCase())) {
						frequency = 1;
						candidateFrequency.put(temp.toLowerCase(), frequency);
					}
					else {
						frequency = candidateFrequency.get(temp.toLowerCase());
						candidateFrequency.put(temp.toLowerCase(), ++frequency);
					}
				}
			}
		}	
	}
	
	/*
	 * Final function call. 
	 * Sorts the HashMap by the values in the descending order in sortByValues function.
	 * Displays relevant topics according to the user selected maximum display count.
	 * 
	 */
	public void displayRelevantTopics(int maxDisplayCount) {
		
		HashMap<String, Integer> map = sortByValues(candidateFrequency); 
	    ArrayList<String> keys = new ArrayList<>();
	    for (String key: map.keySet()) {
	    	keys.add(key);
	    }

		for (int j = 1; j < map.size(); j++) {

			System.out.print(keys.get(j-1));
			if (map.get(keys.get(j-1)) != map.get(keys.get(j))) {
				System.out.print(" (Frequency : "+map.get(keys.get(j-1))+")\n");
			}
			while (map.get(keys.get(j-1)) == map.get(keys.get(j))) {

				System.out.print(", "+keys.get(j));
				j++;

				// Checking for Last Element
				if (j == map.size()) {
					System.out.print(" (Frequency : "+map.get(keys.get(j-1))+")\n");
				}
				
				// If the index is greater than the map size, break the loop
				if (j >= map.size()) {
					break;
				}
				
				// Display the value (number of occurrences) of the specific keywords
				if (map.get(keys.get(j-1)) != map.get(keys.get(j))) {
					System.out.print(" (Frequency : "+map.get(keys.get(j-1))+")\n");
				}
			}
			
			maxDisplayCount--;
			if (maxDisplayCount == 0) {
				break;
			}
			
		}
		
	}
	
	/*
	 * Removes the special characters from each word as it is checked for a candidate word
	 */
	private String removeSpecialCharacters(String word) {
		
		String newWord = word;
		
		newWord = newWord.replace("&", "");
		newWord = newWord.replace("!", "");
		newWord = newWord.replace("?", "");
		newWord = newWord.replace(":", "");
		newWord = newWord.replace("(", "");
		newWord = newWord.replace(")", "");
		newWord = newWord.replace("<", "");
		newWord = newWord.replace(">", "");
		newWord = newWord.replace("*", "");
		newWord = newWord.replace("#", "");
		newWord = newWord.replace(",", "");
		newWord = newWord.replace("\"", "");
		newWord = newWord.replace("[", "");
		newWord = newWord.replace("]", "");
		newWord = newWord.replace(".", "");
		newWord = newWord.replace("=", "");
		
		if (newWord.startsWith("'") || newWord.endsWith("'")) {
			newWord = newWord.replace("'", "");
		}
		
		return newWord;
	}

	/*
	 * Checks if the entered word is a valid word and not a whitespace or non-ASCII character
	 */
	private boolean isValidWord(String s){
		
		boolean isEmpty=true;
		for(int i=0;i<s.length();i++) {
			char c=s.charAt(i);
			if(c==' ' || c < 32 || c >= 127) {
				isEmpty=true;
			}
			else {
				isEmpty = false;
			}
		}
		return !isEmpty;
	}
	
	/*
	 * Takes a HashMap candidateFrequency and sorts it in descending order of the frequency
	 * of the keywords. Returns this new HashMap to the calling function.
	 * 
	 */
	private static HashMap sortByValues(HashMap map) { 
	       List list = new LinkedList(map.entrySet());
	       
	       /*
	        *  Custom Comparator is defined here (sorts in descending order of values - 
	        *  frequency of occurrence)
	        */
	       Collections.sort(list, new Comparator() {
	            public int compare(Object o1, Object o2) {
	               return ((Comparable) ((Map.Entry) (o2)).getValue())
	                  .compareTo(((Map.Entry) (o1)).getValue());
	            }
	       });

	       /*
	        * Copies sorted list in HashMap sortedHashMap and uses LinkedHashMap
	        * to preserve the insertion order
	        */
	       HashMap sortedHashMap = new LinkedHashMap();
	       for (Iterator it = list.iterator(); it.hasNext();) {
	              Map.Entry entry = (Map.Entry) it.next();
	              sortedHashMap.put(entry.getKey(), entry.getValue());
	       } 
	       return sortedHashMap;
	  }

}
