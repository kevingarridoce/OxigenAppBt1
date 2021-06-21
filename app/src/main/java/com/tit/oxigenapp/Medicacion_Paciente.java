package com.tit.oxigenapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

public class Medicacion_Paciente extends AppCompatActivity {
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    TextView medicacionPac, nomDoctor;
    Button regresarBtn;
    private String idUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicacion_paciente);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        idUser = fAuth.getCurrentUser().getUid();

        medicacionPac = findViewById(R.id.medicacion_txt);
        nomDoctor = findViewById(R.id.nom_doctor_txt);
        regresarBtn = findViewById(R.id.regresar_doc_btn);

        //obtenerDoctor();

        /*DocumentReference pacienteStore = fStore.collection("Usuarios").document(idUser);
        pacienteStore.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String codigo = value.getString("Codigo Doctor");
                if (codigo != null) {
                    DocumentReference docStore = fStore.collection("Usuarios").document(codigo);
                    docStore.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            nomDoctor.setText(value.getString("Nombre Completo"));
                        }
                    });
                } else {
                    nomDoctor.setText("No tiene un Doctor asignado.");
                }
            }
        });*/

        obtenerDatos();

        regresarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Patient.class));
                finish();
            }
        });
    }

    private void obtenerDatos() {
        DocumentReference pacienteStore = fStore.collection("Usuarios").document(idUser);
        pacienteStore.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String codigo = value.getString("Codigo Doctor");
                String codigoPac = value.getString("Codigo");
                if (codigo != null) {
                    /*DocumentReference docStore = fStore.collection("Usuarios").document(codigo);
                    docStore.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            nomDoctor.setText(value.getString("Nombre Completo"));
                        }
                    });*/
                    CollectionReference comparacion = fStore.collection("Usuarios").document(codigo).collection("Pacientes");
                    Log.d( "Paciente",codigoPac);
                    Log.d( "Doctor",codigo);
                    DocumentReference pacienteStore = fStore.collection("Usuarios").document(codigo);
                    //nomDoctor.setText(pacienteStore);
                    comparacion.whereEqualTo("Codigo Paciente",codigoPac).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    medicacionPac.setText(document.getString("Medicacion"));
                                }
                            } else {
                                Log.d("Paciente", "Error getting documents: ", task.getException());
                            }
                        }
                    });
                } else {
                    medicacionPac.setText("Espere a la asignacion de Doctor.");
                }
            }
        });
    }

    /*private void obtenerDoctor () {
        DocumentReference pacienteStore = fStore.collection("Usuarios").document(idUser);
        pacienteStore.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String codigo = value.getString("Codigo Doctor");
                if (codigo != null) {
                    DocumentReference docStore = fStore.collection("Usuarios").document(codigo);
                    docStore.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            nomDoctor.setText(value.getString("Nombre Completo"));
                        }
                    });
                } else {
                    nomDoctor.setText("No tiene un Doctor asignado.");
                }
            }
        });
    }*/
}
