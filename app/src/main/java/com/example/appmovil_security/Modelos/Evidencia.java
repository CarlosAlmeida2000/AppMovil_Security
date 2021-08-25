package com.example.appmovil_security.Modelos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmovil_security.R;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;

import org.json.JSONException;
import org.json.JSONObject;

@NonReusable
@Layout(R.layout.evidencia)
public class Evidencia {

    @View(R.id.txtHoraEvi)
    TextView hora;

    @View(R.id.imgEvidencia)
    ImageView imagen;

    Context contexto;
    JSONObject unaEvidencia;
    String base64Image;
    byte[] decodedString;
    Bitmap decodedByte;

    public Evidencia(Context contexto, JSONObject unaEvidencia) {
        this.contexto = contexto;
        this.unaEvidencia = unaEvidencia;
    }

    @Resolve
    protected void onResolved(){
        try{
            this.hora.setText(unaEvidencia.getString("hora"));
            base64Image = unaEvidencia.getString("foto").split(",")[1];
            decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            this.imagen.setImageBitmap(decodedByte);
        }catch (JSONException ex){
            Toast.makeText(contexto, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
