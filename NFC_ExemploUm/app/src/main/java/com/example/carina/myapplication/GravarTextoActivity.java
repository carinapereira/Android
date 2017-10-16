package com.example.carina.myapplication;

import android.Manifest;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.nio.charset.Charset;
import java.util.Arrays;

public class GravarTextoActivity extends AppCompatActivity implements View.OnClickListener{
    private NfcAdapter nfcAdapter;
    private Button btnGravarTexto;
    private EditText editTexto;
    private ProgressDialog progressDialog;
    private boolean escrevendoMsg;
    private NfcHelper nfcHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gravar_texto);

        nfcAdapter = nfcAdapter.getDefaultAdapter(this);

        btnGravarTexto = (Button)findViewById(R.id.btnGravarTexto);
        btnGravarTexto.setOnClickListener(this);
        editTexto = (EditText) findViewById(R.id.edtGravarTexto);

        nfcHelper = new NfcHelper(this);
    }

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);

        if(nfcHelper.isNfcIntent(intent)) {
            if (escrevendoMsg) {
                String texto = editTexto.getText().toString();

                NdefRecord registroTexto = nfcHelper.criarRegistroText(texto);
                NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{registroTexto});

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

                if(nfcHelper.escreverNdefMessage(tag,ndefMessage)){
                    Toast.makeText(this, "Mensagem escrita com sucesso!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Não foi possível gravar!", Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();
                escrevendoMsg = false;

            } else {
                try {

                    NdefMessage ndefMessage = nfcHelper.getNdefMessageFromIntent(intent);
                    if (ndefMessage != null) {
                        NdefRecord ndefRecord = nfcHelper.getFirstRecord(ndefMessage);

                        if (ndefRecord != null) {
                            byte[] payload = ndefRecord.getPayload();

                            int languageSize = payload[0] & 0063;
                            String conteudo = new String(payload, languageSize +1, payload.length - languageSize -1, "UTF-8");
                            Toast.makeText(this, String.format("O conteudo é %s", conteudo), Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(this, "Não encontrou arquivo NDEF!", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(this, "NDEF Message vazio!", Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    Log.e("Error: ", e.getMessage());
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        IntentFilter[] intentFilters = new IntentFilter[]{};

        String[][] techList = new String[][]{{android.nfc.tech.Ndef.class.getName()},{android.nfc.tech.NdefFormatable.class.getName()}};

        if(Build.DEVICE.matches(".*generic.*")){
            techList = null;
        }

        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, techList);
    }

    @Override
    protected void onPause() {
        super.onPause();
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnGravarTexto:
                String texto = editTexto.getText().toString();

                if(texto.isEmpty()){
                    Toast.makeText(this, "Informe uma mensagem", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog = ProgressDialog.show(this, "Aguardando", "Tap on Tag", false, true, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        escrevendoMsg = false;
                    }
                });

                escrevendoMsg = true;
                break;
        }
    }
}
