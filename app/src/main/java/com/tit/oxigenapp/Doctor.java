package com.tit.oxigenapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Doctor extends AppCompatActivity {
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    private String idUser;
    Spinner sp_Paciente = null;
    Button NuevoPaciente, InformacionPaciente, historico, medicacion, diagrama;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        fStore = FirebaseFirestore.getInstance();

        sp_Paciente = findViewById(R.id.spinner_Paciente);
        NuevoPaciente = findViewById(R.id.buttonAgregarPaciente);
        InformacionPaciente = findViewById(R.id.button_informacion);
        historico = findViewById(R.id.button_historico_paciente);
        medicacion = findViewById(R.id.button_medicacion);
        diagrama=findViewById(R.id.button_diagrama_paciente);

        //Lamada de la funcion de carga de paciente
        carga_Paciente();

        NuevoPaciente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Ingreso_Paciente.class));
            }
        });
    }

    //Cargar Paciente
    public void carga_Paciente () {
        List<String> usuarios = new ArrayList<>();
        fAuth = FirebaseAuth.getInstance();

        idUser = fAuth.getCurrentUser().getUid();

        CollectionReference pacienteRef = fStore.collection("Usuarios").document(idUser).collection("Pacientes");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, usuarios);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_Paciente.setAdapter(adapter);

        pacienteRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String subject = document.getString("Nombre Completo");
                        usuarios.add(subject);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

        InformacionPaciente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sp_Paciente.getSelectedItem() != null) {
                    String datos = sp_Paciente.getSelectedItem().toString();
                    CollectionReference pacienteRef = fStore.collection("Usuarios").document(idUser).collection("Pacientes");
                    pacienteRef.whereEqualTo("Nombre Completo", datos).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d( "Paciente",document.getId() + " => " + document.getData());
                                    String datos = document.getString("Codigo Paciente").toString();

                                    Bundle parmetros = new Bundle();
                                    parmetros.putString("datos", datos);
                                    Intent i = new Intent(getApplicationContext(), Informacion_Paciente_Doctor.class);
                                    i.putExtras(parmetros);
                                    startActivity(i);
                                }
                            } else {
                                Log.d("Paciente", "Error getting documents: ", task.getException());
                            }
                        }
                    });
                } else {
                    Toast.makeText(Doctor.this,"Debe agregar un nuevo paciente para ver la informacion.",Toast.LENGTH_SHORT).show();
                }

            }
        });

        historico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sp_Paciente.getSelectedItem() != null) {
                    String datos =sp_Paciente.getSelectedItem().toString();
                    CollectionReference pacienteRef = fStore.collection("Usuarios").document(idUser).collection("Pacientes");
                    String datos2;
                    pacienteRef.whereEqualTo("Nombre Completo", datos).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d( "Paciente",document.getId() + " => " + document.getData());
                                    String datos=document.getString("Codigo Paciente").toString();

                                    Bundle parmetros = new Bundle();
                                    parmetros.putString("datos", datos);
                                    Intent i = new Intent(getApplicationContext(), Historico_Paciente_Doctor.class);
                                    i.putExtras(parmetros);
                                    startActivity(i);
                                }
                            } else {
                                Log.d("Paciente", "Error getting documents: ", task.getException());
                            }
                        }
                    });
                } else {
                    Toast.makeText(Doctor.this,"Debe agregar un nuevo paciente para ver el historico.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        medicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sp_Paciente.getSelectedItem() != null) {
                    String datos = sp_Paciente.getSelectedItem().toString();
                    CollectionReference pacienteRef = fStore.collection("Usuarios").document(idUser).collection("Pacientes");
                    pacienteRef.whereEqualTo("Nombre Completo", datos).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d( "Paciente",document.getId() + " => " + document.getData());
                                    String datos = document.getId().toString();

                                    Bundle parmetros = new Bundle();
                                    parmetros.putString("datos3", datos);
                                    Intent i = new Intent(getApplicationContext(), Medicacion_Doctor.class);
                                    i.putExtras(parmetros);
                                    startActivity(i);
                                }
                            } else {
                                Log.d("Paciente", "Error getting documents: ", task.getException());
                            }
                        }
                    });
                } else {
                    Toast.makeText(Doctor.this,"Debe agregar un nuevo paciente para ver la medicacion.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        diagrama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sp_Paciente.getSelectedItem() != null) {
                    String datos =sp_Paciente.getSelectedItem().toString();
                    CollectionReference pacienteRef = fStore.collection("Usuarios").document(idUser).collection("Pacientes");
                    String datos2;
                    pacienteRef.whereEqualTo("Nombre Completo", datos).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d( "Paciente",document.getId() + " => " + document.getData());
                                    String datos=document.getString("Codigo Paciente").toString();

                                    Bundle parmetros = new Bundle();
                                    parmetros.putString("datos", datos);
                                    Intent i = new Intent(getApplicationContext(), Diagrama_Paciente_Doctor.class);
                                    i.putExtras(parmetros);
                                    startActivity(i);
                                }
                            } else {
                                Log.d("Paciente", "Error getting documents: ", task.getException());

                            }
                        }
                    });
                } else {
                    Toast.makeText(Doctor.this,"Debe agregar un nuevo paciente para ver el diagrama.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void logoutAdmin(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }
}