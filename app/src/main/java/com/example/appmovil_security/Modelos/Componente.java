package com.example.appmovil_security.Modelos;

import android.content.Context;
import android.content.Intent;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmovil_security.R;
import com.example.appmovil_security.WebServices.Asynchtask;
import com.example.appmovil_security.WebServices.ServicioTask;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;

import org.json.JSONException;
import org.json.JSONObject;

@NonReusable
@Layout(R.layout.componente)
public class Componente implements Asynchtask {

    int componente_id;

    @View(R.id.txtNumEvidencia)
    TextView componente;

    @View(R.id.txtHoraEvi)
    TextView sector;

    @View(R.id.sw_componente)
    Switch swComponente;

    boolean estado;

    Context contexto;
    JSONObject unComponente;
    Intent changeActivity;
    String link_api;
    JSONObject json_data;
    IConfiguSistema callback = null;

    public Componente(Context contexto, JSONObject unComponente, IConfiguSistema callback) {
        this.contexto = contexto;
        this.unComponente = unComponente;
        this.callback = callback;
    }

    @Resolve
    protected void onResolved(){
        try{
            this.componente_id = this.unComponente.getInt("componente_id");
            this.componente.setText(this.unComponente.getString("componente"));
            this.sector.setText(this.unComponente.getString("sector"));
            this.estado = this.unComponente.getBoolean("estado");
            this.swComponente.setChecked(this.estado);
        }catch (JSONException ex){
            System.out.println(ex.getMessage());
        }
    }

    @Click(R.id.sw_componente)
    public void cambiarEstado(){
        try{
            link_api = "https://wssecurity.herokuapp.com/api-seguridad/componentes/";
            json_data = new JSONObject();
            json_data.put("componente_id", this.componente_id);
            json_data.put("estado", this.swComponente.isChecked());
            ServicioTask servicioTask = new ServicioTask(contexto, "PUT", link_api, json_data.toString(), this);
            servicioTask.execute();
        }catch (JSONException ex){
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void processFinish(String result) throws JSONException {
        try{
            JSONObject json_data = new JSONObject(result);
            if(json_data.getBoolean("mensaje")){
                // mostrar mensaje de carga
                HiloWebService peticion1 = new HiloWebService("Componentes", contexto, "GET", "https://wssecurity.herokuapp.com/api-seguridad/componentes/", callback);
                HiloWebService peticion2 = new HiloWebService("EstadoSistema", contexto, "GET", "https://wssecurity.herokuapp.com/api-seguridad/sistema/", callback);
                Thread hilo1 = new Thread(peticion1);
                Thread hilo2 = new Thread(peticion2);
                hilo1.start();
                hilo2.start();
                // eliminar mensaje de carga
                Toast.makeText(contexto, "Sistema actualizado.", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(contexto, "Existió un error al actualizar el sistema, por favor intente nuevamente.", Toast.LENGTH_LONG).show();
            }
        }catch (JSONException ex){
            System.out.println(ex.getMessage());
        }
    }
}
