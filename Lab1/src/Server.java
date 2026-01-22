import java.util.Scanner;

public class Server {
	
	public static void main(String[] args) {

        Scanner in = new Scanner(System.in);

        System.out.print("Veuillez saisir l'adresse IP : ");
        String ip = in.next();
        
        if (!Utils.isValidIPv4(ip)) {
            System.out.println("Adresse IP invalide, veuillez réessayer");
            in.close();
            return;
        }

        System.out.print("Veuillez saisir le port d'écoute : ");
        int port = in.nextInt();
        
        if (!Utils.isValidPort(port)) {
            System.out.println("Port invalide, veuillez réessayer");
            in.close();
            return;
        }
        
        in.close();
        
    }

}
