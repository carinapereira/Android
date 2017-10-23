package com.example.carina.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.UUID;

public class BluetoothChatClienteActivity extends BluetoothCheckActivity implements ChatController.ChatListener, View.OnClickListener {
    protected static final UUID uuid = UUID.fromString("a503589d-f0b1-4761-8590-56772729a004");
    protected BluetoothDevice device;
    protected ChatController chat;
    protected EditText edtMsg;
    protected TextView txtMsgRecebidas;
    protected Button btnEnviar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_chat_cliente);

        edtMsg = (EditText) findViewById(R.id.edtMsg);
        txtMsgRecebidas = (TextView) findViewById(R.id.txtMsgRecebidas);
        btnEnviar = (Button) findViewById(R.id.btnEnviar);
        btnEnviar.setOnClickListener(this);
        device = getIntent().getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

        try{
            if(device != null){
                getSupportActionBar().setTitle("Conectado: " + device.getName());
                BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuid);
                socket.connect();

                chat = new ChatController(socket, this);
                chat.star();
                btnEnviar.setEnabled(true);
            }
        }catch (IOException E){

        }
    }

    @Override
    public void onMessageReceived(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String txt = txtMsgRecebidas.getText().toString();
                txtMsgRecebidas.setText(txt + "\nrecebido: " + msg);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnEnviar:
                try{
                    String msgEnviada = edtMsg.getText().toString();
                    chat.sendMessage(msgEnviada);
                    edtMsg.setText("");
                    String txt = txtMsgRecebidas.getText().toString();
                    txtMsgRecebidas.setText(txt + "\n enviado: " +msgEnviada);
                }catch (IOException e){

                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(chat != null){
            chat.stop();
        }
    }
}
