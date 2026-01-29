import java.util.Scanner;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Server {

    private static ServerSocket Listener;
    private static volatile boolean running = true;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        try {
            // Saisie de l'adresse IP
            System.out.print("Veuillez saisir l'adresse IP : ");
            String ip = scanner.next();
            // Vérification de l'adresse IP
            if (!Utils.isValidIPv4(ip)) {
                System.out.println("Adresse IP invalide, veuillez réessayer.");
                System.out.println("Arrêt du serveur...");
                scanner.close();
                return;
            }

            // Saisie du port d'écoute
            System.out.print("Veuillez saisir le port d'écoute : ");
            int port = scanner.nextInt();
            // Vérification du port d'écoute
            if (!Utils.isValidPort(port)) {
                System.out.println("Port invalide, veuillez réessayer.");
                System.out.println("Arrêt du serveur...");
                scanner.close();
                return;
            }

            // Création du socket serveur
            Listener = new ServerSocket();
            Listener.setReuseAddress(true);
            InetAddress serverIP = InetAddress.getByName(ip);
            Listener.bind(new InetSocketAddress(serverIP, port));

            // Confirmation de l'ouverture du serveur
            System.out.printf("\nLe serveur est actif avec %s:%d%n", ip, port);
            System.out.println("Pour arrêter le serveur, tapez \"exit\" puis Entrée.");

            // Thread du serveur (gestion des clients)
            Thread serverThread = new Thread(() -> {
            	
            	// Création d'un compteur des clients connectés
                int clientNumber = 0;

                try {
                    while (running) {
                        try {
                            new ClientHandler( // Exécution d'un nouveau ClientHandler
                                Listener.accept(),
                                clientNumber++ // Augmentation du compteur
                            ).start();
                        } catch (IOException e) {
                            if (running) {
                                System.out.println("Erreur serveur : " + e.getMessage());
                            }
                        }
                    }
                } finally {
                    try {
                        if (Listener != null && !Listener.isClosed()) {
                            Listener.close();
                        }
                    } catch (IOException ignored) {}
                    System.out.println("Serveur arrêté avec succès.");
                }
            });

            serverThread.start();

            // Lecture de la commande "exit"
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (running) {
                String command = reader.readLine();
                if (command == null) break;

                if (command.equalsIgnoreCase("exit")) {
                    System.out.println("\nCommande exit reçue. Arrêt du serveur...");
                    running = false;

                    // Sauvegarde de la base de données des utilisateurs
                    Utils.saveUsers();

                    if (Listener != null && !Listener.isClosed()) {
                        Listener.close();
                    }
                    break;
                }
            }

        } catch (Exception e) {
            System.out.println("Erreur fatale : " + e.getMessage());
        }
        scanner.close();
    }
}