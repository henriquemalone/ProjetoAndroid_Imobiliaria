package com.example.si700_imobiliaria;


import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeusAnuncios extends Fragment {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference anunciosReferencia = databaseReference.child("anuncios");
    private FirebaseAuth firebaseauth;

    ProgressDialog progressDialog;

    List<Anuncio> list = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter ;
    ImageView teste;

    public MeusAnuncios() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_meus_anuncios, container, false);

        firebaseauth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseauth.getCurrentUser();
        recyclerView = view.findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading Data from Firebase Database");
        progressDialog.show();
        teste = view.findViewById(R.id.foto);

        anunciosReferencia.orderByChild("anunciante").equalTo(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    final Anuncio anuncio = dataSnapshot.getValue(Anuncio.class);
                    anuncio.setId(dataSnapshot.getKey());
                    String id = dataSnapshot.getKey();
                    System.out.println(id);
                    //list.add(anuncio);

                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                    StorageReference dateRef = storageRef.child("FotosAnuncio/"+id+
                            "/"+ id + "_0.jpg");
                    dateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>(){
                        @Override
                        public void onSuccess(Uri downloadUrl)
                        {
                            anuncio.setTeste(downloadUrl);
                            //System.out.println(downloadUrl);
                        }
                    });

                    list.add(anuncio);
                }

                System.out.println(list + "teste");

                adapter = new Adapter(getActivity(), list);
                recyclerView.setAdapter(adapter);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressDialog.dismiss();
            }
        });
        return view;
    }
}
