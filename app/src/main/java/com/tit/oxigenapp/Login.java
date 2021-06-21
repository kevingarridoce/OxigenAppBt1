package com.tit.oxigenapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {
    EditText email, password;
    Button loginBtn, registerBtn;
    boolean valid = true;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        email = findViewById(R.id.email_txt);
        password = findViewById(R.id.password_txt);
        loginBtn = findViewById(R.id.login_btn);
        registerBtn = findViewById(R.id.register_btn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkField(email);
                checkField(password);
                Log.d("TAG","onClick: " + email.getText().toString());

                if (valid) {
                    fAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(Login.this,"Inicio de sesion exitoso.",Toast.LENGTH_SHORT).show();
                            checkUserAccessLevel(authResult.getUser().getUid());
                            //finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Login.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });
    }

    //Extraccion de la informacion del los documentos
    private void checkUserAccessLevel(String uid) {
        DocumentReference df = fStore.collection("Usuarios").document(uid);
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG","onSucces: " + documentSnapshot.getData());
                //Identificar el nivel de acceso del usuario
                //Llamada a la clases del doctor
                if (documentSnapshot.getString("isDoctor") !=null) {
                    //Usuario doctor
                    startActivity(new Intent(getApplicationContext(), Doctor.class));
                    finish();
                }
                //Identificar el nivel de acceso del usuario
                //Llamada a la clases del paciente
                if (documentSnapshot.getString("isPatient") !=null) {
                    startActivity(new Intent(getApplicationContext(), Patient.class));
                    finish();
                }
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

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            DocumentReference df = FirebaseFirestore.getInstance().collection("Usuarios").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.getString("isDoctor") != null) {
                        Toast.makeText(Login.this,"Inicio exitoso", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Doctor.class));
                        finish();
                    }
                    if (documentSnapshot.getString("isPatient") !=null) {
                        Toast.makeText(Login.this,"Inicio exitoso", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Patient.class));
                        finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(), Login.class));
                    finish();
                }
            });
        }
    }
}