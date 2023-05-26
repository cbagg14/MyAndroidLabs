package algonquin.cst2335.bagg0051.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import algonquin.cst2335.bagg0051.R;
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

        variableBinding.textview.setText("Your edit has: " + model.editString.getValue());
        variableBinding.mybutton.setOnClickListener(click -> {
            model.editString.setValue(variableBinding.myedittext.getText().toString());
        });

        // Register MainActivity as an observer of the MutableLiveData<Boolean> variable
        model.isSelected.observe(this, selected -> {
            // Update the CompoundButton objects (CheckBox, Switch, RadioButton)
            variableBinding.checkbox.setChecked(selected);
            variableBinding.myswitch.setChecked(selected);
            variableBinding.radiobutton.setChecked(selected);

            //Show a toast message with the updated value
            String toastMessage = "The value is now: " + selected;
            Toast.makeText(MainActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
        });

        // Set onCheckedChangedListener for each CompoundButton
        variableBinding.checkbox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            model.isSelected.setValue(isChecked);
        });

        variableBinding.myswitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            model.isSelected.setValue(isChecked);
        });

        variableBinding.radiobutton.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            model.isSelected.setValue(isChecked);
        });

        variableBinding.imageview.setImageResource(R.drawable.logo_algonquin);

        variableBinding.myimagebutton.setOnClickListener(view -> {
            int width = view.getWidth();
            int height = view.getHeight();
            String toastMessage = "The width = " + width + " and height = " + height;
            Toast.makeText(MainActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
        });
    }
}
