package fr.saveyourdreams.app.models;

import android.util.ArraySet;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import fr.saveyourdreams.app.repositories.UserRepository;

public class User extends Model {

    private String username;

    private Set<Marker> markers = new ArraySet<>();

    private String password;

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public User setMarkers(Set<Marker> markers) {
        this.markers = markers;
        return this;
    }

    public Set<Marker> getMarkers() {
        return markers;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    //@TODO: on peut certainement generaliser cette fonction a tous les models sans avoir a la redéfinir à chaque fois
    public boolean save() throws Exception {
        return new UserRepository().save(this);
    }
}
