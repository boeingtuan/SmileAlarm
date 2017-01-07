package yolo.bachkhoa.com.smilealarm.Model;

/**
 * Created by acer on 1/7/2017.
 */
public interface EventHandleWithKey<K, T> {
    public void onSuccess(K key, T o);
    public void onError(String o);
}
