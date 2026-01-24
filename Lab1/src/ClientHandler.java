import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

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

            System.out.println(
                "Client #" + clientNumber +
                " (" + username + ") -> " + result
            );

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