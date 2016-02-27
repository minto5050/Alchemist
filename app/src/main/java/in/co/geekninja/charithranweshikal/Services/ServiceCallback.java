package in.co.geekninja.charithranweshikal.Services;

/**
 * Created by PS on 2/26/2016.
 */
public interface ServiceCallback<T> {
    void success(T t);
    void failure(RuntimeException error);
}
