package com.zebra.rondaprf;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Fabio on 28/11/16.
 */

    public class InfracoesRepo {
        private DatabaseHelper dbHelper;

        public InfracoesRepo(Context context) {
            dbHelper = new DatabaseHelper(context);
        }

        public int insert(Infracoes infracoes) {

            //Open connection to write data
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Infracoes.KEY_codigo, infracoes.codigo);
            values.put(Infracoes.KEY_descricao,infracoes.descricao);
            values.put(Infracoes.KEY_obs_infracao, infracoes.obs_sugerida);

            // Inserting Row
            long infracoes_Id = db.insert(Infracoes.TABLE, null, values);
            db.close(); // Closing database connection
            return (int) infracoes_Id;
        }

        public Cursor getInfracoesList() {
            //Open connection to read only
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String selectQuery =  "SELECT  rowid as " +
                    Infracoes.KEY_ROWID + "," +
                    Infracoes.KEY_ID + "," +
                    Infracoes.KEY_codigo + "," +
                    Infracoes.KEY_descricao + "," +
                    Infracoes.KEY_obs_infracao + "," +
                    Infracoes.KEY_tipo_infrator + "," +
                    Infracoes.KEY_pontos_cnh + "," +
                    Infracoes.KEY_amparo_legal + "," +
                    Infracoes.KEY_enquadramento +
                    " FROM " + Infracoes.TABLE;


            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list

            if (cursor == null) {
                return null;
            } else if (!cursor.moveToFirst()) {
                cursor.close();
                return null;
            }
            return cursor;


        }


        public Cursor  getInfracoesListByKeyword(String search) {
            //Open connection to read only
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String selectQuery =  "SELECT  rowid as " +
                    Infracoes.KEY_ROWID + "," +
                    Infracoes.KEY_ID + "," +
                    Infracoes.KEY_codigo + "," +
                    Infracoes.KEY_descricao + "," +
                    Infracoes.KEY_obs_infracao + "," +
                    Infracoes.KEY_tipo_infrator + "," +
                    Infracoes.KEY_pontos_cnh + "," +
                    Infracoes.KEY_amparo_legal + "," +
                    Infracoes.KEY_enquadramento +
                    " FROM " + Infracoes.TABLE +
                    " WHERE " +  Infracoes.KEY_codigo + "  LIKE  '%" +search + "%' or  " +
                    Infracoes.KEY_descricao + "  LIKE  '%" +search + "%' or " +
                    Infracoes.KEY_obs_infracao + "  LIKE  '%" +search + "%' "
                    ;


            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list

            if (cursor == null) {
                return null;
            } else if (!cursor.moveToFirst()) {
                cursor.close();
                return null;
            }
            return cursor;


        }

        public Infracoes getInfracoesById(int Id){
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String selectQuery =  "SELECT " +
                    Infracoes.KEY_ID + "," +
                    Infracoes.KEY_codigo + "," +
                    Infracoes.KEY_descricao + "," +
                    Infracoes.KEY_obs_infracao + "," +
                    Infracoes.KEY_tipo_infrator + "," +
                    Infracoes.KEY_pontos_cnh + "," +
                    Infracoes.KEY_amparo_legal + "," +
                    Infracoes.KEY_enquadramento +
                    " FROM " + Infracoes.TABLE
                    + " WHERE " +
                    Infracoes.KEY_ID + "=?";// It's a good practice to use parameter ?, instead of concatenate string

            int iCount =0;
            Infracoes infracoes = new Infracoes();

            Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(Id) } );

            if (cursor.moveToFirst()) {
                do {
                    infracoes.infracao_ID =cursor.getInt(cursor.getColumnIndex(Infracoes.KEY_ID));
                    infracoes.codigo =cursor.getString(cursor.getColumnIndex(Infracoes.KEY_codigo));
                    infracoes.descricao  =cursor.getString(cursor.getColumnIndex(Infracoes.KEY_descricao));
                    infracoes.obs_sugerida =cursor.getString(cursor.getColumnIndex(Infracoes.KEY_obs_infracao));
                    infracoes.tipo_infrator =cursor.getString(cursor.getColumnIndex(Infracoes.KEY_tipo_infrator));
                    infracoes.pontos_cnh =cursor.getString(cursor.getColumnIndex(Infracoes.KEY_pontos_cnh));
                    infracoes.amparo_legal =cursor.getString(cursor.getColumnIndex(Infracoes.KEY_amparo_legal));
                    infracoes.enquadramento =cursor.getString(cursor.getColumnIndex(Infracoes.KEY_enquadramento));

                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
            return infracoes;
        }




    }