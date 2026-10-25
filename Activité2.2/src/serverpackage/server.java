package serverpackage;

import java.io.IOException;
import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import shared.operation;

public class server {
     public static void main(String[] args) {
        int port = parsePort(args);
        System.out.println("Serveur en attente de connexion sur le port " + port + "...");

        try (ServerSocket serverSocket = new ServerSocket(port);
             Socket clientSocket = serverSocket.accept();
             ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {

            System.out.println("Client connecté : " + clientSocket.getInetAddress());

            try {
                Object obj;
                while ((obj = in.readObject()) != null) {
                    if (!(obj instanceof operation)) continue;

                    operation op = (operation) obj;
                    System.out.println("Opération reçue : " + afficher(op));
                    double res = calculer(op);
                    out.writeObject(res);
                    out.flush();
                }
            } catch (EOFException e) {
                System.out.println("Client déconnecté.");
            }

        } catch (BindException e) {
            System.err.println("Port " + port + " déjà utilisé. Arrêtez le processus existant ou choisissez un autre port.");
            System.err.println("Exemple: java serverpackage.server 8081");
        } catch (IOException | ClassNotFoundException e) {
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

    private static double calculer(operation op) {
        double a = op.getOperande1();
        double b = op.getOperande2();
        String operateur = op.getOperateur();

        return switch (operateur) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "*" -> a * b;
            case "/" -> (b != 0 ? a / b : Double.NaN);
            default -> Double.NaN;
        };
    }

    private static String afficher(operation op) {
        return formatNombre(op.getOperande1()) + op.getOperateur()
                + formatNombre(op.getOperande2());
    }

    private static String formatNombre(double nombre) {
        if (nombre == (long) nombre) {
            return Long.toString((long) nombre);
        }
        return Double.toString(nombre);
    }
}
