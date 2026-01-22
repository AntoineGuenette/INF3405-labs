import java.util.Scanner;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class Server {
	
	private static ServerSocket Listener;
	
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
        
        scanner.close();
        
        // Compteur incrémenté à chaque connexion d'un client au serveur
        int clientNumber = 0;

        // Création de la connexion pour communiquer avec les clients
        Listener = new ServerSocket();
        Listener.setReuseAddress(true);

        InetAddress serverIP = InetAddress.getByName(ip);

        // Association de l'adresse et du port à la connexion
        Listener.bind(new InetSocketAddress(serverIP, port));

        System.out.format(
            "Le serveur est actif avec %s:%d%n",
            ip,
            port
        );

        try {
            // À chaque fois qu'un nouveau client se connecte,
            // on exécute la fonction run() de l'objet ClientHandler
            while (true) {

                // Important : la fonction accept() est bloquante :
                // attend qu'un prochain client se connecte

                // Une nouvelle connexion : on incrémente le compteur clientNumber
                new ClientHandler(
                    Listener.accept(),
                    clientNumber++
                ).start();
            }

        } finally {
            // Fermeture de la connexion
            Listener.close();
        }
    }

}
