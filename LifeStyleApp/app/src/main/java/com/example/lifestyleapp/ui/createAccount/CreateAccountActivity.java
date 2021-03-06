package com.example.lifestyleapp.ui.createAccount;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lifestyleapp.MainDrawerActivity;
import com.example.lifestyleapp.R;
import com.example.lifestyleapp.models.User;
import com.example.lifestyleapp.ui.bmi.BMIViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener{
    CreateAccountActivityViewModel model;

    String country, sex, username, email, password, confirm_pw, city;
    String dob = "";
    int weight, height;

    Spinner spin_country, spin_sex;
    NumberPicker picker_weight, picker_height;
    EditText et_DOB, et_username, et_email, et_password, et_confirm_pw, et_city;
    Button bt_signUp;

    final Calendar calendar_DOB = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        getSupportActionBar().hide();

        model = ViewModelProviders.of(this).get(CreateAccountActivityViewModel.class);

        et_username = findViewById(R.id.et_username);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_confirm_pw = findViewById(R.id.et_confirm_password);
        et_city = findViewById(R.id.et_city);

        bt_signUp = findViewById(R.id.bt_signUp);
        bt_signUp.setOnClickListener(this);

        // Find DOB edit text.
        et_DOB = findViewById(R.id.et_DOB);
        initializeDOB();

        // Set up pickers.
        picker_weight = findViewById(R.id.np_weight);
        initializeWeightPicker();
        picker_height = findViewById(R.id.np_height);
        initializeHeightPicker();

        // Set up spinners.
        spin_country = findViewById(R.id.spinner_country);
        ArrayAdapter<CharSequence> country_adapter = ArrayAdapter.createFromResource(this,
                R.array.countries_array, R.layout.spinner_item);
        country_adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spin_country.setAdapter(country_adapter);

        spin_sex = findViewById(R.id.spinner_sex);
        ArrayAdapter<CharSequence> sex_adapter = ArrayAdapter.createFromResource(this,
                R.array.sex_array, R.layout.spinner_item);
        country_adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spin_sex.setAdapter(sex_adapter);
    }

    public void initializeWeightPicker() {
        picker_weight.setMaxValue(600);
        picker_weight.setMinValue(50);
        picker_weight.setValue(130);
    }

    public void initializeHeightPicker() {
        picker_height.setMaxValue(96);
        picker_height.setMinValue(24);
        picker_height.setValue(65);
    }

    public void initializeDOB() {

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar_DOB.set(Calendar.YEAR, year);
                calendar_DOB.set(Calendar.MONTH, month);
                calendar_DOB.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        et_DOB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateAccountActivity.this, date, calendar_DOB
                        .get(Calendar.YEAR), calendar_DOB.get(Calendar.MONTH),
                        calendar_DOB.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dob = sdf.format(calendar_DOB.getTime());
        et_DOB.setText(sdf.format(calendar_DOB.getTime()));
    }

    @Override
    public void onClick(View v) {
        username = et_username.getText().toString();
        email = et_email.getText().toString();
        password = et_password.getText().toString();
        confirm_pw = et_confirm_pw.getText().toString();
        city = et_city.getText().toString();
        country = spin_country.getSelectedItem().toString();
        int country_idx = spin_country.getSelectedItemPosition();
        sex = spin_sex.getSelectedItem().toString();
        int sex_idx = spin_sex.getSelectedItemPosition();
        weight = picker_weight.getValue();
        height = picker_height.getValue();

        if (username.matches("") || email.matches("") || password.matches("") ||
                confirm_pw.matches("") || dob.matches("") || city.matches("")) {
            Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show();
            return;
        } else if (!password.matches(confirm_pw)) {
            Toast.makeText(this, "Passwords must match.", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User();

        user.setName(username);
        user.setSex(sex);
        user.setSex_idx(sex_idx);
        user.setBirthday(dob);
        user.setCity(city);
        user.setCountry(country);
        user.setCountry_idx(country_idx);
        user.setHeight(height);
        user.setWeight(weight);
        user.setPassword(password);

        model.init(this.getApplication(), user);

        this.startActivity(new Intent(this, MainDrawerActivity.class));
    }
}