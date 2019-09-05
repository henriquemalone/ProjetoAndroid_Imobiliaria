package com.example.si700_imobiliaria;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class Anunciar_fotos extends Fragment{

    private Button anunciar;

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
        getActivity().setTitle("Anunciar");

        anunciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getData() != null){
                    if(savePublish() == true){
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment, new Anuncio_finalizado()).commit();
                    }
                } else{
                    Toast.makeText(getActivity(),"Erro",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    public String[] getData(){
        String[] dados = null;
        try{
            Bundle mBundle = new Bundle();
            if(mBundle != null) {
                mBundle = getArguments();
                dados = mBundle.getStringArray("dados");
                Log.i("getData", dados[0]+dados[1]+dados[2]+dados[3]+dados[4]+dados[5]+dados[6]+dados[7]+dados[8]);
            }

            return dados;
        } catch(Exception e){
            Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
            Log.i("getData", e.toString());
            return null;
        }
    }

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
            anuncio.setAnunciante(user.getUid());

            anuncioReferencia.push().setValue(anuncio); //salva dados no banco de dados firebase e usa o UID como ID*/
            Log.i("savePublish", "ok");

            return true;
        } catch (Exception e){
            Log.i("savePublish", e.toString());

            return false;
        }
    }

}
