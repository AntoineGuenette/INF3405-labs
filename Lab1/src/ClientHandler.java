import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class ClientHandler extends Thread {

    private Socket socket;
    private int clientNumber;

    public ClientHandler(Socket socket, int clientNumber) {
        this.socket = socket;
        this.clientNumber = clientNumber;
        System.out.println("\nNouvelle connexion avec le client #" + clientNumber + " at " + socket);
    }

    public void run() {
        try {
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            // Réception des identifiants
            String username = in.readUTF();
            String password = in.readUTF();

            // Authentification
            String result = Utils.authenticate(username, password);

            // Envoi du résultat au client
            out.writeUTF(result);

            System.out.println("Client #" + clientNumber + " (" + username + ") -> " + result);
            
            // Format de la date et de l'heure
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd@HH:mm:ss");
            
            // Réception des images
            String imageNameOriginal = in.readUTF();
            int imageSize = in.readInt();
            byte[] imageBytes = new byte[imageSize];
            in.readFully(imageBytes);
            
            // Conversion bytes -> BufferedImage
            BufferedImage inputImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
            if (inputImage == null) {
                throw new IOException("Image non reconnue ou format invalide");
            }
            System.out.println("[" + username + " - " + socket.getRemoteSocketAddress() + " - " + now.format(formatter) + "] : Image " + imageNameOriginal + " reçue pour traitement");
            
            // Application du filtre Sobel
            BufferedImage filteredImage = Sobel.process(inputImage);

            // Conversion BufferedImage -> bytes
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            boolean success = ImageIO.write(filteredImage, "png", baos);
            if (!success) {
                throw new IOException("Impossible d'écrire l'image en PNG");
            }
            byte[] filteredBytes = baos.toByteArray();
            
            // Envoi de l'image traitée
            out.writeInt(filteredBytes.length);
            out.write(filteredBytes);
            out.flush();
            System.out.println("Image traitée envoyée au client");
            
        } catch (IOException e) {
            System.out.println("Erreur avec le client #" + clientNumber + ": " + e);

        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Impossible de fermer le socket");
            }

            System.out.println("Connexion avec le client #" + clientNumber + " fermée");
        }
    }
}