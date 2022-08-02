package chatServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;

    public Server(ServerSocket ss) {
        this.serverSocket = ss;
    }

    public void startServer() {
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("A new client does connected.");


                ClientHandler сlientHandler = new ClientHandler(socket);
                Thread thread = new Thread(сlientHandler);
                thread.start();
            }
        } catch (IOException e) {
            closeServerSocket();
        }
    }


    public void closeServerSocket(){
         try {
             if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(2008);
        Server server = new Server(serverSocket);
        server.startServer();
    }

}
