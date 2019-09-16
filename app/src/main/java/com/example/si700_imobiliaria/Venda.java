package com.example.si700_imobiliaria;


import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
public class Venda extends Fragment {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference anunciosReferencia = databaseReference.child("anuncios");

    ProgressDialog progressDialog;

    List<Anuncio> list = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter ;

    public Venda() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alugar, container, false);

        recyclerView = view.findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading Data from Firebase Database");
        progressDialog.show();

        anunciosReferencia.orderByChild("modalidade").equalTo("Venda").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    final Anuncio anuncio = dataSnapshot.getValue(Anuncio.class);
                    anuncio.setId(dataSnapshot.getKey());
                    String id = dataSnapshot.getKey();
                    System.out.println(id);

                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("/FotosAnuncio/" + id + "/" + id +"_2.jpg"); // get reference of shop_id named logo
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // puttindg logo from Firebase Storage to Image View
                            System.out.println(uri);
                            anuncio.setTeste(uri);// adding to list
                            list.add(anuncio);
                            // change event listener for in databaess
                            adapter.notifyDataSetChanged();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            list.add(anuncio);
                            // change event listener for in databaess
                            adapter.notifyDataSetChanged();
                        }
                    });
                    System.out.println(list);
                    //ist.add(anuncio);
                }

                adapter = new Adapter(getActivity(), list);
                recyclerView.setAdapter(adapter);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressDialog.dismiss();
            }
        });

        DividerItemDecoration decoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(decoration);

        return view;
    }

}
