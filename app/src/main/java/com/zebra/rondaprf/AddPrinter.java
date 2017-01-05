package com.zebra.rondaprf;

/**
 * Created by Fabio on 11/10/16.
 */

        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.view.inputmethod.InputMethodManager;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Switch;

public class AddPrinter extends Activity implements View.OnClickListener {

    private Button addTodoBtn;
    private EditText nomeEditText;
    private EditText macEditText;
    private Switch defImp;

    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Adicionar Impressora");

        setContentView(R.layout.add_printer);

        nomeEditText = (EditText) findViewById(R.id.nome_edittext);
        macEditText = (EditText) findViewById(R.id.mac_edittext);
        macEditText.addTextChangedListener(Mask.insert("##:##:##:##:##:##", macEditText));
        defImp = (Switch) findViewById(R.id.def_imp);


        addTodoBtn = (Button) findViewById(R.id.add_record);

        dbManager = new DBManager(this);
        dbManager.open();
        addTodoBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_record:

                final String nome = nomeEditText.getText().toString();
                final String mac = macEditText.getText().toString();
                final Boolean def = defImp.isChecked();

                dbManager.insertImp(nome, mac, def);

// esconde o teclado
                View view = this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }


                Intent main = new Intent(AddPrinter.this, ListPrinter.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(main);
                break;
        }
    }

}