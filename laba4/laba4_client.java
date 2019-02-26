package laba4;
import java.io.*;
import java.net.*;

public class laba4_client {
    public static int PORT = 2500;
    public static String HOST = "localhost";
    public static final int CLIENT_COUNT = 6;
    public static final int READ_BUFFER_SIZE = 10;
    private String name = null;
    public laba4_client(String s){
        name = s;
    }
    public void run(){
        char[] readed = new char[READ_BUFFER_SIZE];
        StringBuffer strBuff = new StringBuffer();
        try{
            Socket socket = new Socket(HOST, PORT);
            InputStream in = socket.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);
            while(true){
                int count = reader.read(readed, 0, READ_BUFFER_SIZE);
                if(count == -1) break;
                strBuff.append(readed, 0, count);
                Thread.yield();
            }
        } catch (UnknownHostException e) {
            System.err.println("Исключение: " + e.toString());
        } catch (IOException e) {
            System.err.println("Исключение: " + e.toString());
        }
        System.out.println("Клиент " + name + " прочёл: " + strBuff.toString());
    }
    public static void main(String[] args) {
        String name = "имя";
        PORT = Integer.parseInt(args[0]);
        HOST = args[1];
        for(int i = 1; i <= CLIENT_COUNT; i++){
            TCPClient ja = new TCPClient(name+i);
            Thread th = new Thread(ja);
            th.start();
        }
    }
}
