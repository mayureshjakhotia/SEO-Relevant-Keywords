import java.io.IOException;
import java.net.URL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/*
 * Gets HTML document from URL & parses it using Jsoup Library.
 * Has getter and setter for Document and URL respectively.
 * Implemented Singleton Design Pattern.
 * Implements Parser interface.
 * 
 */
public class HTMLParser implements Parser {
	
	private URL url;
	private Document doc;
	private static HTMLParser htmlParser = null; 
	
	/*
	 * Private constructor to avoid instantiation of class using new keyword
	 */
	private HTMLParser(){}
	
	/*
	 * Returns a new instance or an existing one if already created
	 */	
	public static HTMLParser getHTMLParser() {
		if(htmlParser == null) {
         htmlParser = new HTMLParser();
        }
		return htmlParser;
	}

	/*
	 * Sets the input URL entered by user
	 */
	public void setURL(URL url) {
		this.url = url;
	}

	/*
	 * Returns the document
	 */
	public Document getDocument() {
		return doc;
	}

	/*
	 * Calls the function which parses the HTML document
	 */
	public void parse() {
		parseHTMLDocument();
	}
	
	/*
	 * The connect method creates a new Connection, and get() fetches and parses the HTML file
	 */
	private void parseHTMLDocument() {
		doc = null;
		try {
			doc = Jsoup.connect(url.toString()).get();
		} catch (IOException e) {
			System.out.println("Jsoup cannot give the document given the url : "+url.toString());
		}
	}

}