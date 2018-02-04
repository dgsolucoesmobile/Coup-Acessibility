package com.dgsm.acessibilitycoup;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dgsm.acessibilitycoup.Activity.Info;
import com.dgsm.acessibilitycoup.Activity.MenuActivity;
import com.dgsm.acessibilitycoup.Activity.RegisterCardActivity;
import com.dgsm.acessibilitycoup.Utils.RegrasJogo;

import java.util.Locale;

import tardigrade.Tardigrade;
import tardigrade.deck.ICard;
import tardigrade.resources.impl.Deck;

public class ReadCardActivity extends AppCompatActivity {

    private final String TAG = "ReadCardActivity";
    private Tardigrade game;
    private Deck deck = null;

    /*Componente botão*/
    private Button btVoltar;

    //NFC e TextToSpeech
    private NfcAdapter mNfcAdapter;
    private TextToSpeech textToSpeech;

    /*Variável do Arquivo gerado do SharedPreferences*/
    private static final String ARQUIVO_CARTAS = "ArquivoCartas";

    private ICard card;

    /*Usados para recuperar nome e descrição das cartas, através do CSV*/
    private String nameCard;
    private String descriptionCard;

    /*Usados para recuperar os dados do SharedPreferences*/
    private String[] mDuque      = new String[3];
    private String[] mAssassino  = new String[3];
    private String[] mCondessa   = new String[3];
    private String[] mCapitao    = new String[3];
    private String[] mEmbaixador = new String[3];

    CharSequence[] values = {" Duque ", " Assassino ", " Condessa ",
            " Capitão ", " Embaixador "};

    int[] mContador = new int[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_read_card);

        game = Tardigrade.getInstance(ReadCardActivity.this);
        deck = Deck.getInstance(ReadCardActivity.this);
        //Casts
        btVoltar = findViewById(R.id.btVoltar);

        //Verifica o NFC e TTS
        verificaNFC();
        verificaTTS();

        setTitleActivity(recebeDados());

        btVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recebeDados() == 1 || recebeDados() == 2 || recebeDados() == 3)
                    startActivity(new Intent(ReadCardActivity.this, MenuActivity.class));
                else if (recebeDados() == 4 || recebeDados() == 5 || recebeDados() == 5)
                    startActivity(new Intent(ReadCardActivity.this, Info.class));
            }
        });
    }

    /*Seta o titulo da Activity*/
    private void setTitleActivity(int dados){
        switch (dados){
            case 1:{
                setTitle("Tela de Ler Carta");
                break;
            }

            case 2:{
                setTitle("Tela de Cadastro");
                break;
            }

            case 3:{
                setTitle("Tela de Apagar Dados");
                break;
            }

            case 4:{
                setTitle("Tela de Descrição");
                break;
            }

            case 5:{
                setTitle("Tela de Descrição Detalhada");
                break;
            }
        }
    }

    private void alertDialogCadastrar(final String id){
        new MaterialDialog.Builder(this)
                .title("Selecione a carta que deseja atribuir a TAG!")
                .items(values)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int myItem, CharSequence text) {

                        switch (myItem) {
                            case 0: {
                                switch (mContador[0]) {
                                    case 0: {
                                        Log.i(TAG, "Duque 1");
                                        salvarDados("Duque","duque1", id);
                                        mContador[0]++;
                                        break;
                                    }
                                    case 1: {
                                        Log.i(TAG, "Duque 2");
                                        salvarDados("Duque","duque2", id);
                                        mContador[0]++;
                                        break;
                                    }
                                    case 2: {
                                        Log.i(TAG, "Duque 3");
                                        salvarDados("Duque","duque3", id);
                                        mContador[0]++;
                                        break;
                                    }
                                    default: mContador[0] = 0;
                                }
                                break;
                            }

                            case 1:{
                                switch (mContador[1]) {
                                    case 0: {
                                        Log.i(TAG, "Assassino 1");
                                        salvarDados("Assassino","assassino1", id);
                                        mContador[1]++;
                                        break;
                                    }
                                    case 1: {
                                        Log.i(TAG, "Assassino 2");
                                        salvarDados("Assassino","assassino2", id);
                                        mContador[1]++;
                                        break;
                                    }
                                    case 2: {
                                        Log.i(TAG, "Assassino 3");
                                        salvarDados("Assassino","assassino3", id);
                                        mContador[1]++;
                                        break;
                                    }
                                    default: mContador[1] = 0;
                                }
                                break;
                            }

                            case 2:{//Condessa
                                switch (mContador[2]) {
                                    case 0: {
                                        Log.i(TAG, "Condessa 1");
                                        salvarDados("Condessa","condessa1", id);
                                        mContador[2]++;
                                        break;
                                    }
                                    case 1: {
                                        Log.i(TAG, "Condessa 2");
                                        salvarDados("Condessa","condessa2", id);
                                        mContador[2]++;
                                        break;
                                    }
                                    case 2: {
                                        Log.i(TAG, "Condessa 3");
                                        salvarDados("Condessa","condessa3", id);
                                        mContador[2]++;
                                        break;
                                    }
                                    default: mContador[2] = 0;
                                }
                                break;
                            }

                            case 3:{//Capitão
                                switch (mContador[3]) {
                                    case 0: {
                                        Log.i(TAG, "Capitão 1");
                                        salvarDados("Capitão","capitao1", id);
                                        mContador[3]++;
                                        break;
                                    }
                                    case 1: {
                                        Log.i(TAG, "Capitão 2");
                                        salvarDados("Capitão","capitao2", id);
                                        mContador[3]++;
                                        break;
                                    }
                                    case 2: {
                                        Log.i(TAG, "Capitão 3");
                                        salvarDados("Capitão","capitao3", id);
                                        mContador[3]++;
                                        break;
                                    }
                                    default: mContador[3] = 0;
                                }
                                break;
                            }

                            case 4:{//Embaixador
                                switch (mContador[4]) {
                                    case 0: {
                                        Log.i(TAG, "Embaixador 1");
                                        salvarDados("Embaixador","embaixador1", id);
                                        mContador[4]++;
                                        break;
                                    }
                                    case 1: {
                                        Log.i(TAG, "Embaixador 2");
                                        salvarDados("Embaixador","embaixador2", id);
                                        mContador[4]++;
                                        break;
                                    }
                                    case 2: {
                                        Log.i(TAG, "Embaixador 3");
                                        salvarDados("Embaixador","embaixador3", id);
                                        mContador[4]++;
                                        break;
                                    }
                                    default: mContador[4] = 0;
                                }
                                break;
                            }
                        }

                        return true;
                    }
                })
                //.positiveText("Cadastrar")
                .show();
    }

    private void enviaDados(int meusDados){
        Intent intent = new Intent(this, Info.class);
        intent.putExtra("mDados",meusDados);
        startActivity(intent);
    }

    /*Recebe os dados passados pela Activity de Menu*/
    private int recebeDados(){

        Bundle extra = getIntent().getExtras();

        int dados;

        if(extra != null){
            dados = extra.getInt("botao");
            Log.i(TAG,"RECEBE_DADOS: "+dados);
            return dados;
        }
        return 0;
    }

    /*Chama os métodos correspondentes a cada dado passado*/
    public void chamaMetodos(int dados, String id){

        //N("A carta foi lida");

        switch (dados){
            case 1:{ //ler carta
                Log.i(TAG,"Entrou no Ler Carta: "+id);
                readNameCard(id);
                break;
            }

            case 2:{ //atributos
                Log.i(TAG,"Cadastra carta: "+id);
                alertDialogCadastrar(id);
                break;
            }

            case 3:{
                Log.i(TAG,"Entrou Apagar Dados");
                limpaTodosDadosSharedPreferences();
                break;
            }

            case 4:{
                Log.i(TAG,"Entrou no Ler Descrição: "+id);
                readDescription(id);
                break;
            }

            case 5: {
                Log.i(TAG,"Entrou no Ler Descrição Detalhada");
                readDescriptionDetailed(id);
                break;
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

    /*Recupera as informações do SharedPreferences para as Variáveis*/
    private void recuperaDadosParaVariaveis() {
        mDuque[0] = recuperarDados("duque1");
        mDuque[1] = recuperarDados("duque2");
        mDuque[2] = recuperarDados("duque3");

        mAssassino[0] = recuperarDados("assassino1");
        mAssassino[1] = recuperarDados("assassino2");
        mAssassino[2] = recuperarDados("assassino3");

        mCondessa[0] = recuperarDados("condessa1");
        mCondessa[1] = recuperarDados("condessa2");
        mCondessa[2] = recuperarDados("condessa3");

        mCapitao[0] = recuperarDados("capitao1");
        mCapitao[1] = recuperarDados("capitao2");
        mCapitao[2] = recuperarDados("capitao3");

        mEmbaixador[0] = recuperarDados("embaixador1");
        mEmbaixador[1] = recuperarDados("embaixador2");
        mEmbaixador[2] = recuperarDados("embaixador3");
    }

    /*Recupera o nome da carta do CSV*/
    private void recuperaNomeCartaCSV(String idCsv){
        card = deck.getCard(idCsv);
        nameCard = card.getName();
        Toast.makeText(this, "A carta lida foi: "+nameCard, Toast.LENGTH_SHORT).show();
        Log.i(TAG,"A carta lida foi: "+nameCard);
        startActivity(new Intent(ReadCardActivity.this,Info.class));
    }

    /*Recupera a descrição da carta do CSV*/
    private void recuperaDescricaoCartaCSV(String idCsv){
        card = deck.getCard(idCsv);
        descriptionCard = card.getDescription();
        speechMyText(descriptionCard);
        if(!textToSpeech.isSpeaking())
            startActivity(new Intent(ReadCardActivity.this,Info.class));
    }

    /*Nome das Cartas*/
    public void readNameCard(String id){

        recuperaDadosParaVariaveis();

        if (mDuque[0].equals(id) || mDuque[1].equals(id) || mDuque[2].equals(id)){
            Log.i(TAG,"mDuque: "+id);
            recuperaNomeCartaCSV("1");
        }

        if (mAssassino[0].equals(id) || mAssassino[1].equals(id) || mAssassino[2].equals(id)){
            Log.i(TAG,"mAssassino: "+id);
            recuperaNomeCartaCSV("2");
            if (!textToSpeech.isSpeaking()){
                startActivity(new Intent(this,Info.class));
            }
        }

        if (mCondessa[0].equals(id) || mCondessa[1].equals(id) || mCondessa[2].equals(id)){
            Log.i(TAG,"mCondessa: "+id);
            recuperaNomeCartaCSV("3");
        }


        if (mCapitao[0].equals(id) || mCapitao[1].equals(id) || mCapitao[2].equals(id)){
            Log.i(TAG,"mCapitao: "+id);
            recuperaNomeCartaCSV("4");
        }


        if (mEmbaixador[0].equals(id) || mEmbaixador[1].equals(id) || mEmbaixador[2].equals(id)){
            Log.i(TAG,"mEmbaixador: "+id);
            recuperaNomeCartaCSV("5");
        }

    }

    /*Descrição das Cartas*/
    public void readDescription(String id){

        recuperaDadosParaVariaveis();

        if (mDuque[0].equals(id) || mDuque[1].equals(id) || mDuque[2].equals(id)){
            Log.i(TAG,"mDuque: "+id);
            //N("Duque");
            recuperaDescricaoCartaCSV("1");
        }

        if (mAssassino[0].equals(id) || mAssassino[1].equals(id) || mAssassino[2].equals(id)){
            Log.i(TAG,"mAssassino: "+id);
            //N("Assassino");
            recuperaDescricaoCartaCSV("2");
        }

        if (mCondessa[0].equals(id) || mCondessa[1].equals(id) || mCondessa[2].equals(id)){
            Log.i(TAG,"mCondessa: "+id);
            //N("Condessa");
            recuperaDescricaoCartaCSV("3");
        }


        if (mCapitao[0].equals(id) || mCapitao[1].equals(id) || mCapitao[2].equals(id)){
            Log.i(TAG,"mCapitao: "+id);
            //N("Capitão");
            recuperaDescricaoCartaCSV("4");
        }


        if (mEmbaixador[0].equals(id) || mEmbaixador[1].equals(id) || mEmbaixador[2].equals(id)){
            Log.i(TAG,"mEmbaixador: "+id);
            //N("Embaixador");
            recuperaDescricaoCartaCSV("5");
        }
    }

    /*Descrição das Cartas*/
    public void readDescriptionDetailed(String id){

        recuperaDadosParaVariaveis();

        if (mDuque[0].equals(id) || mDuque[1].equals(id) || mDuque[2].equals(id)){
            Log.i(TAG,"mDuque: "+id);
            //N("Duque");
            recuperaDescricaoCartaCSV("6");
        }

        if (mAssassino[0].equals(id) || mAssassino[1].equals(id) || mAssassino[2].equals(id)){
            Log.i(TAG,"mAssassino: "+id);
            //N("Assassino");
            recuperaDescricaoCartaCSV("7");
        }

        if (mCondessa[0].equals(id) || mCondessa[1].equals(id) || mCondessa[2].equals(id)){
            Log.i(TAG,"mCondessa: "+id);
            //N("Condessa");
            recuperaDescricaoCartaCSV("8");
        }


        if (mCapitao[0].equals(id) || mCapitao[1].equals(id) || mCapitao[2].equals(id)){
            Log.i(TAG,"mCapitao: "+id);
            //N("Capitão");
            recuperaDescricaoCartaCSV("9");
        }


        if (mEmbaixador[0].equals(id) || mEmbaixador[1].equals(id) || mEmbaixador[2].equals(id)){
            Log.i(TAG,"mEmbaixador: "+id);
            //N("Embaixador");
            recuperaDescricaoCartaCSV("10");
        }
    }


    /************************************** SHARED PREFERENCES *********************************************/
    private void salvarDados(String nome,String nomeCarta, String tagCarta){

        limpaDadoCartaEspecifica(tagCarta);

        SharedPreferences preferences = getSharedPreferences(ARQUIVO_CARTAS, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(nomeCarta, tagCarta);
        editor.commit();
        N("Carta "+nome+" cadastrada com sucesso!");
        Log.i(TAG,"Nome Carta: "+nomeCarta+"\tTAG ID: "+tagCarta);
    }

    private String recuperarDados(String nameCard){
        SharedPreferences preferences = getSharedPreferences(ARQUIVO_CARTAS, MODE_PRIVATE);
        String cardID = preferences.getString(nameCard,"Carta não cadastrada!");
        return cardID;
    }

    private void limpaDadoCartaEspecifica(String removerCarta){
        SharedPreferences preferences = getSharedPreferences(ARQUIVO_CARTAS, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(removerCarta);
        editor.commit();
    }

    public void limpaTodosDadosSharedPreferences(){
        SharedPreferences preferences = getSharedPreferences(ARQUIVO_CARTAS, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        Toast.makeText(this, "Dados limpos com sucesso!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(ReadCardActivity.this, Info.class));
    }


    /************************************* MÉTODOS DA TAG NFC ***********************************************/
    public void verificaNFC(){

        //Check for available NFC Adapter
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter == null){
            Toast.makeText(this, "O dispositivo não possui NFC!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if((!mNfcAdapter.isEnabled())){
            Toast.makeText(this, "Ative o NFC do seu dispositivo!", Toast.LENGTH_SHORT).show();
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

    /*Lê o ID da TAG*/
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i(TAG,"Carta lida");
        String tagContentID = ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)).toString();
        recebeDados();
        chamaMetodos(recebeDados(),tagContentID);
    }

    /*Converte o ID da TAG de Hexadecimal para Decimal*/
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

    /*Códigos extras da TAG NFC*/
    private void enableForegroundDispatchSystem(){

        Intent intent = new Intent(this,ReadCardActivity.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);
        IntentFilter[] intentFilters = new IntentFilter[]{};

        mNfcAdapter.enableForegroundDispatch(this,pendingIntent,intentFilters,null);
    }

    private void disableForegroundDispatchSystem(){
        mNfcAdapter.disableForegroundDispatch(this);
    }

    /**************************************** TEXT TO SPEECH *************************************************/
    public void verificaTTS(){
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status){
                if (status != TextToSpeech.ERROR){
                    textToSpeech.setLanguage(Locale.getDefault());
                    if(recebeDados() == 1){
                        speechMyText("LER NOME! APROXIME A CARTA DO CELULAR!!!");
                    }

                    else if(recebeDados() == 2){
                        speechMyText("CADASTRAR TAGS! APROXIME A CARTA DO CELULAR!!!");
                    }

                    else if(recebeDados() == 3){
                        speechMyText("APAGAR DADOS DAS TAGS! APROXIME QUALQUER CARTA DO CELULAR!!!");
                    }

                    else if(recebeDados() == 4){
                        speechMyText("LER DESCRIÇÃO! APROXIME A CARTA DO CELULAR!!!");
                    }
                    else if(recebeDados() == 5){
                        speechMyText("LER REGRAS! APROXIME QUALQUER CARTA DO CELULAR!!!");
                    }

                }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void speechMyText(String texto){
        textToSpeech.speak(texto,TextToSpeech.QUEUE_FLUSH,null,null);
    }



    /********************************* CICLO DE VIDA DA ACTIVITY ********************************************/
    @Override
    protected void onResume() {
        enableForegroundDispatchSystem();
        Log.i(TAG,"onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        disableForegroundDispatchSystem();
        textToSpeech.stop();
        Log.i(TAG,"onPause");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        textToSpeech.stop();
        Log.i(TAG,"onDestroy");
        super.onDestroy();
    }


    /******************************************** TARDIGRADE ***********************************************/
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

}
