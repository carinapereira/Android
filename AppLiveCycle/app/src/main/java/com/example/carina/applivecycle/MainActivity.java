package com.example.carina.applivecycle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    protected static final String tag = "Book";

    protected void onCreate(Bundle icicle){
        super.onCreate(icicle);
        Log.i(tag,getClassName() + " .onCreate() chamado: " + icicle);
    }

    protected void onStart(){
        super.onStart();
        Log.i(tag,getClassName() + " .onStart() chamado.");
    }

    protected void onRestard(){
        super.onRestart();
        Log.i(tag, getClassName() + " .onRestard() chamado.");
    }

    protected void onResume(){
        super.onResume();
        Log.i(tag, getClassName() + " .onResume() chamado.");
    }

    protected  void onPause(){
        super.onPause();
        Log.i(tag, getClassName() + " .onPause() chamado.");
    }

    @Override
    protected  void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        Log.i(tag, getClassName() + " .onSaveInstanceState() chamado.");
    }

    protected  void onStop(){
        super.onStop();
        Log.i(tag, getClassName() + " .onStop() chamado.");
    }

    protected  void onDestroy(){
        super.onDestroy();
        Log.i(tag, getClassName() + " .onDestroy() chamado.");
    }

    private String getClassName(){
        String s = getClass().getName();
        return s.substring(s.lastIndexOf("."));
    }

}
