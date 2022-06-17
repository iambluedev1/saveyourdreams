package fr.saveyourdreams.app.models;

import java.time.Instant;
import java.util.UUID;

public class User {

    private UUID id;

    private String username;

    private Instant createdAt;

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
