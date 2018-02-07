package com.dgsm.accessibilitycoup.Activity;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.dgsm.accessibilitycoup.R;

import java.util.Locale;

public class MenuActivity extends AppCompatActivity {

    NfcAdapter mNfcAdapter;
    static final String TAG = "MainActivity";
    TextToSpeech textToSpeech;

    private Button btJogar;
    private Button btCadastrar;
    private Button btApagar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_menu);

        setTitle("Menu Principal");
        verificaTTS(); //Verifica se o dispositivo tem suporte ao TTS
        verificaNFC(); //Verifica se o dispositivo tem suporte ao NFC e está ativado

        btJogar = findViewById(R.id.btJogar);
        btCadastrar = findViewById(R.id.btCadastrar);
        btApagar = findViewById(R.id.btApagar);

        btJogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dados(1);
            }
        });

        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dados(2);
            }
        });

        btApagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dados(3);
            }
        });

    }

    public void verificaNFC(){

        //Check for available NFC Adapter
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter == null){
            //Toast.makeText(this, "O dispositivo não possui NFC!", Toast.LENGTH_SHORT).show();
            speechMyText("O seu dispositivo não possui NFC!!!");
            finish();
            return;
        }

        if((!mNfcAdapter.isEnabled())) {
            speechMyText("Por favor ative o NFC do seu Dispositivo!!!");
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

    /*Verifica se o Dispositivo possui suporte ao TextToSpeech*/
    public void verificaTTS(){
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR){
                    textToSpeech.setLanguage(Locale.getDefault());
                }
            }
        });
    }

    private void speechMyText(String texto){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(texto,TextToSpeech.QUEUE_FLUSH,null,null);
        }
    }

    private void dados(int dados){
        Intent intent = new Intent(this, ReadCardActivity.class);
        intent.putExtra("botao",dados);
        startActivity(intent);
    }

}
