package com.example.letraria;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.letraria.adapters.BookAdapter;
import com.example.letraria.entities.BookEntity;
import com.example.letraria.entities.UserEntity;
import com.example.letraria.enums.BookStatus;
import com.example.letraria.global.UserSession;
import com.example.letraria.repositories.BookRepository;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookAdapter adapter;
    private List<BookEntity> todosLivros = new ArrayList<>();
    private List<BookEntity> livrosFiltrados = new ArrayList<>();
    private Spinner spinner;
    private TextView textViewEmpty;
    private BookRepository bookRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recyclerViewBooks);
        spinner = findViewById(R.id.spinnerFilter);
        textViewEmpty = findViewById(R.id.textViewEmpty);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        bookRepository = new BookRepository(this);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item,
                new String[]{"Todos", "Lido", "Lendo", "Quero Ler"}
        );
        ImageButton buttonLogout = findViewById(R.id.buttonLogout);

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

        loadBooks();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBooks();
    }

    private void loadBooks() {
        UserEntity user = UserSession.getInstance(this).getUser();
        if (user == null) {
            Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_SHORT).show();
            textViewEmpty.setVisibility(View.VISIBLE);
            textViewEmpty.setText("Usuário não autenticado");
            todosLivros.clear();
            filtrarLivros(spinner.getSelectedItem().toString());
            return;
        }

        try {
            int userId = user.getUserId();
            todosLivros = bookRepository.getBookByUserId(userId);
            filtrarLivros(spinner.getSelectedItem().toString());
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao carregar livros: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            todosLivros.clear();
            filtrarLivros(spinner.getSelectedItem().toString());
        }
    }

    private void filtrarLivros(String status) {
        livrosFiltrados.clear();
        if (status.equals("Todos")) {
            livrosFiltrados.addAll(todosLivros);
        } else {
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
                    return;
            }
            for (BookEntity livro : todosLivros) {
                if (livro.getStatus() == statusInt) {
                    livrosFiltrados.add(livro);
                }
            }
        }

        textViewEmpty.setVisibility(livrosFiltrados.isEmpty() ? View.VISIBLE : View.GONE);
        if (livrosFiltrados.isEmpty() && !status.equals("Todos")) {
            textViewEmpty.setText("Nenhum livro encontrado para " + status);
        } else if (livrosFiltrados.isEmpty()) {
            textViewEmpty.setText("Nenhum livro cadastrado");
        }

        adapter = new BookAdapter(livrosFiltrados);
        adapter.setOnItemClickListener(livro -> {
            Intent intent = new Intent(HomeActivity.this, BookDetailActivity.class);
            intent.putExtra("id", livro.getBookId());
            intent.putExtra("title", livro.getTitle());
            intent.putExtra("author", livro.getAutor());
            intent.putExtra("status", BookStatus.fromValue(livro.getStatus()).getLabel());
            intent.putExtra("nota", livro.getNota());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }

    public void adicionarLivro(View v) {
        Intent intent = new Intent(this, CadastroLivroActivity.class);
        startActivity(intent);
    }


    public void logout(View view) {
        UserSession.getInstance(this).setUser(null);

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
