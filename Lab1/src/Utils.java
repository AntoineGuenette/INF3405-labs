import java.util.HashMap;
import java.util.Map;

public class Utils {

	// Fonction de vérification de l'adresse IP
    public static boolean isValidIPv4(String ip) {
    	// L'adresse ne doit pas être vide
        if (ip == null) return false;

        String[] parts = ip.split("\\.");

        // L'adresse doit contenir 4 parties
        if (parts.length != 4) return false;

        for (String part : parts) {
        	
        	// Chaque partie contient un ou plusieurs chiffres
            if (!part.matches("\\d+")) return false;

            // Chaque partie est un nombre entre 0 et 255
            int value = Integer.parseInt(part);
            if (value < 0 || value > 255) return false;
        }
        return true;
    }
    
    // Fonction de vérification du port d'écoute
    public static boolean isValidPort(int port) {

        // Le port doit être entre 5000 et 5050
        if (port < 5000 || port > 5050) return false;
        
        return true;
    }
    
    // Fonction d'authentification
    private static final Map<String, String> users = new HashMap<>();
    public static synchronized String authenticate(String username, String password) {
        if (users.containsKey(username)) {
            if (users.get(username).equals(password)) {
                return "OK";
            } else {
                return "MAUVAIS_MDP";
            }
        } else {
            users.put(username, password);
            return "COMPTE_CREE";
        }
    }
}