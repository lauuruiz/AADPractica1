package com.example.aadpractica1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuardarActivity extends AppCompatActivity {
    private Button btGuardar;
    private EditText etNombreArchivo;
    private RadioGroup rgb;

    private static final int NONE = -1;
    private static final int INTERN = 0;
    private static final int PRIVATE = 1;

    private List <Contacto> contactos = new ArrayList<Contacto>();
    private static final int ID_PERMISO_LEER_CONTACTOS = 1;

    private ArrayList<String> historial = new ArrayList<String>();

    private List<String> telefonos = new ArrayList<String>();
    private List<Contacto> contactosFinal = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardar);

        initComponents();
        initEvents();

        String[] historico = new String[historial.size()];
        for(int i = 0; i < historial.size(); i++){
            historico[i] = historial.get(i);
        }
    }

    //Declaracion de componentes
    private void initComponents(){
        btGuardar = findViewById(R.id.btEmpezar);
        etNombreArchivo = findViewById(R.id.etNombreArchivo);
        rgb = findViewById(R.id.rgb);
    }

    //Declaracion de los eventos
    private void initEvents(){
        btGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int type = GuardarActivity.getCheckedType(rgb.getCheckedRadioButtonId()); //Coge el valor del radio button pulsado
                String nombre = etNombreArchivo.getText().toString();
                if(nombre.isEmpty()) {
                    Toast.makeText(GuardarActivity.this, "Error, escriba un nombre al archivo", Toast.LENGTH_LONG).show();
                }else if(type == -1){
                    Toast.makeText(GuardarActivity.this, "Error, seleccione un lugar donde guardar", Toast.LENGTH_LONG).show();
                }else {
                    pedirPermisos(nombre, type);
                    /*Toast.makeText(GuardarActivity.this, "Su archivo se ha guardado con el nombre: " + nombre, Toast.LENGTH_LONG).show();
                    etNombreArchivo.setText(nombre.trim());
                    historial.add(nombre.trim());*/
                }
            }
        });
    }

    //comprobar donde guardar el archivo
    private static int getCheckedType(int item) { //Le pasamos el radio button pulsado
        int tipo = NONE;
        switch (item) {
            case R.id.rbInterno:
                tipo = INTERN;
                break;
            case R.id.rbPrivado:
                tipo = PRIVATE;
                break;
        }
        return tipo;
    }

    //creacion del menu y opciones del menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_guardar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuGuardar:
                if(etNombreArchivo.getText().toString().isEmpty()){
                    Toast.makeText(GuardarActivity.this, "Error, escriba un nombre al archivo", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(GuardarActivity.this, "Su archivo se ha guardado con el nombre: " + etNombreArchivo.getText().toString(), Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.menuLeer:
                Intent aLeer = new Intent(this, LeerActivity.class);
                String[] historico = new String[historial.size()];
                for(int i = 0; i < historial.size(); i++){
                    historico[i] = historial.get(i);
                }
                aLeer.putExtra("nombre_archivos", historico);
                startActivity(aLeer);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Obtener archivo
    private static File getFile(Context context, int type){
        File file = null;
        switch(type) {
            case INTERN:
                file = context.getExternalFilesDir(null);
                break;
            case PRIVATE:
                file = context.getFilesDir();
                break;
        }
        return file;
    }

    private File getFile(int type){
        return GuardarActivity.getFile(this, type);
    }

    //Escribir archivos
    private void escribirArchivo(String nombre, int type){
        File fileEscribir = new File(getFile(GuardarActivity.this, type),nombre+".csv");
        String value = obtenerListaContactos().toString();
        try{
            if(value != "[]") {
                FileWriter fw = new FileWriter(fileEscribir);
                fw.write(value);
                fw.flush();
                fw.close();
                Toast.makeText(GuardarActivity.this, "Su archivo se ha guardado con el nombre: " + nombre, Toast.LENGTH_LONG).show();
                etNombreArchivo.setText(nombre.trim());
                historial.add(nombre.trim());
            }else{
                Toast.makeText(GuardarActivity.this, "No hay contactos" , Toast.LENGTH_LONG).show();
            }
        }catch (IOException e){
            Toast.makeText(GuardarActivity.this, e.getMessage() , Toast.LENGTH_LONG).show();
        }
    }

    //Obtener contactos
    public List<Contacto> getListaContactos(){
        List<Contacto> listaContactos = new ArrayList<>();
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String proyeccion[] = null;
        String seleccion = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = ? and "+
                            ContactsContract.Contacts.HAS_PHONE_NUMBER + "= ?";
        String argumentos[] = new String[]{"1","1"};
        String orden = ContactsContract.Contacts.DISPLAY_NAME + " collate localized asc";
        Cursor cursor = getContentResolver().query(uri, proyeccion, seleccion, argumentos, orden);
        int indiceId = cursor.getColumnIndex(ContactsContract.Contacts.NAME_RAW_CONTACT_ID);
        int indiceNombre = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        int indiceTelefono = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);

        Contacto contacto;
        String telefono;
        /*telefono = Integer.toString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));*/
        while (cursor.moveToNext()) {
                contacto = new Contacto();
                contacto.setId(cursor.getLong(indiceId));
                contacto.setNombre(cursor.getString(indiceNombre));
                listaContactos.add(contacto);

        }
        return listaContactos;
    }

    public List<String> getListaTelefonos(long id){
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String proyeccion[] = null;
        String seleccion = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?";
        String argumentos[] = new String[]{id+""};
        String orden = ContactsContract.CommonDataKinds.Phone.NUMBER;
        Cursor cursor = getContentResolver().query(uri, proyeccion, seleccion, argumentos, orden);
        int indiceNumero = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        List<String> lista = new ArrayList<>();
        String numero = null;
        while(cursor.moveToNext()){
            numero = cursor.getString(indiceNumero);
            lista.add(numero);
        }
        return lista;
    }

    //Pedir permisos
    public void pedirPermisos(String nombre, int type){
        // AQUI SE COMPRUEBA SI LA APP TIENE PERMISOS PARA LO QUE SOLICITAMOS
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            // SI NO TUVIERA PERMISO LA APP VOLVERA A PEDIRLA
            // DEBERIA VOLVER A PREGUNTAR POR EL PERMISO
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)){
                Toast.makeText(this, "Lo necesito", Toast.LENGTH_LONG).show();
            }
            // 2º VEZ QUE LE PIDO PERMISO AL USUARIO
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, ID_PERMISO_LEER_CONTACTOS);
        }else {
            //Tengo permiso por lo que realizo la accion
            escribirArchivo(nombre.trim(), type);
        }
    }

    //Obtener lista de contactos
    private List<Contacto> obtenerListaContactos() {
        contactos = getListaContactos();
        long id;
        for(int i=0; i < contactos.size(); i++){
            id = contactos.get(i).getId();
            telefonos = getListaTelefonos(id);
            contactos.get(i).setTelefono(telefonos.toString());
        }
        return contactos;
    }
}
