package com.example.si700_imobiliaria;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //Esconde barra no topo da pagina
        setContentView(R.layout.activity_main);
    }
}
