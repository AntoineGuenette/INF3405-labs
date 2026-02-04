import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

public class UserDatabase {

    private final Path dbPath;

    public UserDatabase(String filename) {
        this.dbPath = Paths.get(filename);
    }

    // Charge la DB depuis le fichier (si le fichier n'existe pas -> map vide)
    public Map<String, String> load() throws IOException {
        Map<String, String> map = new HashMap<>();

        if (!Files.exists(dbPath)) {
            return map;
        }

        for (String line : Files.readAllLines(dbPath, StandardCharsets.UTF_8)) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) continue;

            // Format: username:password
            int idx = line.indexOf(':');
            if (idx <= 0 || idx == line.length() - 1) continue;

            String user = line.substring(0, idx).trim();
            String pass = line.substring(idx + 1).trim();
            if (!user.isEmpty()) {
                map.put(user, pass);
            }
        }

        return map;
    }

    // Sauvegarde la DB dans le fichier
    public void save(Map<String, String> map) throws IOException {
        // Crée le fichier si nécessaire
        if (!Files.exists(dbPath)) {
            Files.createFile(dbPath);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(dbPath, StandardCharsets.UTF_8)) {
            writer.write("# format: username:password");
            writer.newLine();
            for (Map.Entry<String, String> e : map.entrySet()) {
                writer.write(e.getKey() + ":" + e.getValue());
                writer.newLine();
            }
        }
    }
}
