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

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_register_link:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.login_submit:
                EditText username = findViewById(R.id.login_username);
                EditText password = findViewById(R.id.login_password);

                if (username.getText().toString().isEmpty()) {
                    Log.d("SAVE_YOUR_DREAMS", "Username is missing");
                    username.setError("Merci d'indiquer un nom d'utilisateur");
                } else if (password.getText().toString().isEmpty()) {
                    Log.d("SAVE_YOUR_DREAMS", "Password is missing");
                    password.setError("Merci d'indiquer un mot de passe");
                } else {
                    AuthService authService = new AuthService();
                    authService
                            .verify(username.getText().toString(), password.getText().toString(),
                                    status -> {
                                        Log.d("SAVE_YOUR_DREAMS", "username=" + username.getText().toString() + ", " + "password=" + password.getText().toString() + ", login is correct ?=" + status);
                                        if (status) {
                                            authService.login(view.getContext(), username.getText().toString(),
                                                    (ignored) -> runOnUiThread(() -> {
                                                        Toast.makeText(view.getContext(), "Connecté avec success !", Toast.LENGTH_LONG).show();
                                                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                                        startActivity(i);
                                                        finish();
                                                    }),
                                                    (error) -> runOnUiThread(() -> Toast.makeText(view.getContext(), "Nom d'utilisateur ou mot de passe pas bon", Toast.LENGTH_LONG).show()));
                                        } else {
                                            runOnUiThread(() -> Toast.makeText(view.getContext(), "Nom d'utilisateur ou mot de passe pas bon", Toast.LENGTH_LONG).show());
                                            username.setError("Incorrect");
                                            password.setError("Incorrect");
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
