package com.example.carina.materialdesign;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ToolbarActivity extends AppCompatActivity implements View.OnClickListener {
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Toolbar");
        toolbar.setSubtitle("Material Toolbar");
        toolbar.setLogo(R.mipmap.ic_launcher);

        ImageView imageView = (ImageView) findViewById(R.id.imgAnd);
        registerForContextMenu(imageView);
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.caixa_dialogo);

        TextView txt_canel = (TextView) dialog.findViewById(R.id.txt_cancel);
        txt_canel.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_download:
                Toast.makeText(this, "Clicou no Download", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_novo:
                dialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_contextual, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_compartilhar:
                Toast.makeText(this, "Clicou no compartilhar", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_excluir:
                dialog.show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_cancel:
                dialog.dismiss();
                break;
        }
    }
}
