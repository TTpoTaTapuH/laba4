package laba4;
import java.io.*;
import java.net.*;

public class laba4_server {
    public static int PORT = 8888;
    private ServerSocket servSocket;

    public static void main(String[] args) {
        laba4_server lab = new laba4_server();
        //lab.read_file();
        lab.go();
    }
    public void read_file(){
        StringBuilder sb = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader("conf.txt"));
            try{
                PORT = Integer.parseInt(br.readLine());//построчное чтение
            }finally{br.close();}
        }catch(IOException e){throw new RuntimeException();}
    }

    public laba4_server(){
        try{
            //общение на машине сервера
            servSocket = new ServerSocket(PORT);
        }catch(IOException e){
            System.err.println("Не удаётся открыть сокет для сервера: " + e.toString());
        }
    }
    public void go(){
        System.out.println("Сервер запущен...");
        while(true){
            try{
                Socket socket = servSocket.accept();
                Listener listener = new Listener(socket);
                Thread thread = new Thread(listener);
                thread.start();
            }catch(IOException e){
                System.err.println("Исключение: " + e.toString());
            }
        }
    }
}
class Listener implements Runnable{
    public static final int READ_BUFFER_SIZE = 10;
    Socket socket;
    public Listener(Socket aSocket){
        socket = aSocket;
    }
    public void run(){
        char[] readed = new char[READ_BUFFER_SIZE];
        StringBuffer strBuff = new StringBuffer();
        try{
            System.out.println("Слушатель запущен");
            int count = 0;
            OutputStream out = socket.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(out);
            PrintWriter pWriter = new PrintWriter(writer);
            while(count < 10){
                count++;
                pWriter.print(((count>1) ? "," : "") + "говорит " + count);
                Thread.sleep(100);
            }
            //pWriter.close();

            BufferedReader fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = fromClient.readLine();
            //while(true){
                //int count1 = reader.read(readed, 0, READ_BUFFER_SIZE);
                //if(count1 == -1) break;
                //strBuff.append(readed, 0, count1);
                Thread.yield();
            //}

            System.out.println("Клиент " + " прочёл: " + line);
        }catch(IOException e){
            System.err.println("Исключение: " + e.toString());
        } catch (InterruptedException e) {
            System.err.println("Исключение: " + e.toString());
        }
    }
}
