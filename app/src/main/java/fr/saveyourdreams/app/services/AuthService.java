package fr.saveyourdreams.app.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.ArraySet;
import android.util.Log;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.saveyourdreams.app.models.Marker;
import fr.saveyourdreams.app.models.User;
import fr.saveyourdreams.app.repositories.Database;
import fr.saveyourdreams.app.repositories.MarkerRepository;
import fr.saveyourdreams.app.utils.AsyncCallback;

public class AuthService {

    public static User connectedUser = null;

    private SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("SAVE_YOUR_DREAMS", 0); // 0 pour le mode privée
    }

    public boolean isConnected(Context context) {
        return getSharedPreferences(context).getBoolean("connected", false) && getSharedPreferences(context).contains("username");
    }

    public interface AuthVerificationCallback extends AsyncCallback<Boolean> {
    }

    public interface LoginCallback extends AsyncCallback<Boolean> {
    }

    /**
     * Verifier si les infos de connexion son juste
     *
     * @param username
     * @param password
     * @param callback      pour récupérer si oui ou non les informations de connexion son juste
     * @param errorCallback
     */
    public void verify(String username, String password, AuthVerificationCallback callback, AsyncCallback.ErrorCallback errorCallback) {
        // On va verifier dans la base de donnée si le mot de passe est bon
        // @TODO: Pour plus de sécurité on pourrait chiffrer les mots de passe avec SHA512 ou bcrypt par exemple mais ici, volontairement on ne le fait pas
        // Comme on fait un appel a la base de donnée, forcément on se cale dans un nouveau thread

        new Thread(() -> {
            if (!Database.getInstance().isConnected()) {
                try {
                    Database.getInstance().connect();
                } catch (ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                    errorCallback.get("Unable to connect to DB");
                    return;
                }
            }

            try {
                // Il est préférable d'utiliser des requetes préparées pour eviter du SQL injection...
                PreparedStatement stmt = Database.getInstance().getConnection().prepareStatement("SELECT password FROM users WHERE username = ?");
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                boolean hasNext = rs.next();

                if (hasNext) {
                    String passwordInDb = rs.getString("password");

                    // On a l'information de si les informations de connexion spécifiées sont bon ou non alors on execute le callback
                    callback.get(passwordInDb.equals(password));
                } else {
                    callback.get(false);
                }

                return;
            } catch (SQLException e) {
                e.printStackTrace();
                errorCallback.get("Unable to execute request");
                return;
            }
        }).start();
    }

    public void reconnect(Context context, LoginCallback callback, AsyncCallback.ErrorCallback errorCallback) {
        login(context, getSharedPreferences(context).getString("username", null), callback, errorCallback);
    }

    public void login(Context context, String username, LoginCallback callback, AsyncCallback.ErrorCallback errorCallback) {
        new Thread(() -> {
            if (!Database.getInstance().isConnected()) {
                try {
                    Database.getInstance().connect();
                } catch (ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                    errorCallback.get("Unable to connect to DB");
                    return;
                }
            }

            try {
                // Il est préférable d'utiliser des requetes préparées pour eviter du SQL injection...
                PreparedStatement stmt = Database.getInstance().getConnection().prepareStatement("SELECT * FROM users WHERE username = ?");
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                boolean hasNext = rs.next();

                if (hasNext) {
                    UUID id = UUID.fromString(rs.getString("id"));
                    Date createdAt = rs.getDate("created_at");

                    // On stock dans les préferences l'username de l'user et s'il est connecté seulement pour pouvoir sauter l'étape de connexion les prochaines fois
                    // @TODO: Normalement on ne stockerait pas cette information comme ça, mais plutot via un token jwt, une authorisation oauth2 etc...
                    SharedPreferences.Editor editor = getSharedPreferences(context).edit();
                    editor.putBoolean("connected", true);
                    editor.putString("username", rs.getString("username"));
                    editor.commit();

                    connectedUser = new User();
                    connectedUser.setId(id);
                    connectedUser.setUsername(username);
                    connectedUser.setCreatedAt(createdAt.toInstant());

                    try {
                        Set<Marker> markers = Stream.concat(
                                MarkerRepository.getMakersFromUser(connectedUser).stream(),
                                MarkerRepository.getMakersSharedWithUser(connectedUser).stream()
                        ).collect(Collectors.toSet());

                        Log.d("SAVE_YOUR_DREAMS", "Fetched " + markers.size() + " markers");
                        connectedUser.setMarkers(markers);
                    } catch (Exception e) {
                        e.printStackTrace();
                        errorCallback.get("Unable to fetch markers");
                        return;
                    }


                    Log.d("SAVE_YOUR_DREAMS", "user connected !");
                    callback.get(true);
                } else {
                    errorCallback.get("Compte inexistant");
                }

                return;
            } catch (SQLException e) {
                e.printStackTrace();
                errorCallback.get("Unable to execute request");
                return;
            }
        }).start();
    }

    public void logout(Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean("connected", true);
        editor.remove("username");
        editor.commit();

        Log.d("SAVE_YOUR_DREAMS", "Disconnect user");
    }

    public void exists(String username, AuthVerificationCallback callback, AsyncCallback.ErrorCallback errorCallback) {
        new Thread(() -> {
            if (!Database.getInstance().isConnected()) {
                try {
                    Database.getInstance().connect();
                } catch (ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                    errorCallback.get("Unable to connect to DB");
                    return;
                }
            }

            try {
                // Il est préférable d'utiliser des requetes préparées pour eviter du SQL injection...
                PreparedStatement stmt = Database.getInstance().getConnection().prepareStatement("SELECT username FROM users WHERE username = ?");
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();

                callback.get(rs.next());
                return;
            } catch (SQLException e) {
                e.printStackTrace();
                errorCallback.get("Unable to execute request");
                return;
            }
        }).start();
    }

    public void register(Context context, String username, String password, LoginCallback callback, AsyncCallback.ErrorCallback errorCallback) {
        new Thread(() -> {
            if (!Database.getInstance().isConnected()) {
                try {
                    Database.getInstance().connect();
                } catch (ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                    errorCallback.get("Unable to connect to DB");
                    return;
                }
            }

            User user = new User();
            user.setUsername(username);
            user.setPassword(password);

            try {
                boolean saved = user.save();
                if (saved)
                    callback.get(true);
                else
                    errorCallback.get("Unable to save User to DB");
            } catch (Exception e) {
                e.printStackTrace();
                errorCallback.get("Unable to save User to DB");
                return;
            }

        }).start();
    }
}
