package ar.edu.utn.frsf.dam.isi.laboratorio05;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import ar.edu.utn.frsf.dam.isi.laboratorio05.modelo.Reclamo;


/**
 * A simple {@link Fragment} subclass.
 */
public class BusquedaFragment extends Fragment {
    private Spinner tipoReclamo;
    private Button btnBuscarReclamo;
    private ArrayAdapter<Reclamo.TipoReclamo> tipoReclamoAdapter;


    public BusquedaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_busqueda, container, false);
        tipoReclamo= (Spinner) v.findViewById(R.id.busqueda_reclamo_tipo);
        btnBuscarReclamo = (Button) v.findViewById(R.id.btnBuscar);
        tipoReclamoAdapter = new ArrayAdapter<Reclamo.TipoReclamo>(getActivity(),android.R.layout.simple_spinner_item,Reclamo.TipoReclamo.values());
        tipoReclamoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoReclamo.setAdapter(tipoReclamoAdapter);
        btnBuscarReclamo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tag="mapaReclamos";
                Fragment fragment =  getActivity().getSupportFragmentManager().findFragmentByTag(tag);
                if(fragment==null) fragment = new MapaFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("tipo_mapa",4);
                bundle.putString("tipo_reclamo",tipoReclamo.getSelectedItem().toString());
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.contenido, fragment,tag)
                        .commit();
            }
        });
        return v;
    }

}
