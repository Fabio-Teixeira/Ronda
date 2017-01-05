package com.zebra.rondaprf;

/**
 * Created by Fabio on 11/10/16.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Information
    static final String DB_NAME = "RONDA.DB";

    // database version
    static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table tbl_impressoras(_id integer primary key autoincrement, nome text not null, endereco_mac text, padrao boolean);");
        db.execSQL("create table tbl_usuario (_id integer primary key autoincrement, nome text not null, matricula text, cpf text not null, rg text, email text, senha text);");
        db.execSQL("create table tbl_georeferencia (_id integer primary key autoincrement, latitude double, longitude double, br integer, km double);");
        db.execSQL("create table tbl_municipios ( codigo integer primary key not null, sigla_uf char(2) not null, municipio varchar(100) not null, br integer, kmi double, kmf double);");
        db.execSQL("create table tbl_grupo_infracoes (_id integer primary key autoincrement, grupo text not null);");
        db.execSQL("create table tbl_infracoes ( codigo varchar(5) primary key not null, descricao varchar(500) not null, amparo_legal varchar(50), data_vigencia_inicio date, data_vigencia_fim date, pontos_cnh smallint, tipo_infracao char(1), tipo_infrator char(1), obs_sugerida varchar(255), enquadramento varchar(50), id_grupo smallint);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists tbl_impressoras");
        db.execSQL("drop table if exists tbl_usuario");
        db.execSQL("drop table if exists tbl_georeferencia");
        db.execSQL("drop table if exists tbl_municipios");
        db.execSQL("drop table if exists tbl_grupo_infracoes");
        db.execSQL("drop table if exists tbl_infracoes");
        onCreate(db);
    }
}
