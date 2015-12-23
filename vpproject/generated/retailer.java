import java.util.LinkedList;

public class retailer extends Employee {
	public SystemStock aSystemStock;
	public LinkedList<Command> aCommand = new LinkedList<Command>();

	public void orderArticle(String pArticle, String pInt_10) {
		throw new UnsupportedOperationException();
	}

	public void handleCommand(String pCommand) {
		throw new UnsupportedOperationException();
	}

	public void displayCommad(String pCommand) {
		throw new UnsupportedOperationException();
	}

	public void printStock() {
		throw new UnsupportedOperationException();
	}

	public void printInsufficientArticle() {
		throw new UnsupportedOperationException();
	}
}