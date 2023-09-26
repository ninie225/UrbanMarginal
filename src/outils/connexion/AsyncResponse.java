package outils.connexion;

/**
 * Permet la r�cup�ration asynchrone d'une r�ponse
 * @author emds
 *
 */
public interface AsyncResponse {
	/**
	 * M�thode � red�finir pour r�cup�rer la r�ponse de l'ordinateur distant
	 * @param connection contient l'objet qui permet de recontacter l'ordinateur distant (pour un envoi)
	 * @param ordre contient "connexion" si nouvelle connexion, "r�ception" si nvelle information re�ue, "d�connexion" si d�connexion
	 * @param info contient l'information re�ue (si ordre = "r�ception")
	 */
    void reception(Connection connection, String ordre, Object info);
}
