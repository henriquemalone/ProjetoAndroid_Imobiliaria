package com.example.si700_imobiliaria;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class Perfil_frag extends Fragment {

    private TextView nomeSobr;
    private RadioButton rbCliente;
    private RadioButton rbProprietario;
    private RadioButton rbFeminino;
    private RadioButton rbMasculino;
    private EditText edtTel;
    private EditText edtCel;
    private EditText edtEmail;
    private Button btnSalvar;
    private ProgressBar progressBar;

    private CircleImageView imgFoto;
    private final int GALERIA_IMAGENS = 1;
    private final int PERMISSAO_REQUEST = 2;

    private ArrayList<String> pathArray;
    private int progressStatus = 0;

    private StorageReference mStorageRef;
    private FirebaseAuth firebaseauth;
    private DatabaseReference firebasereference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference usuarioReferencia = firebasereference.child("usuarios");

    final String[] nome = new String[1];
    final String[] sobrenome = new String[1];
    final String[] email = new String[1];
    final String[] senha = new String[1];

    public Perfil_frag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil_frag, container, false);

        nomeSobr = view.findViewById(R.id.textNome);
        rbCliente = view.findViewById(R.id.rbCliente);
        rbProprietario = view.findViewById(R.id.rbProprietario);
        rbFeminino = view.findViewById(R.id.rbFeminino);
        rbMasculino = view.findViewById(R.id.rbMasculino);
        edtTel = view.findViewById(R.id.edtTel);
        edtCel = view.findViewById(R.id.edtCel);
        edtEmail = view.findViewById(R.id.edtEmail);
        btnSalvar = view.findViewById(R.id.btnSalvar);
        imgFoto = view.findViewById(R.id.imgFoto);
        pathArray = new ArrayList<>();
        progressBar = view.findViewById(R.id.progressBar);

        firebaseauth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        loadData();

        imgFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Solicita permissao para acessar Galeria de imagens
                if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)){
                        Toast.makeText(getActivity(), "Permissão de acesso negada pelo usuário",Toast.LENGTH_LONG).show();
                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSAO_REQUEST);
                    }
                }

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, GALERIA_IMAGENS);
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pathArray.isEmpty()) {
                    pathArray.add(0, "vazio");
                }
                if (checkPhones() == true) { //se os campos de telefone e celular estiverem preenchidos
                    if (updateData() == true) { //se a atualização dos dados ocorrer bem
                        update(); //chama progressBar para esperar a activity atualizar
                    } else {
                        Toast.makeText(getActivity(), "Erro ao atualizar o registro", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Há campos não preenchidos", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    //Exibir nome do usuário no menu
    public void setUserMenu(String nome, String email){
        // Obtém a referência do layout de navegação
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);

        // Obtém a referência da view de cabeçalho
        View headerView = navigationView.getHeaderView(0);

        // Obtém a referência do nome do usuário e altera seu nome
        TextView txtNomeUser = headerView.findViewById(R.id.nav_nome);
        txtNomeUser.setText("Olá, " +nome);

        TextView txtEmailUser = headerView.findViewById(R.id.nav_email);
        txtEmailUser.setText(email);

        final CircleImageView imgPhotoUser = headerView.findViewById(R.id.nav_PhotoUser);
        firebaseauth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseauth.getCurrentUser();

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("FotosPerfil/"+user.getUid()+".jpg");
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getActivity())
                        .load(uri.toString())
                        .into(imgPhotoUser);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //Se falar, retorna a imagem de perfil padrão
                imgPhotoUser.setImageResource(R.drawable.ic_photo_512);
            }
        });
    }

    //Espera 3segs pra salvar as mudanças e atualizar as informações na activity
    public void update(){
        progressBar.setVisibility(View.VISIBLE);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                while (progressStatus < 100) {
                    progressStatus += 1;
                    // Update the progress bar and display the
                    //current value in the text view
                    handler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressStatus);
                            //textView.setText(progressStatus+"/"+progressBar.getMax());
                        }
                    });
                }
                loadData(); //método para carregar dados
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(),"Registro atualizado com sucesso",Toast.LENGTH_LONG).show();
                //getActivity().recreate();
            }
        }, 4000);

    }

    //Libera o Glide para recuperar imagens
    @GlideModule
    public class MyAppGlideModule extends AppGlideModule {

        @Override
        public void registerComponents(Context context, Glide glide, Registry registry) {
            // Register FirebaseImageLoader to handle StorageReference
            registry.append(StorageReference.class, InputStream.class,
                    new FirebaseImageLoader.Factory());
        }
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
        final FirebaseUser user = firebaseauth.getCurrentUser();

        usuarioReferencia.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nome[0] = dataSnapshot.child("nome").getValue().toString();
                sobrenome[0] = dataSnapshot.child("sobrenome").getValue().toString();
                email[0] = dataSnapshot.child("email").getValue().toString();
                senha[0] = dataSnapshot.child("senha").getValue().toString();

                nomeSobr.setText(dataSnapshot.child("nome").getValue().toString() + " " + dataSnapshot.child("sobrenome").getValue().toString());
                edtTel.setText(dataSnapshot.child("telefone").getValue().toString());
                edtCel.setText(dataSnapshot.child("celular").getValue().toString());
                edtEmail.setText(dataSnapshot.child("email").getValue().toString());

                if (dataSnapshot.child("tipo").getValue().toString().equals("Cliente")) {
                    rbCliente.setChecked(true);
                    rbProprietario.setChecked(false);
                } else {
                    rbProprietario.setChecked(true);
                    rbCliente.setChecked(false);
                }

                if (dataSnapshot.child("sexo").getValue().toString().equals("Feminino")) {
                    rbFeminino.setChecked(true);
                    rbMasculino.setChecked(false);
                } else {
                    rbFeminino.setChecked(false);
                    rbMasculino.setChecked(true);
                }

                setUserMenu(nome[0], email[0]);

                //Retorna foto de perfil
                loadPhoto(imgFoto);
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
            savePhoto();

            return true;
        } catch(Exception e){
            Toast.makeText(getActivity(),"Erro ao atualizar o cadastro "+e.toString(),Toast.LENGTH_SHORT).show();

            return false;
        }
    }

    //Carrega foto de perfil do Storage Firebase
    public void loadPhoto(final ImageView photo){
        firebaseauth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseauth.getCurrentUser();

        try{
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("FotosPerfil/"+user.getUid()+".jpg");
            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(getActivity())
                            .load(uri.toString())
                            .into(imgFoto);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    //Se falar, retorna a imagem de perfil padrão
                    photo.setImageResource(R.drawable.ic_photo_512);
                }
            });
        } catch (Exception e){
            Log.i("ErroFoto", e.toString());
            Toast.makeText(getActivity(),"Erro "+e.toString(),Toast.LENGTH_LONG).show();
        }
    }

    //Salva foto de perfil no Storage Firebase
    public void savePhoto(){
        firebaseauth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseauth.getCurrentUser();

        Uri uri = Uri.fromFile(new File( pathArray.get(0)));
        StorageReference storagereference = mStorageRef.child("FotosPerfil/" + user.getUid() + ".jpg");
        storagereference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.i("uploadPhoto", "Ok");
                //pathArray.clear();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("uploadPhoto", "Erro com a foto");
                //pathArray.clear();
            }
        });
    }

    //Carrega imagem da galeria no perfil
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == GALERIA_IMAGENS){
            Uri selectedImage = data.getData();
            String[] filePath = { MediaStore.Images.Media.DATA };
            Cursor c = getActivity().getApplicationContext().getContentResolver().query(selectedImage, filePath, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath[0]);
            String picturePath = c.getString(columnIndex);
            c.close();
            Bitmap imagemGaleria = (BitmapFactory.decodeFile(picturePath));
            Log.i("PathFileImage", picturePath);
            imgFoto.setImageBitmap(imagemGaleria);
            pathArray.add(0, picturePath);
        }
    }
}

