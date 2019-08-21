package com.example.usuario.teste;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Main2Activity extends AppCompatActivity {

    Button btnCadastra;
    EditText preçoGaso;
    EditText consumoCarro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        btnCadastra = (Button) findViewById(R.id.btnCadastrar);

        btnCadastra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            preçoGaso = findViewById(R.id.preçoGaso);
            consumoCarro = findViewById(R.id.consumoCarro);

                SharedPreferences preferences = getSharedPreferences("prefs",0);
                SharedPreferences.Editor editor = preferences.edit();

                String preço = preçoGaso.getText().toString();
                String consumo = consumoCarro.getText().toString();

                editor.putString("preço", preço);
                editor.putString("consumo", consumo);

                editor.commit();
                finish();

            }
        });

    }

}
