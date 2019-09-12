package com.example.si700_imobiliaria;

import android.net.Uri;

public class Anuncio {

    private String tipo;
    private String endereco;
    private String numero;
    private String cep;
    private String bairro;
    private String cidade;
    private String uf;
    private float valor;
    private String telefone;
    private String celular;
    private String email;
    private String modalidade;
    private String anunciante;
    private String id;
    private int foto;
    private boolean state;
    private Uri teste;

    public Anuncio(){

    }

    public Anuncio(String tipo, String endereco, String numero, String cep, String bairro, String cidade, String uf,
                   float valor, String telefone, String celular, String email, String modalidade, String anunciante, String id, int foto,
                   Uri teste){
        this.tipo = tipo;
        this.endereco = endereco;
        this.numero = numero;
        this.cep = cep;
        this.bairro = bairro;
        this.cidade = cidade;
        this.uf = uf;
        this.valor = valor;
        this.telefone = telefone;
        this.celular = celular;
        this.email = email;
        this.modalidade = modalidade;
        this.anunciante = anunciante;
        this.id = id;
        this.foto = foto;
        this.teste = teste;
    }


    public String toString(){
        return this.id + "\n" + "Valor: " + "R$"+ valor + "\n" + "Cidade: " + cidade + "\n" + "Bairro: " + bairro + "\n" +
                "Endereço: " + endereco;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getModalidade() {
        return modalidade;
    }

    public void setModalidade(String modalidade) {
        this.modalidade = modalidade;
    }

    public String getAnunciante() {
        return anunciante;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAnunciante(String anunciante) {
        this.anunciante = anunciante;
    }

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public Uri getTeste() {
        return teste;
    }

    public void setTeste(Uri teste) {
        this.teste = teste;
    }
}
