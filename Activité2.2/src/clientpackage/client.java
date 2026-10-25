package clientpackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import shared.operation;

public class client {
     public static void main(String[] args) {
        String host = "localhost";
        int port = parsePort(args);

        try (Socket socket = new Socket(host, port);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             Scanner sc = new Scanner(System.in)) {

            System.out.println(" Connecté au serveur " + host + ":" + port);

            while (true) {
                System.out.print("Entrez une opération (ex: 5 + 3 ou 'exit') : ");
                String input = sc.nextLine();
                
                if (input.equalsIgnoreCase("exit"))
                {
                    System.out.println("Déconnexion du client");
                    break;
                }

                if (!input.matches("\\d+\\s*[\\+\\-\\*/]\\s*\\d+")) {
                    System.out.println("Syntaxe incorrecte");
                    continue;
                }

                String[] parts = input.split("\\s*[\\+\\-\\*/]\\s*");
                String operateur = input.replaceAll("\\d+", "").trim();

                operation op = new operation(
                    Double.parseDouble(parts[0]),
                    operateur,
                    Double.parseDouble(parts[1])
                );

                out.writeObject(op);

                double resultat = (double) in.readObject();
                System.out.println("Résultat = " + resultat);
            }

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Impossible de se connecter au serveur " + host + ":" + port);
            System.err.println("Vérifiez que le serveur est démarré et que le port est correct.");
            e.printStackTrace();
        }
    }

    private static int parsePort(String[] args) {
        if (args.length > 0) {
            try {
                int port = Integer.parseInt(args[0]);
                if (port > 0 && port <= 65535) {
                    return port;
                }
            } catch (NumberFormatException ignored) {
            }
        }
        return 8080;
    }
}
