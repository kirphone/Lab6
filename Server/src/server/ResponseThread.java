package server;

import control.CollectionManager;
import control.Command;
import control.CommandHandler;
import server.Response;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ResponseThread extends Thread {

    private CommandHandler handler;
    private int port;
    private InetAddress address;
    private DatagramSocket datagramSocket;
    private DatagramPacket inDatagramPacket;

    public ResponseThread(DatagramSocket _datagramSocket, DatagramPacket datagramPacket, CollectionManager manager){
        handler = new CommandHandler(manager);
        datagramSocket = _datagramSocket;
    }

    public void run(){

        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(inDatagramPacket.getData()));
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {

            String actions = handler.doCommand((Command) ois.readObject());

            //Оставляем информацию на сервере
            synchronized (System.out) {
                System.out.printf("\nКоманда %s выполнена\n\nРезультат:\n%s", (String)  ois.readObject(), actions);
            }

            Response response = new Response(actions, handler.getCollection());
            oos.writeObject(response);
            oos.flush();

            DatagramPacket outDatagramPacket = new DatagramPacket(baos.toByteArray(), baos.toByteArray().length,
                    inDatagramPacket.getAddress(), inDatagramPacket.getPort());

            datagramSocket.send(outDatagramPacket);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}