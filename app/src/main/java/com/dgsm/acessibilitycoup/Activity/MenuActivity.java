package com.dgsm.acessibilitycoup.Activity;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dgsm.acessibilitycoup.R;

public class MenuActivity extends AppCompatActivity {

    private Button btLer, btAtributos, btDicas;
    ReadCardActivity readCardActivity;

    int contador = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mCasts();

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

    private void mCasts(){
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
}
