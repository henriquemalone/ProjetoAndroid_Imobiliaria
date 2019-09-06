package com.example.si700_imobiliaria;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.lang.reflect.Array;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeusAnuncios extends Fragment {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference anunciosReferencia = databaseReference.child("anuncios");
    ListView listView;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;

    private FirebaseAuth firebaseauth;

    public MeusAnuncios() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_meus_anuncios, container, false);

        firebaseauth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseauth.getCurrentUser();

        //anunciosReferencia = FirebaseDatabase.getInstance().getReference("anuncios").orderByChild("anunciante").equalTo(user.getUid());
        listView = view.findViewById(R.id.list);
        arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        listView.setAdapter(arrayAdapter);
        anunciosReferencia.orderByChild("anunciante").equalTo(user.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //Log.i("lista", databaseReference.toString());
                //dataSnapshot.getValue(Anuncio.class);
                Anuncio anuncio = new Anuncio();
                anuncio.setId(dataSnapshot.getKey());
                anuncio.setCidade(dataSnapshot.child("cidade").getValue().toString());
                anuncio.setBairro(dataSnapshot.child("bairro").getValue().toString());
                anuncio.setCidade(dataSnapshot.child("endereco").getValue().toString());
                anuncio.setValor(Float.parseFloat(dataSnapshot.child("valor").getValue().toString()));
                String anuncioString = String.valueOf(anuncio);
                arrayAdapter.add(anuncioString);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

}
