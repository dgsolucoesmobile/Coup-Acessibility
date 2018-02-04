package com.dgsm.acessibilitycoup.Activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresPermission;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.dgsm.acessibilitycoup.R;
import com.dgsm.acessibilitycoup.ReadCardActivity;

public class RegisterCardActivity extends AppCompatActivity {

    private Button btDuque;
    private Button btAssassino;
    private Button btCondessa;
    private Button btCapitao;
    private Button btEmbaixador;

    private TextToSpeech textToSpeech;

    static final String TAG = "Register Card Activity";

    boolean flag = true;
    private static final String ARQUIVO_CARTAS = "ArquivoCartas";
    private static final String ARQUIVO_CONTADORES = "ArquivoContadores";
    int contador = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_register_card);

        mCastViews();

        Log.i(TAG,"contador é: "+contador);

        btDuque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag == true){
                    Toast.makeText(RegisterCardActivity.this, "Clique duas vezes para cadastrar a carta", Toast.LENGTH_SHORT).show();
                    flag = false;
                }else{
                    if (contador == 1){
                        Toast.makeText(RegisterCardActivity.this, "Contador 1", Toast.LENGTH_SHORT).show();
                    }
                    else if (contador == 2){
                        Toast.makeText(RegisterCardActivity.this, "Contador 2", Toast.LENGTH_SHORT).show();
                    }
                    else if (contador == 3){
                        Toast.makeText(RegisterCardActivity.this, "Contador 3", Toast.LENGTH_SHORT).show();
                    }
                    flag = true;
                    contador++;
                }

                //recuperaDadosParaVariaveis();
                //Log.i(TAG,"Clique do Botão contador vale: "+cDuque);

                //cadastrandoCartas("Duque","duque1","duque2","duque3",0,recebeDados());
                //salvarContadores("contadorDuque",cDuque);
            }
        });

//        btAssassino.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                cadastrandoCartas("Assassino","assassino1","assassino2","assassino3",0);
//            }
//        });
//
//        btCondessa.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                cadastrandoCartas("Condessa","condessa1","condessa2","condessa3",0);
//            }
//        });
//
//        btCapitao.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                cadastrandoCartas("Capitão","capitao1","capitao2","capitao3",0);
//            }
//        });
//
//        btEmbaixador.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                cadastrandoCartas("Embaixador","embaixador1","embaixador2","embaixador3",0);
//            }
//        });

    }

    private void cadastrandoCartas(String nomeCarta, String nome1, String nome2, String nome3, int contador,String tagId){
        if (flag == true){
            flag = false;
            Log.i(TAG,"Entrou no IF do(a) "+nomeCarta);
            Toast.makeText(RegisterCardActivity.this, "Clique duas vezes para cadastrar a carta "+nomeCarta, Toast.LENGTH_SHORT).show();
        }else{
            flag = true;
            Log.i(TAG, "Entrou no ELSE do(a)" + nomeCarta);

            if (nomeCarta.equals("Duque")) {
                Log.i(TAG, "o Contador está valendo: " + contador);

                String btDuque1 = String.valueOf(btDuque.getText().toString());
                String btDuque2 = String.valueOf(btDuque.getText().toString());

                if (flag == true){
                    Toast.makeText(this, "Entrou no Duque 1", Toast.LENGTH_SHORT).show();
                    btDuque.setText("Duque 2");
                    flag = false;
                }else if(flag == false){
                    Toast.makeText(this, "Entrou no Duque 2", Toast.LENGTH_SHORT).show();
                    btDuque.setText("Duque 3");
                    flag = true;
                }else{
                    Toast.makeText(this, "Else do Erro", Toast.LENGTH_SHORT).show();
                }

//                if (nome1.isEmpty()) {
//                    Toast.makeText(this, "Entrou no cDuque 1", Toast.LENGTH_SHORT).show();
//                    salvarDados(nome1,tagId);
//                } else if (nome2.isEmpty()) {
//                    Toast.makeText(this, "Entrou no cDuque 2", Toast.LENGTH_SHORT).show();
//                    salvarDados(nome2,tagId);
//                } else if (nome3.isEmpty()) {
//                    Toast.makeText(this, "Entrou no cDuque 3", Toast.LENGTH_SHORT).show();
//                    salvarDados(nome3,tagId);
//                }/*else{
//                    contador = 0;
//                }*/
            }

            dados(2);
        }
        flag = false;
    }


    /*Recebe os dados passados pela Activity de Menu*/
    private String recebeDados(){

        Bundle extra = getIntent().getExtras();

        String tagID;

        if(extra != null){
            tagID = extra.getString("botao");
            Log.i(TAG,"RECEBE_DADOS: "+tagID);
            return tagID;
        }else{
            return "Erro";
        }

    }

    private void dados(int dados){
        Intent intent = new Intent(this, ReadCardActivity.class);
        intent.putExtra("botao",dados);
        startActivity(intent);
    }

    private void salvarDados(String nomeCarta, String tagCarta){

        //limpaDadoCartaEspecifica(tagCarta);

        SharedPreferences preferences = getSharedPreferences(ARQUIVO_CARTAS, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(nomeCarta, tagCarta);
        editor.commit();
        Toast.makeText(this, "Carta cadastrada com sucesso!", Toast.LENGTH_SHORT).show();
        Log.i(TAG,"Nome Carta: "+nomeCarta+"\tTAG ID: "+tagCarta);
        //startActivity(new Intent(this, ReadCardActivity.class));
    }

    private void salvarContadores(String nomeContador,int valorContador){

        //limpaDadoCartaEspecifica(tagCarta);

        SharedPreferences preferences = getSharedPreferences(ARQUIVO_CONTADORES, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(nomeContador, String.valueOf(valorContador));
        editor.commit();
        Toast.makeText(this, "CONTADOR cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
        Log.i(TAG,"Nome Contador: "+nomeContador+"\tVALOR DO CONTADOR: "+valorContador);
        //startActivity(new Intent(this, ReadCardActivity.class));
    }

    private String recuperarDados(String nameCont){
        SharedPreferences preferences = getSharedPreferences(ARQUIVO_CARTAS, MODE_PRIVATE);
        String cardCont = preferences.getString(nameCont,"Carta não cadastrada!");
        return cardCont;
    }

    private String recuperarDadosContadores(String nameCont){
        SharedPreferences preferences = getSharedPreferences(ARQUIVO_CONTADORES, MODE_PRIVATE);
        String cardCont = preferences.getString(nameCont,"Carta não cadastrada!");
        return cardCont;
    }

    String[] mContadores = new String[5];
    /*Recupera as informações do SharedPreferences para as Variáveis*/
    private void recuperaDadosParaVariaveis() {
        mContadores[0] = recuperarDadosContadores("contadorDuque");
        mContadores[1] = recuperarDadosContadores("contadorAssassino");
        mContadores[2] = recuperarDadosContadores("contadorCondessa");
        mContadores[3] = recuperarDadosContadores("contadorCapitao");
        mContadores[4] = recuperarDadosContadores("contadorEmbaixador");
    }

    private void mCastViews(){
        btDuque = findViewById(R.id.btDuque);
        btAssassino = findViewById(R.id.btAssassino);
        btCondessa = findViewById(R.id.btCondessa);
        btCapitao = findViewById(R.id.btCapitao);
        btEmbaixador = findViewById(R.id.btEmbaixador);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void speechMyText(String texto){
        textToSpeech.speak(texto, TextToSpeech.QUEUE_FLUSH,null,null);
    }

    @Override
    protected void onResume() {
        recuperaDadosParaVariaveis();
        super.onResume();
    }
}
