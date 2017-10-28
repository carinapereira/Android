package com.example.carina.notificacao;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener{
    private Button btnSimples, btnGrande, btnHeadsUP, btnProgresso, btnCancelar, btnEnviarBroadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSimples = (Button) findViewById(R.id.btnNotificacao);
        btnSimples.setOnClickListener(this);

        btnGrande = (Button) findViewById(R.id.btnNotificacaoGrande);
        btnGrande.setOnClickListener(this);

        btnHeadsUP = (Button) findViewById(R.id.btnHeadsUp);
        btnHeadsUP.setOnClickListener(this);

        btnProgresso = (Button) findViewById(R.id.btnProgresso);
        btnProgresso.setOnClickListener(this);

        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(this);

        btnEnviarBroadcast = (Button) findViewById(R.id.btnEnviarBroadcast);
        btnEnviarBroadcast.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnNotificacao:
                criarNotificacaoSimples();
                break;
            case R.id.btnNotificacaoGrande:
                criarNotificacaoGrande();
                break;
            case R.id.btnHeadsUp:
                criarNotificacaoHeadsUp();
                break;
            case R.id.btnProgresso:
                criarNotificacaoProgresso();
                break;
            case R.id.btnCancelar:
                cancelarNotificacao(2);
                break;
            case R.id.btnEnviarBroadcast:
                enviarBroadcast();
                break;
        }
    }

    public void criarNotificacaoSimples(){
        int id = 1;
        String titulo = "Título da Notificação";
        String texto = "Texto da notificação Simples";
        int icone = android.R.drawable.ic_dialog_info;

        Intent intent = new Intent(this, TextoActivity.class);
        intent.putExtra("txt", "Você abriu uma notificação simples");

        PendingIntent p = getPedingIntent(id,intent,this);

        NotificationCompat.Builder notificacao = new NotificationCompat.Builder(this);
        notificacao.setSmallIcon(icone);
        notificacao.setContentTitle(titulo);
        notificacao.setContentText(texto);
        notificacao.setContentIntent(p);
        //cancela a notificacao quando ela for selecionada
        notificacao.setAutoCancel(true);
        //habilita todos os tipos de notificaçãoes
        notificacao.setDefaults(Notification.DEFAULT_ALL);

        //customizando as notificacoes
        notificacao.setVibrate(new long[]{1000,1000,1000,1000,1000,1000});

        //habilita somente o som e luzes, voce pode especificar todos ou somente um ou alguns.
       // notificacao.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS);


        NotificationManagerCompat nm = NotificationManagerCompat.from(this);
        nm.notify(id, notificacao.build());
    }

    public void criarNotificacaoGrande(){
        int id = 2;
        String titulo = "E-mail";
        String texto = "Você possui 3 e-mails";
        int icone = android.R.drawable.ic_dialog_email;

        Intent intent = new Intent(this, TextoActivity.class);
        intent.putExtra("txt", "Você abriu uma notificação Grande");
        PendingIntent p = getPedingIntent(id,intent,this);

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(titulo);
        inboxStyle.addLine("Texto linha 1");
        inboxStyle.addLine("Texto linha 2");
        inboxStyle.addLine("Texto linha 3");
        inboxStyle.setSummaryText(texto);


        NotificationCompat.Builder notificacao = new NotificationCompat.Builder(this);
        notificacao.setSmallIcon(icone);
        notificacao.setContentTitle(titulo);
        notificacao.setContentText(texto);
        notificacao.setAutoCancel(true);

        notificacao.setContentIntent(p);
        notificacao.setStyle(inboxStyle);
        notificacao.setNumber(3);

        //habilita todos os tipos de notificaçãoes
        notificacao.setDefaults(Notification.DEFAULT_ALL);

        //customizando as notificacoes
        notificacao.setVibrate(new long[]{1000,1000,1000,1000,1000,1000});

        //habilita somente o som e luzes, voce pode especificar todos ou somente um ou alguns.
        // notificacao.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS);


        NotificationManagerCompat nm = NotificationManagerCompat.from(this);
        nm.notify(id, notificacao.build());
    }

    public void criarNotificacaoHeadsUp(){
        int id = 3;
        String titulo = "Notificação HeadsUp";
        String texto = "Texto da notificação HeadsUp";
        int icone = android.R.drawable.ic_dialog_alert;

        Intent intent = new Intent(this, TextoActivity.class);
        intent.putExtra("txt", "Você abriu uma notificação HeadsUp");
        PendingIntent p = getPedingIntent(id,intent,this);

        NotificationCompat.Builder notificacao = new NotificationCompat.Builder(this);
        notificacao.setSmallIcon(icone);
        notificacao.setContentTitle(titulo);
        notificacao.setContentText(texto);
        notificacao.setContentIntent(p);
        notificacao.setFullScreenIntent(p,false);
        notificacao.setColor(Color.BLUE);

        //habilita todos os tipos de notificaçãoes
        notificacao.setDefaults(Notification.DEFAULT_ALL);

        //customizando as notificacoes
        notificacao.setVibrate(new long[]{1000,1000,1000,1000,1000,1000});

        //habilita somente o som e luzes, voce pode especificar todos ou somente um ou alguns.
        // notificacao.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS);


        NotificationManagerCompat nm = NotificationManagerCompat.from(this);
        nm.notify(id, notificacao.build());
    }

    private PendingIntent getPedingIntent(int id, Intent intent, Context context){
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(intent.getComponent());
        stackBuilder.addNextIntent(intent);

        PendingIntent p = stackBuilder.getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT);
        return p;
    }

    public void criarNotificacaoProgresso(){
        int id = 4;
        String titulo = "Download";
        String texto = "Download efetuado";
        String textoAtivo = "Aguarde o download...";
        int icone = android.R.drawable.stat_sys_download_done;
        int iconeAtivo = android.R.drawable.stat_sys_download;

        Intent intent = new Intent(this, TextoActivity.class);
        intent.putExtra("txt", "Você abriu uma notificação de progresso");

        PendingIntent p = getPedingIntent(id,intent,this);

        NotificationCompat.Builder notificacao = new NotificationCompat.Builder(this);

        notificacao.setContentTitle(titulo);
        notificacao.setContentIntent(p);

        NotificationManagerCompat nm = NotificationManagerCompat.from(this);

        //barra em progresso
         for(int i=0; i<=100; i+=10){
            try {
                Thread.sleep(1000);
                notificacao.setSmallIcon(iconeAtivo);
                notificacao.setContentText(textoAtivo);
                notificacao.setProgress(100,i,false);
                nm.notify(id, notificacao.build());
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }

        //barra continua
        //notificacao.setProgress(0,0,true);
        notificacao.setSmallIcon(icone);
        notificacao.setContentText(texto);
        nm.notify(id, notificacao.build());
    }

    public void criarNotificacaoBroadcast(){
        int id = 5;
        String titulo = "Mensagem";
        String texto = "Clique para responder";
        int icone = android.R.drawable.ic_dialog_alert;

        Intent intent = new Intent(this, TextoActivity.class);
        intent.putExtra("txt", "Você abriu uma notificação disparada por broadcast");

        PendingIntent p = PendingIntent.getBroadcast(this, id, new Intent("ACAO_RESPONDER"), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificacao = new NotificationCompat.Builder(this);
        notificacao.setSmallIcon(icone);
        notificacao.setContentTitle(titulo);
        notificacao.setContentText(texto);
        notificacao.setContentIntent(p);
        //cancela a notificacao quando ela for selecionada
        notificacao.setAutoCancel(true);
        notificacao.addAction(android.R.drawable.ic_menu_send,"Responder",p);
        //habilita todos os tipos de notificaçãoes
        notificacao.setDefaults(Notification.DEFAULT_ALL);

        //customizando as notificacoes
        notificacao.setVibrate(new long[]{1000,1000,1000,1000,1000,1000});

        //habilita somente o som e luzes, voce pode especificar todos ou somente um ou alguns.
        // notificacao.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS);


        NotificationManagerCompat nm = NotificationManagerCompat.from(this);
        nm.notify(id, notificacao.build());
    }

    public void cancelarNotificacao(){
        NotificationManagerCompat nm = NotificationManagerCompat.from(this);
         //cancela apenas uma notificacao
        nm.cancelAll();
    }

    public void cancelarNotificacao(int id){
        NotificationManagerCompat nm = NotificationManagerCompat.from(this);
        //cancela apenas uma notificacao
        nm.cancel(id);
    }

    public void enviarBroadcast(){
        Intent intent = new Intent("ACAO_NOTIFICAR");
        sendBroadcast(intent);
    }

    public void criarCaixaResposta(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Mensagem");
        alertDialog.setMessage("Informe o texto!");
        alertDialog.setIcon(android.R.drawable.ic_dialog_email);

        final EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);

        alertDialog.setView(editText);
        alertDialog.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, editText.getText(), Toast.LENGTH_LONG).show();
            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String acao = intent.getAction();
            if(acao.equals("ACAO_NOTIFICAR")){
                criarNotificacaoBroadcast();
            }else if(acao.equals("ACAO_RESPONDER")){
                criarCaixaResposta();
            }
        }
    };

    @Override
    protected void onResume() {
        //REGISTRAR A ACAO PARA SER MONITORADA
        registerReceiver(broadcastReceiver, new IntentFilter("ACAO_NOTIFICAR"));
        registerReceiver(broadcastReceiver, new IntentFilter("ACAO_RESPONDER"));
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(broadcastReceiver);
        super.onPause();
    }

}
