package com.example.si700_imobiliaria;


import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class verAnuncio extends Fragment {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference anunciosReferencia = databaseReference.child("anuncios");
    private FirebaseAuth firebaseauth;

    private String id;
    private TextView cidade;
    private TextView tipo;
    private TextView endereco;
    private TextView numero;
    private TextView bairro;
    private TextView cep;
    private TextView estado;
    private TextView valor;
    private TextView telefone;
    private TextView celular;
    private TextView email;
    private TextView modalidade;
    private ImageView foto1;
    private ImageView foto2;
    private ImageView foto3;

    public verAnuncio() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ver_anuncio, container, false);

        cidade = view.findViewById(R.id.cidade);
        tipo = view.findViewById(R.id.tipo);
        endereco = view.findViewById(R.id.endereco);
        numero = view.findViewById(R.id.numero);
        bairro = view.findViewById(R.id.bairro);
        cep = view.findViewById(R.id.cep);
        estado = view.findViewById(R.id.uf);
        valor = view.findViewById(R.id.valor);
        telefone = view.findViewById(R.id.telefone);
        celular = view.findViewById(R.id.celular);
        email = view.findViewById(R.id.email);
        modalidade = view.findViewById(R.id.modalidade);
        foto1 = view.findViewById(R.id.foto1);
        foto2 = view.findViewById(R.id.foto2);
        foto3 = view.findViewById(R.id.foto3);

        loadPhoto();

        anunciosReferencia.child(idAnuncio()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Anuncio anuncio = snapshot.getValue(Anuncio.class); //Manda valores do anuncio para o objeto anuncio
                cidade.setText(anuncio.getCidade());
                modalidade.setText(anuncio.getModalidade());
                tipo.setText(anuncio.getTipo());
                endereco.setText(anuncio.getEndereco());
                numero.setText(anuncio.getNumero());
                bairro.setText(anuncio.getBairro());
                cep.setText(anuncio.getCep());
                estado.setText(anuncio.getUf());
                valor.setText(String.valueOf(anuncio.getValor()));
                telefone.setText(anuncio.getTelefone());
                celular.setText(anuncio.getCelular());
                email.setText(anuncio.getEmail());
                //System.out.println(list);
                // ist.add(anuncio);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    public String idAnuncio() {
        String id = null;

        Bundle data = new Bundle();
        if (data != null) {
            data = getArguments();
            id = data.getString("chave");
            System.out.println(id + " - bundle");
            //Log.i("getData", dados[0]+dados[1]+dados[2]+dados[3]+dados[4]+dados[5]+dados[6]+dados[7]+dados[8]);
        }

        return id;
    }

    //Carrega foto de perfil do Storage Firebase
    public void loadPhoto() {
            try {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("/FotosAnuncio/" +
                        idAnuncio() + "/" + idAnuncio() +"_2.jpg");
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getActivity())
                                .load(uri.toString())
                                .into(foto1);
                    }


                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        //Se falar, retorna a imagem de perfil padrão
                        //photo.setImageResource(R.drawable.ic_photo_512);
                    }
                });

                storageReference = FirebaseStorage.getInstance().getReference().child("/FotosAnuncio/" +
                        idAnuncio() + "/" + idAnuncio() +"_1.jpg");
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getActivity())
                                .load(uri.toString())
                                .into(foto2);
                    }


                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        //Se falar, retorna a imagem de perfil padrão
                        //photo.setImageResource(R.drawable.ic_photo_512);
                    }
                });

                storageReference = FirebaseStorage.getInstance().getReference().child("/FotosAnuncio/" +
                        idAnuncio() + "/" + idAnuncio() +"_0.jpg");
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getActivity())
                                .load(uri.toString())
                                .into(foto3);
                    }


                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        //Se falar, retorna a imagem de perfil padrão
                        //photo.setImageResource(R.drawable.ic_photo_512);
                    }
                });
            } catch (Exception e) {
                Log.i("ErroFoto", e.toString());
                Toast.makeText(getActivity(), "Erro " + e.toString(), Toast.LENGTH_LONG).show();
            }
    }
}
