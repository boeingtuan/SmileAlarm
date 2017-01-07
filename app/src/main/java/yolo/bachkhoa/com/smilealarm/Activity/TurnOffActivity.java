package yolo.bachkhoa.com.smilealarm.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.microsoft.projectoxford.emotion.EmotionServiceRestClient;
import com.microsoft.projectoxford.emotion.contract.RecognizeResult;
import com.microsoft.projectoxford.emotion.rest.EmotionServiceException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import yolo.bachkhoa.com.smilealarm.Model.AlarmImageModel;
import yolo.bachkhoa.com.smilealarm.R;
import yolo.bachkhoa.com.smilealarm.Service.StorageService;

/**
 * Created by Tuân on 07/01/2017.
 */

public class TurnOffActivity extends Activity {

    private int REQUEST_IMAGE_CAPTURE = 1;
    private MediaPlayer mediaPlayer;
    private File myFilesDir;
    private String mCurrentPhotoPath;
    private EmotionServiceRestClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turn_off);
        ringAlarm();
        Button btn = (Button) findViewById(R.id.take_photo);
        if (client == null) {
            client = new EmotionServiceRestClient(getString(R.string.subscription_key));
        }
        Button share = (Button) findViewById(R.id.share);
        share.setVisibility(View.INVISIBLE);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        ex.printStackTrace();
                    }
                    // Continue only if the File was successfully created
                    Log.d("YOLOLOG", "onClick: " + photoFile);
                    if (photoFile != null) {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
            }
        });
        ringAlarm();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                Bitmap mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
                ImageView mImageView = (ImageView) findViewById(R.id.your_photo);
                mImageView.setImageBitmap(mImageBitmap);
                isSmile(mImageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void isSmile(Bitmap img) {
        try {
            DoRequest doRequest = new DoRequest(img);
            doRequest.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double quota = 0.5;

    private class DoRequest extends AsyncTask<String, String, List<RecognizeResult>> {

        Bitmap mBitmap;
        private boolean result;

        public DoRequest(Bitmap bitmap) {
            this.mBitmap = bitmap;
        }

        @Override
        protected List<RecognizeResult> doInBackground(String... strings) {
            try {
                Log.d("YOLOLOG", "doInBackground");
                return processWithAutoFaceDetection(mBitmap);
            } catch (EmotionServiceException | IOException e1) {
                e1.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<RecognizeResult> recognizeResults) {
            if (recognizeResults != null) {
                if (recognizeResults.size() == 0) {
                    result =  false;
                } else {
                    result = false;
                    for (RecognizeResult r : recognizeResults) {
                        if (r.scores.happiness > quota) {
                            result =  true;
                            break;
                        }
                    }
                }
            } else {
                result = false;
            }
            Button share = (Button) findViewById(R.id.share);
            if (result && mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlarmImageModel.getInstance().insert(new Date(), StorageService.scaleBitmap(mBitmap, 512, 512), "Have a nice day");
                    }
                });
                share.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Have a nice day, best luck for you!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "You still look so sad, please try again!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private List<RecognizeResult> processWithAutoFaceDetection(Bitmap mBitmap) throws EmotionServiceException, IOException {
        Gson gson = new Gson();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Log.d("YOLOLOG", mBitmap.getWidth() + " " + mBitmap.getHeight());
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 15, output);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray());

        long startTime = System.currentTimeMillis();
        List<RecognizeResult> result;
        Log.d("YOLOLOG", "doInBackground2");
        result = this.client.recognizeImage(inputStream);
        Log.d("YOLOLOG", "doInBackground3");
        String json = gson.toJson(result);
        Log.d("YOLOLOG", json);
        Log.d("YOLOLOG", String.format("Detection done. Elapsed time: %d ms", (System.currentTimeMillis() - startTime)));

        return result;
    }

    private void ringAlarm() {
        mediaPlayer = new MediaPlayer();
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        try {
            mediaPlayer.setDataSource(this, alarmUri);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);

            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
