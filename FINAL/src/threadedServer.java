import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

public class threadedServer {
        static Database database;
        protected Socket connection;

        public static void main(String[] args) {
            new threadedServer().start();
        }
        public void start() {
            ServerSocket server = null;//Using port 5500 since macs control center uses port 5000
            try {
                server = new ServerSocket(5500);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            database = new Database();
            while (true) {
                System.out.println("server> Waiting to connect.");
                try {
                    connection = server.accept();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("server> Connected " + connection.toString());
                PlayerHandler player= new PlayerHandler(connection);
                Thread thread= new Thread(player);
                thread.start();
            }
        }

        public static class PlayerHandler implements Runnable{
            protected Socket conn;
            public PlayerHandler(Socket socket) {
                this.conn=socket;
            }
            @Override
            public void run(){
            try {
                InputStreamReader stream;
                PrintWriter writer;
                    writer = new PrintWriter(conn.getOutputStream());
                    while(!conn.isClosed()) {
                        try {
                            stream = new InputStreamReader(conn.getInputStream());
                            BufferedReader reader = new BufferedReader(stream);

                            String receivedRequest;
                            ArrayList<String> receivedRequests = new ArrayList<>();

                            while ((receivedRequest = reader.readLine()) != null) {
                                receivedRequests.add(receivedRequest);

                                if (receivedRequests.size() == 3) {
                                    switch (receivedRequests.get(0)) {
                                        case "CREATE ACCOUNT":
                                            if(database.checkUniqueUsername(receivedRequests.get(1))){
                                                writer = new PrintWriter(conn.getOutputStream());
                                                writer.println("NAME EXISTS");
                                                writer.flush();
                                            }
                                            else {
                                                writer = new PrintWriter(conn.getOutputStream());
                                                writer.println("VALID ACCOUNT");
                                                writer.flush();
                                                database.createAccount(receivedRequests.get(1), receivedRequests.get(2));
                                            }
                                            receivedRequests.clear();
                                            break;
                                        case "LOGIN":
                                            if (database.login(receivedRequests.get(1), receivedRequests.get(2))) {
                                                writer = new PrintWriter(conn.getOutputStream());
                                                writer.println("TRUE");
                                                writer.flush();
                                            } else {
                                                writer = new PrintWriter(conn.getOutputStream());
                                                writer.println("FAIL");
                                                writer.flush();
                                            }
                                            receivedRequests.clear();
                                            break;
                                        case "CHECK":
                                            String returnVal=database.getCurrency(receivedRequests.get(1), receivedRequests.get(2));
                                            writer = new PrintWriter(conn.getOutputStream());
                                            writer.println(returnVal);
                                            writer.flush();
                                            receivedRequests.clear();
                                            break;
                                        case "LOGOUT":
                                            receivedRequests.clear();
                                            conn.close();
                                            break;
                                    }
                                } else if ((receivedRequests.size() == 4) &&receivedRequests.get(0).equals("ADD CURRENCY")) {
                                    database.addCurrency(receivedRequests.get(1), receivedRequests.get(2), Integer.valueOf(receivedRequests.get(3)));
                                    receivedRequests.clear();
                                }

                                else if ((receivedRequests.size() == 5) && receivedRequests.get(0).equals("ROLL DIE")) {
                                    int betWin = Integer.valueOf(receivedRequests.get(4)) * 6;
                                    int betLose = Integer.valueOf(receivedRequests.get(4)) * -1;
                                    boolean winLose = diceRollingLogic(Integer.valueOf(receivedRequests.get(3)));
                                    if (winLose) {
                                        database.addCurrency(receivedRequests.get(1), receivedRequests.get(2),betWin);
                                        writer = new PrintWriter(conn.getOutputStream());
                                        writer.println("WON");
                                        writer.flush();
                                        receivedRequests.clear();
                                        break;
                                    } else {
                                        database.addCurrency(receivedRequests.get(1), receivedRequests.get(2),betLose);
                                        writer = new PrintWriter(conn.getOutputStream());
                                        writer.println("DIDNOTWIN");
                                        writer.flush();
                                        receivedRequests.clear();
                                        break;
                                    }


                                } else if ((receivedRequests.size() == 5) && receivedRequests.get(0).equals("FLIP COIN")) {
                                    int betWin = Integer.valueOf(receivedRequests.get(4));
                                    int betLose = Integer.valueOf(receivedRequests.get(4)) * -1;
                                    boolean winLose = coinFlippingLogic(Integer.valueOf(receivedRequests.get(3)));
                                    if (winLose) {
                                        database.addCurrency(receivedRequests.get(1), receivedRequests.get(2),betWin);
                                        writer = new PrintWriter(conn.getOutputStream());
                                        writer.println("WON");
                                        writer.flush();
                                        receivedRequests.clear();
                                    } else {
                                        database.addCurrency(receivedRequests.get(1), receivedRequests.get(2),betLose);
                                        writer = new PrintWriter(conn.getOutputStream());
                                        writer.println("DIDNOTWIN");
                                        writer.flush();
                                        receivedRequests.clear();
                                    }


                                }
                                else if(receivedRequests.size()==1){
                                    if(receivedRequests.get(0).equals("SCOREBOARD")){
                                        String pass=database.getScoreboard();
                                        writer = new PrintWriter(conn.getOutputStream());
                                        writer.println(pass);
                                        writer.flush();
                                        receivedRequests.clear();
                                    }
                                }
                            }
                        } catch (IOException | SQLException e) {
                            conn.close();
                            System.out.println("Client has disconnected from the server");
                        }
                    }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public Boolean diceRollingLogic(int check){
            Random random= new Random();
            int RandomNumber= random.nextInt(6)+1;
            if(RandomNumber==check){
                return true;
            }
            else{
                return false;
            }
        }
        public Boolean coinFlippingLogic(int bool){
//        1 is heads, 2 is tails
            Random random= new Random();
            int RandomNumber= random.nextInt(2)+1;
            if(RandomNumber==bool){
                return true;
            }
            else{
                return false;
            }
        }
        public static boolean hostAvailabilityCheck() {
            try{
                Socket socket= new Socket("127.0.0.1", 5500);
                return true;
            } catch (IOException ex) {
                //do nothing
            }
            return false;
        }
    }

}
