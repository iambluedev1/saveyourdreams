package fr.saveyourdreams.app.models;

import android.util.ArraySet;
import java.util.Optional;
import java.util.Set;

public class Marker extends Model {

    private String title;

    private String description;

    private Set<String> images = new ArraySet<>();

    private User author;

    private double lat;

    private double lng;

    private String streetName;

    private String city;

    private Integer zipCode;

    private Set<User> sharedWithUsers = new ArraySet<>();

    private Group group;

    public String getTitle() {
        return title;
    }

    public Marker setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Marker setDescription(String description) {
        this.description = description;
        return this;
    }

    public Set<String> getImages() {
        return images;
    }

    public Marker setImages(Set<String> images) {
        this.images = images;
        return this;
    }

    public Marker addImage(String image) {
        this.images.add(image);
        return this;
    }

    public User getAuthor() {
        return author;
    }

    public Marker setAuthor(User author) {
        this.author = author;
        return this;
    }

    public double getLat() {
        return lat;
    }

    public Marker setLat(double lat) {
        this.lat = lat;
        return this;
    }

    public double getLng() {
        return lng;
    }

    public Marker setLng(double lng) {
        this.lng = lng;
        return this;
    }

    public Optional<String> getStreetName() {
        return Optional.ofNullable(streetName);
    }

    public Marker setStreetName(String streetName) {
        this.streetName = streetName;
        return this;
    }

    public Optional<String> getCity() {
        return Optional.ofNullable(city);
    }

    public Marker setCity(String city) {
        this.city = city;
        return this;
    }

    public Optional<Integer> getZipCode() {
        return Optional.ofNullable(zipCode);
    }

    public Marker setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    public Set<User> getSharedWithUsers() {
        return sharedWithUsers;
    }

    public Marker setSharedWithUsers(Set<User> sharedWithUsers) {
        this.sharedWithUsers = sharedWithUsers;
        return this;
    }

    public Marker shareWithUser(User user) {
        this.sharedWithUsers.add(user);
        return this;
    }

    public Optional<Group> getGroup() {
        return Optional.ofNullable(group);
    }

    @Override
    public String toString() {
        return "Marker{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", images=" + images +
                ", author=" + author +
                ", lat=" + lat +
                ", lng=" + lng +
                ", streetName='" + streetName + '\'' +
                ", city='" + city + '\'' +
                ", zipCode=" + zipCode +
                ", sharedWithUsers=" + sharedWithUsers +
                ", createdAt=" + createdAt +
                ", group=" + group +
                '}';
    }

    @Override
    public boolean save() {
        return false;
    }
}
