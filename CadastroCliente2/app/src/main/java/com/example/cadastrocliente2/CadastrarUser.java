package com.example.cadastrocliente2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.rtoshiro.util.format.MaskFormatter;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CadastrarUser extends AppCompatActivity {

    private TextInputEditText nome;
    private TextInputEditText endereco;
    private TextInputEditText telefone;
    private TextInputEditText email;
    private Button buttonC;
    private Pessoa p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_user);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        buttonC = (Button) findViewById(R.id.cad);
        nome = (TextInputEditText) findViewById(R.id.edNome);
        endereco = (TextInputEditText) findViewById(R.id.edEnd);
        email = (TextInputEditText) findViewById(R.id.edEmail);
        telefone = (TextInputEditText) findViewById(R.id.edTel);

        //mascarando o campo telefone
        SimpleMaskFormatter smf = new SimpleMaskFormatter("(NN) NNNNN-NNNN");
        MaskTextWatcher mtw = new MaskTextWatcher(telefone, smf);
        telefone.addTextChangedListener(mtw);
        Intent intent = getIntent();
        Bundle dados = intent.getExtras();
        //editar usuario
        if(dados != null){
            p = (Pessoa) dados.getSerializable("pessoa");
            setTitle("Editar usuário - código " + p.getId());
            nome.setText(p.getNome());
            endereco.setText(p.getEnd());
            email.setText(p.getEmail());
            telefone.setText(p.getTel());
            buttonC.setText("Editar");
        }
        buttonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validaCampos()){
                    BD bd = new BD(CadastrarUser.this);
                    if(p != null){
                        p.setNome(nome.getText().toString());
                        p.setEmail(email.getText().toString());
                        p.setTel(telefone.getText().toString());
                        p.setEnd(endereco.getText().toString());
                        if(bd.atualizar(p)){
                            AlertDialog.Builder alert = new AlertDialog.Builder(CadastrarUser.this);
                            alert.setTitle(R.string.alerta_titulo); //Alerta
                            alert.setMessage(R.string.alerta_mensagem_edit); //Continuar enditando?
                            alert.setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    startActivity(new Intent(CadastrarUser.this, MainActivity.class));
                                }
                            });
                            alert.setPositiveButton("continuar", null);
                            alert.create();
                            alert.show();

                        }else{
                            AlertDialog.Builder alert = new AlertDialog.Builder(CadastrarUser.this);
                            alert.setTitle(R.string.alerta_titulo); //Alerta
                            alert.setMessage(R.string.alerta_mensagem_user); //Erro inesperado
                            alert.setNeutralButton("ok", null);
                            alert.create();
                            alert.show();
                        }
                    }else{
                        p = new Pessoa();
                        p.setNome(nome.getText().toString());
                        p.setEmail(email.getText().toString());
                        p.setTel(telefone.getText().toString());
                        p.setEnd(endereco.getText().toString());
                        if(bd.inserir(p)){
                            limpar(); //limpa os campos
                            Toast.makeText(CadastrarUser.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                            p = null; //zera a variavel p
                        }else{
                            AlertDialog.Builder alert = new AlertDialog.Builder(CadastrarUser.this);
                            alert.setTitle(R.string.alerta_titulo); //Alerta
                            alert.setMessage(R.string.alerta_mensagem_user); //Erro inesperado
                            alert.setPositiveButton("ok", null);
                            alert.create();
                            alert.show();
                        }
                    }
                }
            }
        });
    }

    public void limpar(){
        email.setText("");
        nome.setText("");
        endereco.setText("");
        telefone.setText("");
    }

    public boolean isCampoVazio(String campo){
        if(campo.isEmpty()){
            return true;
        }else{
            return false;
        }
    }

    public boolean validaCampos(){
        int qtdVazioEinval = 0;
        if(isCampoVazio(nome.getText().toString())){
            nome.requestFocus();
            nome.setError(getString(R.string.erro_mensagem)); //Campo obrigatório.
            qtdVazioEinval++;
        }

        if(isCampoVazio(endereco.getText().toString())){
            endereco.requestFocus();
            endereco.setError(getString(R.string.erro_mensagem));//Campo obrigatório.
            qtdVazioEinval++;
        }

        if(isCampoVazio(email.getText().toString())){
            email.requestFocus();
            email.setError(getString(R.string.erro_mensagem));//Campo obrigatório.
            qtdVazioEinval++;
        }else if(!isEmailValido(email.getText().toString())){
            email.requestFocus();
            email.setError(getString(R.string.erro_mensagem_email));//Campo inválido.
            qtdVazioEinval++;
        }

        if(isCampoVazio(telefone.getText().toString())){
            telefone.requestFocus();
            telefone.setError(getString(R.string.erro_mensagem));//Campo obrigatório.
            qtdVazioEinval++;
        }else if(telefone.getText().length() < 15){
            telefone.requestFocus();
            telefone.setError(getString(R.string.erro_mensagem_email));//Campo inválido.
            qtdVazioEinval++;
        }

        if(qtdVazioEinval > 0){
            AlertDialog.Builder alert = new AlertDialog.Builder(CadastrarUser.this);
            alert.setIcon(R.drawable.ic_error_black_24dp);
            alert.setTitle(R.string.alerta_titulo); //Alerta
            alert.setMessage(R.string.alerta_mensagem); //Campos inválidos ou em branco.
            alert.setNegativeButton("Ok", null);
            alert.create();
            alert.show();
            return false;
        }
        return  true;
    }

    private boolean isEmailValido(String email) {
        String emailValido = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,4}$";
        Pattern pattern;
        pattern = Pattern.compile(emailValido);
        Matcher matcher = pattern.matcher(email);
        if(!matcher.find()){
            return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


}
