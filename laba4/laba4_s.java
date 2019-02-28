package laba4;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.String;

public class laba4_s {
    public static int PORT = 8887;
    private ServerSocket servSocket;

    public static void main(String[] args) {
        laba4_s lab = new laba4_s();
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

    public laba4_s(){
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
                Listen listener = new Listen(socket);
                Thread thread = new Thread(listener);
                thread.start();
            }catch(IOException e){
                System.err.println("Исключение: " + e.toString());
            }
        }
    }
}
class Listen implements Runnable{
    public static final int READ_BUFFER_SIZE = 10;
    Socket socket;
    public Listen(Socket aSocket){
        socket = aSocket;
    }
    public void run(){
            try {
                System.out.println("Connection accepted: " + socket);
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));
                // Вывод автоматически выталкивается из буфера PrintWriter'ом
                PrintWriter out = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())), true);
                while (true) {
                    String str = in.readLine();
                    if (str.equals("END"))
                        break;
                    System.out.println("Echoing: " + str);
                    out.println(str);
                }
                socket.close();
                // Всегда закрываем два сокета...
            }catch(IOException e){
                System.err.println("Исключение: " + e.toString());
            }
    }
}
