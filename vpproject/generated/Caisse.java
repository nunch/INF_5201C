import java.util.LinkedList;

public class Caisse {
	public Caissier aCaissier;
	public LinkedList<Session> aSession = new LinkedList<Session>();
	public Cle aCle;

	public void login(String pSession) {
		throw new UnsupportedOperationException();
	}

	public void ouvrirCaisse(String pCle) {
		throw new UnsupportedOperationException();
	}
}