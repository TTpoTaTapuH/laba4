package laba4;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class laba4_c implements Runnable{
    public static int PORT = 8887;
    public static String HOST = "localhost";
    public static final int READ_BUFFER_SIZE = 10;
    private String name = null;

    public laba4_c(String s){
        name = s;
    }

    public void run(){
            try {
                Socket socket = new Socket(HOST, PORT);
                System.out.println("socket = " + socket);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket
                        .getInputStream()));
                // Вывод автоматически Output быталкивается PrintWriter'ом.
                PrintWriter out = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())), true);
                //for (int i = 0; i < 10; i++) {
                    out.println("1+");
                    out.println("10+");
                    out.println("15-");
                    out.println("19+");
                    out.println("10-");
                    out.println("8=");
                    String str = in.readLine();
                    System.out.println(str);
                //}
                //out.println("=");
        } catch (UnknownHostException e) {
            System.err.println("Исключение: " + e.toString());
        } catch (IOException e) {
            System.err.println("Исключение: " + e.toString());
        }
    }

    public static void main(String[] args) {
        HOST = args[0];
        PORT = Integer.parseInt(args[1]);
        laba4_c ja1 = new laba4_c("test");
        Thread th = new Thread(ja1);
        th.start();
    }
}

