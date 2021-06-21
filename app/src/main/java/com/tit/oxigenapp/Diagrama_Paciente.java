package com.tit.oxigenapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.time.format.DateTimeFormatter;

public class Diagrama_Paciente extends AppCompatActivity {
    Button regresarBtn, buscarFecha;
    LineChart lineChart;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    private String idUser;
    private int nYear, nMonth, nDay,sYear,sMonth, sDay;
    static final int DATE_ID = 0;
    Calendar C= Calendar.getInstance();
    EditText t1;
    int validacion;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagrama_paciente);

        regresarBtn = findViewById(R.id.regresar_diag_btn);


        sMonth=C.get(Calendar.MONTH)+1;
        sDay=C.get(Calendar.DAY_OF_MONTH);
        sYear=C.get(Calendar.YEAR);
        buscarFecha=findViewById(R.id.ButtonBuscarFecha);
        t1= (EditText)findViewById(R.id.editTextFechaDiagrama);
        t1.setText(sMonth + "-" + sDay  + "-" + sYear);
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialog(DATE_ID);

                validacion=1;
            }
        });


        fStore = FirebaseFirestore.getInstance();


        fAuth = FirebaseAuth.getInstance();

        idUser = fAuth.getCurrentUser().getUid();

        CollectionReference pacienteRef = fStore.collection("Usuarios").document(idUser).collection("spo2");
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, porcentaje);
        //  adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //sp_Paciente.setAdapter(adapter);
        pacienteRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<Entry> porcentaje = new ArrayList<Entry>();
                ArrayList<Entry> porcentaje2 = new ArrayList<Entry>();
                //porcentaje2.add(new Entry(0,0));
                int numero = 0;
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Date subject = document.getDate("fecha");
                        double subject2 = document.getDouble("porcentaje");
                        String timeStamp = new SimpleDateFormat("kk").format(subject.getTime());
                        String timeStamp2 = new SimpleDateFormat("MM-dd-yyyy").format(subject.getTime());
                        // if(timeStamp2.equals(t1.getText().toString())){

                        String dia = new SimpleDateFormat("dd").format(subject.getTime());
                        String mes = new SimpleDateFormat("MM").format(subject.getTime());
                        String anio = new SimpleDateFormat("yyyy").format(subject.getTime());
                        int f1 = Integer.parseInt(dia);
                        int f3 = Integer.parseInt(mes);
                        int f4 = Integer.parseInt(anio);
                        int f = Integer.parseInt(timeStamp)  ;
                        int f2 = (int) subject2;
                        // Log.d( "Paciente",f1+ " => "+nDay + " => "+ f3+ " => "+nMonth+ " => "+f4+ " => "+nYear);
                        if (f1 == sDay && f3 == sMonth && f4 == sYear) {
                            if(f==24){
                                f=0;
                            }

                            porcentaje.add(new Entry((f), (f2)));
                            //Log.d("Paciente", f + " => " + f2);
                            numero = 1;
                        } else {
                            numero = 0;
                        }


                    }
                    lineChart = findViewById(R.id.lineGrafica);
                    if(numero==1) {
                        LineDataSet lineDataSet = new LineDataSet(porcentaje, "SPO2");
                        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                        lineDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                        dataSets.add(lineDataSet);

                        LineData data = new LineData(dataSets);
                        lineChart.setData(data);
                        lineChart.invalidate();
                    }
                    else{
                        t1.setText(sMonth + "-" + sDay  + "-" + sYear);
                        Toast.makeText(Diagrama_Paciente.this,"No hay datos para esa fecha", Toast.LENGTH_SHORT).show();
                        numero=0;

                    }

                }

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

                    ArrayList<Entry> porcentaje = new ArrayList<Entry>();
                    ArrayList<Entry> porcentaje2 = new ArrayList<Entry>();
                    fStore = FirebaseFirestore.getInstance();


                    fAuth = FirebaseAuth.getInstance();

                    idUser = fAuth.getCurrentUser().getUid();

                    CollectionReference pacienteRef = fStore.collection("Usuarios").document(idUser).collection("spo2");

                    pacienteRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {

                                int numero=0;
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Date subject = document.getDate("fecha");
                                    double subject2 = document.getDouble("porcentaje");
                                    String timeStamp = new SimpleDateFormat("kk").format(subject.getTime());
                                    String timeStamp2 = new SimpleDateFormat("MM-dd-yyyy").format(subject.getTime());
                                    // if(timeStamp2.equals(t1.getText().toString())){

                                    String dia = new SimpleDateFormat("dd").format(subject.getTime());
                                    String mes = new SimpleDateFormat("MM").format(subject.getTime());
                                    String anio = new SimpleDateFormat("yyyy").format(subject.getTime());
                                    int f1 = Integer.parseInt(dia);
                                    int f3 = Integer.parseInt(mes);
                                    int f4 = Integer.parseInt(anio);
                                    int f = Integer.parseInt(timeStamp) ;
                                    int f2 = (int) subject2;
                                    if (f1 == nDay && f3 == nMonth + 1 && f4 == nYear) {

                                        if(f==24){
                                            f=0;
                                        }
                                        porcentaje.add(new Entry((f), (f2)));
                                        Log.d("Paciente", f + " "+f2);
                                        numero=1;
                                    }


                                }

                                lineChart = findViewById(R.id.lineGrafica);
                                if(numero==1) {
                                    LineDataSet lineDataSet = new LineDataSet(porcentaje, "SPO2");
                                    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                                    lineDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                                    dataSets.add(lineDataSet);

                                    LineData data = new LineData(dataSets);
                                    lineChart.setData(data);
                                    lineChart.invalidate();
                                }
                                else{
                                    t1.setText(sMonth + "-" + sDay  + "-" + sYear);
                                    Toast.makeText(Diagrama_Paciente.this,"No hay datos para esa fecha", Toast.LENGTH_SHORT).show();
                                    numero=0;

                                }

                            }


                        }
                    });


                }
            }
        });
    }



    private void colocar_fecha() {
        t1.setText((nMonth +1   ) + "-" + nDay + "-" + nYear);

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

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_ID:
                return new DatePickerDialog(this, mDateSetListener, sYear, sMonth-1, sDay);


        }


        return null;
    }
}