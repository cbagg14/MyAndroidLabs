package algonquin.cst2335.bagg0051;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent nextPage = new Intent( MainActivity.this, SecondActivity.class);

        EditText emailEditText = findViewById(R.id.emailEditText);
        Button loginButton = findViewById(R.id.loginButton);


        loginButton.setOnClickListener( clk-> {
            String Email =  emailEditText.getText().toString();

            nextPage.putExtra("EmailAddress", Email);
            startActivity(nextPage);

          /*  String email = emailEditText.getText().toString();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(EMAIL_KEY,email);
            editor.apply(); */
        });

        Log.w(TAG, "onCreate() - Activity created");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.w(TAG, "onDestroy() - Activity destroyed");
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.w(TAG, "onStart() - Activity started");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.w(TAG, "onStop() - Activity stopped");
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

}
