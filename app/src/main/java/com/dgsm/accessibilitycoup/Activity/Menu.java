package com.dgsm.accessibilitycoup.Activity;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dgsm.accessibilitycoup.R;

import tardigrade.Tardigrade;
import tardigrade.deck.ICard;
import tardigrade.resources.impl.Deck;

public class Menu extends AppCompatActivity {

    static final String TAG = "Menu";
    private Tardigrade game;
    private Deck deck = null;

    Toast toast;
    LayoutInflater inflater;

    //NFC e TextToSpeech
    private NfcAdapter mNfcAdapter;

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
    //private String[] mInquisidor = new String[3];

    CharSequence[] values = {" Assassino ", " Capitão ", " Condessa ", " Duque ",
             " Embaixador "};

    int[] mContador = new int[7];

    private Button btJogar;
    private Button btApagarDados;

    /*Importando Classes*/
    Utilitarios utilitarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //utilitarios = new Utilitarios();
        //utilitarios.fullScreen(getBaseContext());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_menu);

        game = Tardigrade.getInstance(this);
        deck = Deck.getInstance(this);
        toast = new Toast(this);
        inflater = getLayoutInflater();
        setTitle("Menu Principal");
        /*Cast dos Botões*/
        mCasts();
        //utilitarios = new Utilitarios();

        /*Checa se tem permissão para usar o NFC*/
        checkPermissionNFC();

        //Verifica o NFC e TTS
        verificaNFC();

        btJogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Menu.this, ReadCard.class));
            }
        });

        btApagarDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //alertDeletaDados();
                alertDeletaDados();
            }
        });

    }

    /*Confirma se o usuário realmente deseja apagar todos os dados*/
    private void alertDeletaDados(){
        new MaterialDialog.Builder(this)
                .title("TELA DE CONFIRMAÇÃO!\n\nClique em SIM se você realmente deseja apagar todas as suas cartas!")
                .positiveText("SIM")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        limpaTodosDadosSharedPreferences();
                    }
                })
                .negativeText("NÃO")
                .show();
    }

    /*Cadastrar Cartas*/
    private void alertDialogCadastrar(final String id){
        new MaterialDialog.Builder(this)
                .title("Tela de Cadastro!\n\nSelecione a carta que deseja atribuir a TAG!")
                .items(values)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int myItem, CharSequence text) {

                        switch (myItem) {

                            case 0:{
                                switch (mContador[0]) {
                                    case 0: {
                                        Log.i(TAG, "Assassino 1");
                                        salvarDados("Assassino","assassino1", id);
                                        mContador[0]++;
                                        break;
                                    }
                                    case 1: {
                                        Log.i(TAG, "Assassino 2");
                                        salvarDados("Assassino","assassino2", id);
                                        mContador[0]++;
                                        break;
                                    }
                                    case 2: {
                                        Log.i(TAG, "Assassino 3");
                                        salvarDados("Assassino","assassino3", id);
                                        mContador[0]++;
                                        break;
                                    }
                                    default: mContador[0] = 0;
                                }
                                break;
                            }

                            case 1:{//Capitão
                                switch (mContador[1]) {
                                    case 0: {
                                        Log.i(TAG, "Capitão 1");
                                        salvarDados("Capitão","capitao1", id);
                                        mContador[1]++;
                                        break;
                                    }
                                    case 1: {
                                        Log.i(TAG, "Capitão 2");
                                        salvarDados("Capitão","capitao2", id);
                                        mContador[1]++;
                                        break;
                                    }
                                    case 2: {
                                        Log.i(TAG, "Capitão 3");
                                        salvarDados("Capitão","capitao3", id);
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

                            case 3: {
                                switch (mContador[3]) {
                                    case 0: {
                                        Log.i(TAG, "Duque 1");
                                        salvarDados("Duque","duque1", id);
                                        mContador[3]++;
                                        break;
                                    }
                                    case 1: {
                                        Log.i(TAG, "Duque 2");
                                        salvarDados("Duque","duque2", id);
                                        mContador[3]++;
                                        break;
                                    }
                                    case 2: {
                                        Log.i(TAG, "Duque 3");
                                        salvarDados("Duque","duque3", id);
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

                            /*case 5:{
                                switch (mContador[5]){
                                    case 0: {
                                        Log.i(TAG, "Inquisidor 1");
                                        salvarDados("Inquisidor","inquisidor1", id);
                                        mContador[5]++;
                                        break;
                                    }
                                    case 1: {
                                        Log.i(TAG, "Inquisidor 2");
                                        salvarDados("Inquisidor","inquisidor2", id);
                                        mContador[5]++;
                                        break;
                                    }
                                    case 2: {
                                        Log.i(TAG, "Inquisidor 3");
                                        salvarDados("Inquisidor","inquisidor3", id);
                                        mContador[5]++;
                                        break;
                                    }
                                    default: mContador[5] = 0;
                                break;
                                }
                            }*/
                        }

                        return true;
                    }
                })
                //.positiveText("Cadastrar")
                .show();
    }

    /*Cast dos Botões*/
    private void mCasts(){
        btJogar = findViewById(R.id.btJogar);
        btApagarDados = findViewById(R.id.btApagarDados);
    }

    /*Mostra Mensagens Toast*/
    public void mToasts(final String message){
        View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.custom_toast_container));
        TextView myText = layout.findViewById(R.id.text);
        myText.setText(message);
        toast.setView(layout);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER,0,100);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    /*Recupera as informações do SharedPreferences para as Variáveis*/
    private void recuperaDadosParaVariaveis() {

        mAssassino[0] = recuperarDados("assassino1");
        mAssassino[1] = recuperarDados("assassino2");
        mAssassino[2] = recuperarDados("assassino3");

        mCapitao[0] = recuperarDados("capitao1");
        mCapitao[1] = recuperarDados("capitao2");
        mCapitao[2] = recuperarDados("capitao3");

        mCondessa[0] = recuperarDados("condessa1");
        mCondessa[1] = recuperarDados("condessa2");
        mCondessa[2] = recuperarDados("condessa3");

        mDuque[0] = recuperarDados("duque1");
        mDuque[1] = recuperarDados("duque2");
        mDuque[2] = recuperarDados("duque3");

        mEmbaixador[0] = recuperarDados("embaixador1");
        mEmbaixador[1] = recuperarDados("embaixador2");
        mEmbaixador[2] = recuperarDados("embaixador3");

//        mInquisidor[0] = recuperarDados("inquisidor1");
//        mInquisidor[1] = recuperarDados("inquisidor2");
//        mInquisidor[2] = recuperarDados("inquisidor3");
    }

    /*Recupera o nome da carta do CSV*/
    private void recuperaNomeCartaCSV(String idCsv){
        card = deck.getCard(idCsv);
        nameCard = card.getName();
        mToasts(nameCard);
        Log.i(TAG,"A carta lida foi: "+nameCard);
        startActivity(new Intent(this,ReadCard.class));
    }

    /*Nome das Cartas*/
    public void readNameCard(String id){

        recuperaDadosParaVariaveis();

        if (mAssassino[0].equals(id) || mAssassino[1].equals(id) || mAssassino[2].equals(id)){
            Log.i(TAG,"mAssassino: "+id);
            recuperaNomeCartaCSV("1");
            salvarDadosUltimaCartaLida(id);
        }

        else if (mCapitao[0].equals(id) || mCapitao[1].equals(id) || mCapitao[2].equals(id)){
            Log.i(TAG,"mCapitao: "+id);
            recuperaNomeCartaCSV("2");
            salvarDadosUltimaCartaLida(id);
        }

        else if (mCondessa[0].equals(id) || mCondessa[1].equals(id) || mCondessa[2].equals(id)){
            Log.i(TAG,"mCondessa: "+id);
            recuperaNomeCartaCSV("3");
            salvarDadosUltimaCartaLida(id);
        }

        else if (mDuque[0].equals(id) || mDuque[1].equals(id) || mDuque[2].equals(id)){
            Log.i(TAG,"mDuque: "+id);
            recuperaNomeCartaCSV("4");
            salvarDadosUltimaCartaLida(id);
        }

        else if (mEmbaixador[0].equals(id) || mEmbaixador[1].equals(id) || mEmbaixador[2].equals(id)){
            Log.i(TAG,"mEmbaixador: "+id);
            recuperaNomeCartaCSV("5");
            salvarDadosUltimaCartaLida(id);
        }

        /*else if (mInquisidor[0].equals(id) || mInquisidor[1].equals(id) || mInquisidor[2].equals(id)){
            Log.i(TAG, "mInquisidor"+id);
            recuperaNomeCartaCSV("6");
            salvarDadosUltimaCartaLida(id);
        }*/

        else{
            alertDialogCadastrar(id);
        }

    }


    /************************************** SHARED PREFERENCES *********************************************/
    private void salvarDados(String nome,String nomeCarta, String tagCarta){

        limpaDadoCartaEspecifica(tagCarta);

        SharedPreferences preferences = getSharedPreferences(ARQUIVO_CARTAS, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(nomeCarta, tagCarta);
        editor.commit();
        mToasts("Carta "+nome+" cadastrada com sucesso!");
        Log.i(TAG,"Nome Carta: "+nomeCarta+"\tTAG ID: "+tagCarta);
    }

    private void salvarDadosUltimaCartaLida(String idUltimaCarta){

        limpaDadosUltimaCartaLida(idUltimaCarta);

        SharedPreferences preferences = getSharedPreferences(ARQUIVO_CARTAS, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("idUltimaCarta", idUltimaCarta);
        editor.commit();
        Log.i(TAG,"id da última carta SALVA foi: "+idUltimaCarta);
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

    private void limpaDadosUltimaCartaLida(String idUltimaCarta){
        SharedPreferences preferences = getSharedPreferences(ARQUIVO_CARTAS, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(idUltimaCarta);
        editor.commit();
    }

    public void limpaTodosDadosSharedPreferences(){
        SharedPreferences preferences = getSharedPreferences(ARQUIVO_CARTAS, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        mToasts("Dados apagados com sucesso!!!");
        //startActivity(new Intent(this, ReadCard.class)); //modificar depois
    }

    /************************************* MÉTODOS DA TAG NFC ***********************************************/
    private void checkPermissionNFC(){

        Log.i(TAG,"Entrou nas permissões!");

        if (ActivityCompat.checkSelfPermission(this,NFC_SERVICE) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.NFC)){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.NFC},0);
            }else
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.NFC},0);
        }
    }

    public void verificaNFC(){

        //Check for available NFC Adapter
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter == null){
            mToasts("O seu dispositivo não possui NFC!");
            finish();
            return;
        }

        if((!mNfcAdapter.isEnabled())){
            mToasts("Ative o NFC do seu dispositivo! Após ativá-lo aproxime o celular da carta" +
                    " que deseja ler a qualquer momento.");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
                startActivity(intent);
            } else {
                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                startActivity(intent);
            }
            Log.i(TAG,"Pede para ativar o NFC!");
        }

        else {
            Log.i(TAG,"O NFC já está ativado!");
            mToasts("APROXIME O CELULAR DA CARTA QUE DESEJA LER A QUALQUER MOMENTO!");
        }
    }

    /*Lê o ID da TAG*/
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i(TAG,"Carta lida");
        String tagContentID = ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)).toString();
        readNameCard(tagContentID);
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

        Intent intent = new Intent(this,Menu.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);
        IntentFilter[] intentFilters = new IntentFilter[]{};

        mNfcAdapter.enableForegroundDispatch(this,pendingIntent,intentFilters,null);
    }

    private void disableForegroundDispatchSystem(){
        mNfcAdapter.disableForegroundDispatch(this);
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
        Log.i(TAG,"onPause");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
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
