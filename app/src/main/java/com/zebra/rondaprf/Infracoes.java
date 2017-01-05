package com.zebra.rondaprf;

/**
 * Created by Fabio on 28/11/16.
 */

    public class Infracoes {
        // Labels table name
        public static final String TABLE = "tbl_infracoes";

        // Labels Table Columns names
        public static final String KEY_ROWID = "_id";
        public static final String KEY_ID = "codigo";
        public static final String KEY_codigo = "codigo";
        public static final String KEY_descricao = "descricao";
        public static final String KEY_obs_infracao = "obs_sugerida";
        public static final String KEY_tipo_infrator = "tipo_infrator";
        public static final String KEY_pontos_cnh = "pontos_cnh";
        public static final String KEY_enquadramento = "enquadramento";
        public static final String KEY_amparo_legal = "amparo_legal";

        // property help us to keep data
        public int infracao_ID;
        public String codigo;
        public String descricao;
        public String obs_sugerida;
        public String tipo_infrator;
        public String pontos_cnh;
        public String amparo_legal;
        public String enquadramento;

    }