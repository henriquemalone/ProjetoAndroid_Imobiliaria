package com.example.si700_imobiliaria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(255, 101, 0)));
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
                if(checkFields() == 0){ //Verifica se todos os campos foram preenchidos
                    createUser();
                    if(saveData() == true){ //se todos os campos foram preenchidos e foram salvos no banco de dados corretamente
                        Toast.makeText(getApplicationContext(),"Usuário criado com sucesso",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Cadastro.this, MainActivity.class)); //retorna para tela de login
                    } else{
                        Toast.makeText(getApplicationContext(),"E-mail e/ou senha não coincidem",Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"Há campos não preenchidos",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //Criar usuário/senha
    public void createUser(){
        String email = edtEmail.getText().toString();
        String password = edtSenha.getText().toString();

        firebaseauth = FirebaseAuth.getInstance();

        firebaseauth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Se criado com sucesso
                            Toast.makeText(getApplicationContext(),"Usuário criado com sucesso",Toast.LENGTH_LONG).show();
                            Log.d("createUser", "createUserWithEmail:success");
                        } else {
                            // Se der erro, exibirá a mensagem
                            Log.w("createUser", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Cadastro.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                    }
                });
    }

    //Salvar dados do usuário
    public boolean saveData(){
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

        //Se os campos Email e Senha forem preenchidos corretamente
        if(checkEmail() == true && checkPassword() == true){
            try{
                usuarioReferencia.push().setValue(usuario); //salva dados no banco de dados firebase
                return true;
            } catch(Exception e){
                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                return false;
            }
        } else{
            return false;
        }
    }

    //Verifica se o campo email e confirmar email estão iguais
    public boolean checkEmail(){
        if(edtEmail.getText().toString().equals(edtEmailConfirm.getText().toString())){
            return true;
        }else{
            return false;
        }
    }

    //Verifica se o campo senha e confirmar senha estão iguais
    public boolean checkPassword(){
        if(edtSenha.getText().toString().equals(edtSenhaConfirm.getText().toString())){
            return true;
        } else {
            return false;
        }
    }

    //Verifica se todos os campos foram preenchdios. Caso não, a variável aux sera incrementado +1
    public int checkFields(){
        int aux = 0;

        //Checa se o radio button foi selecionado
        if(rbCliente.isChecked() == false && rbProprietario.isChecked() == false){
            aux++;
        }

        //Checa se o radio button foi selecionado
        if(rbMasculino.isChecked() == false && rbFeminino.isChecked() == false){
            aux++;
        }

        //Checa se os campos foram preenchidos
        if(edtNome.getText().toString().equals("") || edtTel.getText().toString().equals("") ||
                edtCel.getText().toString().equals("") || edtEmail.getText().toString().equals("") ||
                edtEmailConfirm.getText().toString().equals("") || edtSenha.getText().toString().equals("") ||
                edtSenhaConfirm.getText().toString().equals("")){
            aux++;
        }

        return aux;
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


