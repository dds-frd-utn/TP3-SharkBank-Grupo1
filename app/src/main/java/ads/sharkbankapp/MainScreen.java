package ads.sharkbankapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.IOException;

public class MainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        Intent intent = getIntent();
        final Integer idCliente = intent.getIntExtra(LoginScreen.EXTRA_IDCLIENTE, 0);
        final String nombreCliente = intent.getStringExtra(LoginScreen.EXTRA_NOMBRECLIENTE);

        final Button saldoButton = findViewById(R.id.saldoButton);
        final LinearLayout saldoLayout = findViewById(R.id.saldoLayout);
        final Button cuentaConsultarButton = findViewById(R.id.cuentaConsultarButton);
        final EditText cuentaConsultarField = findViewById(R.id.cuentaConsultarField);
        final TextView respuestaText = findViewById(R.id.respuestaText);

        final Button ultimosButton = findViewById(R.id.ultimosButton);
        final TableLayout ultimosLayout = findViewById(R.id.ultimosLayout);


        if (nombreCliente != null)
            Toast.makeText(getApplicationContext(), "Bienvenido, " + nombreCliente + "!", Toast.LENGTH_LONG).show();

        saldoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saldoLayout.setVisibility(View.VISIBLE);
                ultimosLayout.setVisibility(View.GONE);
                respuestaText.setVisibility(View.VISIBLE);
            }
        });
        ultimosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saldoLayout.setVisibility(View.GONE);
                ultimosLayout.setVisibility(View.VISIBLE);
                respuestaText.setVisibility(View.GONE);
                new ConsultarUltimosTask(view.getContext()).execute();
            }
        });
        cuentaConsultarButton.setOnClickListener(new View.OnClickListener() {
            private String id = cuentaConsultarField.getText().toString();
            @Override
            public void onClick(View view) {
                new ConsultarCuentaTask(id, idCliente, view.getContext()).execute();
            }
        });

    }

    private class ConsultarCuentaTask extends AsyncTask<String, String, String> {

        private String id;
        private Integer idCliente;
        private Context context;

        private ConsultarCuentaTask(String id, Integer idCliente, Context context) {
            this.id = id;
            this.idCliente = idCliente;
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            try {
                result = RESTService.makeGetRequest("http://localhost:8080/tibuuroncitos/rest/cliente/5");
            } catch (IOException e) {
                Log.d("INFO", e.toString());
            }
            if (result != null)
                Log.d("INFO", result.toString());
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null)
                if (result.length() > 0) {
                    try {
                        JSONObject cuenta = new JSONObject(result);
                        Integer idCliente = cuenta.getInt("id_cliente");
                        if (idCliente == this.idCliente) {
                            new ConsultarSaldoTask(id, this.context).execute();
                        }
                    } catch (JSONException e) {
                        Log.d("INFO", e.toString());
                    }
                }
        }
    }

    private class ConsultarSaldoTask extends AsyncTask<String, String, String> {

        private String id;
        private Context context;

        private ConsultarSaldoTask(String id, Context context) {
            this.id = id;
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            try {
                result = RESTService.makeGetRequest("http://localhost:8080/tibuuroncitos/rest/cliente/5");
            } catch (IOException e) {
                Log.d("INFO", e.toString());
            }
            Log.d("INFO", result.toString());
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                TextView respuestaText = findViewById(R.id.respuestaText);
                respuestaText.setText("Saldo de la cuenta " + this.id + ": " + result.toString());
            }
        }
    }

    private class ConsultarUltimosTask extends AsyncTask<String, String, String> {

        private Context context;

        private ConsultarUltimosTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            try {
                result = RESTService.makeGetRequest("http://localhost:8080/tibuuroncitos/rest/cliente/5");
            } catch (IOException e) {
                Log.d("INFO", e.toString());
            }
            Log.d("INFO", result.toString());
            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            if (result != null){
                    JSONArray movimientos;
                    JSONObject movimiento = new JSONObject();
                    TextView target;
                    try {
                        // Fila 1
                        target = findViewById(R.id.tipo1);
                        movimientos = new JSONArray(result);
                        Log.d("INFO", movimientos.toString());
                        movimiento = movimientos.getJSONObject(0);
                        if( movimiento.has("nombre") && !movimiento.isNull("nombre") ){
                            if (movimiento.getInt("tipo") == 0){
                                target.setText("EXTRACCIÓN");
                            } else {
                                target.setText("DEPOSITO");
                            }
                            target = findViewById(R.id.cantidad1);
                            target.setText(String.valueOf(movimiento.getInt("importe")));
                        }
                        // Fila 2
                        target = findViewById(R.id.tipo2);
                        movimientos = new JSONArray(result);
                        Log.d("INFO", movimientos.toString());
                        movimiento = movimientos.getJSONObject(0);
                        if( movimiento.has("nombre") && !movimiento.isNull("nombre") ){
                            if (movimiento.getInt("tipo") == 0){
                                target.setText("EXTRACCIÓN");
                            } else {
                                target.setText("DEPOSITO");
                            }
                            target = findViewById(R.id.cantidad2);
                            target.setText(String.valueOf(movimiento.getInt("importe")));
                        }
                        // Fila 3
                        target = findViewById(R.id.tipo3);
                        movimientos = new JSONArray(result);
                        Log.d("INFO", movimientos.toString());
                        movimiento = movimientos.getJSONObject(0);
                        if( movimiento.has("nombre") && !movimiento.isNull("nombre") ){
                            if (movimiento.getInt("tipo") == 0){
                                target.setText("EXTRACCIÓN");
                            } else {
                                target.setText("DEPOSITO");
                            }
                            target = findViewById(R.id.cantidad3);
                            target.setText(String.valueOf(movimiento.getInt("importe")));
                        }
                        // Fila 4
                        target = findViewById(R.id.tipo4);
                        movimientos = new JSONArray(result);
                        Log.d("INFO", movimientos.toString());
                        movimiento = movimientos.getJSONObject(0);
                        if( movimiento.has("nombre") && !movimiento.isNull("nombre") ){
                            if (movimiento.getInt("tipo") == 0){
                                target.setText("EXTRACCIÓN");
                            } else {
                                target.setText("DEPOSITO");
                            }
                            target = findViewById(R.id.cantidad4);
                            target.setText(String.valueOf(movimiento.getInt("importe")));
                        }
                    } catch (JSONException e) {Log.d("INFO", e.toString());}
            }
        }
    }

}

