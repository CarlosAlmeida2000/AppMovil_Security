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

    @View(R.id.txtNumEvidencia)
    TextView txtnumEvidencia;

    @View(R.id.txtHoraEvi)
    TextView hora;

    @View(R.id.imgEvidencia)
    ImageView imagen;

    Context contexto;
    int numEvidencia;
    JSONObject unaEvidencia;
    String base64Image;
    byte[] decodedString;
    Bitmap decodedByte;
    DecoderImagen decoder;

    public Evidencia(Context contexto, JSONObject unaEvidencia, int numEvidencia) {
        this.contexto = contexto;
        this.unaEvidencia = unaEvidencia;
        this.numEvidencia = numEvidencia;
    }

    @Resolve
    protected void onResolved(){
        try{
            this.txtnumEvidencia.setText("Evidencia # " + numEvidencia);
            this.hora.setText(unaEvidencia.getString("hora"));
            decoder = new DecoderImagen(unaEvidencia.getString("foto"));
            this.imagen.setImageBitmap(decoder.getImagen());
        }catch (JSONException ex){
            Toast.makeText(contexto, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
