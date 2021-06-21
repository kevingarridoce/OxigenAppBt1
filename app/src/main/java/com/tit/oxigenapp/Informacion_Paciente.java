package com.tit.oxigenapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class Informacion_Paciente extends AppCompatActivity {
    TextView email, prueba;
    EditText nombre_txt, dir_txt, tel_txt, id;
    FirebaseFirestore fstore;
    FirebaseAuth fAuth;
    private String idUser;
    Button regresarBtn, actualizarBtn;

    //DocumentReference documentReference = fstore.collection("Usuarios").document(idUser);
    //final FirebaseUser user = fAuth.getCurrentUser();

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_paciente);

        email = findViewById(R.id.doctor_info_correo_txt);
        regresarBtn = findViewById(R.id.doctor_info_regresar_button);
        id = findViewById(R.id.doctor_id_editTxt);
        nombre_txt = findViewById(R.id.doctor_nombre_info_txt);
        dir_txt = findViewById(R.id.doctor_dir_info_txt);
        tel_txt = findViewById(R.id.doctor_tel_info_txt);
        actualizarBtn = findViewById(R.id.actualizar_btn);

        fAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = fAuth.getCurrentUser();
        fstore = FirebaseFirestore.getInstance();
        idUser = fAuth.getCurrentUser().getUid();

        //Mostrar Informacion
        email.setText(user.getEmail());
        id.setText(idUser);

        //Clase para obtener datos
        obtenerDatos();

        //Boton Actualizar
        actualizarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference df = fstore.collection("Usuarios").document(idUser);
                Map<String, Object> map = new HashMap<>();
                map.put("Nombre Completo",nombre_txt.getText().toString());
                map.put("Direccion",dir_txt.getText().toString());
                map.put("Telefono",tel_txt.getText().toString());
                df.update(map);
            }
        });

        regresarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Patient.class));
            }
        });
    }

    //Acceder a la documentacion
    private void obtenerDatos() {
        DocumentReference documentReference = fstore.collection("Usuarios").document(idUser);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                nombre_txt.setText(documentSnapshot.getString("Nombre Completo"));
                dir_txt.setText(documentSnapshot.getString("Direccion"));
                tel_txt.setText(documentSnapshot.getString("Telefono"));
            }
        });
    }
}
