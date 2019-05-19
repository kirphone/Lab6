package server;

import control.CollectionManager;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Server {

    private DatagramSocket datagramSocket;
    private CollectionManager collectionManager;

    public Server(int port, File importFile) throws IOException{
        datagramSocket = new DatagramSocket(9876);
        //datagramSocket.connect(InetAddress.getLocalHost(), port);

        collectionManager = new CollectionManager();

        System.out.println(collectionManager.importFile(importFile));

        System.out.println("Сервер запущен");
        System.out.println("IP: " + datagramSocket.getLocalAddress());
        System.out.println("host: " + datagramSocket.getLocalAddress().getHostName());
    }

    private void listen() throws IOException{
        while(true){
            DatagramPacket ip = new DatagramPacket(new byte[4096], 4096);
            datagramSocket.receive(ip);

            System.out.println("Поступил запрос от клиента");

            ResponseThread thread = new ResponseThread(datagramSocket, ip, collectionManager);
            thread.start();
        }
    }

    public static void main(String[] args) {

        if(args.length == 0) {
            System.out.println("Путь до файла должен задаваться с помощью аргумента командной строки");
            return;
        }

        Scanner sc = new Scanner(System.in);
        int port;
        while (true) {
            System.out.println("Введите порт для запуска сервера");
            try {
                port = Integer.parseInt(sc.nextLine());
                break;
            } catch (NumberFormatException e){
                System.out.println("Необходимо ввести число, задающее существующий порт");
            }
        }
        try{
            Server server = new Server(port, new File(args[0]));
            server.listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}