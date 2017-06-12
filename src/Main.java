import org.jsoup.nodes.Document;

/*
 * Main entry point into the code.
 * Input class - Gets the input from the user and validates it.
 * HTMLParser class - Gets HTML document from URL & parses it.
 * WordsParser class - Parses the fetched document, removes stop words, identifies candidate 
 * words, displays relevant results according to maximum count along with the frequency of
 * occurrence.
 * Singleton Pattern is implemented on all the 3 classes.
 * 
 */
public class Main {

	private static Input inputInstance;
	private static HTMLParser htmlParser;
	private static WordsParser wordsParser;

	public static void main(String[] args) {
		
		/*
		 * Accept the input URL and maximum results to display using the input instance and
		 * validate it.
		 */
		inputInstance = Input.getInputInstance();
		inputInstance.validateInputURL(args);
		
		/*
		 * Get the HTML document using the Jsoup library
		 */
		htmlParser = HTMLParser.getHTMLParser();
		htmlParser.setURL(inputInstance.getURL());
		htmlParser.parse();
		Document doc = htmlParser.getDocument();
		
		/*
		 * Parse document for the stop and candidate words, get the count and display relevant
		 * words accordingly.
		 */
		wordsParser = WordsParser.getWordsParser();
		wordsParser.setDocument(doc);
		wordsParser.parse();
		int maxDisplayCount = inputInstance.acceptMaximumDisplayCount();
		wordsParser.displayRelevantTopics(maxDisplayCount);

	}

}