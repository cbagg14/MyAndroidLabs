package algonquin.cst2335.bagg0051.ui;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import algonquin.cst2335.bagg0051.data.MainViewModel;
import algonquin.cst2335.bagg0051.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding variableBinding;
    private MainViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(this).get(MainViewModel.class);

        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());

        variableBinding.textview.setText("Your edit has: " + model.editString);
        variableBinding.mybutton.setOnClickListener(click ->
        {
            model.editString.observe(this, s -> {
               variableBinding.textview.setText("Your edit text has "+ s);
            });
        });
        }

    }
