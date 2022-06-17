package fr.saveyourdreams.app.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

import fr.saveyourdreams.app.models.User;

public class UserRepository {

    public static User byId(String id) throws Exception {
        PreparedStatement stmt = Database.getInstance().getConnection().prepareStatement("SELECT * FROM users WHERE id = ?::uuid");
        stmt.setString(1, id);
        ResultSet rs = stmt.executeQuery();
        
        return new User()
                .setId(UUID.fromString(rs.getString("id")))
                .setUsername(rs.getString("username"))
                .setCreatedAt(rs.getDate("create_at").toInstant());
    }

}
