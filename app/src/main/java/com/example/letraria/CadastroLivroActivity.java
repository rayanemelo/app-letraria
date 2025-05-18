package com.example.letraria;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CadastroLivroActivity extends AppCompatActivity {

    private EditText inputTitulo;
    private EditText inputAutor;
    private Spinner spinnerStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_livro);

        inputTitulo = findViewById(R.id.inputTitulo);
        inputAutor = findViewById(R.id.inputAutor);
        spinnerStatus = findViewById(R.id.spinnerStatus);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.status_livro,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);

    }

    public void cadastrarLivro(View v) {
        String titulo = inputTitulo.getText().toString().trim();
        String autor = inputAutor.getText().toString().trim();
        String status = spinnerStatus.getSelectedItem().toString();

        if (titulo.isEmpty()) {
            inputTitulo.setError("Informe o título");
            inputTitulo.requestFocus();
            return;
        }

        if (autor.isEmpty()) {
            inputAutor.setError("Informe o autor");
            inputAutor.requestFocus();
            return;
        }

        Toast.makeText(this,
                "Livro cadastrado:\nTítulo: " + titulo +
                        "\nAutor: " + autor +
                        "\nStatus: " + status,
                Toast.LENGTH_LONG).show();

        inputTitulo.setText("");
        inputAutor.setText("");
        spinnerStatus.setSelection(0);
    }

    public void voltar(View v) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
