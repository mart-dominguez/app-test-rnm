package ar.edu.utn.frsf.dam.isi.laboratorio05;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio05.modelo.MyDatabase;
import ar.edu.utn.frsf.dam.isi.laboratorio05.modelo.Reclamo;
import ar.edu.utn.frsf.dam.isi.laboratorio05.modelo.ReclamoDao;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapaFragment extends SupportMapFragment implements OnMapReadyCallback {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 101;
    private Boolean FLAG_PERMISO_UBICACION = false;
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
        tipoMapa = 0;
        Bundle argumentos = getArguments();
        if (argumentos != null) tipoMapa = argumentos.getInt("tipo_mapa", 0);

        getMapAsync(this);

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        miMapa = map;
        switch (tipoMapa) {
            case 1:
                cargarTodosLosReclamos();
                break;
            case 2:
                miMapa.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng) {
                        listener.coordenadasSeleccionadas(latLng);
                    }
                });
                break;
            case 3:
                mostrarHeatMap();
                break;
            case 4:
                filtrarPorTipo();
                break;
            case 5:
                if(getArguments()!=null) cargarUnReclamo(getArguments().getInt("idReclamo",0));
                break;
        }

        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            FLAG_PERMISO_UBICACION = false;
            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            FLAG_PERMISO_UBICACION = true;
            updateLocationUI();
        }

    }

    private void cargarTodosLosReclamos() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                listaReclamos = reclamoDao.getAll();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (Reclamo r : listaReclamos) {
                            Log.d("mapa_lab", r.getLatitud() + " -- " + r.getLongitud());
                            miMapa.addMarker(new MarkerOptions()
                                    .position(new LatLng(r.getLatitud(), r.getLongitud()))
                                    .title(r.getId() + " [" + r.getTipo().toString() + "]")
                                    .snippet(r.getReclamo()));
                        }
                    }
                });
            }
        };
        Thread t1 = new Thread(r);
        t1.start();
    }

    private void cargarUnReclamo(final int id) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                final Reclamo r= reclamoDao.getById(id);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                            Log.d("mapa_lab", r.getLatitud() + " -- " + r.getLongitud());
                            miMapa.addMarker(new MarkerOptions()
                                    .position(new LatLng(r.getLatitud(), r.getLongitud()))
                                    .title(r.getId() + " [" + r.getTipo().toString() + "]")
                                    .snippet(r.getReclamo()));
                        Log.d("mapa_lab","agrega circulo");
                        miMapa.addCircle(new
                                CircleOptions().center(new LatLng(r.getLatitud(), r.getLongitud()))
                                .radius(500)
                                .strokeColor(Color.RED)
                                .fillColor(0x22FF0000)
                        );

                    }
                });
            }
        };
        Thread t1 = new Thread(r);
        t1.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        FLAG_PERMISO_UBICACION = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    FLAG_PERMISO_UBICACION = true;
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LatLng santaFe = new LatLng(-31.631788, -60.715111);
        miMapa.setMyLocationEnabled(true);
        miMapa.getUiSettings().setMyLocationButtonEnabled(true);
        miMapa.moveCamera(CameraUpdateFactory.newLatLngZoom(santaFe,12));
    }


    private void mostrarHeatMap() {
        Log.d("mapa_lab","MOSTRAR HEAT MAP");
        Runnable r = new Runnable() {
            @Override
            public void run() {
                listaReclamos = reclamoDao.getAll();
                final List<LatLng> puntos = new ArrayList<>();
                for(Reclamo r : listaReclamos){
                    puntos.add(new LatLng(r.getLatitud(),r.getLongitud()));
                }
                Log.d("mapa_lab","MOSTRAR HEAT MAP- Puntos: "+puntos.size());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                            // Create a heat map tile provider, passing it the latlngs of the police stations.
                        HeatmapTileProvider mProvider = new HeatmapTileProvider.Builder()
                                    .data(puntos)
                                    .build();
                            // Add a tile overlay to the map, using the heat map tile provider.
                        TileOverlay mOverlay = miMapa.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
                    }
                });
            }
        };
        Thread t1 = new Thread(r);
        t1.start();
    }

    private void filtrarPorTipo() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                String tipoReclamo = getArguments().getString("tipo_reclamo");
                listaReclamos = reclamoDao.getByTipo(tipoReclamo);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        PolylineOptions lineaConf = new PolylineOptions()
                                .color(Color.RED)
                                .clickable(true);
                        for (Reclamo r : listaReclamos) {
                            LatLng punto = new LatLng(r.getLatitud(), r.getLongitud());
                            miMapa.addMarker(new MarkerOptions()
                                    .position(punto)
                                    .title(r.getId() + " [" + r.getTipo().toString() + "]")
                                    .snippet(r.getReclamo()));
                            lineaConf.add(punto);

                        }
                        miMapa.addPolyline(lineaConf);
                    }
                });
            }
        };
        Thread t1 = new Thread(r);
        t1.start();
    }
}
