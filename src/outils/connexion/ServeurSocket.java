package outils.connexion;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Gestion d'un serveur : cr�ation d'une connexion de type serveur pour attendre les connexions de clients
 * @author emds
 *
 */
public class ServeurSocket extends Thread {

	/**
	 * objet pour une connexion de type serveur (pour attendre des connexions de clients)
	 */
	private ServerSocket serverSocket ; 
	/**
	 * objet de lien avec une autre classe qui impl�mente AsyncResponse pour transf�rer les r�ponses
	 */
	private AsyncResponse delegate=null; 
	
	/**
	 * Constructeur
	 * @param delegate instance de la classe vers laquelle il faut transf�rer les r�ponses
	 * @param port num�ro port d'�coute du serveur
	 */
	public ServeurSocket(AsyncResponse delegate, int port) {
		// cr�ation du socket serveur d'�coute des clients
		try {
			this.delegate = delegate;
			this.serverSocket = new ServerSocket(port);
			// le d�marrage de l'�coute ne peut se faire que si un objet delegate existe (pour r�cup�rer la r�ponse)
			if(delegate != null) {
				this.start();		
			}
		} catch (IOException e) {
			// probl�me grave qui ne devrait pas se produire : arr�t du programme
			System.out.println("erreur grave cr�ation socket serveur : "+e);
			System.exit(0);
		}
	}
	
	/**
	 * M�thode thread qui va attendre la connexion d'un client
	 */
	public void run() {
		// objet qui va r�cup�rer le socket du client qui s'est connect�
		Socket socket ;
		// boucle infinie pour attendre un nouveau client
		while (true) {
			try {
				// attente d'une connexion
				System.out.println("le serveur attend");
				socket = serverSocket.accept();
				System.out.println("un client s'est connect�");
				// cr�ation d'une connexion vers ce client, pour la communication (envoi et r�ception d'informations)
				new Connection(socket, delegate);
			} catch (IOException e) {
				// probl�me grave qui ne devrait pas se produire : arr�t du programme
				System.out.println("erreur sur l'objet serverSocket : "+e);
				System.exit(0);
			}
		}
	}
	
}
