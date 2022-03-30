package Clientside;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

// i choose implementing runnable over extend Thread for the Reader class as it shares the same object to multiple
//threads rather than making a new object for each Thread
class Reader implements Runnable {
    String botName;
    Socket socket;
    BufferedReader inputStream;
    PrintWriter outputStream;

// constructor for the Reader class
    public Reader(Socket socket, String name) throws IOException {
        this.socket = socket;
        inputStream = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        outputStream = new PrintWriter(this.socket.getOutputStream(), true);
        this.botName = name;
    }
//run function
    @Override
    public void run() {
        try {
            do {
                //reads line from the inputstream and runs an if else
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
// socket contains the host IP and port in our case the host is either localhost or 127.0.0.1 and i set port to 9090
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
        //the bufferedreader lets us read the message from the server
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Choose a friend");
        System.out.println(availableBots);
//reads next line
        String start = bufferedReader.readLine();

        for (String bot : availableBots) {
            if (bot.equals(start)) {
                Client localhost = new Client("127.0.0.1", 9091, start);
                list.BotUsed(bot);
                break;
            }
        }

    }
}