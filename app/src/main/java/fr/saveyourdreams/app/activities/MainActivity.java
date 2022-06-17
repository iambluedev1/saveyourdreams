package fr.saveyourdreams.app.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.viewpager.widget.ViewPager;

import fr.saveyourdreams.app.R;
import fr.saveyourdreams.app.databinding.ActivityMainBinding;
import fr.saveyourdreams.app.services.AuthService;
import fr.saveyourdreams.app.ui.main.SectionsPagerAdapter;

import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = binding.fab;

        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.i("SAVE_YOUR_DREAMS", "Ya pas les permissions");
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15000,
                    100, location -> {
                        Log.i("SAVE_YOUR_DREAMS", "Loc: " + location.getLatitude() + " _ " + location.getLongitude());
                        Geocoder gc = new Geocoder(this, Locale.getDefault());
                        try {
                            List<Address> addresses = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            for (int i = 0; i < addresses.size(); i++) {
                                Log.d("=Adress=", addresses.get(i).getAddressLine(0));
                                Log.d("=Adress=", "countryCode=" + addresses.get(i).getCountryCode());
                                Log.d("=Adress=", "countryName=" + addresses.get(i).getCountryName());
                                Log.d("=Adress=", "locality=" + addresses.get(i).getLocality());
                                Log.d("=Adress=", "postalCode=" + addresses.get(i).getPostalCode());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_logout) {
            // On se deconnecte
            new AuthService().logout(this);

            // Et on repart sur l'activity de connexion
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            // Pour ne pas pouvoir revenir en arrière
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);

            // On oublie pas de dispose cette activity
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}