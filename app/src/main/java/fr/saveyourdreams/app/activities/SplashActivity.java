package fr.saveyourdreams.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.SQLException;

import fr.saveyourdreams.app.R;
import fr.saveyourdreams.app.repositories.Database;
import fr.saveyourdreams.app.services.AuthService;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        // On lance dans un thread pour ne pas bloquer le UI Thread (le thread dans lequel se lance l'application)
        new Thread(() -> {
            try {
                // On initialise la connexion avec la bdd (Database est un singleton)
                Database.getInstance().connect();

                // On va utiliser le AuthService pour savoir si l'user est déjà connecté  pour afficher la bonne activity
                AuthService authService = new AuthService();

                if (authService.isConnected(this)) {
                    authService.reconnect(getApplicationContext(), (ignored) -> {
                        Log.d("SAVE_YOUR_DREAMS", "User successfully reconnected");
                        Intent i = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(i);
                    }, error -> runOnUiThread(() -> Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show()));
                } else {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                }

                // On nettoie l'activity courante car on en a plus besoin
                finish();
            } catch (ClassNotFoundException | SQLException e) {
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.DB_ERROR_CONNECT), Toast.LENGTH_LONG);
                toast.show();
                e.printStackTrace();
            }
        }).start();
    }

}
