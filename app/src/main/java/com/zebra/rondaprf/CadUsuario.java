package com.zebra.rondaprf;

/**
 * Created by Fabio on 11/10/16.
 */


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

public class CadUsuario extends Activity implements OnClickListener {

    private EditText nomeUsuario;
    private EditText matriculaUsuario;
    private EditText cpfUsuario;
    private EditText rgUsuario;
    private EditText emailUsuario;
    private EditText senhaUsuario;
    private Button updateBtn;

    private long _id;

    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Cadastro do Usuário");

        setContentView(R.layout.usuario);

        dbManager = new DBManager(this);
        dbManager.open();
        String Usuario = dbManager.getUsuario();

        nomeUsuario = (EditText) findViewById(R.id.nomeUsuario);
        matriculaUsuario = (EditText) findViewById(R.id.matriculaUsuario);
        cpfUsuario = (EditText) findViewById(R.id.cpfUsuario);
        rgUsuario = (EditText) findViewById(R.id.rgUsuario);
        emailUsuario = (EditText) findViewById(R.id.emailUsuario);
        senhaUsuario = (EditText) findViewById(R.id.senhaUsuario);
        updateBtn = (Button) findViewById(R.id.btn_salvaUsuario);

        nomeUsuario.setText(Usuario.split(";")[0]);
        matriculaUsuario.setText(Usuario.split(";")[1]);
        cpfUsuario.setText(Usuario.split(";")[2]);
        rgUsuario.setText(Usuario.split(";")[3]);
        emailUsuario.setText(Usuario.split(";")[4]);
        senhaUsuario.setText(Usuario.split(";")[5]);
        updateBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_salvaUsuario:
                if (updateBtn.getText().equals("Editar")) {
                    updateBtn.setText("Salvar");
                    nomeUsuario.setEnabled(true);
                    matriculaUsuario.setEnabled(true);
                    cpfUsuario.setEnabled(true);
                    rgUsuario.setEnabled(true);
                    emailUsuario.setEnabled(true);
                    senhaUsuario.setEnabled(true);
                } else {
                    String nome = nomeUsuario.getText().toString();
                    String matricula = matriculaUsuario.getText().toString();
                    String cpf = cpfUsuario.getText().toString();
                    String rg = rgUsuario.getText().toString();
                    String email = emailUsuario.getText().toString();
                    String senha = senhaUsuario.getText().toString();

                    dbManager.updateUsuario(_id, nome, matricula, cpf, rg, email, senha);
//                this.returnHome();

                    Intent intent = new Intent(this, ActivityMenu.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

                break;
        }
    }

    public void returnHome() {
// esconde o teclado
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
//
        Intent home_intent = new Intent(getApplicationContext(), ListPrinter.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);
    }
}
