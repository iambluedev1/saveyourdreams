package fr.saveyourdreams.app.models;

import android.util.ArraySet;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public class User {

    private UUID id;

    private String username;

    private Instant createdAt;

    private Set<Marker> markers = new ArraySet<>();

    public UUID getId() {
        return id;
    }

    public User setId(UUID id) {
        this.id = id;
        return this;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public User setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setMarkers(Set<Marker> markers) {
        this.markers = markers;
    }

    public Set<Marker> getMarkers() {
        return markers;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
