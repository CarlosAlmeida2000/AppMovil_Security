package com.example.appmovil_security.Fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appmovil_security.Modelos.Historial;
import com.example.appmovil_security.R;
import com.example.appmovil_security.WebServices.Asynchtask;
import com.example.appmovil_security.WebServices.WebService;
import com.mindorks.placeholderview.PlaceHolderView;
import com.mindorks.placeholderview.SmoothLinearLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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

    PlaceHolderView phv_historial;


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

        Map<String, String> datos = new HashMap<String, String>();
        WebService ws = new WebService("https://wssecurity.herokuapp.com/api-seguridad/historial-anomalias/", datos, getContext(), this);
        ws.execute("GET");
        return rootView;
    }

    @Override
    public void processFinish(String result) throws JSONException {
        System.out.println(result);
        JSONObject json_data = new JSONObject(result);
        JSONArray json_historial = json_data.getJSONArray("historial");
        for(int i = 0; i < json_historial.length(); i++){
            JSONObject un_historial = json_historial.getJSONObject(i);
            this.phv_historial.addView(new Historial(getContext(), un_historial));
        }
    }
}