package Serverside;

import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


import static java.lang.System.*;

public class Server {

    //Copy on write arrays have operations like add and set that are implemented by making a new copy of the array
    // therefore the name copy on write
    public static CopyOnWriteArrayList<client> clientsList = new CopyOnWriteArrayList<>();

    static final ThreadLocal<ExecutorService> pool = ThreadLocal.withInitial(() ->
            Executors.newFixedThreadPool(4));

    public Server(int port) throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            var input = new input(clientsList);
            new Thread(input).start();

            out.println("WELCOME");
// do while loop for getting the clients connected. important that it is a while loop as it needs to be open continuously
            do {

                if (clientsList.isEmpty()) {
                    out.println("SERVER : Waiting for bot!");
                }
                //accepts connection
                Socket s = serverSocket.accept();

                out.println("A bot have joined the room " + s.getRemoteSocketAddress());


                client serverClient = new client(s, clientsList);

                clientsList.add(serverClient);

                pool.get().execute(serverClient);

            } while (true);
        }
    }

// an input method that reads the Inputstream
    static class input implements Runnable {
        // inputStream
        final ThreadLocal<BufferedReader> inputStream = ThreadLocal.withInitial(() ->
                new BufferedReader(new InputStreamReader(in)));

        CopyOnWriteArrayList<client> List;

        public input(CopyOnWriteArrayList<client> List) {
            this.List = List;
        }

        @Override
        public void run() {

            do {
                String msgForClient = null;
                try {
                    msgForClient = inputStream.get().readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (Objects.equals(msgForClient, "exit")) {
                    exit(0);
                }

                if (!Objects.requireNonNull(msgForClient).isBlank() && !clientsList.isEmpty()) {
                    if (!msgForClient.startsWith("kick")) {
                        out.println("Host :" + msgForClient);
                    }

                    CopyOnWriteArrayList<client> arrayListClients = client.getArrayListOffClients();
                    for (int i = 0; i < arrayListClients.size(); i++) {
                        client client = arrayListClients.get(i);
                        client.outputStream.println(new StringBuilder().append("Host: ").append(msgForClient.toLowerCase()).toString());
                    }
                }
            } while (true);
        }
    }


    static class client implements Runnable {
        //attributes for the socket and in/output streams where input is what comes in to the client and output is what
        //is written out in the printwriter
        private  Socket socket;
        private AtomicReference<BufferedReader> InputStream = new AtomicReference<>();
        private  PrintWriter outputStream;

        private static CopyOnWriteArrayList<client> arrayBots;

        public client(Socket Socket, CopyOnWriteArrayList<client> ListClients) throws IOException {

            this.socket = Socket;
            Server.client.setArrayListOffClients(ListClients);

            InputStream.set(new BufferedReader(new InputStreamReader(socket.getInputStream())));
            outputStream = new PrintWriter(socket.getOutputStream(), true);
        }

        public static CopyOnWriteArrayList<client> getArrayListOffClients() {
            return arrayBots;
        }

        public static void setArrayListOffClients(CopyOnWriteArrayList<client> arrayListOffClients) {
            Server.client.arrayBots = arrayListOffClients;
        }

        @Override
        public void run() {
            try {
                do {
                    try {
                        //if socket is open read the the next line in the Buffered reader.
                        if (!socket.isClosed()) {
                            String fromClient = InputStream.get().readLine();
                            //if there is no next line, stop
                            if (fromClient == null) {
                                break;
                            } else {// but if there is a next line, print it and send
                                System.out.println(fromClient);
                                send(fromClient);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } while (true);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Function that prints the client output stream
        private void send(String msg) {
            CopyOnWriteArrayList<Server.client> arrayListClients = getArrayListOffClients();
            for (Server.client client : arrayListClients) {

                if (!(client.socket == this.socket)) {
                    client.outputStream.println(msg);
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        // just a server with port
        Server server = new Server(9091);
    }
}