package algonquin.cst2335.bagg0051;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(clk -> {
            Intent nextPage = new Intent(SecondActivity.this, MainActivity.class);
            startActivity(nextPage);
        });
    }
}
