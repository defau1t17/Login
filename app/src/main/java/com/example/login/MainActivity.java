package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText getUserName;
    EditText getUserPassword;
    EditText getUserCountry;
    EditText getUserEmailAddress;
    AutoCompleteTextView autoCompleteTextView;
    TextInputLayout textInputLayout;
    Button confirmButton;

    int DB_VERSION = 1;

    Cursor cursor;
    SQLiteDatabase database;

    String userName;
    String userPassword;
    String userCountry;
    String userEmailAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getUserName = findViewById(R.id.userName_textField);
        getUserPassword = findViewById(R.id.userPassword_textField);
        getUserEmailAddress = findViewById(R.id.userEmail_textField);
        textInputLayout = findViewById(R.id.country_field);

        autoCompleteTextView = findViewById(R.id.userCountry_field);
        confirmButton = findViewById(R.id.confirmReg_button);


        // импортируйте в gradle  implementation 'com.google.android.material:material:1.1.0-alpha07'
        //функционал для стран
        ArrayList<String> countries = new ArrayList<>();
        String arr[] = new String[]{"dad", "sda"};
        for (int i = 0; i < arr.length; i++) {
            countries.add(arr[i]);
        }
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.list, R.id.list_1, countries);
        ((MaterialAutoCompleteTextView) textInputLayout.getEditText()).setAdapter(adapter);
        //


//подключение к бд
        try {
            SQLiteOpenHelper sqLiteOpenHelper = new User_DB(MainActivity.this, "CAR_USERS", null, DB_VERSION);
            database = sqLiteOpenHelper.getWritableDatabase();
            Log.i("TAG", "Database connected");
            cursor = database.query("USER", new String[]{"NAME"}, null, null, null, null, null);

        } catch (Exception exception) {
            Log.i("TAG", "Connection failed");
            exception.printStackTrace();
        }


//добавление в бд пользователя
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userName = getUserName.getText().toString().replaceAll(" ", "");
                userPassword = getUserPassword.getText().toString().replaceAll(" ", "");
                userEmailAddress = getUserEmailAddress.getText().toString().replaceAll(" ", "");
                userCountry = textInputLayout.getEditText().getText().toString();

                if (userName != "" && userPassword != "" && userEmailAddress != "" && userCountry != "") {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("NAME", userName);
                    contentValues.put("PASSWORD", userPassword);
                    contentValues.put("EMAIL", userEmailAddress);
                    contentValues.put("COUNTRY", userCountry);
                    long i = database.insert("USER", null, contentValues);

                    Log.i("TAG", "" + i);

//                    database.insert("USER", null, contentValues);

                    database.close();
                } else {
                    userName = "";
                    userPassword = "";
                    userCountry = "";
                    userEmailAddress = "";
                    Toast.makeText(getApplicationContext(), "....", Toast.LENGTH_SHORT).show();
                }

            }
            //смена активити
        });


    }
}