import java.util.LinkedList;

public class Detaillant extends Employe {
	public LinkedList<Commande> aCommande = new LinkedList<Commande>();

	public void CommanderArticle() {
		throw new UnsupportedOperationException();
	}
}