package com.example.si700_imobiliaria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class Cadastro extends AppCompatActivity {

    private Button btnCancelar;
    private Button btnCadastrar;
    private RadioButton rbCliente;
    private RadioButton rbProprietario;
    private EditText edtNome;
    private EditText edtTel;
    private EditText edtCel;
    private EditText edtEmail;
    private EditText edtEmailConfirm;
    private EditText edtSenha;
    private EditText edtSenhaConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Cadastro");
        setContentView(R.layout.activity_cadastro);

        edtNome = findViewById(R.id.edtNome);
        edtTel = findViewById(R.id.edtTel);
        edtCel = findViewById(R.id.edtCel);
        edtEmail = findViewById(R.id.edtEmail);
        edtEmailConfirm = findViewById(R.id.edtEmailConfirm);
        edtSenha = findViewById(R.id.edtSenha);
        edtSenhaConfirm = findViewById(R.id.edtSenhaConfirm);
        rbCliente = findViewById(R.id.rbCliente);
        rbProprietario = findViewById(R.id.rbProprietario);


        btnCancelar = findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Cadastro.this, MainActivity.class));
            }
        });


        btnCadastrar = findViewById(R.id.btnCadastrar);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public boolean checkTipo(){
        /*if(rbProprietario.isSelected() == false && rbCliente.isSelected() == false){
            return false;
        } else {
            return true;
        }*/
        return true;
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
