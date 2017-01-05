package com.zebra.rondaprf;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Fabio on 28/10/16.
 */

    public class ListInfracao extends AppCompatActivity {

        private InfraAdapter customAdapter;
        ListView listView;
        Cursor cursor;
        InfracoesRepo infracaoRepo ;
        private final static String TAG= ListInfracao.class.getName().toString();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.infracoes);
            infracaoRepo = new InfracoesRepo(this);
            cursor=infracaoRepo.getInfracoesList();
            customAdapter = new InfraAdapter(ListInfracao.this,  cursor, 0);
            listView = (ListView) findViewById(R.id.list_infracao);
            listView.setAdapter(customAdapter);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_infracao);
            setSupportActionBar(toolbar);
            // OnCLickListiner For List Items
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {
                    TextView obsTextView = (TextView) arg1.findViewById(R.id.obs_infracao);
                    String obs = obsTextView.getText().toString();
                    setClipboard(ListInfracao.this,obs);
                    Toast.makeText(getApplicationContext(), "Obs Copiada", Toast.LENGTH_LONG).show();

                    return true;
                }
            });


        }

        private void setClipboard(Context context,String text) {
            if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setText(text);

            } else {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("Texto Copiado", text);
                clipboard.setPrimaryClip(clip);

            }
        }

        @Override
        public void onResume(){
            super.onResume();

        }

        @Override
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        public boolean onCreateOptionsMenu(Menu menu) {

            getMenuInflater().inflate(R.menu.menu_infracao, menu);


            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
                SearchView search = (SearchView) menu.findItem(R.id.busca_record).getActionView();
                search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

                search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        Log.d(TAG, "onQueryTextSubmit ");
                        cursor=infracaoRepo.getInfracoesListByKeyword(s);
                        if (cursor==null){
                            Toast.makeText(ListInfracao.this,"Nenhum registro encontrado!",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(ListInfracao.this, cursor.getCount() + " registros encontrados!",Toast.LENGTH_LONG).show();
                        }
                        customAdapter.swapCursor(cursor);

                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        Log.d(TAG, "onQueryTextChange ");
                        cursor=infracaoRepo.getInfracoesListByKeyword(s);
                        if (cursor!=null){
                            customAdapter.swapCursor(cursor);
                        }
                        return false;
                    }

                });

            }

            return true;

        }

    }
