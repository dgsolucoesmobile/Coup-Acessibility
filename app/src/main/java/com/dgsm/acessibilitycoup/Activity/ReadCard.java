//package com.dgsm.acessibilitycoup.Activity;
//
//import android.annotation.TargetApi;
//import android.app.PendingIntent;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.SharedPreferences;
//import android.nfc.NfcAdapter;
//import android.os.Build;
//import android.os.Bundle;
//import android.provider.Settings;
//import android.speech.tts.TextToSpeech;
//import android.support.annotation.RequiresApi;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.Toast;
//
//import com.afollestad.materialdialogs.MaterialDialog;
//import com.dgsm.acessibilitycoup.R;
//import com.dgsm.acessibilitycoup.Utils.RegrasJogo;
//
//import java.util.Locale;
//
//import tardigrade.Tardigrade;
//import tardigrade.deck.ICard;
//import tardigrade.resources.impl.Deck;
//
//public class ReadCard extends AppCompatActivity {
//
//
//    private final String TAG = "ReadCard";
//    private Tardigrade game;
//    private Deck deck = null;
//
//    /*Componente botão*/
//    private Button btVoltar;
//
//    //NFC e TextToSpeech
//    private NfcAdapter mNfcAdapter;
//    private TextToSpeech textToSpeech;
//
//
//    /**/
//    AlertDialog alertDialog1;
//    CharSequence[] values = {" 1º Duque "     ,      " 2º Duque " ,      " 3º Duque ",
//                             " 1º Assassino " ,  " 2º Assassino " ,  " 3º Assassino ",
//                             " 1º Condessa "  ,   " 2º Condessa " ,   " 3º Condessa ",
//                             " 1º Capitão "   ,    " 2º Capitão " ,    " 3º Capitão ",
//                             " 1º Embaixador ", " 2º Embaixador " , " 3º Embaixador "};
//
//    /*Variável do Arquivo gerado do SharedPreferences*/
//    private static final String ARQUIVO_CARTAS = "ArquivoCartas";
//
//    private ICard card;
//
//    /*Usados para recuperar nome e descrição das cartas, através do CSV*/
//    private String nameCard;
//    private String descriptionCard;
//
//    /*Usados para recuperar os dados do SharedPreferences*/
//    private String[] mDuque      = new String[3];
//    private String[] mAssassino  = new String[3];
//    private String[] mCondessa   = new String[3];
//    private String[] mCapitao    = new String[3];
//    private String[] mEmbaixador = new String[3];
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//        setContentView(R.layout.activity_main);
//
//        game = Tardigrade.getInstance(ReadCard.this);
//        deck = Deck.getInstance(ReadCard.this);
//
//        //Casts
//        btVoltar = findViewById(R.id.btVoltar);
//
//        if (recebeDados() == 1){
//            setTitle("Ler Carta");
//        }
//
//        //RegrasJogo reg = new RegrasJogo();
//
//        //reg.verificaNfc(this);
//
//        /*Verificações se tem Suporte a NFC e ao TTS*/
//        verificaNFC();
//        verificaTTS();
//
//        btVoltar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(ReadCard.this, Info.class));
//            }
//        });
//    }
//
//
//    private void salvarDados(String nomeCarta, String tagCarta){
//
//        limpaDadoCartaEspecifica(tagCarta);
//
//        SharedPreferences preferences = getSharedPreferences(ARQUIVO_CARTAS, MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(nomeCarta, tagCarta);
//        editor.commit();
//        N("Carta cadastrada com sucesso!");
//        Log.i(TAG,"Nome Carta: "+nomeCarta+"\tTAG ID: "+tagCarta);
//    }
//
//    private String recuperarDados(String nameCard){
//        SharedPreferences preferences = getSharedPreferences(ARQUIVO_CARTAS, MODE_PRIVATE);
//        String cardID = preferences.getString(nameCard,"Carta não cadastrada!");
//        return cardID;
//    }
//
//    private void limpaDadoCartaEspecifica(String removerCarta){
//        SharedPreferences preferences = getSharedPreferences(ARQUIVO_CARTAS, MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.remove(removerCarta);
//        editor.commit();
//    }
//
//    public void limpaTodosDadosSharedPreferences(){
//        SharedPreferences preferences = getSharedPreferences(ARQUIVO_CARTAS, MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.clear();
//        editor.commit();
//        Toast.makeText(this, "Dados limpos com sucesso!", Toast.LENGTH_SHORT).show();
//        startActivity(new Intent(ReadCard.this, Info.class));
//    }
//
//}
