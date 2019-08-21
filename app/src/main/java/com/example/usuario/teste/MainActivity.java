package com.example.usuario.teste;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    TextView tvResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResultado = (TextView) findViewById(R.id.tvResultado);
        Button btnLoginVolley = (Button) findViewById(R.id.btnEnviar);
        Button btnConfigurar = (Button) findViewById(R.id.btnConfigurar);

        btnLoginVolley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requisicaoVolley();
            }
        });

        btnConfigurar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);
            }
        });

    }

    private void requisicaoVolley(){

        RequestQueue mRequestQueue;
        Cache cache = new DiskBasedCache(getCacheDir(), 1024*1024);
        Network network = new BasicNetwork(new HurlStack());
        mRequestQueue = new RequestQueue(cache, network);
        mRequestQueue.start();

        String cidade1 = ((EditText) findViewById(R.id.cidade1)).getText().toString();
        String cidade2 = ((EditText) findViewById(R.id.cidade2)).getText().toString();

        JSONArray locations = new JSONArray();
        locations.put(cidade1);
        locations.put(cidade2);

        JSONObject options = new JSONObject();
        try{
            options.put("unit", "k");
        }catch (JSONException e){
            e.printStackTrace();
        }

        JSONObject params = new JSONObject();
        try{
            params.put("locations", locations);
            params.put("options", options);
        }catch (JSONException e){
            e.printStackTrace();
        }

        final String url = "http://www.mapquestapi.com/directions/v2/route?key=VxLhufBanyuGNANzh7OApcoBI9NxhNmC";

        

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                url,
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONObject object = response.getJSONObject("route");
                            Double distancia = response.getJSONObject("route").getDouble("distance");

                            SharedPreferences preferences = getSharedPreferences("prefs",0);
                            Double consumo = Double.parseDouble(preferences.getString("consumo","0"));
                            Double preço = Double.parseDouble(preferences.getString("preço","0"));

                            Double calcLitros = (distancia/consumo);
                            Double custo = preço * calcLitros;
                            String custoGaso = String.format("%.2f", custo);

                            tvResultado.setText(" A distância é de " + String.valueOf(distancia) + "KM\n Seu carro consome: " + String.valueOf(consumo) + "L Por KM\n Você irá gastar: " +
                                    String.valueOf(custoGaso) + "R$ de gasolina. \n \nBoa Viagem =)" ); //mostra valor gasto
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Erro volley", error.toString());
                    }
                }
        );
        mRequestQueue.add(request);
    } // Fim requisicaoVolley
}

