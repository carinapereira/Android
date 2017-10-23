package com.example.carina.bluetooth;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by carina on 22/10/2017.
 */

public class ChatController {
    private BluetoothSocket socket;
    private InputStream in;
    private OutputStream out;
    private ChatListener listener;
    private boolean running;

    public interface ChatListener{
        public void onMessageReceived(String msg);
    }

    public ChatController(BluetoothSocket socket, ChatListener listener) throws IOException {
        this.socket = socket;
        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();
        this.listener = listener;
        this.running = true;
    }

    public void star(){
        new Thread(){
            @Override
            public void run() {
                super.run();

                running = true;
                byte[] bytes = new byte[1024];
                int tamanho;

                while (running){
                    try {
                        tamanho = in.read(bytes);
                        String msg = new String(bytes, 0, tamanho);
                        listener.onMessageReceived(msg);
                    }catch (Exception e){

                    }
                }
            }
        }.start();
    }

    public void sendMessage(String msg) throws IOException{
        if(out != null){
            out.write(msg.getBytes());
        }
    }

    public void stop(){
        running = false;
        try{
            if(socket != null){
                socket.close();
            }
            if(in != null){
                in.close();
            }
            if(out != null){
                out.close();
            }
        }catch (IOException e){

        }
    }
}
