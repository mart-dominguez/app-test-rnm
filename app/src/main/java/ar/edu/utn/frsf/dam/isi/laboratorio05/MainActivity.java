package ar.edu.utn.frsf.dam.isi.laboratorio05;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {
    private DrawerLayout drawerLayout;
    private NavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        //Handle when activity is recreated like on orientation Change
        shouldDisplayHomeUp();
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        navView = (NavigationView)findViewById(R.id.navview);
        BienvenidoFragment fragmentInicio = new BienvenidoFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contenido, fragmentInicio)
                .commit();

        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        boolean fragmentTransaction = false;
                        Fragment fragment = null;

                        switch (menuItem.getItemId()) {
                            case R.id.optNuevoReclamo:
                                fragment = new NuevoReclamoFragment();
                                fragmentTransaction = true;
                                break;
                            case R.id.optListaReclamo:
                                fragment = new ListaReclamosFragment();
                                fragmentTransaction = true;
                                break;
                            case R.id.optVerMapa:
                                break;
                            case R.id.optHeatMap:
                                Log.d("NavigationView", "Pulsada opción 1");
                                break;
                        }

                        if(fragmentTransaction) {
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.contenido, fragment)
                                    .addToBackStack(null)
                                    .commit();

                            menuItem.setChecked(true);

                            getSupportActionBar().setTitle(menuItem.getTitle());
                        }

                        drawerLayout.closeDrawers();

                        return true;
                    }
                });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackStackChanged() {
        shouldDisplayHomeUp();
    }

    public void shouldDisplayHomeUp(){
        //Enable Up button only  if there are entries in the back stack
        boolean canback = getSupportFragmentManager().getBackStackEntryCount()>0;
        getSupportActionBar().setDisplayHomeAsUpEnabled(canback);
    }
}
