package algonquin.cst2335.bagg0051;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.w(TAG, "onCreate() - Activity created");

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to launch SecondActivity
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.w(TAG, "onStart() - Activity started");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.w(TAG, "onResume() - Activity resumed");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.w(TAG, "onPause() - Activity paused");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.w(TAG, "onStop() - Activity stopped");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.w(TAG, "onDestroy() - Activity destroyed");
    }
}
