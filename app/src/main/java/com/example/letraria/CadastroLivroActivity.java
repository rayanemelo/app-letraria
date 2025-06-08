package com.example.letraria;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.letraria.entities.BookEntity;
import com.example.letraria.entities.UserEntity;
import com.example.letraria.global.UserSession;
import com.example.letraria.repositories.BookRepository;

public class CadastroLivroActivity extends AppCompatActivity {
    private EditText inputTitulo;
    private EditText inputAutor;
    private Spinner spinnerStatus;
    private BookRepository bookRepository;

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
        bookRepository = new BookRepository(this);
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

        UserEntity user = UserSession.getInstance(this).getUser();
        int userId = user.getUserId();

        int statusInt;
        switch (status) {
            case "Quero Ler":
                statusInt = 0;
                break;
            case "Lendo":
                statusInt = 1;
                break;
            case "Lido":
                statusInt = 2;
                break;
            default:
                Toast.makeText(this, "Status inválido", Toast.LENGTH_SHORT).show();
                return;
        }

        BookEntity newBook = new BookEntity();
        newBook.setUserId(userId);
        newBook.setTitle(titulo);
        newBook.setAutor(autor);
        newBook.setStatus(statusInt);

        try {
            bookRepository.save(newBook);
            Toast.makeText(this,
                    "Livro cadastrado com sucesso!",
                    Toast.LENGTH_SHORT).show();

            inputTitulo.setText("");
            inputAutor.setText("");
            spinnerStatus.setSelection(0);

            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao cadastrar livro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void voltar(View v) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
