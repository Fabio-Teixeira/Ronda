package com.zebra.rondaprf;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import static java.lang.Math.round;

/**
 * Created by Fabio on 23/10/16.
 */

public class KmMunicipio extends AppCompatActivity{
    private Button locButton;
    private DBManager dbManager;
    private ListView listView;
    private String _id;

    public class TodoCursorAdapter extends CursorAdapter {
        public TodoCursorAdapter(Context context, Cursor cursor) {
            super(context, cursor, 0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.view_municipio, parent, false);
        }
        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            // Find fields to populate in inflated template
            TextView tvCod = (TextView) view.findViewById(R.id.lv_codigo_municipio);
            TextView tvMunicipio = (TextView) view.findViewById(R.id.lv_municipio);
            TextView tvTrecho = (TextView) view.findViewById(R.id.lv_trecho);
            // Extract properties from cursor
            String codigo = cursor.getString(cursor.getColumnIndexOrThrow("codigo"));
            String municipio = cursor.getString(cursor.getColumnIndexOrThrow("municipio"));
            String trecho = cursor.getString(cursor.getColumnIndexOrThrow("kmi"))+" até "+cursor.getString(cursor.getColumnIndexOrThrow("kmf"));
            // Populate fields with extracted properties
            tvCod.setText(codigo);
            tvMunicipio.setText(municipio);
            tvTrecho.setText(trecho);
            tvTrecho.setTextColor(Color.BLUE);
            if (codigo.equals(_id)) {
                view.setBackgroundColor(Color.parseColor("#FFB6B546"));
//                listView.setBackgroundColor(getResources().getColor(R.color.light_gray));
                listView.smoothScrollToPosition(listView.getLastVisiblePosition()+3);
            }

        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.km_municipio);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_localiza);
        setSupportActionBar(toolbar);

        dbManager = new DBManager(this);
        dbManager.open();

        locButton = (Button) this.findViewById(R.id.bt_localiza);
        locButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

         //       listView.setAdapter(null);

                enableLocButton(false);

                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, new LocationListener() {

                @Override
                public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
                    Toast.makeText(getApplicationContext(), "Status alterado", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onProviderEnabled(String arg0) {
                    Toast.makeText(getApplicationContext(), "Localização Habilitada", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onProviderDisabled(String arg0) {
                    Toast.makeText(getApplicationContext(), "Localização Desabilitada. Habilite.", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onLocationChanged(Location location) {
                    TextView latitude = (TextView) findViewById(R.id.latitude);
                    TextView longitude = (TextView) findViewById(R.id.longitude);
                    TextView time = (TextView) findViewById(R.id.hora);
                    TextView acuracy = (TextView) findViewById(R.id.Acuracy);
                    TextView provider = (TextView) findViewById(R.id.provider);
                    TextView local = (TextView) findViewById(R.id.km);
                    TextView municipio = (TextView) findViewById(R.id.municipio);
                    String cod_municipio;
                    String localizacao;
                    String distancia;
                    String km;
                    String mts;
                    Integer pos;
                    if (location != null) {
                        latitude.setText("Latitude: " + location.getLatitude());
                        longitude.setText("Longitude: " + location.getLongitude());
                        acuracy.setText("Precisão: " + location.getAccuracy() + "");
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        time.setText("Data:" + sdf.format(location.getTime()));
                        provider.setText(location.getProvider());
                        localizacao = dbManager.getNearGeo(String.valueOf(location.getLatitude()));
                        distancia = String.valueOf(calculaDistancia(Double.parseDouble(localizacao.split(";")[0]), Double.parseDouble(localizacao.split(";")[1]), location.getLatitude(), location.getLongitude()));
                        if (Double.parseDouble(localizacao.split(";")[0]) - (location.getLatitude())>0) {
//                            if (Double.parseDouble(localizacao.split(";")[0]) - (location.getLatitude())<0) { //para teste no android virtual
                            distancia = String.valueOf(Double.parseDouble(localizacao.split(";")[3])+Double.valueOf(distancia));
                        } else {
                            distancia = String.valueOf(Double.parseDouble(localizacao.split(";")[3])-Double.valueOf(distancia));
                        }
                        pos = distancia.indexOf(".");
                        km = distancia.substring(0, pos);
                        mts = distancia.substring(pos + 1, pos + 4);
                        local.setText("BR "+localizacao.split(";")[2]+", Km " + km + "+" + mts + "mts");
                        cod_municipio = dbManager.getMunicipio(localizacao.split(";")[2],distancia);
                        municipio.setText("Codigo: " + cod_municipio.split(";")[0] + ", " + cod_municipio.split(";")[1] + "-" + cod_municipio.split(";")[2]);
                        _id=cod_municipio.split(";")[0];
                        Cursor cursor = dbManager.fetchMunicipios();
                        listView = (ListView) findViewById(R.id.LvMunicipios);
                        TodoCursorAdapter todoAdapter = new TodoCursorAdapter(KmMunicipio.this, cursor);//cod_municipio.split(";")[0]);
                        listView.setAdapter(todoAdapter);
//                        listView.getPositionForView(cod_municipio.split(";")[0]);
                        enableLocButton(true);
                    }

                }
            }, null);
        }
    });
    }
    private void enableLocButton(final boolean enabled) {
        runOnUiThread(new Runnable() {
            public void run() {
                locButton.setEnabled(enabled);
            }
        });
    }

    private double calculaDistancia(double lat1, double lng1, double lat2, double lng2) {
        //double earthRadius = 3958.75;//miles
        double earthRadius = 6371;//kilometers
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;
 //       BigDecimal distkm = new BigDecimal(dist);
 //       distkm = distkm.setScale(3, BigDecimal.ROUND_HALF_UP);
 //       dist = distkm.doubleValue();

        return dist; //* 1000; //em metros
    }
}