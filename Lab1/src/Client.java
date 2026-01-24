import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);

        // Saisie de l'adresse IP
        System.out.print("Veuillez saisir l'adresse IP : ");
        String ip = scanner.next();
        // Vérification de l'adresse IP
        if (!Utils.isValidIPv4(ip)) {
            System.out.println("Adresse IP invalide, veuillez réessayer");
            scanner.close();
            return;
        }

        // Saisie du port d'écoute
        System.out.print("Veuillez saisir le port d'écoute : ");
        int port = scanner.nextInt();
        // Vérification du port d'écoute
        if (!Utils.isValidPort(port)) {
            System.out.println("Port invalide, veuillez réessayer");
            scanner.close();
            return;
        }
        
        // Création du socket avec l'adresse IP et le port d'écoute
        Socket socket = new Socket(ip, port);
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        DataInputStream in = new DataInputStream(socket.getInputStream());

        // Saisie du nom d'utilisateur + mot de passe
        System.out.print("Veuillez saisir votre nom d'utilisateur : ");
        String username = scanner.next();
        System.out.print("Veuillez saisir votre mot de passe : ");
        String password = scanner.next();

        // Envoi du nom d'utilisateur + mot de passe au serveur
        out.writeUTF(username);
        out.writeUTF(password);

        // Lecture de la réponse d'authentification de la part du serveur
        String response = in.readUTF();
        
        switch (response) {
            case "OK":
                System.out.println("Connexion acceptée. Accès au service.");
                break;
            case "COMPTE_CREE":
                System.out.println("Compte créé. Connexion acceptée.");
                break;
            case "MAUVAIS_MDP":
                System.out.println("Erreur dans la saisie du mot de passe. Veuillez réessayer");
                break;
            default:
                System.out.println("Réponse inconnue du serveur");
        }

        socket.close();
        scanner.close();
    }
}