package Clientside;


import java.io.*;
import java.net.*;
import java.util.ArrayList;


class Reader implements Runnable {
    String botName;
    Socket socket;
    BufferedReader inputStream;
    PrintWriter outputStream;


    public Reader(Socket socket, String name) throws IOException {
        this.socket = socket;
        inputStream = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        outputStream = new PrintWriter(this.socket.getOutputStream(), true);
        this.botName = name;

    }

    @Override
    public void run() {

        try {
            do {
                String fromServer = inputStream.readLine();

                if (fromServer != null) {

                    if (!fromServer.contains("/disconnect " + botName)) {
                        if (fromServer.startsWith("Host: ") && !fromServer.contains("/disconnect")) {

                            System.out.println(fromServer);

                            generate(fromServer);

                        } else {
                            System.out.println(fromServer);
                            if (!fromServer.contains("/disconnect")) {
                                continue;
                            }

                            outputStream.println(botName + ": " + "Ah man...");

                        }
                    }
                    else {
                        break;
                    }
                }
            }
            while (true);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    // function to generate a Bot
    private void generate(String fromServer) {
        Bot bot = new Bot(fromServer, botName);
        var botAnswer = bot.answerBot();

        System.out.println("Me: " + botAnswer);

        outputStream.println(botName + ": " + botAnswer);
    }
}

//client class
public class Client {

    public Client(String host, int port, String navn) throws IOException {

        Socket socket = new Socket(host, port);
        System.out.println(navn + " has arrived");

        Reader con = new Reader(socket, navn);
        new Thread(con).start();
    }

    //  psvm starts every time you run another client.
    public static void main(String[] args) throws IOException {

        List list = new List();

        if (list.getAvailableBots().isEmpty()) {
            list.addBots();
        }

        ArrayList<String> availableBots = list.getAvailableBots();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Choose a friend");
        System.out.println(availableBots);

        String start = bufferedReader.readLine();

        for (String bot : availableBots) {
            if (bot.equals(start)) {
                Client localhost = new Client("127.0.0.1", 9090, start);
                list.BotUsed(bot);
                break;
            }
        }

    }
}