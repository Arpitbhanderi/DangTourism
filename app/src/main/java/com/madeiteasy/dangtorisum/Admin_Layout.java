package com.madeiteasy.dangtorisum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Admin_Layout extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ImageView backbtn;
    ConstraintLayout edit_layout, add_data_layout;
    LinearLayout add_data_layer, Delete_data_layer, edit_data_layer;
    EditText insert_place_name, insert_City_name, insert_info, insert_stars, insert_image_url, insert_map_url, insert_website_url;
    Button add_image_btn, Submit_btn_for_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_layout);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        insert_place_name = findViewById(R.id.insert_place_name);
        insert_City_name = findViewById(R.id.insert_City_name);
        insert_info = findViewById(R.id.insert_info);
        insert_stars = findViewById(R.id.insert_stars);
        insert_image_url = findViewById(R.id.insert_image_url);
        insert_map_url = findViewById(R.id.insert_map_url);
        insert_website_url = findViewById(R.id.insert_website_url);

        add_image_btn = findViewById(R.id.add_image_btn);
        Submit_btn_for_data = findViewById(R.id.Submit_btn_for_data);

        Spinner spinner = findViewById(R.id.database_selector);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.numbers, R.layout.spinner_style);
        adapter.setDropDownViewResource(R.layout.spinner_style);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        backbtn = findViewById(R.id.back_press_btn1);
        edit_layout = findViewById(R.id.edit_layout);
        add_data_layout = findViewById(R.id.add_data_layout);
        add_data_layer = findViewById(R.id.add_data_layer);
        Delete_data_layer = findViewById(R.id.Delete_data_layer);
        edit_data_layer = findViewById(R.id.edit_data_layer);


        add_data_layer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_layout.setVisibility(View.GONE);
                add_data_layout.setVisibility(View.VISIBLE);
            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_layout.getVisibility() == View.VISIBLE) {
                    Admin_Layout.super.onBackPressed();
                } else {
                    edit_layout.setVisibility(View.VISIBLE);
                    add_data_layout.setVisibility(View.GONE);
                }
            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        try {
            String type = parent.getItemAtPosition(position).toString();
            senddata(type);
        } catch (Exception e) {
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void senddata(String type) {
        Submit_btn_for_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                Map<String, String> data = new HashMap<>();
                data.put("name", insert_place_name.getText().toString());
                data.put("city", insert_City_name.getText().toString());
                data.put("info", insert_info.getText().toString());
                data.put("rating", insert_stars.getText().toString());
                data.put("image_url", insert_image_url.getText().toString());
                data.put("map_url", insert_map_url.getText().toString());
                data.put("website_url", insert_website_url.getText().toString());

                try {
                    FirebaseFirestore.getInstance().collection(type).document(insert_place_name.getText().toString()).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(Admin_Layout.this, "Data Inserted", Toast.LENGTH_SHORT).show();
                            insert_place_name.setText("");
                            insert_City_name.setText("");
                            insert_info.setText("");
                            insert_stars.setText("");
                            insert_image_url.setText("");
                            insert_map_url.setText("");
                            insert_website_url.setText("");
                        }
                    });

                } catch (Exception e) {
                    Toast.makeText(Admin_Layout.this, "Something went wrong! Try Again.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


}