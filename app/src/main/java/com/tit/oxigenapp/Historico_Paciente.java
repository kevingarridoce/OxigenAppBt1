package com.tit.oxigenapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Historico_Paciente extends AppCompatActivity {
    Button regresarBtn, buscarFecha;;
    FirebaseFirestore fstore;
    FirebaseAuth fAuth;
    private String idUser;
    RecyclerView recyclerViewHisorico;
    historicoAdapter mAdapter,m2Adapter;
    FirebaseFirestore mFirestore,m2Firestore;
    private int nYear, nMonth, nDay,sYear,sMonth, sDay, n2Year, n2Month, n2Day;
    static final int DATE_ID = 0,DATE_ID2=1;
    Calendar C= Calendar.getInstance();
    EditText t1,t2;
    int validacion;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_paciente);

        regresarBtn =  findViewById(R.id.regresar_btn);

        fAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = fAuth.getCurrentUser();
        fstore = FirebaseFirestore.getInstance();
        idUser = fAuth.getCurrentUser().getUid();

        recyclerViewHisorico= findViewById(R.id.recyclerHistorico);
        recyclerViewHisorico.setLayoutManager(new LinearLayoutManager(this ));
        mFirestore = FirebaseFirestore.getInstance();
        m2Firestore = FirebaseFirestore.getInstance();
        Date fecha = C.getTime();

        Query query = mFirestore.collection("Usuarios").document(idUser).collection("spo2").orderBy("fecha", Query.Direction.DESCENDING);
        //Log.d( "Paciente",fecha.toString());

        FirestoreRecyclerOptions<historico> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<historico>().setQuery(query, historico.class).build();
        mAdapter = new historicoAdapter(firestoreRecyclerOptions);
        Log.d( "Datos",mAdapter.toString());
        mAdapter.notifyDataSetChanged();
        recyclerViewHisorico.setAdapter(mAdapter);


        sMonth=C.get(Calendar.MONTH)+1;
        sDay=C.get(Calendar.DAY_OF_MONTH);
        sYear=C.get(Calendar.YEAR);
        buscarFecha=findViewById(R.id.ButtonBuscarFechaHistorico);
        t1= (EditText)findViewById(R.id.editTextFechaHistorico);
        t1.setText(sDay + "/" + sMonth  + "/" + sYear);
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialog(DATE_ID);

                validacion=1;
            }
        });
        t2= (EditText)findViewById(R.id.editTextFechaHistorico2);
        t2.setText(sDay + "/" + sMonth  + "/" + sYear);
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialog(DATE_ID2);

                validacion=1;
            }
        });









        regresarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Patient.class));
            }
        });



        buscarFecha.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                if (validacion == 1) {
                    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                    try {

                        Date desde=formato.parse(t1.getText().toString());
                        Date hasta=formato.parse(t2.getText().toString());

                        if(hasta.after(desde)) {
                            Log.d("Desde", desde.toString());
                            Log.d("Hasta", hasta.toString());


                            m2Firestore = FirebaseFirestore.getInstance();

                            Query query = m2Firestore.collection("Usuarios").document(idUser).collection("spo2").whereLessThan("fecha", hasta).whereGreaterThan("fecha", desde).orderBy("fecha", Query.Direction.DESCENDING);

                            // String dia = new SimpleDateFormat("dd").format(subject.getTime());
                            FirestoreRecyclerOptions<historico> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<historico>().setQuery(query, historico.class).build();
                            m2Adapter = new historicoAdapter(firestoreRecyclerOptions);
                            m2Adapter.notifyDataSetChanged();
                            recyclerViewHisorico.setAdapter(m2Adapter);
                            if (validacion == 1) {
                                m2Adapter.startListening();
                            }
                        }
                        else {
                            Toast.makeText(Historico_Paciente.this,"Ingrese datos correctos",Toast.LENGTH_SHORT).show();
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                }
            }
        });






    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
       // if(validacion==1){
          //  m2Adapter.stopListening();
        //}
    }







    private void colocar_fecha() {
        t1.setText((nDay    ) + "/" +(nMonth+1)  + "/" + nYear);

    }

    private void colocar_fecha2() {
        t2.setText(( n2Day  ) + "/" + (n2Month +1) + "/" + n2Year);

    }
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    nYear = year;
                    nMonth = monthOfYear;
                    nDay = dayOfMonth;
                    colocar_fecha();

                }

            };
    private DatePickerDialog.OnDateSetListener mDateSetListener2 =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    n2Year = year;
                    n2Month = monthOfYear;
                    n2Day = dayOfMonth;
                    colocar_fecha2();

                }

            };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 0:
                return new DatePickerDialog(this, mDateSetListener, sYear, sMonth-1, sDay);
            case 1:
                return new DatePickerDialog(this, mDateSetListener2, sYear, sMonth-1, sDay);


        }


        return null;
    }


}