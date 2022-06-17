package fr.saveyourdreams.app.repositories;

import android.util.ArraySet;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import fr.saveyourdreams.app.models.Marker;
import fr.saveyourdreams.app.models.User;

public class MarkerRepository {

    public static Set<Marker> getMakersFromUser(User user) throws Exception {
        Set<Marker> markers = new ArraySet<>();
        PreparedStatement stmt = Database.getInstance().getConnection().prepareStatement("SELECT * FROM markers WHERE author = ?::uuid");
        stmt.setString(1, user.getId().toString());
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Marker marker = new Marker();
            marker.setId(UUID.fromString(rs.getString("id")));
            marker.setCreatedAt(rs.getDate("created_at").toInstant());

            markers.add(marker
                    .setTitle(rs.getString("title"))
                    .setDescription(rs.getString("description"))
                    .setAuthor(user)
                    .setImages(Arrays.stream(rs.getString("images").split("<<>>")).collect(Collectors.toSet()))
                    .setLat(rs.getFloat("lat"))
                    .setLng(rs.getFloat("lng"))
                    .setStreetName(rs.getString("street"))
                    .setZipCode(rs.getString("zipCode") == null ? null : Integer.valueOf(rs.getString("zipcode")))
            );
        }

        return markers;
    }

    public static Set<Marker> getMakersSharedWithUser(User user) throws Exception {
        Set<Marker> markers = new ArraySet<>();
        PreparedStatement stmt = Database.getInstance().getConnection().prepareStatement("SELECT * FROM markers_shared_with WHERE user_id = ?::uuid");
        stmt.setString(1, user.getId().toString());
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Marker marker = new Marker();
            marker.setId(UUID.fromString(rs.getString("id")));
            marker.setCreatedAt(rs.getDate("created_at").toInstant());

            markers.add(marker
                    .setTitle(rs.getString("title"))
                    .setDescription(rs.getString("description"))
                    .setAuthor(UserRepository.byId(rs.getString("author")))
                    .setImages(Arrays.stream(rs.getString("images").split("<<>>")).collect(Collectors.toSet()))
                    .setLat(rs.getFloat("lat"))
                    .setLng(rs.getFloat("lng"))
                    .setStreetName(rs.getString("street"))
                    .setZipCode(rs.getString("zipCode") == null ? null : Integer.valueOf(rs.getString("zipcode")))
            );
        }

        return markers;
    }

}
