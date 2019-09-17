package com.example.si700_imobiliaria;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class Anunciar_fotos extends Fragment{

    private Button anunciar;
    private ImageView foto1;
    private ImageView foto2;
    private ImageView foto3;
    private LinearLayout layou1;
    private LinearLayout layou2;
    private LinearLayout layou3;
    private String id;

    private final int GALERIA_IMAGENS = 1;
    private ArrayList<String> pathArray;
    private int clicked = 0;

    private StorageReference mStorageRef;
    private FirebaseAuth firebaseauth;
    private DatabaseReference firebasereference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference anuncioReferencia = firebasereference.child("anuncios");

    public Anunciar_fotos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_anunciar_fotos, container, false);

        anunciar = view.findViewById(R.id.btnAnunciar);
        foto1 = view.findViewById(R.id.foto1);
        foto2 = view.findViewById(R.id.foto2);
        foto3 = view.findViewById(R.id.foto3);
        layou1 = view.findViewById(R.id.layout1);
        layou2 = view.findViewById(R.id.layout2);
        layou3 = view.findViewById(R.id.layout3);
        pathArray = new ArrayList<>();

        firebaseauth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        getActivity().setTitle("Anunciar");

        anunciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getData() != null){
                    if(savePublish() == true){
                        if(savePhoto() == true){
                            //Abre fragment Anuncio finalizado
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fragment, new Anuncio_finalizado()).commit();
                        } else{
                            Toast.makeText(getActivity(),"Erro Fotos",Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(),"Erro Publicação",Toast.LENGTH_SHORT).show();
                    }
                } else{
                    Toast.makeText(getActivity(),"Erro Dados",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Botão layout 1
        layou1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked = 1;
                //abre galeria
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, GALERIA_IMAGENS);
            }
        });

        //Botão layout 2
        layou2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked = 2;
                //abre galeria
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, GALERIA_IMAGENS);
            }
        });

        //Botão layout 3
        layou3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked = 3;
                //abre galeria
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, GALERIA_IMAGENS);
            }
        });
        return view;
    }

    //Recebe dados do anuncio preenchidos pelo usuário
    public String[] getData(){
        String[] dados = null;
        try{
            Bundle mBundle = new Bundle();
            if(mBundle != null) {
                mBundle = getArguments();
                dados = mBundle.getStringArray("dados");
                //Log.i("getData", dados[0]+dados[1]+dados[2]+dados[3]+dados[4]+dados[5]+dados[6]+dados[7]+dados[8]);
            }
            return dados;
        } catch(Exception e){
            Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
            Log.i("getData", e.toString());
            return null;
        }
    }

    //Salva anuncio
    public boolean savePublish(){
        Anuncio anuncio = new Anuncio();
        firebaseauth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseauth.getCurrentUser();

        try{
            anuncio.setTipo(getData()[0]);
            anuncio.setUf(getData()[1]);
            anuncio.setModalidade(getData()[2]);
            anuncio.setEndereco(getData()[3]);
            anuncio.setNumero(getData()[4]);
            anuncio.setCep(getData()[5]);
            anuncio.setCidade(getData()[6]);
            anuncio.setBairro(getData()[7]);
            anuncio.setValor(Float.valueOf(getData()[8]));
            anuncio.setEmail(getData()[9]);
            anuncio.setCelular(getData()[10]);
            anuncio.setTelefone(getData()[11]);
            anuncio.setAnunciante(user.getUid());

            id = anuncioReferencia.push().getKey(); //recebe ID do anuncio
            anuncioReferencia.child(id).setValue(anuncio); //salva dados no banco de dados firebase e usa o UID como ID*/
            Log.i("savePublish",id);
            Log.i("savePublish", "ok");

            Anuncio_finalizado anuncio_finalizado = new Anuncio_finalizado();
            Bundle data = new Bundle();
            data.putString("chave",id);
            anuncio_finalizado.setArguments(data);

            return true;
        } catch (Exception e){
            Log.i("savePublish", e.toString());

            return false;
        }
    }

    //Salva foto do anuncio no Storage Firebase
    public boolean savePhoto(){
        int i = 0;

        try {
            for (i = 0; i < 3; i++) {
                final String aux = String.valueOf(i);
                Uri uri = Uri.fromFile(new File(pathArray.get(i)));
                StorageReference storagereference = mStorageRef.child("FotosAnuncio/" + id + "/" + id + "_" + aux + ".jpg");
                Log.i("uploadPhoto", aux);
                storagereference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
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

            return true;
        } catch (Exception e) {
            Log.i("uploadPhoto", e.toString());

            return false;
        }
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
            //foto1.setImageBitmap(imagemGaleria);
            if(clicked == 1){
                setFoto1(imagemGaleria);
            }
            if(clicked == 2){
                setFoto2(imagemGaleria);
            }
            if(clicked == 3){
                setFoto3(imagemGaleria);
            }
            pathArray.add(0, picturePath);
        }
    }

    //Carrega foto selecionada no bitmap 1
    public void setFoto1(Bitmap imagemGaleria){
        foto1.setImageBitmap(imagemGaleria);
    }

    //Carrega foto selecionada no bitmap 2
    public void setFoto2(Bitmap imagemGaleria){
        foto2.setImageBitmap(imagemGaleria);
    }

    //Carrega foto selecionada no bitmap 3
    public void setFoto3(Bitmap imagemGaleria){
        foto3.setImageBitmap(imagemGaleria);
    }
}
