import java.util.LinkedList;

public class Article {
	private String aNom;
	private String aCodebar;
	private String aStock;
	private String aSeuil;
	private String aPrix;
	private String aPoids;
	public Entrepot aEntrepot;
	public Rayon aRayon;
	public LinkedList<Ticket> aTicket = new LinkedList<Ticket>();
	public Commande aCommande;
}