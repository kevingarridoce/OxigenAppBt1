package com.tit.oxigenapp;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class Dispositivos_Encontrados extends ListActivity {

    private ArrayAdapter<String> mArrayAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothSocket btSocket; //permite el envio de la informacion y viceversa
    private ArrayList<BluetoothDevice> btDeviceArray = new ArrayList<BluetoothDevice>();
    private ConnectAsyncTask connectAsyncTask;
    private BluetoothAdapter mBTAdapter;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private Button conectarDis;
    Button regresarBtn;
    TextView spo2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispositivos_encontrados);
        mBTAdapter = BluetoothAdapter.getDefaultAdapter();

        regresarBtn = findViewById(R.id.Regresar_Dis_Btn);
        spo2 = findViewById(R.id.textView14);

        //inicia BT
        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        setListAdapter(mArrayAdapter);

        // Instance AsyncTask
        connectAsyncTask = new ConnectAsyncTask();

        //Get Bluettoth Adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Check smartphone support Bluetooth
        if(mBluetoothAdapter == null){
            //Device does not support Bluetooth
            Toast.makeText(getApplicationContext(), "Not support bluetooth", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Check Bluetooth enabled
        if(!mBluetoothAdapter.isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }

        // Queryng paried devices
        Set<BluetoothDevice> pariedDevices = mBluetoothAdapter.getBondedDevices();
        if(pariedDevices.size() > 0){
            for(BluetoothDevice device : pariedDevices){
                if (device.getName().equals("HC-05")) {
                    mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                    btDeviceArray.add(device);
                }
            }
        }

        conectarDis = findViewById(R.id.Conectar_Dis_Btn);
        conectarDis.setOnClickListener(conectar);

        regresarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Patient.class));
                finish();
            }
        });
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        BluetoothDevice device = btDeviceArray.get(position);
        connectAsyncTask.execute(device);
        Toast.makeText(getApplicationContext(),"Conectado",Toast.LENGTH_SHORT).show();
    }

    private View.OnClickListener conectar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getApplicationContext(),"Conexion en proceso",Toast.LENGTH_SHORT).show();
            OutputStream mmOutStream = null;
            InputStream intputStream = null;

            try {
                if(btSocket.isConnected()){
                    mmOutStream = btSocket.getOutputStream();
                    mmOutStream.write(new String("1").getBytes());
                }
            } catch (IOException e) { }

            try {
                intputStream = btSocket.getInputStream();
                //intputStream.skip(intputStream.available());
                final byte delimiter = 10;//Codigo ASCII de nueva linea
                int readBufferPosition = 0;
                byte[] readBuffer = new byte[1024];
                int bytesAvailable = intputStream.available();
                byte[] packetBytes = new byte[bytesAvailable];
                byte a = (byte) intputStream.read(packetBytes);
                String total = null;
                int totalNum = 0;
                //byte b = (byte) intputStream.read();
                //System.out.println((char) b);
                if (bytesAvailable > 0) {
                    System.out.println("Nuevo Valor");
                    int i2 = 0;
                    for(int i=0; i<bytesAvailable; i++) {
                        byte b = packetBytes[i];
                        i2++;
                        //System.out.println((char) b);
                        if (i2 < 2) {
                            byte c = packetBytes[i+1];
                            System.out.println("Esto es B: " + (char) b);
                            System.out.println("Esto es c: " + (char) c);
                            //System.out.println("Total: " + ((char) b) + ((char) c));
                            total = Character.toString((char) b) + Character.toString((char) c);
                        }
                    }
                    totalNum = Integer.parseInt(total);
                    System.out.println(totalNum);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private class ConnectAsyncTask extends AsyncTask<BluetoothDevice, Integer, BluetoothSocket> {
        private BluetoothSocket mmSocket;
        private BluetoothDevice mmDevice;

        @Override
        protected BluetoothSocket doInBackground(BluetoothDevice... device) {
            mmDevice = device[0];

            try {
                String mmUUID = "00001101-0000-1000-8000-00805F9B34FB";
                mmSocket = mmDevice.createInsecureRfcommSocketToServiceRecord(UUID.fromString(mmUUID));
                mmSocket.connect();

            } catch (Exception e) { }

            return mmSocket;
        }

        @Override
        protected void onPostExecute(BluetoothSocket result) {
            btSocket = result;
        }
    }
}