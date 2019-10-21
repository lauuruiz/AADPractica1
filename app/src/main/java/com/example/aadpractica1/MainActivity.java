package com.example.aadpractica1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button btEmpezar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        initEvents();
    }

    private void initComponents(){
        btEmpezar = findViewById(R.id.btEmpezar);
    }

    private void initEvents(){
        btEmpezar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lanzarActividadEmpezar();
            }
        });
    }

    private void lanzarActividadEmpezar(){
        Intent i = new Intent(this, GuardarActivity.class);
        startActivity(i);
    }
}
