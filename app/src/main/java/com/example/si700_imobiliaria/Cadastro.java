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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
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
    private RadioButton rbFeminino;
    private RadioButton rbMasculino;
    private EditText edtNome;
    private EditText edtTel;
    private EditText edtCel;
    private EditText edtEmail;
    private EditText edtEmailConfirm;
    private EditText edtSenha;
    private EditText edtSenhaConfirm;

    private FirebaseAuth firebaseauth;
    private DatabaseReference firebasereference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference usuarioReferencia = firebasereference.child("usuarios");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Cadastro");
        setContentView(R.layout.activity_cadastro);

        firebaseauth = FirebaseAuth.getInstance();

        edtNome = findViewById(R.id.edtNome);
        edtTel = findViewById(R.id.edtTel);
        edtCel = findViewById(R.id.edtCel);
        edtEmail = findViewById(R.id.edtEmail);
        edtEmailConfirm = findViewById(R.id.edtEmailConfirm);
        edtSenha = findViewById(R.id.edtSenha);
        edtSenhaConfirm = findViewById(R.id.edtSenhaConfirm);
        rbCliente = findViewById(R.id.rbCliente);
        rbProprietario = findViewById(R.id.rbProprietario);
        rbFeminino = findViewById(R.id.rbFeminino);
        rbMasculino = findViewById(R.id.rbMasculino);

        //Botão cancelar - Volta para a tela de login sem efetivar o cadastro do user
        btnCancelar = findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Cadastro.this, MainActivity.class));
            }
        });

        //Botão cadastrar - Faz o cadastro do user no firebase
        btnCadastrar = findViewById(R.id.btnCadastrar);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser(); //cadastra usuário
                saveData(); //salva dados
                Toast.makeText(getApplicationContext(),"Usuário criado com sucesso",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Cadastro.this, MainActivity.class)); //retorna para tela de login
            }
        });
    }

    //Criar usuário/senha
    public void createUser(){
        firebaseauth.createUserWithEmailAndPassword(edtEmail.getText().toString(), edtSenha.getText().toString()).
                addOnCompleteListener(Cadastro.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){ //se usuario criado com sucesso
                    Log.i("createUser", "Usuário criado com sucesso");
                    Toast.makeText(getApplicationContext(),"Usuário criado com sucesso",Toast.LENGTH_LONG).show();
                } else{ //se der erro no cadastro
                    Log.i("createUser", "ERRO");
                    Toast.makeText(getApplicationContext(),"Erro. Usuário inválido",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Salvar dados do usuário
    public void saveData(){
        Usuario usuario = new Usuario();

        usuario.setNome(edtNome.getText().toString());
        usuario.setTelefone(edtTel.getText().toString());
        usuario.setCelular(edtCel.getText().toString());
        usuario.setEmail(edtEmail.getText().toString());
        usuario.setSenha(edtSenha.getText().toString());

        //Verifica se o usuário é Cliente ou proprietário
        if(rbCliente.isChecked()){
            usuario.setTipo("Cliente");
        } else{
            usuario.setTipo("Proprietário");
        }
        //Verifica se o usuário é Homem ou Mulher
        if(rbFeminino.isChecked()){
            usuario.setSexo("Feminino");
        } else{
            usuario.setSexo("Masculino");
        }

        usuarioReferencia.push().setValue(usuario);
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


