package com.example.appmovil_security;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appmovil_security.WebServices.Asynchtask;
import com.example.appmovil_security.WebServices.ServicioTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity implements Asynchtask {

    private TextView txtUsuario;
    private TextView txtClave;
    private ProgressDialog progDailog;
    public static JSONObject usuario;

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
            txtUsuario.setText("");
            txtClave.setText("");
            servicioTask.execute();
            progDailog = new ProgressDialog(this);
            progDailog.setTitle("Verificando usuario");
            progDailog.setMessage("por favor, espere...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public static JSONObject getUsuario() {
        return usuario;
    }

    public void setUsuario(JSONObject usuario) {
        this.usuario = usuario;
    }

    @Override
    public void processFinish(String result) throws JSONException {
        try {
            JSONObject json_response = new JSONObject(result);
            progDailog.dismiss();
            if(json_response.has("usuario")){
                Intent newActivity = new Intent(Login.this, MainActivity.class);
                JSONArray json_array = json_response.getJSONArray("usuario");
                 this.setUsuario(json_array.getJSONObject(0));
                Toast.makeText(this, "Bienvenido (a) " + getUsuario().get("nombre").toString(), Toast.LENGTH_LONG).show();
                startActivity(newActivity);
            }else{
                Toast.makeText(this, json_response.get("mensaje").toString(), Toast.LENGTH_LONG).show();
            }
        }catch (Exception ex){
            progDailog.dismiss();
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}