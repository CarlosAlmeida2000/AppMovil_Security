package com.example.appmovil_security;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appmovil_security.Modelos.Asynchtask;
import com.example.appmovil_security.Modelos.ServicioTask;
import com.example.appmovil_security.WebServices.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
public class Login extends AppCompatActivity implements Asynchtask {

    TextView txtUsuario;
    TextView txtClave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);
        this.txtUsuario = (TextView) findViewById(R.id.txtUsuario);
        this.txtClave = (TextView) findViewById(R.id.txtClave);
    }

    public void login(View view) {
        try {
            JSONObject json_mensaje = new JSONObject();
            json_mensaje.put("usuario", txtUsuario.getText());
            json_mensaje.put("clave", txtClave.getText());
            ServicioTask servicioTask = new ServicioTask(this, "POST","https://wssecurity.herokuapp.com/api-usuario/login/", json_mensaje.toString(), this);
            servicioTask.execute();
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void processFinish(String result) throws JSONException {
        try {
            JSONObject json_response = new JSONObject(result);
            if(json_response.has("usuario")){
                Intent newActivity = new Intent(Login.this, MainActivity.class);
                startActivity(newActivity);
            }else{
                Toast.makeText(this, json_response.get("mensaje").toString(), Toast.LENGTH_LONG).show();
            }
        }catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}