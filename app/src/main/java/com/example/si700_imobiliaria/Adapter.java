package com.example.si700_imobiliaria;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{
    Context context;
    List<Anuncio> MainImageUploadInfoList;

    public Adapter(Context context, List<Anuncio> TempList) {
        this.MainImageUploadInfoList = TempList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_layout, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id;

                id = viewHolder.id.getText().toString();

                verAnuncio verAnuncio = new verAnuncio();
                Bundle data = new Bundle();
                data.putString("chave",id);
                verAnuncio.setArguments(data);


                System.out.println(id+" - adapter");

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment, verAnuncio).addToBackStack(null).commit();

            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Anuncio anuncio = MainImageUploadInfoList.get(position);
        holder.id.setText(anuncio.getId());
        holder.valor.setText(String.valueOf(anuncio.getValor()));
        holder.cidade.setText(anuncio.getCidade());
        holder.bairro.setText(anuncio.getBairro());
        holder.endereco.setText(anuncio.getEndereco());

        Glide.with(context)
                .load(anuncio.getTeste())
                .into(holder.foto);
    }

    @Override
    public int getItemCount() {
        //System.out.println(MainImageUploadInfoList.size());
        return MainImageUploadInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView id;
        public TextView valor;
        public TextView cidade;
        public TextView bairro;
        public TextView endereco;
        public ImageView foto;

        public ViewHolder(View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.txtId);
            valor = itemView.findViewById(R.id.txtValor);
            cidade = itemView.findViewById(R.id.txtCidade);
            bairro = itemView.findViewById(R.id.txtBairro);
            endereco = itemView.findViewById(R.id.txtEndereco);
            foto = itemView.findViewById(R.id.foto);
        }
    }
}
