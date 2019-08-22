package com.example.si700_imobiliaria;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.internal.InternalTokenProvider;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Perfil extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView nomeSobr;
    private RadioButton rbCliente;
    private RadioButton rbProprietario;
    private RadioButton rbFeminino;
    private RadioButton rbMasculino;
    private EditText edtTel;
    private EditText edtCel;
    private EditText edtEmail;
    private Button btnSalvar;

    private ImageView imgFoto;
    private final int GALERIA_IMAGENS = 1;
    private final int PERMISSAO_REQUEST = 2;

    private FirebaseAuth firebaseauth;
    private DatabaseReference firebasereference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference usuarioReferencia = firebasereference.child("usuarios");

    final String[] nome = new String[1];
    final String[] sobrenome = new String[1];
    final String[] email = new String[1];
    final String[] senha = new String[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSAO_REQUEST);
            }
        }

        nomeSobr = findViewById(R.id.textNome);
        rbCliente = findViewById(R.id.rbCliente);
        rbProprietario = findViewById(R.id.rbProprietario);
        rbFeminino = findViewById(R.id.rbFeminino);
        rbMasculino = findViewById(R.id.rbMasculino);
        edtTel = findViewById(R.id.edtTel);
        edtCel = findViewById(R.id.edtCel);
        edtEmail = findViewById(R.id.edtEmail);
        btnSalvar = findViewById(R.id.btnSalvar);
        imgFoto = findViewById(R.id.imgFoto);

        //firebaseauth = FirebaseAuth.getInstance();

        loadData(); //carrega dados do usuário

        imgFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, GALERIA_IMAGENS);
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkPhones() == true){ //se os campos de telefone e celular estiverem preenchidos
                    if(updateData() == true){ //se a atualização dos dados ocorrer bem
                        Toast.makeText(getApplicationContext(),"Registro atualizado com sucesso",Toast.LENGTH_LONG).show();
                    } else{
                        Toast.makeText(getApplicationContext(),"Erro ao atualizar o registro",Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"Há campos não preenchidos",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.perfil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_perfil) {
            startActivity(new Intent(getApplicationContext(), Perfil.class)); //retorna para tela de login
        } else if (id == R.id.nav_anunciar) {

        } else if (id == R.id.nav_alugar) {

        } else if (id == R.id.nav_comprar) {

        } else if (id == R.id.nav_sair) {
            Log.i("logOut", "saiu");
            firebaseauth = FirebaseAuth.getInstance();
            firebaseauth.signOut();
            startActivity(new Intent(getApplicationContext(), MainActivity.class)); //retorna para tela de login

            return true;
        } else if (id == R.id.nav_feedback) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Exibir nome do usuário no menu
    public void setUserName(String nome){
        // Obtém a referência do layout de navegação
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Obtém a referência da view de cabeçalho
        View headerView = navigationView.getHeaderView(0);

        // Obtém a referência do nome do usuário e altera seu nome
        TextView txtNomeUser = headerView.findViewById(R.id.nav_nome);
        txtNomeUser.setText("Olá, " +nome);
    }

    //Exibir email do usuário no menu
    public void setUserEmail(String email){
        // Obtém a referência do layout de navegação
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Obtém a referência da view de cabeçalho
        View headerView = navigationView.getHeaderView(0);

        // Obtém a referência do nome do usuário e altera seu nome
        TextView txtEmailUser = headerView.findViewById(R.id.nav_email);
        txtEmailUser.setText(email);
    }

    //Verifica se os campos telefone e celular estão preenchidos
    public boolean checkPhones(){
        if(edtTel.getText().toString().equals("") || edtCel.getText().toString().equals("")){
            return false;
        } else{
            return true;
        }
    }

    //Carrega dados do usuário no perfil
    public void loadData(){
        firebaseauth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseauth.getCurrentUser();

        usuarioReferencia.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nome[0] = dataSnapshot.child("nome").getValue().toString();
                sobrenome[0] = dataSnapshot.child("sobrenome").getValue().toString();
                email[0] = dataSnapshot.child("email").getValue().toString();
                senha[0] = dataSnapshot.child("senha").getValue().toString();

                nomeSobr.setText(dataSnapshot.child("nome").getValue().toString()+" "+dataSnapshot.child("sobrenome").getValue().toString());
                edtTel.setText(dataSnapshot.child("telefone").getValue().toString());
                edtCel.setText(dataSnapshot.child("celular").getValue().toString());
                edtEmail.setText(dataSnapshot.child("email").getValue().toString());

                if(dataSnapshot.child("tipo").getValue().toString().equals("Cliente")){
                    rbCliente.setChecked(true);
                    rbProprietario.setChecked(false);
                } else{
                    rbProprietario.setChecked(true);
                    rbCliente.setChecked(false);
                }

                if(dataSnapshot.child("sexo").getValue().toString().equals("Feminino")){
                    rbFeminino.setChecked(true);
                    rbMasculino.setChecked(false);
                } else{
                    rbFeminino.setChecked(false);
                    rbMasculino.setChecked(true);
                }

                setUserName(nome[0]);
                setUserEmail(email[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Atualiza os dados do usuário
    public boolean updateData(){
        firebaseauth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseauth.getCurrentUser();

        Usuario usuario = new Usuario();

        usuario.setNome(nome[0]);
        usuario.setSobrenome(sobrenome[0]);
        usuario.setTelefone(edtTel.getText().toString());
        usuario.setCelular(edtCel.getText().toString());
        usuario.setEmail(edtEmail.getText().toString());
        usuario.setSenha(senha[0]);

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

        try{
            usuarioReferencia.child(user.getUid()).setValue(usuario); //salva dados no banco de dados firebase e usa o UID como ID

            return true;
        } catch(Exception e){
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();

            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grandResults){
        if(requestCode == PERMISSAO_REQUEST){
            if(grandResults.length > 0 && grandResults[0] == PackageManager.PERMISSION_GRANTED){

            } else {

            }
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == GALERIA_IMAGENS){
            Uri selectedImage = data.getData();
            String[] filePath = { MediaStore.Images.Media.DATA };
            Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath[0]);
            String picturePath = c.getString(columnIndex);
            c.close();
            Bitmap imagemGaleria = (BitmapFactory.decodeFile(picturePath));
            imgFoto.setImageBitmap(imagemGaleria);
        }
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
