package algonquin.cst2335.bagg0051;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import algonquin.cst2335.bagg0051.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private RequestQueue queue;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        queue = Volley.newRequestQueue(this);

        binding.forecastButton.setOnClickListener(click -> {
            String cityName = binding.etCity.getText().toString();
            String stringURL = null;
            try {
                stringURL = "https://api.openweathermap.org/data/2.5/weather?q="
                        + URLEncoder.encode(cityName, "UTF-8")
                        + "&appid=7e943c97096a9784391a981c4d878b22&units=metric";
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringURL, null,
                    response -> {
                        try {
                            JSONObject main = response.getJSONObject("main");
                            double current = main.getDouble("temp");
                            double min = main.getDouble("temp_min");
                            double max = main.getDouble("temp_max");
                            int humidity = main.getInt("humidity");

                            runOnUiThread(() -> {
                                binding.current.setText("The current temperature is " + current + " degrees");
                                binding.current.setVisibility(View.VISIBLE);

                                binding.maxTemp.setText("The max temperature is " + max + " degrees");
                                binding.maxTemp.setVisibility(View.VISIBLE);

                                binding.minTemp.setText("The min temperature is " + min + " degrees");
                                binding.minTemp.setVisibility(View.VISIBLE);

                                binding.humidity.setText("The humidity is " + humidity + " degrees");
                                binding.humidity.setVisibility(View.VISIBLE);
                            });

                            JSONArray weatherArray = response.getJSONArray("weather");
                            JSONObject position0 = weatherArray.getJSONObject(0);

                            String description = position0.getString("description");
                            String iconName = position0.getString("icon");
                            String imageUrl = "http://openweathermap.org/img/w/" + iconName + ".png";

                            runOnUiThread(() -> {
                                binding.description.setText(description);
                                binding.description.setVisibility(View.VISIBLE);
                            });

                            // Check if the image already exists in internal storage
                            String imageName = iconName + ".png";
                            String imagePath = getFilesDir() + "/" + imageName;
                            File imageFile = new File(imagePath);

                            if (imageFile.exists()) {
                                Bitmap imageBitmap = BitmapFactory.decodeFile(imagePath);
                                if (imageBitmap != null) {
                                    // Image exists in internal storage, set it to the ImageView
                                    runOnUiThread(() -> {
                                        binding.icon.setImageBitmap(imageBitmap);
                                        binding.icon.setVisibility(View.VISIBLE);
                                    });
                                } else {
                                    Log.d("ImageLoad", "Failed to load image from internal storage: " + imagePath);
                                }
                            } else {
                                // Image doesn't exist in internal storage, make an ImageRequest to download it
                                ImageRequest imgReq = new ImageRequest(imageUrl, bitmap -> {
                                    // Do something with loaded bitmap...
                                    runOnUiThread(() -> {
                                        binding.icon.setImageBitmap(bitmap);
                                        binding.icon.setVisibility(View.VISIBLE);
                                    });

                                    try {
                                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, MainActivity.this.openFileOutput(imageName, Activity.MODE_PRIVATE));
                                        Log.d("ImageSave", "Image saved to internal storage: " + imagePath);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }, 1024, 1024, ImageView.ScaleType.CENTER, null,
                                        error -> {
                                            // Handle error if image download fails
                                            Log.e("ImageDownload", "Error downloading image: " + error.getMessage());
                                        });
                                queue.add(imgReq);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }, error -> {
                // Handle error if JSON request fails
                Log.e("JSONRequest", "Error making JSON request: " + error.getMessage());
            });

            queue.add(request);
        });
    }
}
