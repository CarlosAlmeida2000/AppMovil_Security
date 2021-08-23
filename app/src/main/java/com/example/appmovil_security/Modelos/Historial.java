package com.example.appmovil_security.Modelos;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmovil_security.R;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

@NonReusable
@Layout(R.layout.historial)
public class Historial {

    int historial_id;

    @View(R.id.txtComponente)
    TextView componente;

    @View(R.id.txtSector)
    TextView sector;

    @View(R.id.txtFecha)
    TextView fecha;

    Context contexto;
    JSONObject unHistorial;
    Intent changeActivity;

    public Historial(Context contexto, JSONObject unHistorial) {
        this.contexto = contexto;
        this.unHistorial = unHistorial;
    }

    @Resolve
    protected void onResolved(){
        try{
            this.historial_id = this.unHistorial.getInt("historial_id");
            this.componente.setText(this.unHistorial.getString("componente_nombre"));
            this.sector.setText(this.unHistorial.getString("sector"));
            this.fecha.setText(this.unHistorial.getString("fecha"));
        }catch (JSONException ex){
            System.out.println(ex.getMessage());
        }
    }

    @Click(R.id.btnVerEvidencias)
    public void onClick_VerEvidencias(){
        Toast.makeText(contexto, "desde el boton", Toast.LENGTH_LONG).show();
    }

}
