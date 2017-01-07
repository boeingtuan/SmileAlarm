package yolo.bachkhoa.com.smilealarm.Service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import yolo.bachkhoa.com.smilealarm.Model.EventHandle;

public class StorageService {
    private static String FIREBASE_STORE_REF_URL = "gs://smilealarm-2678d.appspot.com";
    private static FirebaseStorage storage = FirebaseStorage.getInstance();
    private static StorageReference storageRef = storage.getReferenceFromUrl(FIREBASE_STORE_REF_URL).child("AlarmImage");
    public static String saveImage(String name, Bitmap bitmap, final EventHandle<String> eventHandle){
        StorageReference imageStorage = storageRef.child(name);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageStorage.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                eventHandle.onError(exception.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                eventHandle.onSuccess(taskSnapshot.getDownloadUrl().toString());
            }
        });
        return null;
    }

    public static void getImage(String name, final EventHandle<Bitmap> eventHandle){
        StorageReference ref = storageRef.child(name);

        final long ONE_MEGABYTE = 442661L;
        ref.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                eventHandle.onSuccess(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                eventHandle.onError(exception.getMessage());
            }
        });
    }
}