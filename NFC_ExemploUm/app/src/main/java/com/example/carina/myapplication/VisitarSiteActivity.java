package com.example.carina.myapplication;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.Arrays;

public class VisitarSiteActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnGravarUriSite;
    private EditText edtSite;
    private NfcAdapter nfcAdapter;
    private NfcHelper nfcHelper;
    private ProgressDialog progressDialog;
    private boolean escrevendoMsg = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitar_site);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcHelper = new NfcHelper(this);

        btnGravarUriSite = (Button) findViewById(R.id.btnGravarUriSite);
        btnGravarUriSite.setOnClickListener(this);
        edtSite = (EditText) findViewById(R.id.editTextLinkSite);
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
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);

        if(escrevendoMsg){
            if(nfcHelper.isNfcIntent(intent)){
                String url = edtSite.getText().toString();
                NdefRecord registro = nfcHelper.criarRegistroUri(url);
                NdefMessage ndefMessage = new NdefMessage(new NdefRecord[] {registro});

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

                if(nfcHelper.escreverNdefMessage(tag, ndefMessage)){
                    if(nfcHelper.escreverNdefMessage(tag, ndefMessage)){
                        Toast.makeText(this, "Mensagem escrita com sucesso!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this, "Não foi possível gravar!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            progressDialog.dismiss();
            escrevendoMsg = false;
        }else{
            try{
                if(nfcHelper.isNfcIntent(intent)){
                    NdefMessage ndefMessage = nfcHelper.getNdefMessageFromIntent(intent);
                    if(ndefMessage != null){
                        NdefRecord ndefRecord = nfcHelper.getFirstRecord(ndefMessage);

                        if(ndefRecord != null){

                            byte[] payload = ndefRecord.getPayload();
                            byte[] urlCompleta = Arrays.copyOfRange(payload, 1, payload.length);
                            Uri uri = Uri.parse(new String(urlCompleta, Charset.forName("UTF-8")));

                            startActivity(new Intent(Intent.ACTION_VIEW, uri));

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
            case R.id.btnGravarUriSite:
                String url = edtSite.getText().toString();
                if(url.isEmpty() || !URLUtil.isValidUrl(url)){
                    Toast.makeText(this, "URL inválida", Toast.LENGTH_SHORT).show();
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
