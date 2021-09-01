package com.example.appmovil_security.Fragmentos;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.appmovil_security.Modelos.DecoderImagen;
import com.example.appmovil_security.R;
import com.example.appmovil_security.Fragmentos.WebServices.Asynchtask;
import com.example.appmovil_security.Fragmentos.WebServices.ServicioTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link frag_control#newInstance} factory method to
 * create an instance of this fragment.
 */
public class frag_control extends Fragment implements Asynchtask {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String link_api;
    private ProgressDialog progDailog;
    private JSONObject json_data;

    private ImageView imgEstadoActual;

    public frag_control() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Control.
     */
    // TODO: Rename and change types and number of parameters
    public static frag_control newInstance(String param1, String param2) {
        frag_control fragment = new frag_control();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_control, container, false);
        Button btnConsultar = (Button) rootView.findViewById(R.id.btnConsultar);
        imgEstadoActual = (ImageView) rootView.findViewById(R.id.imgEstadoActual);
        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                link_api = "https://wssecurity.herokuapp.com/api-seguridad/solicitud/";
                json_data = new JSONObject();
                try {
                    json_data.put("estado", true);
                    ServicioTask servicioTask = new ServicioTask(getContext(), "PUT", link_api, json_data.toString(), frag_control.this::processFinish);
                    servicioTask.execute();
                    showDialog();
                } catch (JSONException e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        return rootView;
    }
    private void showDialog(){
        progDailog = new ProgressDialog(getContext());
        progDailog.setTitle("Consulting a current image of your real estate");
        progDailog.setMessage("please wait...");
        progDailog.setIndeterminate(false);
        progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDailog.setCancelable(true);
        progDailog.show();
    }

    @Override
    public void processFinish(String result) throws JSONException {
        try {
            json_data = new JSONObject(result);
            if (json_data.has("historial")) {
                progDailog.dismiss();
                JSONArray jsonHistorial = json_data.getJSONArray("historial");
                JSONObject unHistorial = jsonHistorial.getJSONObject(0);
                JSONArray jsonEvidencias = unHistorial.getJSONArray("evidencias");
                JSONObject unaEvidencia = jsonEvidencias.getJSONObject(0);
                DecoderImagen decoder = new DecoderImagen(unaEvidencia.getString("foto"));
                imgEstadoActual.setImageBitmap(decoder.getImagen());
            } else if(json_data.has("solicitud")) {
                if(json_data.getBoolean("solicitud") != true){
                    link_api = "https://wssecurity.herokuapp.com/api-seguridad/historial-anomalias/?tipo_ultimo=Solicitado";
                    ServicioTask servicioTask = new ServicioTask(getContext(), "GET", link_api, this);
                    servicioTask.execute();
                }else{
                    consul_solicitud();
                }
            }else{
                if(json_data.getBoolean("mensaje")){
                    consul_solicitud();
                }else{
                    Toast.makeText(getContext(), "Existió un error, por favor intente nuevamente", Toast.LENGTH_LONG).show();
                    progDailog.dismiss();
                }
            }
        }catch (JSONException ex){
            Toast.makeText(getContext(), "Existió un error, por favor intente nuevamente", Toast.LENGTH_LONG).show();
        }
    }
    public void consul_solicitud(){
        link_api = "https://wssecurity.herokuapp.com/api-seguridad/solicitud/";
        ServicioTask servicioTask = new ServicioTask(getContext(), "GET", link_api, this);
        servicioTask.execute();
    }
}