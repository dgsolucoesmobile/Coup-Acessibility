package com.dgsm.acessibilitycoup.Activity;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

    private final String TAG = "ReadCardActivity";
    private Tardigrade game;
    private Deck deck = null;
    private Hub  hub  = null;
    private Button btVoltar;

    //NFC e TextToSpeech
    private NfcAdapter mNfcAdapter;
    private TextToSpeech textToSpeech;

    AlertDialog alertDialog1;
    CharSequence[] values = {" Duque "," Assassino "," Condessa "," Capitão "," Embaixador "};
    private static final String ARQUIVO_CARTAS = "ArquivoCartas";

    private String cartaCadastrada = "Carta cadastrada com sucesso!";

    private ICard card;

    /*Usados para recuperar nome e descrição das cartas, através do CSV*/
    private String nameCard;
    private String descriptionCard;

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
        if (requestCode == 0){
            if (resultCode == RESULT_OK){
                String result = intent.getStringExtra("SCAN_RESULT");
                ICard card = deck.getCard(result);
                card.execute();
            }
        }
    }

    /*Mostra Mensagens Toast*/
    public void N(final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ReadCardActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /* ************************************ TAG NFC ***********************************************/

    public void verificaNFC(){

        //Check for available NFC Adapter
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);


        if (mNfcAdapter == null){
            Toast.makeText(this, "O dispositivo não possui NFC!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if((!mNfcAdapter.isEnabled())){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
                startActivity(intent);
            } else {
                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                startActivity(intent);
            }
            Log.i(TAG,"Pede para ativar o NFC!");
        }

        else
            Log.i(TAG,"O NFC já está ativado!");
    }

    //Identifica a TAG NFC
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Log.i(TAG,"Carta lida");
        String tagContentID = ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)).toString();
        recebeDados();
        chamaMetodos(recebeDados(),tagContentID);
    }

    /* ******************************* TEXT TO SPEECH ******************************************/
    public void verificaTTS(){
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR){
                    textToSpeech.setLanguage(Locale.getDefault());
                    speechMyText("APROXIME A CARTA DO CELULAR PARA FAZER A LEITURA!!!");
                }
            }
        });
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

        String mDuque = recuperarDados("duque");
        String mAssassino = recuperarDados("assassino");
        String mCondessa = recuperarDados("condessa");
        String mCapitao = recuperarDados("capitao");
        String mEmbaixador = recuperarDados("embaixador");

        if (mDuque.equals(id)){
            Log.i(TAG,"mDuque: "+id);
            recuperaNomeCartaCSV("1");
        }

        if (mAssassino.equals(id)){
            Log.i(TAG,"mAssassino: "+id);
            recuperaNomeCartaCSV("2");
        }

        if (mCondessa.equals(id)){
            Log.i(TAG,"mCondessa: "+id);
            recuperaNomeCartaCSV("3");
        }

        if (mCapitao.equals(id)){
            Log.i(TAG,"mCapitao: "+id);
            recuperaNomeCartaCSV("4");
        }

        if (mEmbaixador.equals(id)){
            Log.i(TAG,"mEmbaixador: "+id);
            recuperaNomeCartaCSV("5");
        }

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void recuperaNomeCartaCSV(String idCsv){
        card = deck.getCard(idCsv);
        nameCard = card.getName();
        speechMyText(nameCard);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void recuperaDescricaoCartaCSV(String idCsv){
        card = deck.getCard(idCsv);
        descriptionCard = card.getDescription();
        speechMyText(descriptionCard);
    }

    //Dados das Cartas
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void readDescription(String id){

        String mDuque = recuperarDados("duque");
        String mAssassino = recuperarDados("assassino");
        String mCondessa = recuperarDados("condessa");
        String mCapitao = recuperarDados("capitao");
        String mEmbaixador = recuperarDados("embaixador");

        if (mDuque.equals(id)){
            Log.i(TAG,"mDuque: "+id);
            N("Duque");
            recuperaDescricaoCartaCSV("1");
        }

        if (mAssassino.equals(id)){
            Log.i(TAG,"mAssassino: "+id);
            N("Assassino");
            recuperaDescricaoCartaCSV("2");
        }

        if (mCondessa.equals(id)){
            Log.i(TAG,"mCondessa: "+id);
            N("Condessa");
            recuperaDescricaoCartaCSV("3");
        }

        if (mCapitao.equals(id)){
            Log.i(TAG,"mCapitao: "+id);
            N("Capitão");
            recuperaDescricaoCartaCSV("4");
        }

        if (mEmbaixador.equals(id)){
            Log.i(TAG,"mEmbaixador: "+id);
            N("Embaixador");
            recuperaDescricaoCartaCSV("5");
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
            Log.i(TAG,"RECEBE_DADOS: "+dados);
            return dados;

        }
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void chamaMetodos(int dados, String id){

        switch (dados){
            case 1:{ //ler carta
                Log.i(TAG,"Entrou no Ler Carta: "+id);
                readNameCard(id);
                break;
            }

            case 2:{ //atributos
                Log.i(TAG,"Entrou no Ler Descrição: "+id);
                readDescription(id);
                break;
            }

            case 3:{
                Log.i(TAG,"Não faz nada ainda - Button Dicas");
                break;
            }

            case 4:{
                Log.i(TAG,"Cadastra carta");
                createAlertDialogWithRadioButtonGroup(id);
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

            case 4:{
                Log.i(TAG,"CADASTRA CARTAS");
            }
        }

    }

    public void createAlertDialogWithRadioButtonGroup(final String tagID) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Selecione a carta que deseja atribuir a TAG!");

        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                switch (item) {
                    case 0: {
                        Log.i(TAG, "Duque");
                        salvarDados("duque", tagID);
                        break;
                    }
                    case 1: {
                        Log.i(TAG, "Assassino");
                        salvarDados("assassino", tagID);
                        break;
                    }
                    case 2: {
                        Log.i(TAG, "Condessa");
                        salvarDados("condessa", tagID);
                        break;
                    }
                    case 3: {
                        Log.i(TAG, "Capitão");
                        salvarDados("capitao", tagID);
                        break;
                    }
                    case 4: {
                        Log.i(TAG, "Embaixador");
                        salvarDados("embaixador", tagID);
                        break;
                    }
                }
                alertDialog1.dismiss();
            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();
    }

    private void salvarDados(String nomeCarta, String tagCarta){

        limpaDadosSharedPreferences(tagCarta);

        SharedPreferences preferences = getSharedPreferences(ARQUIVO_CARTAS, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(nomeCarta, tagCarta);
        editor.commit();
        Toast.makeText(this, "Carta "+nomeCarta.toUpperCase()+" cadastrada com sucesso!", Toast.LENGTH_SHORT).show();
        Log.i(TAG,"Nome Carta: "+nomeCarta+"\tTAG ID: "+tagCarta);
    }

    private String recuperarDados(String nameCard){
        SharedPreferences preferences = getSharedPreferences(ARQUIVO_CARTAS, MODE_PRIVATE);
        String cardID = preferences.getString(nameCard,"Carta não cadastrada!");
        //Toast.makeText(this, "Carta Desconhecida!", Toast.LENGTH_SHORT).show();
        return cardID;
    }

    private void limpaDadosSharedPreferences(String removerCarta){
        SharedPreferences preferences = getSharedPreferences(ARQUIVO_CARTAS, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(removerCarta);
        editor.commit();
    }

}
