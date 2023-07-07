package algonquin.cst2335.bagg0051;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String SHARED_PREFS_FILE_NAME = "MyData";
    private static final String EMAIL_ADDRESS_KEY = "LoginName";

    private EditText emailEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEditText = findViewById(R.id.emailEditText);
        loginButton = findViewById(R.id.loginButton);

        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE);
        String emailAddress = prefs.getString(EMAIL_ADDRESS_KEY, "");
        emailEditText.setText(emailAddress);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredEmail = emailEditText.getText().toString();
                saveEmailAddress(enteredEmail);
                navigateToSecondActivity(enteredEmail);
            }
        });

        Log.w(TAG, "onCreate() - Activity created");
    }

    private void saveEmailAddress(String emailAddress) {
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(EMAIL_ADDRESS_KEY, emailAddress);
        editor.apply();
    }

    private void navigateToSecondActivity(String emailAddress) {
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        intent.putExtra("EmailAddress", emailAddress);
        startActivity(intent);
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
