package com.well.whospy.Activity;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.well.whospy.R;

public class MenuActivity extends AppCompatActivity {

    private Button btLer, btAtributos, btDicas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mCasts();

        btLer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, MainActivity.class));
            }
        });

        btAtributos.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {

            }
        });

        btDicas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

    private void mCasts(){
        btLer = (Button) findViewById(R.id.btLerCarta);
        btAtributos = (Button) findViewById(R.id.btAtributos);
        btDicas = (Button) findViewById(R.id.btDicas);
    }
}
