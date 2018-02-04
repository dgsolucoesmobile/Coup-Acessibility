package com.dgsm.acessibilitycoup.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.dgsm.acessibilitycoup.R;
import com.dgsm.acessibilitycoup.ReadCardActivity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Info extends AppCompatActivity {

    private static final String TAG = "Info";
    private Button btLer, btAtributos, btDicas;

    NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_info);

        setTitle("Tela ler informações das Cartas");
        checkPermissionNFC();
        mCasts();
        verificaNFC();

        btLer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dados(1);
            }
        });

        btAtributos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dados(4);
            }
        });

        btDicas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dados(5);
            }
        });

    }

    public void verificaNFC(){

        //Check for available NFC Adapter
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);


        if (mNfcAdapter == null){
            Toast.makeText(this, "O dispositivo não possui NFC!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if((!mNfcAdapter.isEnabled())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

                Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
                startActivity(intent);
            } else {
                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                startActivity(intent);
            }
            Log.i(TAG,"Pede para ativar o NFC!");
        }else
            Log.i(TAG,"O NFC já está ativado!");
    }

    private void mCasts(){
        Log.i(TAG,"mCasts");
        btLer = findViewById(R.id.btLerCarta);
        btAtributos = findViewById(R.id.btAtributos);
        btDicas = findViewById(R.id.btDicas);
    }

    private void dados(int dados){
        Intent intent = new Intent(this, ReadCardActivity.class);
        intent.putExtra("botao",dados);
        startActivity(intent);
    }

    /*Recebe os dados passados pela Activity de ReadCardActivity*/
    private int recebeDados(){

        Bundle extra = getIntent().getExtras();

        int dados;

        if(extra != null){
            dados = extra.getInt("mDados");
            Log.i(TAG,"RECEBE_DADOS: "+dados);
            return dados;
        }
        return 0;
    }

    private void checkPermissionNFC(){

        Log.i(TAG,"Entrou nas permissões!");

        if (ActivityCompat.checkSelfPermission(this,NFC_SERVICE) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.NFC)){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.NFC},0);
            }else
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.NFC},0);
        }
    }

}
