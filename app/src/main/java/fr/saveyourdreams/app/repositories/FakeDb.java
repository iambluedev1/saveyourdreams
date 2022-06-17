package fr.saveyourdreams.app.repositories;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import fr.saveyourdreams.app.models.Group;
import fr.saveyourdreams.app.models.Marker;
import fr.saveyourdreams.app.models.User;

public class FakeDb {

    public static List<User> users = new ArrayList<>();
    public static List<Marker> markers = new ArrayList<>();
    public static List<Group> groups = new ArrayList<>();

    static {
        users.add(new User()
                .setUsername("guillaume")
                .setId(UUID.randomUUID())
                .setCreatedAt(Instant.now()));

        users.add(new User()
                .setUsername("roch")
                .setId(UUID.randomUUID())
                .setCreatedAt(Instant.now()));

        markers.add(new Marker()
                .setId(UUID.randomUUID())
                .setTitle("Mon premier marker !")
                .setLat(46.253470)
                .setLng(5.653510)
                .addImage("https://images.unsplash.com/photo-1652825111897-e2abd6cd8ece?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwxfDB8MXxyYW5kb218MHx8fHx8fHx8MTY1NTMyOTc4Mg&ixlib=rb-1.2.1&q=80&w=1080")
                .addImage("https://images.unsplash.com/photo-1654525482694-af1b5e9df569?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwxfDB8MXxyYW5kb218MHx8fHx8fHx8MTY1NTMyOTgwOA&ixlib=rb-1.2.1&q=80&w=1080")
                .addImage("https://images.unsplash.com/photo-1654358062106-a2d3b051fef9?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwxfDB8MXxyYW5kb218MHx8fHx8fHx8MTY1NTMyOTgxNg&ixlib=rb-1.2.1&q=80&w=1080")
                .setAuthor(users.get(0))
                .shareWithUser(users.get(1)));

        markers.add(new Marker()
                .setId(UUID.randomUUID())
                .setTitle("Un souvenir de malte !")
                .setDescription("Franchement c'était ouf ce jour la !")
                .setLat(44.940609)
                .setLng(5.852910)
                .addImage("https://images.unsplash.com/photo-1653904780132-b9333771c64b?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwxfDB8MXxyYW5kb218MHx8fHx8fHx8MTY1NTMyOTgyNg&ixlib=rb-1.2.1&q=80&w=1080")
                .addImage("https://images.unsplash.com/photo-1652974281157-3406722410b5?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwxfDB8MXxyYW5kb218MHx8fHx8fHx8MTY1NTMyOTgzMw&ixlib=rb-1.2.1&q=80&w=1080")
                .addImage("https://images.unsplash.com/photo-1653141570968-bdc66d75da1a?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwxfDB8MXxyYW5kb218MHx8fHx8fHx8MTY1NTMyOTg0Mg&ixlib=rb-1.2.1&q=80&w=1080")
                .addImage("https://images.unsplash.com/photo-1653672994990-086a64a40447?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwxfDB8MXxyYW5kb218MHx8fHx8fHx8MTY1NTMyOTg0OQ&ixlib=rb-1.2.1&q=80&w=1080")
                .setAuthor(users.get(0))
                .shareWithUser(users.get(1)));

        markers.add(new Marker()
                .setId(UUID.randomUUID())
                .setLat(35.956532)
                .setLng(14.361250)
                .addImage("https://images.unsplash.com/photo-1654175963392-6f873fc7ef5c?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwxfDB8MXxyYW5kb218MHx8fHx8fHx8MTY1NTMyOTg1Nw&ixlib=rb-1.2.1&q=80&w=1080")
                .setAuthor(users.get(0))
                .shareWithUser(users.get(1)));

        markers.add(new Marker()
                .setId(UUID.randomUUID())
                .setLat(44.940609)
                .setLng(5.852910)
                .addImage("https://images.unsplash.com/photo-1654447398843-f8a74d6f2c78?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwxfDB8MXxyYW5kb218MHx8fHx8fHx8MTY1NTMyOTg2NA&ixlib=rb-1.2.1&q=80&w=1080")
                .setAuthor(users.get(1)));


        groups.add(new Group()
                .setTitle("Malte 🚀")
                .setAuthor(users.get(0))
                .setId(UUID.randomUUID())
                .setCreatedAt(Instant.now())
                .shareWithUser(users.get(1))
                .addMarker(markers.get(1))
                .addMarker(markers.get(2))
        );

        groups.add(new Group()
                .setTitle("Paris ! 🥖")
                .setAuthor(users.get(0))
                .setId(UUID.randomUUID())
                .setCreatedAt(Instant.now())
                .addMarker(markers.get(0))
        );
    }

}
