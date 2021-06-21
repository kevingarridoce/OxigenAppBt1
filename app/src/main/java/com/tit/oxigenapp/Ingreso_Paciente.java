package com.tit.oxigenapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class Ingreso_Paciente extends AppCompatActivity {

    Button AgregarPaciente, RegresarDoctor;
    EditText TxtCodPaciente;
    TextView TxtNombrePaciente;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    boolean valid = true;
    String NombreCompleto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso_paciente);
        AgregarPaciente = findViewById(R.id.buttonAgregarPaciente);
        TxtCodPaciente = findViewById(R.id.cod_paciente_txt);
        RegresarDoctor = findViewById(R.id.regresa_doctor_btn);
        TxtNombrePaciente=findViewById(R.id.nom_paciente_txt);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        AgregarPaciente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkField(TxtCodPaciente);
                obtenerDatos();
            }
        });

        RegresarDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Doctor.class));
            }
        });
    }

    public boolean checkField (EditText textField) {
        if (textField.getText().toString().isEmpty()) {
            textField.setError("Error");
            valid = false;
        } else {
            valid = true;
        }
        return valid;
    }

    private void obtenerDatos() {
        DocumentReference pacienteRef = fStore.collection("Usuarios").document(TxtCodPaciente.getText().toString());
        FirebaseUser user = fAuth.getCurrentUser();
        DocumentReference df = fStore.collection("Usuarios").document(user.getUid()).collection("Pacientes").document();

        pacienteRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                TxtNombrePaciente.setText(documentSnapshot.getString("Nombre Completo"));
                NombreCompleto = documentSnapshot.getString("Nombre Completo");

                String validar_Doctor = documentSnapshot.getString("Codigo Doctor");
                if (validar_Doctor == null && NombreCompleto != null) {
                    Toast.makeText(Ingreso_Paciente.this,"Paciente Agregado", Toast.LENGTH_SHORT).show();
                    Map<String,Object> usuarioInfo = new HashMap<>();
                    usuarioInfo.put("Codigo Paciente",TxtCodPaciente.getText().toString());
                    usuarioInfo.put("Nombre Completo",NombreCompleto);
                    df.set(usuarioInfo);

                    String codigo = TxtCodPaciente.getText().toString();
                    DocumentReference df2 = fStore.collection("Usuarios").document(codigo);
                    Map<String,Object> usuarioInfo2 = new HashMap<>();
                    usuarioInfo2.put("Codigo Doctor",user.getUid());
                    df2.update(usuarioInfo2);
                    Log.d("Paciente", "Agrego paciente");
                } else {
                    Toast.makeText(Ingreso_Paciente.this,"Error al agregar paciente", Toast.LENGTH_SHORT).show();
                    Log.d("Paciente", "Error getting documents");
                }
            }
        });
    }
}
