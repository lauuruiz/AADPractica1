package com.example.aadpractica1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class BuscarActivity extends AppCompatActivity {

    private EditText etNombreArchivo, etBuscar;
    private TextView tvResultado;
    private Button btBuscar;

    private static final String INTERN = "/storage/emulated/0/Android/data/com.example.aadpractica1/files/";
    private static final String PRIVATE = "/data/user/0/com.example.aadpractica1/files/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar);
        initComponents();
        initEvents();
    }

    private void initComponents(){
        etNombreArchivo = findViewById(R.id.etNombreArchivo);
        etBuscar = findViewById(R.id.etBuscar);
        tvResultado = findViewById(R.id.tvResultado);
        btBuscar = findViewById(R.id.btBuscar);
    }

    private void initEvents(){
        btBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = etNombreArchivo.getText().toString();
                if(nombre.isEmpty()) {
                    Toast.makeText(BuscarActivity.this, "Error, escriba un nombre al archivo", Toast.LENGTH_LONG).show();
                }else {
                    readNotes();
                }
            }
        });
    }

    //Leer archivos
    private void readNotes(){
        String texto, contenido ="";
        FileReader archivoLeer = null;

        String buscar = etBuscar.getText().toString();
        try{
            archivoLeer = new FileReader(INTERN+etNombreArchivo.getText().toString()+".csv");
            BufferedReader br = new BufferedReader(archivoLeer);
            while((texto = br.readLine())!=null){
                contenido += texto;
                Log.v("historico", texto);

                String[] palabras = texto.split(" ");
                for (int i=0; i < palabras.length; i++){
                    if(palabras[i].compareTo(buscar)==0){
                        Log.v("historico", "bieeen");
                        tvResultado.setText(texto);
                        br.close();
                    }else {
                        Log.v("historico", "maaal");
                    }
                }
            }
            br.close();
        }catch (FileNotFoundException f){
            //Toast.makeText(LeerActivity.this, f.getMessage() , Toast.LENGTH_LONG).show();
        }catch (IOException e) {

        }

        try{
            archivoLeer = new FileReader(PRIVATE+etNombreArchivo.getText().toString()+".csv");
            BufferedReader br = new BufferedReader(archivoLeer);
            while((texto = br.readLine())!=null){
                contenido += texto;
                    String[] palabras = texto.split(" ");
                    for (int i=0; i < palabras.length; i++){
                        if(palabras[i].compareTo(buscar)==0){
                            Log.v("historico", "bieeen");
                            tvResultado.setText(texto);
                            br.close();
                        }else {
                            Log.v("historico", "maaal");
                        }
                    }
            }
            br.close();
        }catch (FileNotFoundException f){
            //Toast.makeText(LeerActivity.this, f.getMessage() , Toast.LENGTH_LONG).show();
        }catch (IOException e) {

        }

        if(contenido.compareTo("")==0){
            Toast.makeText(BuscarActivity.this, "no se encuentra el archivo" , Toast.LENGTH_LONG).show();
            tvResultado.setText("Archivo no encontrado");
        }

    }
}
