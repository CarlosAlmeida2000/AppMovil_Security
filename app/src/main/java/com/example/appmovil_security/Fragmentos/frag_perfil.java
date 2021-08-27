package com.example.appmovil_security.Fragmentos;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmovil_security.Login;
import com.example.appmovil_security.Modelos.DecoderImagen;
import com.example.appmovil_security.R;
import com.example.appmovil_security.WebServices.Asynchtask;
import com.example.appmovil_security.WebServices.ServicioTask;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link frag_perfil#newInstance} factory method to
 * create an instance of this fragment.
 */
public class frag_perfil extends Fragment implements Asynchtask {

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

    public frag_perfil() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Perfil.
     */
    // TODO: Rename and change types and number of parameters
    public static frag_perfil newInstance(String param1, String param2) {
        frag_perfil fragment = new frag_perfil();
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
        View rootView = inflater.inflate(R.layout.fragment_perfil, container, false);
        TextView txtNombres = rootView.findViewById(R.id.txtNombresP);
        TextView txtUsuario = rootView.findViewById(R.id.txtUsuarioP);
        TextView txtClaveActual = rootView.findViewById(R.id.txtClaveActual);
        TextView txtNuevaClave = rootView.findViewById(R.id.txtNuevaClave);
        TextView txtConfiClave = rootView.findViewById(R.id.txtConfiClave);
        ImageView imgPerfil = rootView.findViewById(R.id.imgPerfil);
        Button btnGuardar = rootView.findViewById(R.id.btnGuardarPerfil);
        JSONObject usuario = Login.getUsuario();
        try {
            txtNombres.setText(usuario.getString("nombre"));
            txtUsuario.setText(usuario.getString("usuario"));
            DecoderImagen decoder = new DecoderImagen(usuario.getString("foto"));
            imgPerfil.setImageBitmap(decoder.getImagen());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                link_api = "https://wssecurity.herokuapp.com/api-usuario/usuario/";
                json_data = new JSONObject();
                try {
                    if(txtNombres.getText().toString() != "" && txtUsuario.getText().toString() != "" && txtNuevaClave.getText().toString() != "" && txtConfiClave.getText().toString() != ""){
                        if(txtNuevaClave.getText().equals(usuario.getString("clave"))){
                            if(txtNuevaClave.getText().equals(txtConfiClave.getText())){
                                json_data.put("usuario_id", usuario.getString("usuario_id"));
                                json_data.put("nombre", txtNombres.getText().toString());
                                json_data.put("usuario", txtUsuario.getText().toString());
                                json_data.put("clave", txtNuevaClave.getText().toString());
                                ServicioTask servicioTask = new ServicioTask(getContext(), "PUT", link_api, json_data.toString(), frag_perfil.this::processFinish);
                                servicioTask.execute();
                                showDialog();
                            }else{
                                Toast.makeText(getContext(), "La nueva clave no coinciden con la clave confirmada.", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(getContext(), "La clave actual es incorrecta", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getContext(), "Existen campos vacíos.", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        return rootView;
    }

    private void showDialog(){
        progDailog = new ProgressDialog(getContext());
        progDailog.setTitle("Procesando solicitud");
        progDailog.setMessage("por favor, espere...");
        progDailog.setIndeterminate(false);
        progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDailog.setCancelable(true);
        progDailog.show();
    }

    @Override
    public void processFinish(String result) throws JSONException {
        json_data = new JSONObject(result);
        if(json_data.has("confirmacion")){
            if(json_data.getBoolean("confirmacion")){
                Toast.makeText(getContext(), "Perfil modificado.", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getContext(), "No se logró actualizar su perfil.", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getContext(), json_data.getString("mensaje"), Toast.LENGTH_LONG).show();
        }
    }
}