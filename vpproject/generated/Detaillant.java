import java.util.LinkedList;

public class Detaillant extends Employe {
	public SystemStock aSystemStock;
	public LinkedList<Commande> aCommande = new LinkedList<Commande>();

	public void CommanderArticle() {
		throw new UnsupportedOperationException();
	}

	public void gérerCommande(String pCommande) {
		throw new UnsupportedOperationException();
	}

	public void afficherCommande(String pCommande) {
		throw new UnsupportedOperationException();
	}

	public void ImprimerStock() {
		throw new UnsupportedOperationException();
	}

	public void ImprimerProduitInsufisant() {
		throw new UnsupportedOperationException();
	}
}