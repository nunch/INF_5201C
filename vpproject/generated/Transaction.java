import java.util.LinkedList;

public class Transaction {
	public Paiement aPaiement;
	public Ticket aTicket;
	public LinkedList<Article> aArticle = new LinkedList<Article>();

	public void ajouterArticle(String pArticle, String pInt_1) {
		throw new UnsupportedOperationException();
	}

	public void retirerArticle(String pArticle, String pInt_2) {
		throw new UnsupportedOperationException();
	}
}