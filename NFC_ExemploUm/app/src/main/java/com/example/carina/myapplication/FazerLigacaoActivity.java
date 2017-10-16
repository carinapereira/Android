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
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.nio.charset.Charset;
import java.util.Arrays;

public class FazerLigacaoActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnLigar;
    private EditText edtNumero;
    private NfcAdapter nfcAdapter;
    private NfcHelper nfcHelper;
    private ProgressDialog progressDialog;
    private boolean escrevendoMsg = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fazer_ligacao);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcHelper = new NfcHelper(this);

        btnLigar = (Button) findViewById(R.id.btnLigar);
        btnLigar.setOnClickListener(this);
        edtNumero = (EditText) findViewById(R.id.editNumero);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        IntentFilter[] intentFilters = new IntentFilter[]{};

        /*DECLARAR NO ARQUIVO MANIFEST AS TECNOLOGIAS QUE A APLICAÇÃO QUER MONITORAR
           <intent-filter>
                <action android:name="android.nfc.action.NDEF_DISCOVERED"/>
                <category android:name="android.intent.category.DEFAULT"/>
            </intent-filter>
            <intent-filter>
                <action android:name="android.nfc.action.TAG_DISCOVERED"/>
            </intent-filter>*/

        String[][] techList = new String[][]{{android.nfc.tech.Ndef.class.getName()},{android.nfc.tech.NdefFormatable.class.getName()}};
        //verificar se está utilizando um emulador, caso esteja limpar, pois pode dar problemas
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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (escrevendoMsg) {
            if (nfcHelper.isNfcIntent(intent)) {
                String url = edtNumero.getText().toString();
                NdefRecord registro = nfcHelper.criarRegistroUri(url);

                NdefRecord aar = NdefRecord.createApplicationRecord(this.getPackageName());

                NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{registro, aar});

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

                if (nfcHelper.escreverNdefMessage(tag, ndefMessage)) {
                    if (nfcHelper.escreverNdefMessage(tag, ndefMessage)) {
                        Toast.makeText(this, "Mensagem escrita com sucesso!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Não foi possível gravar!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            progressDialog.dismiss();
            escrevendoMsg = false;
        } else {
            try {
                if (nfcHelper.isNfcIntent(intent)) {
                    NdefMessage ndefMessage = nfcHelper.getNdefMessageFromIntent(intent);
                    if (ndefMessage != null) {
                        NdefRecord ndefRecord = nfcHelper.getFirstRecord(ndefMessage);

                        if (ndefRecord != null) {

                            byte[] payload = ndefRecord.getPayload();
                            byte[] urlCompleta = Arrays.copyOfRange(payload, 1, payload.length);
                            //foi incluido o (tel) na frente da url para não precisar alterar o metodo criarRegistroUri
                            // alterando o codigo  payload[0] = 0x00;
                            Uri uri = Uri.parse("tel:"+new String(urlCompleta, Charset.forName("UTF-8")));

                            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},0);
                            }

                            startActivity(new Intent(Intent.ACTION_CALL, uri));

                            //utilizar somente apartir da api 16 >
                            //essa linha substitui todas as acima
                            //startActivity(new Intent(Intent.ACTION_VIEW, ndefRecord.toUri()));

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLigar:
                String url = edtNumero.getText().toString();

                if(url.isEmpty()){
                    Toast.makeText(this, "Informe um número", Toast.LENGTH_SHORT).show();
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
