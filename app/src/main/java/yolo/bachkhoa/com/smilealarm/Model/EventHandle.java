package yolo.bachkhoa.com.smilealarm.Model;

public interface EventHandle<T> {
	public void onSuccess(T o);
	public void onError(T o);
}