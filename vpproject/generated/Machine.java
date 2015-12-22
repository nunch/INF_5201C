import java.util.LinkedList;

public class Machine {
	public Caissier aCaissier;
	public LinkedList<Session> aSession = new LinkedList<Session>();
	public Caisse aCaisse;

	public void logIn(String pSession) {
		throw new UnsupportedOperationException();
	}

	public void ouvrirCaisse(String pClé) {
		throw new UnsupportedOperationException();
	}
}