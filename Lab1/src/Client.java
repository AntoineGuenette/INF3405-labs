import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Veuillez saisir l'adresse IP : ");
        String ip = scanner.next();

        if (!Utils.isValidIPv4(ip)) {
            System.out.println("Adresse IP invalide, veuillez réessayer");
            scanner.close();
            return;
        }

        System.out.print("Veuillez saisir le port d'écoute : ");
        int port = scanner.nextInt();

        if (!Utils.isValidPort(port)) {
            System.out.println("Port invalide, veuillez réessayer");
            scanner.close();
            return;
        }

        System.out.print("Veuillez saisir votre nom d'utilisateur : ");
        String username = scanner.next();

        System.out.print("Veuillez saisir votre mot de passe : ");
        String password = scanner.next();

        Socket socket = new Socket(ip, port);

        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        DataInputStream in = new DataInputStream(socket.getInputStream());

        out.writeUTF(username);
        out.writeUTF(password);

        String response = in.readUTF();

        switch (response) {
            case "OK":
                System.out.println("Connexion acceptée. Accès au service.");
                break;
            case "ACCOUNT_CREATED":
                System.out.println("Compte créé. Connexion acceptée.");
                break;
            case "ERROR_PASSWORD":
                System.out.println("Erreur dans la saisie du mot de passe");
                break;
            default:
                System.out.println("Réponse inconnue du serveur");
        }

        socket.close();
        scanner.close();
    }
}