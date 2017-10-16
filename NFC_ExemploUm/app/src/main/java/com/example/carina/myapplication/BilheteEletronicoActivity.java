package com.example.carina.myapplication;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class BilheteEletronicoActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnRecarregar, btnPagar;
    private EditText edtTextValor;
    private NfcAdapter nfcAdapter;
    private NfcHelper nfcHelper;
    private ProgressDialog progressDialog;
    private boolean recarregar = false;
    private boolean pagar  = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bilhete_eletronico);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcHelper = new NfcHelper(this);

        btnRecarregar = (Button) findViewById(R.id.btnRecarregar);
        btnRecarregar.setOnClickListener(this);

        btnPagar = (Button) findViewById(R.id.btnPagarPassagem);
        btnPagar.setOnClickListener(this);

        edtTextValor = (EditText) findViewById(R.id.edtTextValor);
    }

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);

        if(nfcHelper.isNfcIntent(intent)) {
            if (recarregar) {
                String valorRecarga = edtTextValor.getText().toString();
                String valorS = lerValor(intent);

                if(valorS == null){
                    if(gravarValor(valorRecarga, intent)){
                        Toast.makeText(this, "Recarga efetuada com sucesso! Saldo: " + valorRecarga, Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this, "Não foi possível gravar!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Float novoSaldo = Float.parseFloat(valorRecarga) + Float.parseFloat(valorS);

                    if(gravarValor(Float.toString(novoSaldo), intent)){
                        Toast.makeText(this, "Recarga efetuada com sucesso! Saldo: " + Float.toString(novoSaldo), Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this, "Não foi possível gravar!", Toast.LENGTH_SHORT).show();
                    }
                }

                progressDialog.dismiss();
                recarregar = false;

            } else if(pagar){
                String valorS = lerValor(intent);
                Float valorNoBilhete = Float.parseFloat(valorS);
                Float novoSaldo = valorNoBilhete - 4.00f;

                if(gravarValor(Float.toString(novoSaldo), intent)){
                    Toast.makeText(this, "Passe pago! Saldo: " + Float.toString(novoSaldo), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Não foi possível pagar!", Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();
                pagar = false;
            }
        }
    }

    private boolean gravarValor(String valor, Intent intent){
        NdefRecord registroTexto = nfcHelper.criarRegistroText(valor);
        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{registroTexto});

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        return nfcHelper.escreverNdefMessage(tag,ndefMessage);
    }

    private String lerValor(Intent intent){
        try {

            NdefMessage ndefMessage = nfcHelper.getNdefMessageFromIntent(intent);
            if (ndefMessage != null) {
                NdefRecord ndefRecord = nfcHelper.getFirstRecord(ndefMessage);

                if (ndefRecord != null) {
                    byte[] payload = ndefRecord.getPayload();

                    int languageSize = payload[0] & 0063;
                    String conteudo = new String(payload, languageSize +1, payload.length - languageSize -1, "UTF-8");
                    return conteudo;
                }else{
                    Toast.makeText(this, "Não encontrou arquivo NDEF!", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "NDEF Message vazio!", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            Log.e("Error: ", e.getMessage());
        }

        return  null;
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        IntentFilter[] intentFilters = new IntentFilter[]{};

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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRecarregar:
                String url = edtTextValor.getText().toString();

                if(url.isEmpty()){
                    Toast.makeText(this, "Informe um valor", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog = ProgressDialog.show(this, "Aguardando", "Tap on Tag", false, true, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        recarregar = false;
                    }
                });

                recarregar = true;
                pagar = false;
                break;

            case R.id.btnPagarPassagem:
                progressDialog = ProgressDialog.show(this, "Aguardando", "Tap on Tag", false, true, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        pagar = false;
                    }
                });

                pagar = true;
                recarregar = false;
                break;
        }
    }

}
