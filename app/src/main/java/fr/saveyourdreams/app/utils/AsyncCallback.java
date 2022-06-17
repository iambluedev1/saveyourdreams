package fr.saveyourdreams.app.utils;

/**
 * Comme beaucoup de traitement se fait en asynchone, pour une meilleure lisibilité au niveau du code on définit cette interface qui sera utiliser pour passer
 * des fonctions aux fonctions asynchrone
 *
 * @param <T>
 * @see fr.saveyourdreams.app.services.AuthService
 */
public interface AsyncCallback<T> {

    void get(T t);

    /**
     * C'est juste pour pouvoir faire remonter des erreurs liées a des exceptions par exemple et envoyer des Toast (quand on ne se trouve pas dans une activity et que l'execution est async)
     */
    interface ErrorCallback extends AsyncCallback<String> {
        void get(String error);
    }

}
