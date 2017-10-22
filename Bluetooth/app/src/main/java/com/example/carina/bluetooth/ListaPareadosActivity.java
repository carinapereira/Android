package com.example.carina.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListaPareadosActivity extends BluetoothCheckActivity implements AdapterView.OnItemClickListener {
    protected List<BluetoothDevice> listaPareadas;
    private ListView listViewPareados;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_pareados);

        listViewPareados = (ListView) findViewById(R.id.ListVariavel);
    }

    @Override
    protected void onResume(){
        super.onResume();

        listaPareadas = new ArrayList<BluetoothDevice>(bluetoothAdapter.getBondedDevices());
        List<String> nomes = new ArrayList<String>();

        for(BluetoothDevice device: listaPareadas){
            nomes.add(device.getName()+ " - " + device.getAddress());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nomes);
        listViewPareados.setAdapter(adapter);
        listViewPareados.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BluetoothDevice device = listaPareadas.get(position);
        String mag = device.getName()+" - "+device.getAddress();
        Toast.makeText(this, mag, Toast.LENGTH_LONG).show();
    }
}
