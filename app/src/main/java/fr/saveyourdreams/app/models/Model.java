package fr.saveyourdreams.app.models;

import java.time.Instant;
import java.util.UUID;

public abstract class Model {

    /**
     * En mettant ici ces deux champs, on force tous les models a les avoir
     */

    protected UUID id;

    protected Instant createdAt;

    public UUID getId() {
        return id;
    }

    public Model setId(UUID id) {
        this.id = id;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Model setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }


    public abstract boolean save() throws Exception;

}
