import java.io.IOException;
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
            
            // Chaque partie doit commencer par autre chose que 0
            if (part.length() > 1 && part.startsWith("0")) return false;

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
    
    public static synchronized void loadUsers() {
        try {
            // charge les comptes existants dans la map users
            users.clear();
            users.putAll(db.load());
            System.out.println("DB users chargée : " + users.size() + " compte(s).");
        } catch (Exception e) {
            System.out.println("Erreur chargement DB : " + e.getMessage());
        }
    }

    
    // Fonction d'authentification
    private static final Map<String, String> users = new HashMap<>();
    private static UserDatabase db = new UserDatabase("users_db.txt");
    public static synchronized String authenticate(String username, String password) {
        if (users.containsKey(username)) {
            if (users.get(username).equals(password)) {
                return "CONNEXION_ACCEPTEE";
            } else {
                return "MAUVAIS_MDP";
            }
        } else {
            users.put(username, password);
            
            try {
                db.save(users);
            } catch (IOException e) {
                System.out.println("Erreur sauvegarde DB : " + e.getMessage());
            }
            return "COMPTE_CREE";
        }
    }
    
    // Fonction de sauvegarde de la base de données des utilisateurs
    public static void saveUsers() {
    	System.out.println("(Fonction à implémenter)");
    	// TODO : Implémenter la sauvegarde dans un fichier .txt
    }
}