package laba4;
import java.io.*;
import java.net.*;

public class laba4_server {
    public static int PORT = 8888;
    private static final int TIME_SEND_SLEEP = 100;
    private static final int COUNT_TO_SEND = 10;
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
        class Listener implements Runnable{
            Socket socket;
            public Listener(Socket aSocket){
                socket = aSocket;
            }
            public void run(){
                try{
                    System.out.println("Слушатель запущен");
                    int count = 0;
                    OutputStream out = socket.getOutputStream();
                    OutputStreamWriter writer = new OutputStreamWriter(out);
                    PrintWriter pWriter = new PrintWriter(writer);
                    InputStream in = socket.getInputStream();
                    InputStreamReader reader = new InputStreamReader(in);
                    BufferedReader preader = new BufferedReader(reader);
                    String text = preader.readLine();
                    System.out.println(text);
                    pWriter.print("Получил твоё сообщение. Ты ввел " + text);
                    Thread.sleep(100);
                    /*while(count < COUNT_TO_SEND){
                        count++;
                        pWriter.print(((count>1) ? "," : "") + "говорит " + count);
                        Thread.sleep(TIME_SEND_SLEEP);
                    }*/
                    pWriter.close();
                }catch(IOException e){
                    System.err.println("Исключение: " + e.toString());
                } catch (InterruptedException e) {
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

