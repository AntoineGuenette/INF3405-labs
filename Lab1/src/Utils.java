import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
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

    private static final Map<String, String> users = new HashMap<>();
    private static final Path DB_PATH = Paths.get("users_db.txt");
    
    // Fonction d'authentification
    public static synchronized String authenticate(String username, String password) {
        if (users.containsKey(username)) {
            return users.get(username).equals(password)
                ? "CONNEXION_ACCEPTEE"
                : "MAUVAIS_MDP";
        }

        users.put(username, password);
        saveUsers();
        return "COMPTE_CREE";
    }
    
    // Fonction de chargement de la base de données des utilisateurs
    public static synchronized void loadUsers() {
        users.clear();

        if (!Files.exists(DB_PATH)) {
            System.out.println("Aucune base de données trouvée : Aucun compte chargé.");
            return;
        }

        try {
            for (String line : Files.readAllLines(DB_PATH, StandardCharsets.UTF_8)) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                int idx = line.indexOf(':');
                if (idx <= 0 || idx == line.length() - 1) continue;

                String user = line.substring(0, idx).trim();
                String pass = line.substring(idx + 1).trim();
                users.put(user, pass);
            }
            System.out.println("Base de donnée trouvée : " + users.size() + " compte(s) chargé(s).");
        } catch (IOException e) {
            System.out.println("Erreur de chargement de la base de données : " + e.getMessage());
        }
    }

    // Fonction de sauvegarde de la base de données des utilisateurs
    public static synchronized void saveUsers() {
        try {
            if (!Files.exists(DB_PATH)) {
                Files.createFile(DB_PATH);
            }

            try (BufferedWriter writer = Files.newBufferedWriter(DB_PATH, StandardCharsets.UTF_8)) {
                writer.write("# format: username:password");
                writer.newLine();
                for (Map.Entry<String, String> e : users.entrySet()) {
                    writer.write(e.getKey() + ":" + e.getValue());
                    writer.newLine();
                }
            }
            System.out.println("Base de données sauvegardée.");
        } catch (IOException e) {
            System.out.println("Erreur de sauvegarde de la base de données : " + e.getMessage());
        }
    }
    
}