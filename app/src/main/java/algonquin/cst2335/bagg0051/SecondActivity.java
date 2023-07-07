package algonquin.cst2335.bagg0051;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import algonquin.cst2335.bagg0051.databinding.ActivitySecondBinding;

public class SecondActivity extends AppCompatActivity {

    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        textView = findViewById(R.id.titleTextView);
        Intent fromPrevious = getIntent();
       String emailAddress = fromPrevious.getStringExtra("EmailAddress");
        textView.setText("Welcome back " + emailAddress);

    }
}
