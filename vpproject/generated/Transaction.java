import java.util.LinkedList;

public class Transaction {
	public Payment aPayment;
	public Ticket aTicket;
	public LinkedList<Article> aArticle = new LinkedList<Article>();

	public void addArticle(String pArticle, String pInt_1) {
		throw new UnsupportedOperationException();
	}

	public void removeArticle(String pArticle, String pInt_2) {
		throw new UnsupportedOperationException();
	}
}