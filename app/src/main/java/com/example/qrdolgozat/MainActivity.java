package com.example.qrdolgozat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private Button btnScan,btnKiir;
    private TextView tvKiir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnScan = findViewById(R.id.btnScan);
        btnKiir = findViewById(R.id.btnKiir);
        tvKiir = findViewById(R.id.tvKiiras);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("QR Code Scanning by Me");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });

        btnKiir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String szoveges_adat;

                Date datum = Calendar.getInstance().getTime();
                //Dátum formátum felvétele
                SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY.MM.dd HH:mm:ss");
                //A dátumot átírjuk a megadott formában és eltároljuk
                String formazottDatum = dateFormat.format(datum);
                szoveges_adat = tvKiir.getText().toString()+","+ formazottDatum;

                //Fájlba íráshoz a tárolóhelynek elérhetőnek kell lennie
                String state = Environment.getExternalStorageState();
                if (state.equals(Environment.MEDIA_MOUNTED)) {
                    File file = new File(Environment.getExternalStorageDirectory(), "scannedCodes.csv");
                    BufferedWriter writer = null;
                    try {
                        writer = new BufferedWriter(new FileWriter(file, true));
                        writer.append(szoveges_adat);
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (result != null)
        {
            if (result.getContents() == null)
            {
                Toast.makeText(this, "Kiléptünk a scannelésből", Toast.LENGTH_SHORT).show();
            }else
            {
                tvKiir.setText("QR Code eredmény:" + result.getContents());
/*
                //ha van benne link akkor menjen rá
                Uri uri = Uri.parse(result.getContents());
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);*/
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
