package com.example.aadpractica1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LeerActivity extends AppCompatActivity {
    private EditText etNombreArchivo;
    private TextView tvTexto;
    private Button btLeer;
    private Spinner sp;

    private static final String INTERN = "/storage/emulated/0/Android/data/com.example.aadpractica1/files/";
    private static final String PRIVATE = "/data/user/0/com.example.aadpractica1/files/";

    private String[] historial;
    private String nombreArchivoHistorial;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leer);

        initComponents();
        initEvents();

        historial = getIntent().getStringArrayExtra("nombre_archivos");
        sp.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, historial));
        Log.v("historico","leer "+historial);
    }

    //Declaracion de componentes
    private void initComponents(){
        etNombreArchivo = findViewById(R.id.etNombreArchivo);
        tvTexto = findViewById(R.id.tvTexto);
        btLeer = findViewById(R.id.btLeer);
        sp = findViewById(R.id.spinner);
    }

    private void initEvents(){
        btLeer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = etNombreArchivo.getText().toString();
                if(nombre.isEmpty()) {
                    Toast.makeText(LeerActivity.this, "Error, escriba un nombre al archivo", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(LeerActivity.this, "El archivo con el nombre: " + nombre + " se va a abrir", Toast.LENGTH_LONG).show();

                        readNotes();

                }
            }
        });
    }

    //creacion del menu y opciones del menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_leer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuLeer:
                if(etNombreArchivo.getText().toString().isEmpty()){
                    Toast.makeText(LeerActivity.this, "Error, escriba un nombre al archivo", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(LeerActivity.this, "Su archivo se ha abierto con el nombre: " + etNombreArchivo.getText().toString(), Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.menuBuscar:
                Toast.makeText(LeerActivity.this, "Aparece un buscar: ", Toast.LENGTH_SHORT).show();
                Intent buscar = new Intent(this, BuscarActivity.class);
                startActivity(buscar);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Leer archivos
    private void readNotes(){
        String texto, contenido ="";
        FileReader archivoLeer = null;

        try{
            archivoLeer = new FileReader(INTERN+etNombreArchivo.getText().toString()+".csv");
            Log.v("historico", archivoLeer.toString());
            BufferedReader br = new BufferedReader(archivoLeer);
            while((texto = br.readLine())!=null){
                contenido += texto;
            }
            tvTexto.setText(contenido);
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
            }
            tvTexto.setText(contenido);
            br.close();
        }catch (FileNotFoundException f){
            //Toast.makeText(LeerActivity.this, f.getMessage() , Toast.LENGTH_LONG).show();
        }catch (IOException e) {

        }

        if(contenido.compareTo("")==0){
            Toast.makeText(LeerActivity.this, "no se encuentra el archivo" , Toast.LENGTH_LONG).show();
            tvTexto.setText("Archivo no encontrado");
        }

    }
}
