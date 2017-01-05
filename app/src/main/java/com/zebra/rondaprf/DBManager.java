package com.zebra.rondaprf;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class DBManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

//tabela impressora
    public void insertImp(String nome, String mac, Boolean def) {
        if (def) {cbClear(0);}
        ContentValues contentValue = new ContentValues();
        contentValue.put("nome", nome);
        contentValue.put("endereco_mac", mac);
        contentValue.put("padrao", def);
        database.insert("tbl_impressoras", null, contentValue);
    }

    public Cursor fetchImp() {
        String[] columns = new String[] { "_id", "nome", "endereco_mac", "padrao" };
        Cursor cursor = database.query("tbl_impressoras", columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int updateImp(long _id, String nome, String mac, Boolean def) {
        if (def) {cbClear(_id);}
        ContentValues contentValue = new ContentValues();
        contentValue.put("nome", nome);
        contentValue.put("endereco_mac", mac);
        contentValue.put("padrao", def);
        int i = database.update("tbl_impressoras", contentValue, "_id = " + _id, null);
        return i;
    }

    public void deleteImp(long _id) {
        database.delete("tbl_impressoras", "_id = " + _id, null);
    }

    public void cbClear (long _id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("padrao", 0);
        database.update("tbl_impressoras", contentValues, "_id <> " + _id, null);

    }

    public String getMac(String macNo) {

        Cursor cursor = null;
        String mac = "";
        try{

            cursor = database.rawQuery("SELECT endereco_mac FROM tbl_impressoras WHERE padrao=?", new String[] {macNo + ""});

            if(cursor.getCount() > 0) {

                cursor.moveToFirst();
                mac = cursor.getString(cursor.getColumnIndex("endereco_mac"));
            }

            return mac;
        }finally {

            cursor.close();
        }
    }
//Tabelas georeferencia
    public String getNearGeo(String latitude) {

        Cursor cursor = null;
        Double lat=0.0;
        Double lon=0.0;
        Integer br=0;
        Double km=0.0;
        try{

            cursor = database.rawQuery("SELECT latitude,longitude,br,km FROM tbl_georeferencia order by ABS(latitude - (?)) ASC;",new String[] {latitude});
            if(cursor.getCount() > 0) {

                cursor.moveToFirst();
                lat = cursor.getDouble(cursor.getColumnIndex("latitude"));
                lon = cursor.getDouble(cursor.getColumnIndex("longitude"));
                br = cursor.getInt(cursor.getColumnIndex("br"));
                km = cursor.getDouble(cursor.getColumnIndex("km"));
            }

            return (lat+";"+lon+";"+br+";"+km);
        }finally {

                cursor.close();
        }
    }

// Tabela Usuario
public void insertUsuario(String nome, String matricula, String cpf, String rg, String email, String senha) {
    ContentValues contentValue = new ContentValues();
    contentValue.put("nome", nome);
    contentValue.put("matricula", matricula);
    contentValue.put("cpf", cpf);
    contentValue.put("rg", rg);
    contentValue.put("email", email);
    contentValue.put("senha", senha);
    database.insert("tbl_usuario", null, contentValue);
}

    public int updateUsuario(long _id, String nome, String matricula, String cpf, String rg, String email, String senha) {
        ContentValues contentValue = new ContentValues();
        contentValue.put("nome", nome);
        contentValue.put("matricula", matricula);
        contentValue.put("cpf", cpf);
        contentValue.put("rg", rg);
        contentValue.put("email", email);
        contentValue.put("senha", senha);
        int i = database.update("tbl_usuario", contentValue, _id + " = " + _id, null);
        return i;
    }
// get usuario
public String getUsuario() {

    Cursor cursor = null;
    String nome = "";
    String matricula = "";
    String cpf = "";
    String rg = "";
    String email = "";
    String senha = "";
    try{

        cursor = database.rawQuery("SELECT * FROM tbl_usuario",null);

        if(cursor.getCount() > 0) {

            cursor.moveToFirst();
            nome = cursor.getString(cursor.getColumnIndex("nome"));
            matricula = cursor.getString(cursor.getColumnIndex("matricula"));
            cpf = cursor.getString(cursor.getColumnIndex("cpf"));
            rg = cursor.getString(cursor.getColumnIndex("rg"));
            email = cursor.getString(cursor.getColumnIndex("email"));
            senha = cursor.getString(cursor.getColumnIndex("senha"));
        }

        return (nome+";"+matricula+";"+cpf+";"+rg+";"+email+";"+senha);
    }finally {

 //       cursor.close();
    }
}



// tabela georeferencia
    public void insertGeo(String latitude, String longitude, String br,String km) {
        ContentValues contentValue = new ContentValues();
        contentValue.put("latitude", Double.parseDouble(latitude));
        contentValue.put("longitude", Double.parseDouble(longitude));
        contentValue.put("br", Integer.parseInt(br));
        contentValue.put("km", Double.parseDouble(km));
        database.insert("tbl_georeferencia", null, contentValue);
    }
//tabela infraçoes
    public void insertInfracao(String codigo, String descricao, String amparo_legal, String data_vigencia_inicio, String data_vigencia_fim, String pontos_cnh, String tipo_infracao, String tipo_infrator, String obs_sugerida, String enquadramento, String id_grupo) {
        ContentValues contentValue = new ContentValues();
        contentValue.put("codigo",codigo);
        contentValue.put("descricao",descricao);
        contentValue.put("amparo_legal",amparo_legal);
        contentValue.put("data_vigencia_inicio",data_vigencia_inicio);
        contentValue.put("data_vigencia_fim",data_vigencia_fim);
        contentValue.put("pontos_cnh",Integer.parseInt(pontos_cnh));
        contentValue.put("tipo_infracao",tipo_infracao);
        contentValue.put("tipo_infrator",tipo_infrator);
        contentValue.put("obs_sugerida",obs_sugerida);
        contentValue.put("enquadramento",enquadramento);
        contentValue.put("id_grupo",id_grupo);
        database.insert("tbl_infracoes", null, contentValue);
    }

    public Cursor fetchInfra() {
        String[] columns = new String[] {"codigo as _id","codigo", "descricao", "obs_sugerida"};
        Cursor cursor = database.query("tbl_infracoes", columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }


//tabela municipios
    public void insertMunicipios(String codigo, String sigla_uf, String municipio, String br, String kmi, String kmf) {
        ContentValues contentValue = new ContentValues();
        contentValue.put("codigo",Integer.parseInt(codigo));
        contentValue.put("sigla_uf",sigla_uf);
        contentValue.put("municipio",municipio);
        contentValue.put("br",Integer.parseInt(br));
        contentValue.put("kmi",Double.parseDouble(kmi));
        contentValue.put("kmf",Double.parseDouble(kmf));
        database.insert("tbl_municipios", null, contentValue);
    }

    public Cursor fetchMunicipios() {
        String[] columns = new String[] { "codigo as _id","codigo", "municipio", "kmi", "kmf" };
        Cursor cursor = database.query("tbl_municipios", columns, "kmf<>0.0", null, null, null, "kmi");
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }


    public String getMunicipio(String _br, String _km) {

        Cursor cursor = null;
        Integer codigo=0;
        String municipio=" ";
        String sigla_uf=" ";
        try{

            cursor = database.rawQuery("SELECT codigo,sigla_uf,municipio FROM tbl_municipios where br=? and kmi <= ? and kmf >= ? ;",new String[] {_br,_km,_km});

            if(cursor.getCount() > 0) {

                cursor.moveToFirst();
                codigo = cursor.getInt(cursor.getColumnIndex("codigo"));
                municipio = cursor.getString(cursor.getColumnIndex("municipio"));
                sigla_uf = cursor.getString(cursor.getColumnIndex("sigla_uf"));
            }

            return (codigo+";"+municipio+";"+sigla_uf);
        }finally {

        cursor.close();
        }
    }


// registros iniciais
    public void criaRegistros() {
// Carrega coordenadas
        Cursor curGeo = database.rawQuery("SELECT EXISTS (SELECT 1 FROM tbl_georeferencia)", null);

        if (curGeo != null) {
            curGeo.moveToFirst();
            if (curGeo.getInt (0) == 0) {
                AssetManager assetManager = context.getAssets();
                try {
                    InputStream csvStream = assetManager.open("TBL_COORDENADAS.csv");
                    InputStreamReader csvStreamReader = new InputStreamReader(csvStream);
                    BufferedReader csvReader = new BufferedReader(csvStreamReader);
                    String line;
                    while ((line = csvReader.readLine()) != null) {
                        String[] row = line.split(";");
                        String latitude = row[0];
                        String longitude = row[1];
                        String br = row[2];
                        String km = row[3];
                        this.insertGeo(latitude, longitude, br, km);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
        } else {
                // Tabela ja contem dados.
            }
        }
// Carrega infrações e Observações
        Cursor curInfra = database.rawQuery("SELECT EXISTS (SELECT 1 FROM tbl_infracoes)", null);

        if (curInfra != null) {
            curInfra.moveToFirst();
            if (curInfra.getInt (0) == 0) {
                // Tabela esta vazia, preencha com seus dados iniciais
                AssetManager assetManager = context.getAssets();
                try {
                    InputStream csvStream = assetManager.open("TBL_INFRACOES.csv");
                    InputStreamReader csvStreamReader = new InputStreamReader(csvStream);
                    BufferedReader csvReader = new BufferedReader(csvStreamReader);
                    String line;
                    while ((line = csvReader.readLine()) != null) {
                        String[] row = line.split(";");
                        String codigo = row[0];
                        String descricao = row[1];
                        String amparo_legal = row[2];
                        String data_vigencia_inicio = row[3];//data
                        String data_vigencia_fim = row[4];//data
                        String pontos_cnh = row[5];//integer
                        String tipo_infracao = row[6];
                        String tipo_infrator = row[7];
                        String obs_sugerida = row[8];
                        String enquadramento = row[9];
                        String id_grupo = "1";//integer
                        this.insertInfracao(codigo,descricao,amparo_legal,data_vigencia_inicio,data_vigencia_fim,pontos_cnh,tipo_infracao,tipo_infrator,obs_sugerida,enquadramento,id_grupo);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                // Tabela ja contem dados.
            }
        }

// Carrega usuario inicial
        Cursor curUsuario = database.rawQuery("SELECT EXISTS (SELECT 1 FROM tbl_usuario)", null);

        if (curUsuario != null) {
            curUsuario.moveToFirst();
            if (curUsuario.getInt(0) == 0) {
                // Tabela esta vazia, preencha com seus dados iniciais
                this.insertUsuario("Fabio Braga", "0298150", "015.345.077-01", "919.554/SSP-ES", "fabio.braga@prf.gov.br", "senha");
            } else {
                // Tabela ja contem dados.
            }
        }

// Carrega Municipios
        Cursor curMunicipio = database.rawQuery("SELECT EXISTS (SELECT 1 FROM tbl_municipios)", null);

        if (curMunicipio != null) {
            curMunicipio.moveToFirst();
            if (curMunicipio.getInt (0) == 0) {
                // Tabela esta vazia, preencha com seus dados iniciais
                AssetManager assetManager = context.getAssets();
                try {
                    InputStream csvStream = assetManager.open("TBL_MUNICIPIOS.csv");
                    InputStreamReader csvStreamReader = new InputStreamReader(csvStream);
                    BufferedReader csvReader = new BufferedReader(csvStreamReader);
                    String line;
                    while ((line = csvReader.readLine()) != null) {
                        String[] row = line.split(";");
                        String codigo = row[0];
                        String sigla_uf = row[1];
                        String municipio = row[2];
                        String br = row[3];
                        String kmi = row[4];
                        String kmf = row[5];
                        this.insertMunicipios(codigo, sigla_uf, municipio, br, kmi, kmf);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                // Tabela ja contem dados.
            }
        }

//carrega impressoras
        Cursor curImpressoras = database.rawQuery("SELECT EXISTS (SELECT 1 FROM tbl_impressoras)", null);

        if (curImpressoras != null) {
            curImpressoras.moveToFirst();
            if (curImpressoras.getInt (0) == 0) {
                // Tabela esta vazia, preencha com seus dados iniciais
                AssetManager assetManager = context.getAssets();
                try {
                    InputStream csvStream = assetManager.open("TBL_IMPRESSORAS.csv");
                    InputStreamReader csvStreamReader = new InputStreamReader(csvStream);
                    BufferedReader csvReader = new BufferedReader(csvStreamReader);
                    String line;
                    while ((line = csvReader.readLine()) != null) {
                        String[] row = line.split(";");
                        String nome = row[0];
                        String endereco_mac = row[1];
                        String padrao = row[2];
                        this.insertImp(nome, endereco_mac, Boolean.parseBoolean(padrao));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                // Tabela ja contem dados.
            }
        }


    }

}
