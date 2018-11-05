package ar.edu.utn.frsf.dam.isi.laboratorio05;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        navView = (NavigationView)findViewById(R.id.navview);

        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        boolean fragmentTransaction = false;
                        Fragment fragment = null;

                        switch (menuItem.getItemId()) {
                            case R.id.opcion1:
                                fragment = new NuevoReclamoFragment();
                                fragmentTransaction = true;
                                break;
                            case R.id.opcion2:
                                break;
                            case R.id.opcion3:
                                break;
                            case R.id.opcion4:
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

}
