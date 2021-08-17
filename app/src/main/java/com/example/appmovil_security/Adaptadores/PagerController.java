package com.example.appmovil_security.Adaptadores;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.appmovil_security.Fragmentos.frag_ajustes;
import com.example.appmovil_security.Fragmentos.frag_control;
import com.example.appmovil_security.Fragmentos.frag_historial;
import com.example.appmovil_security.Fragmentos.frag_perfil;

public class PagerController extends FragmentPagerAdapter {
    int numoftabs;

    public PagerController(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.numoftabs = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new frag_ajustes();
            case 1:
                return new frag_historial();
            case 2:
                return new frag_control();
            case 3:
                return new frag_perfil();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numoftabs;
    }

}
