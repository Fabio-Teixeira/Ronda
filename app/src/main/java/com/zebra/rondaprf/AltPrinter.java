package com.zebra.rondaprf;

/**
 * Created by Fabio on 11/10/16.
 */


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.Switch;

public class AltPrinter extends Activity implements OnClickListener {

    private EditText nomeText;
    private Button updateBtn, deleteBtn;
    private EditText macText;
    private Switch defImp;

    private long _id;

    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Modificar Registro");

        setContentView(R.layout.alt_printer);

        dbManager = new DBManager(this);
        dbManager.open();

        nomeText = (EditText) findViewById(R.id.nome_edittext);
        macText = (EditText) findViewById(R.id.mac_edittext);
        macText.addTextChangedListener(Mask.insert("##:##:##:##:##:##", macText));

        defImp = (Switch) findViewById(R.id.def_imp);

        updateBtn = (Button) findViewById(R.id.btn_update);
        deleteBtn = (Button) findViewById(R.id.btn_delete);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String nome = intent.getStringExtra("nome");
        String mac = intent.getStringExtra("mac");
        Boolean def = intent.getBooleanExtra("def",true);
        _id = Long.parseLong(id);

        nomeText.setText(nome);
        macText.setText(mac);
        defImp.setChecked(Boolean.valueOf(def));
        updateBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update:
                String nome = nomeText.getText().toString();
                String mac = macText.getText().toString();
                final Boolean def = defImp.isChecked();

                dbManager.updateImp(_id, nome, mac, def);
                this.returnHome();
                break;

            case R.id.btn_delete:
                dbManager.deleteImp(_id);
                this.returnHome();
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
