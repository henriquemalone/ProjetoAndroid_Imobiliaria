package com.example.si700_imobiliaria;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Anuncio_finalizado extends Fragment implements Perfil.OnBackPressedListner{


    public Anuncio_finalizado() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_anuncio_finalizado, container, false);

        return  view;
    }

    //retorna pra fragment Perfil
    @Override
    public boolean onBackPressed() {
        Intent i = new Intent(getActivity(), Perfil.class);
        getActivity().startActivityForResult(i, 1);
        return false;
    }

}
