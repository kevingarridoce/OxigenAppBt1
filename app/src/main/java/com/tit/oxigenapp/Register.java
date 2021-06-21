package com.tit.oxigenapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    EditText completeName, email, phone, addres, password, date;
    Button registerBtn, loginBtn;
    boolean valid = true;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    Spinner spRol, spGenero;

    String [] arrayRol = {"Doctor", "Paciente"};
    String [] arrayGenero = {"Masculino", "Femenino"};

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        completeName = findViewById(R.id.name_register_txt);
        email = findViewById(R.id.email_register_txt);
        phone = findViewById(R.id.phone_register_txt);
        addres = findViewById(R.id.adrres_register_txt);
        password = findViewById(R.id.password_register_txt);
        date = findViewById(R.id.date_register_txt);
        spRol = findViewById(R.id.spinner_Rol);
        spGenero = findViewById(R.id.spinner_Genero);
        registerBtn =  findViewById(R.id.create_acount_btn);
        loginBtn =  findViewById(R.id.login_register_btn);

        spRol.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,arrayRol));
        spGenero.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,arrayGenero));

        spRol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Rol elegido: ",parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkField(completeName);
                checkField(email);
                checkField(phone);
                checkField(addres);
                checkField(password);
                checkField(date);

                //Proceso de registro a usuarios, entro en condicion que es valido
                if (valid) {
                    fAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseUser user = fAuth.getCurrentUser();
                            Toast.makeText(Register.this,"Cuenta Creada", Toast.LENGTH_SHORT).show();
                            DocumentReference df = fStore.collection("Usuarios").document(user.getUid());
                            Map<String,Object> usuarioInfo = new HashMap<>();
                            usuarioInfo.put("Nombre Completo",completeName.getText().toString());
                            usuarioInfo.put("Email",email.getText().toString());
                            String seleccionGenero = spGenero.getSelectedItem().toString();
                            if (seleccionGenero.equals("Masculino")) {
                                usuarioInfo.put("esMasculino","M");
                            }
                            if (seleccionGenero.equals("Femenino")) {
                                usuarioInfo.put("esFemenino","F");
                            }
                            usuarioInfo.put("Telefono",phone.getText().toString());
                            usuarioInfo.put("Direccion",addres.getText().toString());
                            usuarioInfo.put("Password",password.getText().toString());
                            usuarioInfo.put("Fecha de Nacimiento",date.getText().toString());

                            String seleccion = spRol.getSelectedItem().toString();
                            if (seleccion.equals("Doctor")) {
                                usuarioInfo.put("isDoctor","1");
                            }
                            if (seleccion.equals("Paciente")) {
                                usuarioInfo.put("isPatient","2");
                                usuarioInfo.put("Codigo",user.getUid());
                            }

                            df.set(usuarioInfo);

                            if (seleccion.equals("Doctor")) {
                                startActivity(new Intent(getApplicationContext(), Doctor.class));
                                finish();
                            }

                            if (seleccion.equals("Paciente")) {
                                startActivity(new Intent(getApplicationContext(), Patient.class));
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Register.this,"Error en creacion de cuenta", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Login.class));
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
}
