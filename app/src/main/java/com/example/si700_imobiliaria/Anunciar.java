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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Anunciar extends Fragment {

    private Button proximo;
    private Spinner tipo;
    private Spinner uf;
    private RadioButton locacao;
    private Button venda;
    private EditText endereco;
    private EditText numero;
    private EditText cep;
    private EditText cidade;
    private EditText bairro;
    private EditText valor;

    private FirebaseAuth firebaseauth;

    public Anunciar() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_anunciar, container, false);

        proximo = view.findViewById(R.id.btnProximo);
        locacao = view.findViewById(R.id.rbLocação);
        venda = view.findViewById(R.id.rbVenda);
        endereco = view.findViewById(R.id.edtEndereco);
        numero = view.findViewById(R.id.edtNumero);
        cep = view.findViewById(R.id.edtCep);
        cidade = view.findViewById(R.id.edtCidade);
        bairro = view.findViewById(R.id.edtBairro);
        valor = view.findViewById(R.id.edtValor);

        getActivity().setTitle("Anunciar");

        //Cria menu dropdown Tipo de imovel
        tipo = (Spinner) view.findViewById(R.id.tipos_imoveis);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> tipos = ArrayAdapter.createFromResource(getActivity(),
                R.array.tipos_imoveis, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        tipos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        tipo.setAdapter(tipos);
        // Inflate the layout for this fragment

        //Cria menu dropdown UF
        uf = (Spinner) view.findViewById(R.id.uf);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> ufs = ArrayAdapter.createFromResource(getActivity(),
                R.array.uf, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        ufs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        uf.setAdapter(ufs);
        // Inflate the layout for this fragment

        proximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getData() != null){
                    Anunciar_fotos anunciar_fotos = new Anunciar_fotos();
                    Bundle bundle = new Bundle();
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    bundle.putStringArray("dados", getData());
                    anunciar_fotos.setArguments(bundle);
                    fragmentTransaction.replace(R.id.fragment, anunciar_fotos ,"NewFragmentTag");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else{
                    Toast.makeText(getActivity(),"Erro ao publicar anuncio",Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    public String[] getData(){
        String aux;

        try{
            if(locacao.isChecked()){
                aux = "Locação";
            } else{
                aux = "Venda";
            }

            String[] dados = new String[]{tipo.getSelectedItem().toString(),
                    uf.getSelectedItem().toString(),
                    aux,
                    endereco.getText().toString(),
                    numero.getText().toString(),
                    cep.getText().toString(),
                    cidade.getText().toString(),
                    bairro.getText().toString(),
                    String.valueOf(Float.valueOf(valor.getText().toString()))};

            return dados;
        } catch (Exception e){
            Log.i("anunciarErro", e.toString());
            return null;
        }

    }

}
