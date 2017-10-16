package com.example.carina.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Parcelable;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

/**
 * Created by carina on 14/10/2017.
 */

public class NfcHelper {

    private Activity activity;
    private NfcAdapter nfcAdapter;

    public NfcHelper(Activity activity){
        this.activity = activity;
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(activity);
    }

    public boolean isNfcIntent(Intent intent){
        return intent.hasExtra(NfcAdapter.EXTRA_TAG);
    }

    public boolean formatarTag(Tag tag, NdefMessage ndefMessage){
        try{
            NdefFormatable ndefFormatable = NdefFormatable.get(tag);
            if(ndefFormatable != null){
                ndefFormatable.connect();
                ndefFormatable.format(ndefMessage);
                ndefFormatable.close();
                return true;
            }
        }catch (Exception e){
            Log.e("Error: ", e.getMessage());
        }
        return  false;
    }

    public boolean escreverNdefMessage(Tag tag, NdefMessage ndefMessage){
        try{
            if(tag != null){
                Ndef ndef = Ndef.get(tag);

                if (ndef == null){
                    return formatarTag(tag, ndefMessage);
                }else{
                    ndef.connect();
                    if(ndef.isWritable()){
                        ndef.writeNdefMessage(ndefMessage);
                        ndef.close();
                        return true;
                    }
                    ndef.close();
                }
            }
        }catch (Exception e){
            Log.e("Error: ", e.getMessage());
        }
        return  false;
    }

    public NdefMessage getNdefMessageFromIntent(Intent intent){
        NdefMessage ndefMessage = null;

        Parcelable[] extra = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if(extra != null && extra.length > 0){
            ndefMessage = (NdefMessage) extra[0];
        }

        return ndefMessage;
    }

    public NdefRecord getFirstRecord(NdefMessage ndefMessage){
        NdefRecord ndefRecord = null;

        NdefRecord[] ndefRecords = ndefMessage.getRecords();

        if(ndefRecords != null && ndefRecords.length > 0){
            ndefRecord = ndefRecords[0];
        }

        return ndefRecord;
    }

    public NdefRecord criarRegistroUri(String uri){
        NdefRecord registroUri = null;

        try{
            byte[] uriDados;
            uriDados = uri.getBytes("UTF-8");

            byte[] payload = new byte[uriDados.length + 1];
            payload[0] = 0x00;

            System.arraycopy(uriDados, 0, payload, 1, uriDados.length);

            registroUri = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_URI, new byte[0], payload);
        }catch (Exception e){
            Log.e("criarRegistroUri: ", e.getMessage());
        }

        return  registroUri;
    }

    public NdefRecord criarRegistroText(String texto){
        NdefRecord registroTexto = null;

        try{
            byte[] language = Locale.getDefault().getLanguage().getBytes("UTF-8");
            byte[] conteudo = texto.getBytes("UTF-8");
            int languageSize = language.length;
            int conteudoSize = conteudo.length;

            ByteArrayOutputStream payload = new ByteArrayOutputStream(1 + languageSize + conteudoSize);
            payload.write((byte) (languageSize & 0x1f));
            payload.write(language, 0, languageSize);
            payload.write(conteudo, 0, conteudoSize);

            registroTexto = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload.toByteArray());
        } catch (Exception e){
            Log.e("criarRegistroText: ", e.getMessage());
        }

        return registroTexto;
    }
}
