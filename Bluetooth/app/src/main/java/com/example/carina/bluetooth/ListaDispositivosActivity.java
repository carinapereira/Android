package com.example.carina.bluetooth;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListaDispositivosActivity extends BluetoothCheckActivity implements AdapterView.OnItemClickListener {
    protected List<BluetoothDevice> listaDispositivos;
    private ListView listViewDispositivo;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_dispositivos);

        listViewDispositivo = (ListView) findViewById(R.id.ListViewDispositivos);

        if(bluetoothAdapter != null){
            listaDispositivos = new ArrayList<>(bluetoothAdapter.getBondedDevices());

            this.registerReceiver(receiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED));
            this.registerReceiver(receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
            this.registerReceiver(receiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(bluetoothAdapter != null){
            if(bluetoothAdapter.isDiscovering()){
                bluetoothAdapter.cancelDiscovery();
            }

            bluetoothAdapter.startDiscovery();
            progressDialog = ProgressDialog.show(this,"Buscando...", "Buscando dispositivos Bluetooth",false,true);
        }
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        private int count;

        @Override
        public void onReceive(Context context, Intent intent) {
            String acao = intent.getAction();

            if(BluetoothDevice.ACTION_FOUND.equals(acao)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    listaDispositivos.add(device);
                    Toast.makeText(context, "Encontrou: " + device.getName(), Toast.LENGTH_SHORT).show();
                    count++;
                }
            }else if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(acao)) {
                count = 0;
                Toast.makeText(context, "Busca iniciada! ", Toast.LENGTH_SHORT).show();

            }else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(acao)){
                    Toast.makeText(context, "Busca finalizada. " + count + " dispositivos encontrado!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    AtualizarLista();
            }
        }
    };

    private void AtualizarLista(){
        List<String> nomes = new ArrayList<>();

        for(BluetoothDevice device: listaDispositivos){
            boolean pareado = device.getBondState() == BluetoothDevice.BOND_BONDED;
            nomes.add(device.getName()+ " - " + device.getAddress() + (pareado ? " - pareado" : "- novo"));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nomes);
        listViewDispositivo.setAdapter(adapter);
        listViewDispositivo.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BluetoothDevice device = listaDispositivos.get(position);
        String mag = device.getName()+" - "+device.getAddress();
        Toast.makeText(this, mag, Toast.LENGTH_LONG).show();

        Intent i = new Intent(this, BluetoothChatClienteActivity.class);
        i.putExtra(BluetoothDevice.EXTRA_DEVICE, device);
        startActivity(i);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(bluetoothAdapter != null){
            bluetoothAdapter.cancelDiscovery();
            this.unregisterReceiver(receiver);
        }
    }
}
