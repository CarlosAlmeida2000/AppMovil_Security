package com.example.appmovil_security.Modelos;

import org.json.JSONException;

public interface IConfiguSistema {
    void getComponentes(String result) throws JSONException;
    void getEstadoSistem(String result) throws JSONException;
}
