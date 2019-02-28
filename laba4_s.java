package laba4;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.String;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class laba4_s {
    public int PORT = 8887; //порт из файла
    public String history_file = ""; //файл журнала
    private ServerSocket servSocket;

    //проверка на файл
    public static void main(String[] args) {
        laba4_s lab = new laba4_s();
        if(args.length>0){
            lab.history_file = args[0];
            lab.go();
        }
        else{
            System.out.println("Не указан файл журнала");
        }
    }
    //загрузка порта из файла
    public void read_file(){
        StringBuilder sb = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader("conf.txt"));
            try{
                PORT = Integer.parseInt(br.readLine());//построчное чтение
            }finally{br.close();}
        }catch(IOException e){throw new RuntimeException();}
    }
    //конструктор класса
    public laba4_s(){
        try{
            //считываем порт
            read_file();
            servSocket = new ServerSocket(PORT);
        }catch(IOException e){
            System.err.println("Не удаётся открыть сокет для сервера: " + e.toString());
        }
    }
    //запуск
    public void go(){
        System.out.println("Сервер запущен...");
        while(true){
            try{
                Socket socket = servSocket.accept();
                Listen listener = new Listen(socket,history_file);
                Thread thread = new Thread(listener);
                thread.start();
            }catch(IOException e){
                System.err.println("Исключение: " + e.toString());
            }
        }
    }
}
//класс для сервера
class Listen implements Runnable{
    String regex="^[0-9]=$"; //регулярка для конца
    Pattern pattern =Pattern.compile(regex); //регяларка
    Matcher matcher = null; //сравнение
    Socket socket;
    public String history_file = ""; //название файла журнала

    //конструктор
    public Listen(Socket aSocket, String file_text){
        socket = aSocket;
        history_file = file_text; //инициализация
    }

    //запуск сервера
    public void run(){
            try {
                System.out.println("Connection accepted: " + socket);
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));
                // Вывод автоматически выталкивается из буфера PrintWriter'ом
                PrintWriter out = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())), true);
                String stroka = "";

                // создание файла
                File f1 = new File(history_file);
                try{
                    //проверка файла
                    if(!f1.exists()) f1.createNewFile();
                    PrintWriter pw = new PrintWriter(history_file);
                    try{
                        pw.println("Последние операции:");
                        while (true) {
                            String str = in.readLine();
                            pw.print(str);
                            //проверка конца передачи
                            matcher = pattern.matcher(str);
                            stroka += str;
                            if (matcher.find())
                                break;
                            System.out.print(str);
                        }
                        String znak = "+";
                        String num="";
                        int sum = 0;
                        //сохранение данных
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
                                System.out.print("\nСумма чисел = "+sum);
                                pWriter.println("Сумма чисел = "+sum);
                                pWriter.close();
                                break;
                            }
                            else {
                                System.out.println("Ошибка! Введены неправильные данные");
                            }
                        }
                        out.println("Сумма равна:"+sum);
                        socket.close();
                    }finally{pw.close();}
                }catch(IOException e){throw new RuntimeException();}
            }catch(IOException e){
                System.err.println("Исключение: " + e.toString());
            }
    }
}

    }
}
