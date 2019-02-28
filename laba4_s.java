package laba4;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.String;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    String regex="^[0-9]=$";
    Pattern pattern =Pattern.compile(regex);
    Matcher matcher = null;
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
                String stroka = "";
                while (true) {
                    String str = in.readLine();
                    matcher = pattern.matcher(str);
                    stroka += str;
                    if (matcher.find())
                        break;
                    System.out.println("Echoing: " + str);
                }
                String znak = "+";
                String num="";
                int sum = 0;
                for(int i=0;i<stroka.length();i++){
                    //System.out.print(stroka.charAt(i));
                    if(stroka.charAt(i)== '0' || stroka.charAt(i)== '1' || stroka.charAt(i)== '2' || stroka.charAt(i)== '3'
                            || stroka.charAt(i)== '4' || stroka.charAt(i)== '5' || stroka.charAt(i)== '6' || stroka.charAt(i)== '7' ||
                            stroka.charAt(i)== '8' || stroka.charAt(i)== '9'){
                        num += stroka.charAt(i);
                    }
                    //System.out.println("\n  dddd    "+num);
                    else if(stroka.charAt(i)== '-'){
                        if(znak=="+")sum+= Integer.parseInt(num);
                        if(znak=="-")sum-= Integer.parseInt(num);
                        znak = "-";
                        num="";
                    }
                    else if(stroka.charAt(i)== '+'){
                        if(znak=="+")sum+= Integer.parseInt(num);
                        if(znak=="-")sum-= Integer.parseInt(num);
                        znak = "+";
                        num="";
                    }
                    else if(stroka.charAt(i)=='='){
                        if(znak=="+")sum+= Integer.parseInt(num);
                        if(znak=="-")sum-= Integer.parseInt(num);
                        znak = "+";
                        num = "";
                        PrintWriter pWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
                        System.out.println("Сумма чисел = "+sum);
                        pWriter.print("Сумма чисел = "+sum);
                        pWriter.close();
                        break;
                    }
                    else {
                        System.out.println("Ошибка! Введены неправильные данные");
                    }
                }
                    out.println("Сумма равна:"+sum);
                socket.close();
                // Всегда закрываем два сокета...
            }catch(IOException e){
                System.err.println("Исключение: " + e.toString());
            }
    }
}
