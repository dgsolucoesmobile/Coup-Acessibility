package com.dgsm.accessibilitycoup.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.dgsm.accessibilitycoup.R;

import tardigrade.Tardigrade;
import tardigrade.deck.ICard;
import tardigrade.resources.impl.Deck;

public class Ajuda extends AppCompatActivity {

    private Button btAssassino, btCapitao, btCondessa, btDuque, btEmbaixador, btVoltar;

    private static final String TAG = "AjudaActivity";

    /*Usados para recuperar nome e descrição das cartas, através do CSV*/
    private String descriptionCard;

    /*Tardigrade*/
    private Tardigrade game;
    private Deck deck = null;
    private ICard card;

    /*Toast Customizavel*/
    Toast toast;
    LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ajuda);

        mCasts();

        btAssassino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recuperaDescricaoCartaCSV("1");
            }
        });

        btCapitao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recuperaDescricaoCartaCSV("2");
            }
        });

        btCondessa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recuperaDescricaoCartaCSV("3");
            }
        });

        btDuque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recuperaDescricaoCartaCSV("4");
            }
        });

        btEmbaixador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recuperaDescricaoCartaCSV("5");
            }
        });

        btVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Ajuda.this, Menu.class));
            }
        });

    }

    private void mCasts(){
        btAssassino = findViewById(R.id.btAssassino);
        btCapitao = findViewById(R.id.btCapitao);
        btCondessa = findViewById(R.id.btCondessa);
        btDuque = findViewById(R.id.btDuque);
        btEmbaixador = findViewById(R.id.btEmbaixador);
        btVoltar = findViewById(R.id.btVoltar);

        game = Tardigrade.getInstance(this);
        deck = Deck.getInstance(this);
        toast = new Toast(this);
        inflater = getLayoutInflater();
        setTitle("Tela de Ajuda");
    }

    /*Recupera a descrição da carta do CSV*/
    private void recuperaDescricaoCartaCSV(String idCsv) {
        card = deck.getCard(idCsv);
        descriptionCard = card.getDescription();
        mToasts(descriptionCard);
        Log.i(TAG, "recuperaDescricaoCartaCSV: leu a descrição da carta"+idCsv);
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
}
