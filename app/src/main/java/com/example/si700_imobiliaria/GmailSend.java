package com.example.si700_imobiliaria;

import android.content.pm.PackageInstaller;
import android.os.AsyncTask;
import android.util.Log;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class GmailSend extends javax.mail.Authenticator{

    public GmailSend(final String username, final String password,
                     String email_to,
                     String email_subject,
                     String email_body){

        Log.i("GmailSend", email_to);
        Log.i("GmailSend",email_body);

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(username,password);
            }
        });

        try {
            final Message message =  new MimeMessage(session);
            message.setFrom(new InternetAddress("from@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email_to));
            message.setSubject(email_subject);
            message.setText(email_body);

            new AsyncTask<Void, Void, Void>() {
                @Override public Void doInBackground(Void... arg) {
                    try {
                        Transport.send(message);
                    } catch (Exception e) {
                        Log.e("GmailSend", e.getMessage(), e);
                    }
                    return null;}
            }.execute();



            System.out.println("Enviado");

        }catch (MessagingException e){
            e.printStackTrace();
        }
    }
}
