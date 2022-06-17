package fr.saveyourdreams.app.models;

import android.util.ArraySet;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public class Group {

    private UUID id;

    private String title;

    private User author;

    private Set<User> sharedWithUsers = new ArraySet<>();


    private Set<Marker> markers = new ArraySet<>();

    private Instant createdAt;

    public UUID getId() {
        return id;
    }

    public Group setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Group setTitle(String title) {
        this.title = title;
        return this;
    }

    public User getAuthor() {
        return author;
    }

    public Group setAuthor(User author) {
        this.author = author;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Group setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Set<User> getSharedWithUsers() {
        return sharedWithUsers;
    }

    public Group setSharedWithUsers(Set<User> sharedWithUsers) {
        this.sharedWithUsers = sharedWithUsers;
        return this;
    }

    public Group shareWithUser(User user) {
        this.sharedWithUsers.add(user);
        return this;
    }

    public Set<Marker> getMarkers() {
        return markers;
    }

    public Group setMarkers(Set<Marker> markers) {
        this.markers = markers;
        return this;
    }

    public Group addMarker(Marker marker) {
        this.markers.add(marker);
        return this;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author=" + author +
                ", sharedWithUsers=" + sharedWithUsers +
                ", markers=" + markers +
                ", createdAt=" + createdAt +
                '}';
    }
}
