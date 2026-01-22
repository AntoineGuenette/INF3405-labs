public class Utils {

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
    
    public static boolean isValidPort(int port) {

        // Le port doit être entre 5000 et 5050
        if (port < 5000 || port > 5050) return false;
        
        return true;
    }
    
}