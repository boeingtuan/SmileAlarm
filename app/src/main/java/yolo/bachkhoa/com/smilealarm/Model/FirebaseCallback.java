package yolo.bachkhoa.com.smilealarm.Model;
public interface FirebaseCallback<T> {
	public void onInserted(T o);
	public void onUpdated(T o);
	public void onDeleted(T o);
}