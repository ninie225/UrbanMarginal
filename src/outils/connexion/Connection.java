package outils.connexion;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Gestion de la connexion entre 2 ordinateurs distants
 * @author emds
 *
 */
public class Connection extends Thread {
	
	/**
	 * canal d'entr�e
	 */
	private ObjectInputStream in ;
	/**
	 * canal de sortie
	 */
	private ObjectOutputStream out ; 
	/**
	 * objet de lien avec une autre classe qui impl�mente AsyncResponse pour transf�rer les r�ponses
	 */
	private AsyncResponse delegate;

	/**
	 * Constructeur : cr�e une connexion � partir d'un socket (contenant les sp�cificit�s de l'ordinateur distant)
	 * @param socket objet de connexion de type serveur ou client
	 * @param delegate instance de la classe vers laquelle il faut transf�rer les r�ponses
	 */
	public Connection(Socket socket, AsyncResponse delegate) {
		this.delegate = delegate;
		// cr�ation du canal de sortie pour envoyer des informations
		try {
			this.out = new ObjectOutputStream(socket.getOutputStream()) ;
		} catch (IOException e) {
			System.out.println("erreur cr�ation canal out : "+e);
			System.exit(0);
		}
		// cr�ation du canal d'entr�e pour recevoir des informations
		try {
			this.in = new ObjectInputStream(socket.getInputStream()) ;
		} catch (IOException e) {
			System.out.println("erreur cr�ation canal in : "+e);
			System.exit(0);
		}
		// d�marrage du thread d'�coute (attente d'un message de l'ordi distant)
		this.start() ;
		// envoi de l'instance de connexion vers la classe qui impl�mente AsyncResponse pour r�cup�rer la r�ponse
		this.delegate.reception(this, "connexion", null);
	}
	
	/**
	 * Envoi d'un objet vers l'ordinateur distant, sur le canal de sortie
	 * @param unObjet contieny l'objet � envoyer
	 */
	public synchronized void envoi(Object unObjet) {
		// l'envoi ne peut se faire que si un objet delegate existe (pour r�cup�rer la r�ponse)
		if(delegate != null) {
			try {
				this.out.reset();
				out.writeObject(unObjet);
				out.flush();
			} catch (IOException e) {
				System.out.println("erreur d'envoi sur le canal out : "+e);
			}
		}
	}
	
	/**
	 * M�thode thread qui permet d'attendre des messages provenant de l'ordi distant
	 */
	public void run() {
		// permet de savoir s'il faut continuer � �couter
		boolean inOk = true ;
		// objet qui va r�cup�rer l'information re�ue
		Object reception ;
		// boucle tant qu'il faut �couter
		while (inOk) {
			try {
				// r�ception d'un objet sur le canal d'entr�e
				reception = in.readObject();
				// envoi de l'information re�ue vers la classe qui impl�mente AsyncResponse pour r�cup�rer la r�ponse
				delegate.reception(this, "r�ception", reception);
			} catch (ClassNotFoundException e) {
				// probl�me grave qui ne devrait pas se produire : arr�t du programme
				System.out.println("erreur de classe sur r�ception : "+e);
				System.exit(0);
			} catch (IOException e) {
				// envoi de l'information de d�connexion  vers la classe qui impl�mente AsyncResponse pour r�cup�rer la r�ponse
				delegate.reception(this, "d�connexion", null);
				// demande d'arr�ter de boucler sur l'attente d'une r�ponse
				inOk = false ;
				// l'ordinateur distant n'est plus accessible
				System.out.println("l'ordinateur distant est d�connect�");
				// fermeture du canal d'entr�e
				try {
					in.close();
				} catch (IOException e1) {
					System.out.println("la fermeture du canal d'entr�e a �chou� : "+e);
				}
			}
		}
		
	}
	
}
