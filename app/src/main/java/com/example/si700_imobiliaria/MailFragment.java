package com.example.si700_imobiliaria;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class MailFragment extends Fragment {

    private Button send;
    private EditText login;
    private EditText password;
    private EditText to;
    private EditText assunto;
    private EditText message;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_mail, container, false);
        }

        send = view.findViewById(R.id.send);
        login = view.findViewById(R.id.login);
        assunto = view.findViewById(R.id.assunto);
        message = view.findViewById(R.id.message);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GmailSend gmail = new GmailSend("henrique.malone@gmail.com", "M@ximilium3112","henrique.malone@gmail.com",
                        assunto.getText().toString(), message.getText().toString());

                Toast.makeText(view.getContext(), "Feedback enviado!", Toast.LENGTH_SHORT).show();
                String msg = MailFragment.this.message.getText().toString();

                onDestroy();
                //((MainActivity)getActivity()).doSomething(msg);

                /*AutoresFragment autoresFragment = new AutoresFragment();
                Bundle bundle = new Bundle();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                bundle.putString("mensagem", msg);
                autoresFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment, autoresFragment ,"autoresFragment");
                fragmentTransaction.commit();*/
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

}
