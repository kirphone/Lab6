package client;

import control.Command;
import game.Person;
import server.Response;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

class Client {
    private DatagramChannel channel;
    private boolean isRunning;
    private Scanner scanner;
    private ConcurrentHashMap<String, Person> collection;
    private InetSocketAddress serverInetSocketAddress;
    private ByteBuffer buffer;

    Client(int port) throws IOException{
        //Открываем новый канал
        serverInetSocketAddress = new InetSocketAddress(InetAddress.getLocalHost(), port);
        channel = DatagramChannel.open(); //Связываем сокет канала с адресом сервера



        buffer = ByteBuffer.allocate(4096);
        scanner = new Scanner(System.in);
        isRunning = true;
        collection = new ConcurrentHashMap<>();
    }

    void run(){
        if(isRunning)
            System.out.println("Клиент готов к работе");
        else
            System.out.println("Клиент не готов к работе");
        while (isRunning){
            System.out.println("Введите команду");
            Command command = new Command(scanner.nextLine());
            processingCommand(command);
        }

        System.out.println("Сеанс работы клиента завершён");
    }


    private void processingCommand(Command command){
        switch (command.getName()) {
            case "stop":
                isRunning = false;
                break;
            case "import":
                if(command.getSize() == 2){
                    try (Stream<String> stream = Files.lines(Paths.get(command.getBody()
                            .substring(1, command.getBody().length() - 1)))) {
                        StringBuilder res = new StringBuilder();
                        stream.forEach(i -> res.append(i).append("\n"));
                        command.setBody(res.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (IndexOutOfBoundsException ex){
                        System.out.println("Ссылка на файл ограждается фигурными скобками");
                    }
                }
                break;
        }

        try( ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {

            oos.writeObject(command);
            oos.flush();

            buffer.clear();
            buffer.put(baos.toByteArray());
            buffer.flip();

            channel.send(buffer, serverInetSocketAddress);

            buffer.clear();

            channel.receive(buffer);

            byte[] doings = buffer.array();

            try(ByteArrayInputStream bais = new ByteArrayInputStream(doings);
                ObjectInputStream ois = new ObjectInputStream(bais)){
                //System.out.println(ois.readObject().toString());
                Response responce = (Response) ois.readObject();
                System.out.println(responce.getActions());
                collection = responce.getCollection();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int port;
        //String address;
        Scanner sc = new Scanner(System.in);
        while (true) {
            //System.out.println("Введите адрес сервера");
          //  try {
            //    address = sc.nextLine();
           // } catch (NoSuchElementException e){
             //   System.out.println("Необходимо ввести адрес сервера");
               // continue;
            //}
            System.out.println("Введите порт сервера");
            try {
                port = Integer.parseInt(sc.nextLine());
                break;
            } catch (RuntimeException e){
                System.out.println("Необходимо ввести число, задающее существующий порт");
            }
        }
        try{
            Client client = new Client(port);
            System.out.println("Клиент запущен");



            client.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}