import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/*
 * Fetched the input URL and maximum display count from user and validates it.
 * Singleton Pattern used to allow only one instance to be running. 
 * 
 */
public class Input {
	
	private Scanner scanner;
	private URL url;
	private static Input inputInstance = null; 
	
	/*
	 * Private constructor to avoid instantiation of class using new keyword
	 */
	private Input(){}
	
	/*
	 * Returns a new instance or an existing one if already created
	 */
	public static Input getInputInstance() {
		if(inputInstance == null) {
			inputInstance = new Input();
        }
		return inputInstance;
	}
	
	/*
	 * Returns the input URL from user
	 */
	public URL getURL() {
		return url;
	}
	
	/*
	 * Validate URL entered through args in command line.
	 * If invalid, ask again without exiting the program.
	 */
	public void validateInputURL(String[] args) {
		boolean isValid = false;

		if(args.length <= 0) {
			System.out.println("The arg url is invalid. Please try again.");
		    acceptInputURL();
		}
		else {
			String inputURL = args[0];
			while(!isValid) {
				try {
					url = new URL(inputURL);
					url.openStream();
				    isValid = true;
				} catch (MalformedURLException e) {
				    System.out.print("The arg url was invalid, please try again...\n");
				    acceptInputURL();
				} catch (IOException e) {
				    System.out.print("The arg url was invalid, please try again...\n");
				    acceptInputURL();
				}
			}
		}
	}
	
	
	/*
	 * Asks the user for input and keeps asking in case of invalid URL
	 * 
	 */
	public void acceptInputURL() {
		String input = readInputLine();
		boolean isValid = false;
		url = null;
		while(!isValid) {
			try {
			    url = new URL(input);
			    url.openStream();
			    isValid = true;
			    
			} catch (MalformedURLException e) {
			    System.out.print("The url was invalid, please try again...\n");
			    input = readInputLine();
			} catch (IOException e) {
			    System.out.print("The url was invalid, please try again...\n");
			    input = readInputLine();
			}
		}
	}
	
	/*
	 * Asks the user for maximum display count to be within the range of 1-6 results
	 * 
	 */
	public Integer acceptMaximumDisplayCount() {
		System.out.print("Enter maximum display count for results (1-6): ");
		Integer input = readInputNumber();
		boolean isValid = false;
		while(!isValid) {
			if (input <= 6 && input >= 1) {
			    isValid = true;
			}
			else {
			    System.out.print("Please enter a valid integer between 1-6: ");
			    input = readInputNumber();
			}
		}
		return input;
	}
	
	/*
	 * Reads count of maximum results to display from the user
	 */
	private Integer readInputNumber() {
		scanner = new Scanner(System.in);
		return scanner.nextInt();
	}
		
	/*
	 * Reads input URL from the user
	 */
	private String readInputLine() {
	    System.out.println ("Enter a URL: ");
		scanner = new Scanner(System.in);
		return scanner.nextLine();
	}

}
