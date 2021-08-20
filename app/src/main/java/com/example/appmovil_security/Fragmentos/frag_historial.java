package com.example.appmovil_security.Fragmentos;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appmovil_security.Modelos.Asynchtask;
import com.example.appmovil_security.Modelos.Historial;
import com.example.appmovil_security.Modelos.ServicioTask;
import com.example.appmovil_security.R;
import com.mindorks.placeholderview.PlaceHolderView;
import com.mindorks.placeholderview.SmoothLinearLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link frag_historial#newInstance} factory method to
 * create an instance of this fragment.
 */
public class frag_historial extends Fragment implements Asynchtask {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText dtmFechaHasta;
    private EditText dtmFechaDesde;
    private Button btnBuscar;
    private PlaceHolderView phv_historial;
    private Calendar calendario1;
    private Calendar calendario2;
    private Calendar fecha;
    private int dia, mes, anio;
    private String link_api;
    private ProgressDialog progDailog;

    public frag_historial() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Historial.
     */
    // TODO: Rename and change types and number of parameters
    public static frag_historial newInstance(String param1, String param2) {
        frag_historial fragment = new frag_historial();
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
        View rootView = inflater.inflate(R.layout.fragment_historial, container, false);
        phv_historial = (PlaceHolderView)rootView.findViewById(R.id.phv_historial);
        phv_historial.setHasFixedSize(true);
        phv_historial.setLayoutManager(new SmoothLinearLayoutManager(getContext()));
        dtmFechaDesde = (EditText)rootView.findViewById(R.id.dtmFechaDesde);
        dtmFechaHasta = (EditText)rootView.findViewById(R.id.dtmFechaHasta);
        dtmFechaHasta = (EditText)rootView.findViewById(R.id.dtmFechaHasta);
        btnBuscar = (Button)rootView.findViewById(R.id.btnBuscarHistorial);
        fecha = new GregorianCalendar();
        dtmFechaHasta.setText(fecha.get(Calendar.YEAR) +"-"+ (fecha.get(Calendar.MONTH) + 1) +"-"+ fecha.get(Calendar.DAY_OF_MONTH));
        calendario1 = Calendar.getInstance();
        calendario2 = Calendar.getInstance();
        dtmFechaHasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dia = calendario1.get(Calendar.DAY_OF_MONTH);
                mes = calendario1.get(Calendar.MONTH);
                anio = calendario1.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dtmFechaHasta.setText(year +"-"+ (month+1) +"-"+ dayOfMonth);
                        calendario1.set(year, month, dayOfMonth);
                    }
                }, anio, mes, dia);
                datePickerDialog.show();
            }
        });
        dtmFechaDesde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dia = calendario2.get(Calendar.DAY_OF_MONTH);
                mes = calendario2.get(Calendar.MONTH);
                anio = calendario2.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dtmFechaDesde.setText(year +"-"+ (month+1) +"-"+ dayOfMonth);
                        calendario2.set(year, month, dayOfMonth);
                    }
                }, dia, mes, anio);
                datePickerDialog.show();
            }
        });
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String fecha_desde = dtmFechaDesde.getText().toString();
                    if(fecha_desde.length() > 0){
                        link_api = "https://wssecurity.herokuapp.com/api-seguridad/historial-anomalias/?fecha_desde="+ dtmFechaDesde.getText() +"&fecha_hasta="+ dtmFechaHasta.getText();
                        ServicioTask servicioTask = new ServicioTask(getContext(), "GET", link_api, frag_historial.this::processFinish);
                        servicioTask.execute();
                        showDialog();
                    }else{
                        Toast.makeText(getContext(), "Por favor seleccione una fecha desde.", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception ex){
                    Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        link_api = "https://wssecurity.herokuapp.com/api-seguridad/historial-anomalias/";
        ServicioTask servicioTask = new ServicioTask(getContext(), "GET", link_api, this);
        servicioTask.execute();
        showDialog();
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
    public void processFinish(String result) throws JSONException {
        try {
            this.phv_historial.removeAllViews();
            progDailog.dismiss();
            JSONObject json_data = new JSONObject(result);
            JSONArray json_historial = json_data.getJSONArray("historial");
            for(int i = 0; i < json_historial.length(); i++){
                JSONObject un_historial = json_historial.getJSONObject(i);
                this.phv_historial.addView(new Historial(getContext(), un_historial));
            }
        }catch (JSONException ex){
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}