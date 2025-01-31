package com.example.holamundo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends Activity {
    TextView tempVal;
    Spinner spn;
    Button btn;
    String[][] etiquetas = new String[][]{
            new String[]{"Dolar", "Euro", "Quetzalez", "Lempiras", "Cordobas", "Pesos CR"}, //MOnedas
            new String[]{"Dia", "Segundo", "Minutos", "Hora", "Semana", "Mes", "Año"}, //Tiempo
            new String[]{"GB", "Bit", "Byte", "KB", "MB", "TB"}, //Almacenamiento
            new String[]{"Kilometro", "Metro", "Centimetro", "Milimetro", "Micrometro",
                    "Nanometro", "Milla", "Yarda", "Pie", "Pulgada", "Milla Nautica"}, //Longitud
    };
    double[][] valores = new double[][]{
            new double[]{1, 0.96, 7.73, 25.60, 36.78, 506.78},
            new double[]{1, 86400, 1440, 24, 0.142857, 0.0328767, 0.00273973},
            new double[]{1, 8e+9, 1e+9, 1e+6, 1000, 0.001},
            new double[]{1, 1000, 100000, 1000000, 1000000000, 1000000000000.0,
                    0.621371, 1093.61, 3280.84, 39370.1, 0.539957}
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            spn = findViewById(R.id.cboTipoCOnversor);
            spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ArrayAdapter<String> aaEtiquetas = new ArrayAdapter<>(MainActivity.this,
                            android.R.layout.simple_spinner_item, etiquetas[position]);
                    spn = findViewById(R.id.cboa);
                    spn.setAdapter(aaEtiquetas);

                    spn = findViewById(R.id.cboDe);
                    spn.setAdapter(aaEtiquetas);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            btn = findViewById(R.id.btnConvertir);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    spn = findViewById(R.id.cboTipoCOnversor);
                    int tipo = spn.getSelectedItemPosition();

                    spn = findViewById(R.id.cboDe);
                    int de = spn.getSelectedItemPosition();

                    spn = findViewById(R.id.cboa);
                    int a = spn.getSelectedItemPosition();

                    tempVal = findViewById(R.id.txtCantidad);
                    double cantidad = Double.parseDouble(tempVal.getText().toString());

                    double respuesta = convertir(de, a, cantidad, tipo);

                    tempVal = findViewById(R.id.lblrespuesta);
                    tempVal.setText("Respuesta: "+ respuesta);
                }
            });
        }catch (Exception e){
            Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    Double convertir (int de, int a, double cantidad, int tipo){
        return valores[tipo][a] / valores[tipo][de] * cantidad;
    }
}