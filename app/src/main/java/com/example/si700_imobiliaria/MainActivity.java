package com.example.si700_imobiliaria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private TextView cadastrar;
    private EditText usuario;
    private EditText senha;
    private Button login;

    private FirebaseAuth firebaseauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //Esconde barra no topo da pagina
        setContentView(R.layout.activity_main);

        firebaseauth = FirebaseAuth.getInstance();

        cadastrar = findViewById(R.id.cadastrar);
        usuario = findViewById(R.id.user);
        senha = findViewById(R.id.password);
        login = findViewById(R.id.login);

        //Chama activity cadastrar
        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Cadastro.class));
            }
        });

        //Efetuar login
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    //Realizar login
    public void login(){
        firebaseauth.signInWithEmailAndPassword(usuario.getText().toString(), senha.getText().toString()).
                addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){ //login efetuado
                    startActivity(new Intent(MainActivity.this, Perfil.class));
                    Log.i("signIn", "Login realizado com sucesso");
                    //Toast.makeText(getApplicationContext(),"Login realizado com sucesso",Toast.LENGTH_SHORT).show();
                } else{ //erro ao efetuar o login
                    Log.i("signIn", "ERRO");
                    Toast.makeText(getApplicationContext(),"E-mail e/ou senha incorretos",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
