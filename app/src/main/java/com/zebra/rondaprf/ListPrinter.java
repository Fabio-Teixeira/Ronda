package com.zebra.rondaprf;

/**
 * Created by Fabio on 11/10/16.
 */


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Context;
import android.view.ViewGroup;
import android.view.LayoutInflater;

public class ListPrinter extends AppCompatActivity {

    private DBManager dbManager;

    private ListView listView;

 //   private SimpleCursorAdapter adapter;

//    final String[] from = new String[] { "tbl_impressoras","nome", "endereco_mac", "padrao" };

 //   final int[] to = new int[] { R.id.id, R.id.nome, R.id.mac, R.id.cb_def };

    public class TodoCursorAdapter extends CursorAdapter {
        public TodoCursorAdapter(Context context, Cursor cursor) {
            super(context, cursor, 0);
        }

        // The newView method is used to inflate a new view and return it,
        // you don't bind any data to the view at this point.
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.view_print, parent, false);
        }

        // The bindView method is used to bind all data to a given view
        // such as setting the text on a TextView.
        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            // Find fields to populate in inflated template
            TextView tvId = (TextView) view.findViewById(R.id.id);
            TextView tvNome = (TextView) view.findViewById(R.id.nome);
            TextView tvMac = (TextView) view.findViewById(R.id.mac);
            CheckBox cbDef = (CheckBox) view.findViewById(R.id.cb_def);
            // Extract properties from cursor
            String id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
            String nome = cursor.getString(cursor.getColumnIndexOrThrow("nome"));
            String mac = cursor.getString(cursor.getColumnIndexOrThrow("endereco_mac"));
            String def = cursor.getString(cursor.getColumnIndexOrThrow("padrao"));
            // Populate fields with extracted properties
            tvId.setText(id);
            tvNome.setText(nome);
            tvMac.setText(String.valueOf(mac));
            if (Integer.parseInt(def) == 1 ) {
                cbDef.setChecked(true);
            } else {
                cbDef.setChecked(false);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.impressoras);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_print);
        setSupportActionBar(toolbar);

        dbManager = new DBManager(this);
        dbManager.open();
        Cursor cursor = dbManager.fetchImp();

        listView = (ListView) findViewById(R.id.list_view);
        listView.setEmptyView(findViewById(R.id.empty));

// Setup cursor adapter using cursor from last step
        TodoCursorAdapter todoAdapter = new TodoCursorAdapter(this, cursor);
// Attach cursor adapter to the ListView
        listView.setAdapter(todoAdapter);

        // OnCLickListiner For List Items
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long viewId) {
                TextView idTextView = (TextView) view.findViewById(R.id.id);
                TextView nomeTextView = (TextView) view.findViewById(R.id.nome);
                TextView macTextView = (TextView) view.findViewById(R.id.mac);
                CheckBox defTextView = (CheckBox) view.findViewById(R.id.cb_def);

                String id = idTextView.getText().toString();
                String nome = nomeTextView.getText().toString();
                String mac = macTextView.getText().toString();
                Boolean def = defTextView.isChecked();

                Intent modify_intent = new Intent(getApplicationContext(), AltPrinter.class);
                modify_intent.putExtra("nome", nome);
                modify_intent.putExtra("mac", mac);
                modify_intent.putExtra("id", id);
                modify_intent.putExtra("def", def);

                startActivity(modify_intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_print, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.add_record) {

            Intent add_mem = new Intent(this, AddPrinter.class);
            startActivity(add_mem);

        }
        return super.onOptionsItemSelected(item);
    }

}