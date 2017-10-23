package com.example.carina.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnVerificar, btnPareados, btnBuscar, btnVisivel, btnServidor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnVerificar = (Button) findViewById(R.id.btnVerificar);
        btnVerificar.setOnClickListener(this);

        btnPareados = (Button) findViewById(R.id.btnPareados);
        btnPareados.setOnClickListener(this);

        btnBuscar = (Button) findViewById(R.id.btnBuscarDispositivo);
        btnBuscar.setOnClickListener(this);

        btnVisivel = (Button) findViewById(R.id.btnVisivel);
        btnVisivel.setOnClickListener(this);

        btnServidor = (Button) findViewById(R.id.btnServidor );
        btnServidor.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnVerificar:
                Intent intentCheckBluetooth = new Intent(this, BluetoothCheckActivity.class);
                startActivity(intentCheckBluetooth);
                break;

            case R.id.btnPareados:
                Intent intentListaPareados = new Intent(this, ListaPareadosActivity.class);
                startActivity(intentListaPareados);
                break;

            case R.id.btnBuscarDispositivo:
                Intent intentBuscarDispositivo = new Intent(this, ListaDispositivosActivity.class);
                startActivity(intentBuscarDispositivo);
                break;

            case R.id.btnVisivel:
                Intent intentVisivel = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                intentVisivel.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                startActivity(intentVisivel);
                break;
            case R.id.btnServidor:
                Intent intentIniciarServidor = new Intent(this, BluetoothChatServidorActivity.class);
                startActivity(intentIniciarServidor);
                break;
        }
    }
}
