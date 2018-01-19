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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dgsm.acessibilitycoup.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MenuActivity extends AppCompatActivity {

    private static final String TAG = "MAINACTIVITY";
    private Button btLer, btAtributos, btDicas;
    ReadCardActivity readCardActivity;

    NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        checkPermissionNFC();
        mCasts();
        verificaNFC();

        btLer.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                dados(1);
            }
        });

        btAtributos.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                dados(2);
            }
        });

        btDicas.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                dados(3);
            }
        });

    }

    public void verificaNFC(){

        Toast.makeText(MenuActivity.this, "mCasts", Toast.LENGTH_SHORT).show();
        //Check for available NFC Adapter
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);


        if (mNfcAdapter == null){
            Toast.makeText(this, "O dispositivo não possui NFC!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if((!mNfcAdapter.isEnabled())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                powerNfc(true, getApplicationContext());

                Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
                startActivity(intent);
            } else {
                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                startActivity(intent);
            }
                Toast.makeText(this, "Pede para Ativar", Toast.LENGTH_SHORT).show();
        }else
            Toast.makeText(this, "Já está ativado", Toast.LENGTH_SHORT).show();
    }

    public static boolean powerNfc(boolean isOn, Context context) {
        boolean success = false;
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(context);

        if (nfcAdapter != null) {
            Class<?> NfcManagerClass;
            Method setNfcEnabled;
            try {
                NfcManagerClass = Class.forName(nfcAdapter.getClass().getName());
                setNfcEnabled = NfcManagerClass.getDeclaredMethod(isOn
                        ? "enable" : "disable");
                setNfcEnabled.setAccessible(true);
                success = (Boolean) setNfcEnabled.invoke(nfcAdapter);
            } catch (ClassNotFoundException e) {
                Log.e(TAG, Log.getStackTraceString(e));
            } catch (NoSuchMethodException e) {
                Log.e(TAG, Log.getStackTraceString(e));
            } catch (IllegalArgumentException e) {
                Log.e(TAG, Log.getStackTraceString(e));
            } catch (IllegalAccessException e) {
                Log.e(TAG, Log.getStackTraceString(e));
            } catch (InvocationTargetException e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }
        }

        return success;
    }


    private void mCasts(){
        Toast.makeText(MenuActivity.this, "mCasts", Toast.LENGTH_SHORT).show();
        btLer = (Button) findViewById(R.id.btLerCarta);
        btAtributos = (Button) findViewById(R.id.btAtributos);
        btDicas = (Button) findViewById(R.id.btDicas);
    }

    private void dados(int dados){
        //Toast.makeText(readCardActivity, "Dados Vale: ", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MenuActivity.this, ReadCardActivity.class);
        intent.putExtra("botao",dados);
        startActivity(intent);
    }

    private void checkPermissionNFC(){

        Toast.makeText(this, "Entrou nas permissões", Toast.LENGTH_SHORT).show();

        if (ActivityCompat.checkSelfPermission(this,NFC_SERVICE) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.NFC)){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.NFC},0);
            }else
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.NFC},0);
        }
    }
}
