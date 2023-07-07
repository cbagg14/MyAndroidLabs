package algonquin.cst2335.bagg0051;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SecondActivity extends AppCompatActivity {

    private static final String TAG = "SecondActivity";
    private static final String IMAGE_FILE_NAME = "Picture.png";
    private static final String SHARED_PREFS_FILE_NAME = "MyData";
    private static final String PHONE_NUMBER_KEY = "PhoneNumber";

    private TextView titleTextView;
    private EditText phoneEditText;
    private ImageView imageView;
    private Button callButton;
    private Button changeButton;

    private ActivityResultLauncher<Intent> cameraResult;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        titleTextView = findViewById(R.id.titleTextView);
        phoneEditText = findViewById(R.id.phoneEditText);
        imageView = findViewById(R.id.imageView);
        callButton = findViewById(R.id.callButton);
        changeButton = findViewById(R.id.changeButton);

        Intent fromPrevious = getIntent();
        String emailAddress = fromPrevious.getStringExtra("EmailAddress");
        titleTextView.setText("Welcome back " + emailAddress);

        // Check if the image file exists and load it into the ImageView
        if (isImageFileExists()){
            Bitmap imageBitmap = loadImageFromStorage();
            imageView.setImageBitmap(imageBitmap);
        }

        // Load the phone number from SharedPreferences and set it in the EditText
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE);
        String phoneNumber = prefs.getString(PHONE_NUMBER_KEY, "");
        phoneEditText.setText(phoneNumber);

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phoneEditText.getText().toString();
                makePhoneCall(phoneNumber);
            }
        });

        cameraResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        Bundle extras = data.getExtras();
                        if (extras != null && extras.containsKey("data")) {
                            Bitmap imageBitmap = (Bitmap) extras.get("data");
                            imageView.setImageBitmap(imageBitmap);

                            saveImageToStorage(imageBitmap);
                        }
                    }
                });

        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Save the current phone number to SharedPreferences
        String phoneNumber = phoneEditText.getText().toString();
        savePhoneNumber(phoneNumber);
    }

    private void makePhoneCall(String phoneNumber) {
        if (phoneNumber.trim().length() > 0) {
            String dial = "tel:" + phoneNumber;
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse(dial));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1);
                } else {
                    startActivity(callIntent);
                }
            }
        }
    }

    private void takePicture() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraResult.launch(cameraIntent);
    }
    private void saveImageToStorage(Bitmap bitmap) {
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(IMAGE_FILE_NAME, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            Log.i(TAG, "Image saved successfully");
        } catch (IOException e) {
            Log.e(TAG, "Error saving image to storage: " + e.getMessage());
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error closing FileOutputStream: " + e.getMessage());
                }
            }
        }
    }
    private boolean isImageFileExists() {
        File file = new File(getFilesDir(), IMAGE_FILE_NAME);
        return file.exists();
    }
    private Bitmap loadImageFromStorage() {
        try {
            FileInputStream fis = openFileInput(IMAGE_FILE_NAME);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "Error loading image from storage: " + e.getMessage());
        }
        return null;
    }

    private void savePhoneNumber(String phoneNumber) {
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PHONE_NUMBER_KEY, phoneNumber);
        editor.apply();
    }

}
