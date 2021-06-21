package com.tit.oxigenapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class Medicacion_Doctor extends AppCompatActivity {
    EditText medicacion;
    TextView nom_paciente;
    Button ingresar_mediBtn, actualizar_mediBtn, regresarBtn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    private String idUser, idUser2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicacion_doctor);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        idUser = fAuth.getCurrentUser().getUid();

        medicacion = findViewById(R.id.medicacion_doc_txt);
        nom_paciente = findViewById(R.id.nom_paciente_doc_txt);
        ingresar_mediBtn = findViewById(R.id.ing_medi_doc_btn);
        //actualizar_mediBtn = findViewById(R.id.act_medi_doctor_btn);
        regresarBtn = findViewById(R.id.regresar_doc_btn);

        Bundle parametros = this.getIntent().getExtras();
        String datos = parametros.getString("datos3");
        idUser2 = datos;

        obtenerDatos();
        Log.d( "Paciente",idUser + " => " + idUser2);

        ingresar_mediBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference df = fStore.collection("Usuarios").document(idUser).collection("Pacientes").document(idUser2);
                Map<String, Object> map = new HashMap<>();
                map.put("Medicacion",medicacion.getText().toString());
                if (medicacion.getText().toString() != null) {
                    df.update(map);
                } else {
                    df.set(map);
                }
            }
        });

        regresarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Doctor.class));
            }
        });
    }

    private void obtenerDatos() {
        DocumentReference documentReference = fStore.collection("Usuarios").document(idUser).collection("Pacientes").document(idUser2);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                nom_paciente.setText(documentSnapshot.getString("Nombre Completo"));
                medicacion.setText(documentSnapshot.getString("Medicacion"));
                String validar_Medicacion = documentSnapshot.getString("Medicacion");
                if (validar_Medicacion != null) {
                    ingresar_mediBtn.setText("Actualizar medicamento");
                } else {
                    ingresar_mediBtn.setText("Ingresar medicamento");
                }
            }
        });
    }
}
