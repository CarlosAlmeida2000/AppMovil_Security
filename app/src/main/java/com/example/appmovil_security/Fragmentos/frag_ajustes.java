package com.example.appmovil_security.Fragmentos;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.example.appmovil_security.Modelos.Componente;
import com.example.appmovil_security.Modelos.HiloWebService;
import com.example.appmovil_security.Modelos.IConfiguSistema;
import com.example.appmovil_security.R;
import com.example.appmovil_security.Fragmentos.WebServices.Asynchtask;
import com.example.appmovil_security.Fragmentos.WebServices.ServicioTask;
import com.mindorks.placeholderview.PlaceHolderView;
import com.mindorks.placeholderview.SmoothLinearLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link frag_ajustes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class frag_ajustes extends Fragment implements IConfiguSistema, Asynchtask {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private PlaceHolderView phv_componentes;
    private String link_api;
    private ProgressDialog progDailog;
    private Switch swSistema;
    private JSONObject json_data;

    public frag_ajustes() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Ajustes.
     */
    // TODO: Rename and change types and number of parameters
    public static frag_ajustes newInstance(String param1, String param2) {
        frag_ajustes fragment = new frag_ajustes();
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
        View rootView = inflater.inflate(R.layout.fragment_ajustes, container, false);
        phv_componentes = (PlaceHolderView)rootView.findViewById(R.id.phv_componentes);
        phv_componentes.setHasFixedSize(true);
        phv_componentes.setLayoutManager(new SmoothLinearLayoutManager(getContext()));
        swSistema = (Switch)rootView.findViewById(R.id.sw_componente);
        swSistema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    link_api = "https://wssecurity.herokuapp.com/api-seguridad/sistema/";
                    json_data = new JSONObject();
                    json_data.put("estado", swSistema.isChecked());
                    ServicioTask servicioTask = new ServicioTask(getContext(), "PUT", link_api, json_data.toString(), frag_ajustes.this::processFinish);
                    servicioTask.execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        showDialog();
        HiloWebService peticion1 = new HiloWebService("Componentes", getContext(), "GET", "https://wssecurity.herokuapp.com/api-seguridad/componentes/", this);
        HiloWebService peticion2 = new HiloWebService("EstadoSistema", getContext(), "GET", "https://wssecurity.herokuapp.com/api-seguridad/sistema/", this);
        Thread hilo1 = new Thread(peticion1);
        Thread hilo2 = new Thread(peticion2);
        hilo1.start();
        hilo2.start();
        progDailog.dismiss();
        return rootView;
    }

    private void showDialog(){
        progDailog = new ProgressDialog(getContext());
        progDailog.setTitle("Consultando datos");
        progDailog.setMessage("por favor, espere...");
        progDailog.setIndeterminate(false);
        progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDailog.setCancelable(true);
        progDailog.show();
    }

    @Override
    public void getComponentes(String result) throws JSONException {
        this.phv_componentes.removeAllViews();
        progDailog.dismiss();
        JSONObject json_data = new JSONObject(result);
        if(json_data.has("componente")){
            JSONArray json_componentes = json_data.getJSONArray("componente");
            for(int i = 0; i < json_componentes.length(); i++){
                JSONObject un_componente = json_componentes.getJSONObject(i);
                this.phv_componentes.addView(new Componente(getContext(), un_componente, frag_ajustes.this));
            }
        }else{
            Toast.makeText(getContext(), "Sucedió un error al obtener los componentes de vigilancia.", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void getEstadoSistem(String result) throws JSONException {
        progDailog.dismiss();
        json_data = new JSONObject(result);
        if (json_data.has("sistema")){
            if(json_data.get("sistema").toString().equals("Habilitado")){
                this.swSistema.setChecked(true);
            }else{
                this.swSistema.setChecked(false);
            }
        }else{
            Toast.makeText(getContext(), "Sucedió un error al obtener el estado del sistema.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void processFinish(String result) throws JSONException {
        JSONObject json_data = new JSONObject(result);
        if(json_data.getBoolean("mensaje")){
            showDialog();
            HiloWebService peticion1 = new HiloWebService("Componentes", getContext(), "GET", "https://wssecurity.herokuapp.com/api-seguridad/componentes/", this);
            HiloWebService peticion2 = new HiloWebService("EstadoSistema", getContext(), "GET", "https://wssecurity.herokuapp.com/api-seguridad/sistema/", this);
            Thread hilo1 = new Thread(peticion1);
            Thread hilo2 = new Thread(peticion2);
            hilo1.start();
            hilo2.start();
            progDailog.dismiss();
            Toast.makeText(getContext(), "Sistema actualizado.", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getContext(), "Existió un error al actualizar el sistema, por favor intente nuevamente.", Toast.LENGTH_LONG).show();
        }
    }
}