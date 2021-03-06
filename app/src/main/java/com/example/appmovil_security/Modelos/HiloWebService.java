package com.example.appmovil_security.Modelos;

import android.content.Context;
import android.os.AsyncTask;

import com.example.appmovil_security.Fragmentos.WebServices.Asynchtask;
import com.example.appmovil_security.Fragmentos.WebServices.ServicioTask;

import org.json.JSONException;

public class HiloWebService extends AsyncTask<String, Long, String> implements Runnable, Asynchtask {

    private String link_api;
    private String metodo_request;
    private Context contexto;
    private String accion;
    IConfiguSistema callback;

    public HiloWebService(String accion, Context contexto, String metodo_request, String link_api, IConfiguSistema callback){
        this.accion = accion;
        this.contexto = contexto;
        this.metodo_request = metodo_request;
        this.link_api = link_api;
        this.callback = callback;
    }
    @Override
    public void run() {
        ServicioTask servicioTask = new ServicioTask(contexto, metodo_request, link_api, this);
        servicioTask.execute();
    }

    @Override
    public void processFinish(String result) throws JSONException {
        if (this.accion == "Componentes"){
            callback.getComponentes(result);
        }else{
            callback.getEstadoSistem(result);
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        return null;
    }
}
