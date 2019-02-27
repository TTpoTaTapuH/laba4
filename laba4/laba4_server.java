package laba4;
import java.io.*;
import java.net.*;

public class laba4_server {
    public static int PORT = 8888;
    private static final int TIME_SEND_SLEEP = 100;
    private static final int COUNT_TO_SEND = 10;
    private ServerSocket servSocket;
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out; // поток записи в сокет
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
        class Listener implements Runnable{
            Socket socket;
            public Listener(Socket aSocket){
                socket = aSocket;
            }
            public void run(){
                try {
                    System.out.println("Слушатель запущен");
                    int count = 0;

                    OutputStream outt = socket.getOutputStream();
                    OutputStreamWriter writer = new OutputStreamWriter(outt);
                    out = new BufferedWriter(writer);

                    InputStream inp = socket.getInputStream();
                    InputStreamReader reader = new InputStreamReader(inp);
                    in = new BufferedReader(reader);

                    String word = in.readLine();
                    System.out.println(word);
                    // не долго думая отвечает клиенту
                    out.write("Привет, это Сервер! Подтверждаю, вы написали : " + word + "\n");
                    out.flush(); // выталкиваем все из буфера
                    out.close();
                    in.close();
                }
                catch(IOException e){
                    System.err.println("Исключение: " + e.toString());
                }
            }
        }
        System.out.println("Сервер запущен...");
        while(true){
            try{
                //создаём соединение с клиентом
                Socket socket = servSocket.accept();
                //создаём клиента и передаём ему сокет - соединение с сервером
                Listener listener = new Listener(socket);
                //создаём поток
                Thread thread = new Thread(listener);
                //запускаем поток
                thread.start();
            }catch(IOException e){
                System.err.println("Исключение: " + e.toString());
            }
        }
    }
}



