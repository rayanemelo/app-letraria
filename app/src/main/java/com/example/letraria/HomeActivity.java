package com.example.letraria;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookAdapter adapter;
    private List<Book> todosLivros = new ArrayList<>();
    private List<Book> livrosFiltrados = new ArrayList<>();
    private Spinner spinner;
    private TextView textViewEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recyclerViewBooks);
        spinner = findViewById(R.id.spinnerFilter);
        textViewEmpty = findViewById(R.id.textViewEmpty);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Mock de dados
        todosLivros.add(new Book("Crime e Castigo", "Dostoiévski", "Lido"));
        todosLivros.add(new Book("Código Limpo", "Robert Cecil Martin", "Lido"));
        todosLivros.add(new Book("Jantar Secreto", "Raphael Montes", "Quero Ler"));
        todosLivros.add(new Book("O Alquimista", "Paulo Coelho", "Lendo"));

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item,
                new String[]{"Todos", "Lido", "Lendo", "Quero Ler"}
        );

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String filtro = spinner.getSelectedItem().toString();
                filtrarLivros(filtro);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void filtrarLivros(String status) {
        livrosFiltrados.clear();
        if (status.equals("Todos")) {
            livrosFiltrados.addAll(todosLivros);
        } else {
            for (Book livro : todosLivros) {
                if (livro.getStatus().equalsIgnoreCase(status)) {
                    livrosFiltrados.add(livro);
                }
            }
        }

        textViewEmpty.setVisibility(livrosFiltrados.isEmpty() ? View.VISIBLE : View.GONE);

        adapter = new BookAdapter(livrosFiltrados);
        recyclerView.setAdapter(adapter);
    }

    public void adicionarLivro(View v) {
        Intent intent = new Intent(this, CadastroLivroActivity.class);
        startActivity(intent);
    }
}
