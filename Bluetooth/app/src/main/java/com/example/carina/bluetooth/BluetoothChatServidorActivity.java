package com.example.carina.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class BluetoothChatServidorActivity extends BluetoothChatClienteActivity implements ChatController.ChatListener {
    private static final UUID uuid = UUID.fromString("a503589d-f0b1-4761-8590-56772729a004");
    private BluetoothServerSocket serverSocket;
    private boolean running;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intentVisivel = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        intentVisivel.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(intentVisivel);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new ChatThread().start();
    }

    class ChatThread extends Thread{
        @Override
        public void run() {
            super.run();
            try{
                serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord("Treinaweb: ", uuid);
                BluetoothSocket socket = serverSocket.accept();

                if(socket != null){
                    final BluetoothDevice device = socket.getRemoteDevice();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getSupportActionBar().setTitle("Conecta: " + device.getName());
                            btnEnviar.setEnabled(true);
                            Toast.makeText(getBaseContext(), "Conectou: " + device.getName(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    chat = new ChatController(socket, BluetoothChatServidorActivity.this);
                    chat.star();
                }
            }catch (IOException e){
                running = false;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(serverSocket != null){
            try {
                serverSocket.close();
            } catch (IOException e) {

            }
        }
    }
}
