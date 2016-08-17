package in.co.geekninja.charithranweshikal.Services;

/**
 * Created by PS on 2/26/2016.
 *
 * @param <T> the type parameter
 */
public interface ServiceCallback<T> {
    /**
     * Success.
     *
     * @param t the t
     */
    void success(T t);

    /**
     * Failure.
     *
     * @param error the error
     */
    void failure(RuntimeException error);
}
