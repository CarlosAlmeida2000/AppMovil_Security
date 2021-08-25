package com.example.appmovil_security;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.appmovil_security.Modelos.Evidencia;
import com.example.appmovil_security.Modelos.Historial;
import com.mindorks.placeholderview.PlaceHolderView;
import com.mindorks.placeholderview.SmoothLinearLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ver_evidencias extends AppCompatActivity {

    private PlaceHolderView phv_evidencias;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_evidencias);
        phv_evidencias = (PlaceHolderView) findViewById(R.id.phv_evidencias);
        Bundle b = this.getIntent().getExtras();
        try {
            JSONArray evidencias = new JSONArray(b.getString("jsonArray"));
            for(int i = 0; i < evidencias.length(); i++){
                JSONObject una_evidencia = evidencias.getJSONObject(i);
                phv_evidencias.addView(new Evidencia(getApplicationContext(), una_evidencia));
            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}