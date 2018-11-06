package ar.edu.utn.frsf.dam.isi.laboratorio05;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio05.modelo.MyDatabase;
import ar.edu.utn.frsf.dam.isi.laboratorio05.modelo.Reclamo;
import ar.edu.utn.frsf.dam.isi.laboratorio05.modelo.ReclamoDao;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapaFragment extends SupportMapFragment implements OnMapReadyCallback {

    private GoogleMap miMapa;
    private OnMapaListener listener;

    private List<Reclamo> listaReclamos;
    private ReclamoDao reclamoDao;
    private int tipoMapa;
    public MapaFragment() {
        // Required empty public constructor
    }

    public interface OnMapaListener {
        public void coordenadasSeleccionadas(LatLng c);
    }
    public void setListener(OnMapaListener listener) {
        this.listener = listener;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        reclamoDao = MyDatabase.getInstance(this.getActivity()).getReclamoDao();
        tipoMapa =0;
        Bundle argumentos = getArguments();
        if(argumentos !=null)  tipoMapa = argumentos .getInt("tipo_mapa",0);

        getMapAsync(this);

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        miMapa = map;
        switch (tipoMapa){
            case 1:
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        listaReclamos = reclamoDao.getAll();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for(Reclamo r : listaReclamos){
                                    Log.d("mapa_lab",r.getLatitud()+" -- "+ r.getLongitud());
                                    miMapa.addMarker(new MarkerOptions()
                                            .position(new LatLng(r.getLatitud(),r.getLongitud()))
                                            .title(r.getId()+" ["+r.getTipo().toString()+"]")
                                            .snippet(r.getReclamo()));
                                }
                            }
                        });
                    }
                };
                Thread t1 = new Thread(r);
                t1.start();
                break;
            case 2:
                miMapa.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng) {
                        listener.coordenadasSeleccionadas(latLng);
                    }
                });
                break;
        }
    }

}
