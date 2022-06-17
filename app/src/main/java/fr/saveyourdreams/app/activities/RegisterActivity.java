package fr.saveyourdreams.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import fr.saveyourdreams.app.R;
import fr.saveyourdreams.app.services.AuthService;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
    }

    public void onClick(View view) {
        Log.d("SAVE_YOUR_DREAMS", view.toString());
        switch (view.getId()) {
            case R.id.register_login_link:
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Comme le LoginActivity c'st la première activité a etre rendu, on peut dispose celle la (pour ne pas avoir le bouton back qui fonctionne)
                break;
            case R.id.register_submit:
                EditText username = findViewById(R.id.register_username);
                EditText password = findViewById(R.id.register_password);

                if (username.getText().toString().isEmpty()) {
                    Log.d("SAVE_YOUR_DREAMS", "Username is missing");
                    username.setError("Merci d'indiquer un nom d'utilisateur");
                } else if (password.getText().toString().isEmpty()) {
                    Log.d("SAVE_YOUR_DREAMS", "Password is missing");
                    password.setError("Merci d'indiquer un mot de passe");
                } else {
                    AuthService authService = new AuthService();
                    authService
                            .exists(username.getText().toString(),
                                    status -> {
                                        Log.d("SAVE_YOUR_DREAMS", "username=" + username.getText().toString() + ", username is free ?=" + status);
                                        if (!status) {
                                            authService.register(view.getContext(), username.getText().toString(), password.getText().toString(),
                                                    (ignored) -> runOnUiThread(() -> {
                                                        Toast.makeText(view.getContext(), "Inscrit avec success !", Toast.LENGTH_LONG).show();
                                                        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                                        startActivity(i);
                                                        finish();
                                                    }),
                                                    (error) -> runOnUiThread(() -> Toast.makeText(view.getContext(), "Impossible de s'inscrire", Toast.LENGTH_LONG).show()));
                                        } else {
                                            runOnUiThread(() -> Toast.makeText(view.getContext(), "Nom d'utilisateur non disponible", Toast.LENGTH_LONG).show());
                                            username.setError("Incorrect");
                                        }
                                    },
                                    error ->
                                            // Pour pouvoir lancer le toast, il faut forcement etre dans le UI thread
                                            // Sauf que ce code la est executé dans un thread autre, alors il faut appeller la fonction runOnUiThread pour faire ça
                                            runOnUiThread(() -> Toast.makeText(view.getContext(), error, Toast.LENGTH_LONG).show()));
                }

                break;
            default:
                break;
        }
    }

}
