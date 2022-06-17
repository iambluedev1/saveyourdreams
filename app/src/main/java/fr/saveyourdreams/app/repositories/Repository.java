package fr.saveyourdreams.app.repositories;

public abstract class Repository<T> {

    public abstract boolean save(T t) throws Exception;

}
