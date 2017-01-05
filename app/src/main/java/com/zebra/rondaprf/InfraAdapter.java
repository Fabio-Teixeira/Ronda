package com.zebra.rondaprf;

/**
 * Created by Fabio on 28/11/16.
 */

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
/**
 * Created by Tan on 3/14/2016.
 */

public class InfraAdapter extends CursorAdapter {
    TextView txtId,txtName,txtEmail;
    private LayoutInflater mInflater;

    public InfraAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View   view    =    mInflater.inflate(R.layout.view_infracao, parent, false);
        ViewHolder holder  =   new ViewHolder();
        holder.txtCodigo    =   (TextView)  view.findViewById(R.id.codigo_infracao);
        holder.txtDescricao    =   (TextView)  view.findViewById(R.id.descricao_infracao);
        holder.txtObs_sugerida   =   (TextView)  view.findViewById(R.id.obs_infracao);
        holder.txtInfrator_pontos   =   (TextView)  view.findViewById(R.id.infrator_pontos);
        holder.txtEnquadramento   =   (TextView)  view.findViewById(R.id.base_legal);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //If you want to have zebra lines color effect uncomment below code
        /*if(cursor.getPosition()%2==1) {
             view.setBackgroundResource(R.drawable.item_list_backgroundcolor);
        } else {
            view.setBackgroundResource(R.drawable.item_list_backgroundcolor2);
        }*/

        ViewHolder holder  =   (ViewHolder)    view.getTag();
        holder.txtCodigo.setText(cursor.getString(cursor.getColumnIndex(Infracoes.KEY_ID)));
        holder.txtDescricao.setText(cursor.getString(cursor.getColumnIndex(Infracoes.KEY_descricao)));
        holder.txtObs_sugerida.setText(cursor.getString(cursor.getColumnIndex(Infracoes.KEY_obs_infracao)));
        holder.txtInfrator_pontos.setText(cursor.getString(cursor.getColumnIndex(Infracoes.KEY_tipo_infrator))+" "+cursor.getString(cursor.getColumnIndex(Infracoes.KEY_pontos_cnh))+"pts");
        holder.txtEnquadramento.setText(cursor.getString(cursor.getColumnIndex(Infracoes.KEY_amparo_legal))+" - "+cursor.getString(cursor.getColumnIndex(Infracoes.KEY_enquadramento)));

    }

    static class ViewHolder {
        TextView txtCodigo;
        TextView txtDescricao;
        TextView txtObs_sugerida;
        TextView txtInfrator_pontos;
        TextView txtEnquadramento;
    }
}