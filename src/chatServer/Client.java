package chatServer;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    String userName;
    Socket socket;
    BufferedReader bufferedReader;
    BufferedWriter bufferedWriter;

    public Client(Socket s, String userName) {
        try {

            this.socket = s;

            bufferedReader = new BufferedReader(new InputStreamReader(s.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            this.userName = userName;

        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void closeEverything(Socket s, BufferedReader br, BufferedWriter bw) {
        try {
            if (s != null) s.close();
            if (br != null) br.close();
            if (bw != null) bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String messageFrom;
                while (socket.isConnected()) {
                    try {
                        messageFrom = bufferedReader.readLine();
                        System.out.println(messageFrom);
                    } catch (IOException e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }
        ).start();
    }

    public void sendMessage() {
        try {
            bufferedWriter.write(userName); // первая строчка с именем клиента выслать ClientHandler
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner sc = new Scanner(System.in);
            while (socket.isConnected()) {
                String msgToSend = sc.nextLine();
                bufferedWriter.write(userName + ": " + msgToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }

        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }


    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your user name for group chat: ");
        String userName = scanner.nextLine();
        Socket socket = new Socket("localhost", 2008);
        Client client = new Client(socket, userName);
        client.listenForMessage();
        client.sendMessage();

    }

}
