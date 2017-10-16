package com.example.carina.myapplication;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    NfcAdapter nfcAdapter;
    NfcHelper nfcHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcHelper = new NfcHelper(this);

        if(nfcAdapter != null){
            Toast.makeText(this, "Dispositivo com suporte ao NFC", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Dispositivo som suporte ao NFC", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();

        Intent intent = new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);
        IntentFilter[] intentFilters = new IntentFilter[]{};

        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);
    }

    @Override
    protected void onPause(){
        super.onPause();
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected  void onNewIntent(Intent intent){
        super.onNewIntent(intent);

        /*try{
            if(isNfcIntent(intent)){
                NdefRecord registroVazio = new NdefRecord(NdefRecord.TNF_EMPTY, new byte[]{}, new byte[]{}, new byte[]{});
                NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{registroVazio});

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

                if(escreverNdefMessage(tag, ndefMessage)){
                    Toast.makeText(this, "Mensagem escrita com sucesso!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Não foi possível gravar!", Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e){
            Log.e("Error: ", e.getMessage());
        }*/

        try{
            if(nfcHelper.isNfcIntent(intent)){
                NdefMessage ndefMessage = nfcHelper.getNdefMessageFromIntent(intent);
                if(ndefMessage != null){
                    NdefRecord ndefRecord = nfcHelper.getFirstRecord(ndefMessage);

                    if(ndefRecord != null){
                        Toast.makeText(this, String.format("Registro NDEF encontrado! tamanho = %s, tipo = %s", ndefRecord.getPayload().length, ndefRecord.getType()),Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this, "Não encontrou arquivo NDEF!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "NDEF Message vazio!", Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e){
            Log.e("Error: ", e.getMessage());
        }
    }
}
