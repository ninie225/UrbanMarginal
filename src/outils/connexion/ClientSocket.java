package outils.connexion;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

/**
 * Gestion d'un client : cr�ation d'une connexion cliente
 * @author emds
 *
 */
public class ClientSocket {
	
	/**
	 * Constructeur : cr�e le socket de type client pour se connecter � un serveur (avec son ip et port d'�coute)
	 * @param delegate instance de la classe vers laquelle il faut transf�rer les r�ponses
	 * @param ip adresse IP du serveur
	 * @param port num�ro port d'�coute du serveur
	 */
	public ClientSocket (AsyncResponse delegate, String ip, int port) {
		try {
			Socket socket = new Socket(ip, port);
			System.out.println("connexion serveur r�ussie");
			// la connexion ne peut se faire que si un objet delegate existe (pour r�cup�rer la r�ponse)
			if(delegate != null) {
				// cr�ation d'une connexion pour ce client, pour la communication avec le serveur (envoi et r�ception d'informations)
				new Connection(socket, delegate) ;
			}
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "serveur non disponible");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "IP incorrecte");
		}
	}
	
}
