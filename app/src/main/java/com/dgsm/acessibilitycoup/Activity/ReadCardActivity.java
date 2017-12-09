package com.dgsm.acessibilitycoup.Activity;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dgsm.acessibilitycoup.R;

import java.util.Locale;

import tardigrade.Tardigrade;
import tardigrade.deck.ICard;
import tardigrade.resources.impl.Deck;
import tardigrade.resources.impl.Hub;

public class ReadCardActivity extends AppCompatActivity {

    Tardigrade game;

    private Deck deck = null;
    private Hub  hub  = null;

    Button btVoltar;

    NfcAdapter mNfcAdapter;
    TextToSpeech textToSpeech;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        game = Tardigrade.getInstance(ReadCardActivity.this);
        deck = Deck.getInstance(ReadCardActivity.this);
        hub  = Hub.getInstance(ReadCardActivity.this);

        //Casts
        btVoltar = (Button)findViewById(R.id.btVoltar);

        verificaNFC();
        verificaTTS();

        btVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReadCardActivity.this, MenuActivity.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(requestCode == 0){
            if(resultCode == RESULT_OK){
                String result = intent.getStringExtra("SCAN_RESULT");
                ICard card = deck.getCard(result);
                card.execute();
            }
        }
    }

    public void N(final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ReadCardActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }


    /* ************************************ TAG NFC ***********************************************/

    public void verificaTTS(){
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR){
                    textToSpeech.setLanguage(Locale.getDefault());

                    //informaBotaoClicado(recebeDados());

                    speechMyText("APROXIME A CARTA DO CELULAR PARA FAZER A LEITURA!!!");


                }
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

        if(( mNfcAdapter != null ) && ( !mNfcAdapter.isEnabled() )){
            Toast.makeText(this, "Pede para Ativar", Toast.LENGTH_SHORT).show();
        }

        else
            Toast.makeText(this, "Já está ativado", Toast.LENGTH_SHORT).show();
    }

    //Identifica a TAG NFC
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Toast.makeText(this, "Carta lida", Toast.LENGTH_SHORT).show();
        String tagContentID = ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)).toString();
        recebeDados();
        chamaMetodos(recebeDados(),tagContentID);
        //readDescription(tagContentID);
    }

    //Converte o ID da TAG
    private String ByteArrayToHexString(byte [] inarray) {
        int i, j, in;
        String [] hex = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        String out= "";

        for(j = 0 ; j < inarray.length ; ++j)
        {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }

    //Nome das Cartas
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void readNameCard(String id){

        String nameCard;

        switch (id){

            //tag 01
            case "04B2CBF2F54880":{
                nameCard = "CARTA: DUQUE!!!!";
                speechMyText(nameCard);
                break;
            }

            //tag 02
            case "04E60DB2D94980":{
                nameCard = "CARTA: Assassino!!!";
                speechMyText(nameCard);
                break;
            }

            //tag 03
            case "049ECBF2F54880":{
                nameCard = "CARTA: Condessa!!!";
                speechMyText(nameCard);
                break;
            }

            //tag 04
            case "04BD0EB2D94980":{
                nameCard = "CARTA: Capitão!!!";
                speechMyText(nameCard);
                break;
            }

            //tag 05
            case "04AE0DB2D94980":{
                nameCard = "CARTA: Embaixador!!!";
                speechMyText(nameCard);
                break;
            }

            //tag desconhecida
            default:{
                nameCard = "A CARTA SELECIONADA NÃO ESTÁ CADASTRADA!!!";
                speechMyText(nameCard);
            }
        }

    }

    //Dados das Cartas
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void readDescription(String id){


        String nomeCarta, descCarta;

        switch (id){

            //tag 01
            case "04B2CBF2F54880":{
                descCarta = "Pegue três moedas do Tesouro Central!!! E Bloqueie o pedido de ajuda externa de outro jogador.";
                speechMyText(descCarta);
                break;
            }

            //tag 02
            case "04E60DB2D94980":{
                descCarta = "Pague três moedas e tente assassinar outro jogador.";
                speechMyText(descCarta);
                break;
            }

            //tag 03
            case "049ECBF2F54880":{
                descCarta = "Bloqueie uma tentativa de assassinato contra você.";
                speechMyText(descCarta);
                break;
            }

            //tag 04
            case "04BD0EB2D94980":{
                descCarta = "Pegue duas moedas de outro jogador!!! ou bloqueie outro jogador que tente pegar moedas de você.";
                speechMyText(descCarta);
                break;
            }

            //tag 05
            case "04AE0DB2D94980":{
                descCarta = "Pegue duas cartas do Baralho da Corte!!! Troque de zero a duas cartas!!! Com as suas cartas viradas para baixo!!! e devolva duas cartas para o baralho.!!!";
                speechMyText(descCarta);
                break;
            }

            //tag desconhecida
            default:{
                descCarta = "A CARTA SELECIONADA NÃO ESTÁ CADASTRADA!!!";
                speechMyText(descCarta);
            }
        }
    }

    //Usa o TTS para falar a descrição das cartas
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void speechMyText(String texto){
        textToSpeech.speak(texto,TextToSpeech.QUEUE_FLUSH,null,null);
    }

    //Códigos extras
    private void enableForegroundDispatchSystem(){

        Intent intent = new Intent(this,ReadCardActivity.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);
        IntentFilter[] intentFilters = new IntentFilter[]{};

        mNfcAdapter.enableForegroundDispatch(this,pendingIntent,intentFilters,null);
    }

    private void disableForegroundDispatchSystem(){
        mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onResume() {

        enableForegroundDispatchSystem();
        super.onResume();
    }

    @Override
    protected void onPause() {
        disableForegroundDispatchSystem();
        super.onPause();
    }

    public int recebeDados(){

        Bundle extra = getIntent().getExtras();
        int dados;

        if(extra != null){

            dados = extra.getInt("botao");
            return dados;

        }
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void chamaMetodos(int dados, String id){

        switch (dados){
            case 1:{ //ler carta
                Toast.makeText(this, "Entrou no Ler Carta", Toast.LENGTH_SHORT).show();
                readNameCard(id);
                break;
            }

            case 2:{ //atributos
                Toast.makeText(this, "Entrou no Ler Descrição", Toast.LENGTH_SHORT).show();
                readDescription(id);
                break;
            }

            case 3:{
                Toast.makeText(this, "DICAS: não faz nada ainda!", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void informaBotaoClicado(int dados){

        switch (dados){
            case 1:{
                speechMyText("LER NOME DA CARTA!!!");
                break;
            }

            case 2:{
                speechMyText("LER DESCRIÇÃO DA CARTA!!!");
                break;
            }

            case 3:{
                speechMyText("LER DICAS DO JOGO!!!");
                break;
            }
        }

    }
}
