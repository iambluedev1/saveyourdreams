package fr.saveyourdreams.app.repositories;

import android.util.Log;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import fr.saveyourdreams.app.models.User;

public class UserRepository extends Repository<User> {

    public static User byId(String id) throws Exception {
        PreparedStatement stmt = Database.getInstance().getConnection().prepareStatement("SELECT * FROM users WHERE id = ?::uuid");
        stmt.setString(1, id);
        ResultSet rs = stmt.executeQuery();

        User user = new User();
        user.setId(UUID.fromString(rs.getString("id")));
        user.setUsername(rs.getString("username"));
        user.setCreatedAt(rs.getDate("create_at").toInstant());

        return user;
    }

    @Override
    public boolean save(User model) throws Exception {
        if (model.getId() != null) {
            // Si l'id est présent c'est qu'on met a jour l'entité
        } else {
            // Sinon on le crée
            // RETURN_GENERATED_KEYS permet de récupérer l'id de l'insert pour pouvoir le rajouter dans l'object
            PreparedStatement stmt = Database.getInstance().getConnection().prepareStatement("INSERT INTO users (username, password, created_at) VALUES (?,?,'NOW()')", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, model.getUsername());
            stmt.setString(2, model.getPassword());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    model.setCreatedAt(generatedKeys.getDate("created_at").toInstant());
                    model.setId(UUID.fromString(generatedKeys.getString("id")));
                    Log.d("SAVE_YOUR_DREAMS", "Inserting user with id " + model.getId());

                    return true;
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        }

        return false;
    }
}
