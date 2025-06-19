package com.example.letraria;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.letraria.entities.BookEntity;
import com.example.letraria.enums.BookStatus;
import com.example.letraria.repositories.BookRepository;

public class BookDetailActivity extends AppCompatActivity {

    private TextView textTitle, textAuthor, textStatus;
    private BookRepository bookRepository;
    private int bookId;
    private String title, author;
    private BookStatus status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        textTitle = findViewById(R.id.textTitle);
        textAuthor = findViewById(R.id.textAuthor);
        textStatus = findViewById(R.id.textStatus);
        bookRepository = new BookRepository(this);

        bookId = getIntent().getIntExtra("id", -1);
        title = getIntent().getStringExtra("title");
        author = getIntent().getStringExtra("author");
        String statusString = getIntent().getStringExtra("status");

        status = BookStatus.fromLabel(statusString);

        textTitle.setText(title);
        textAuthor.setText(author);
        textStatus.setText("Status: " + (status != null ? status.getLabel() : "Desconhecido"));
    }

    public void editar(View v) {
        Intent intent = new Intent(this, CadastroLivroActivity.class);
        intent.putExtra("id", bookId);
        intent.putExtra("title", title);
        intent.putExtra("author", author);
        intent.putExtra("status", status != null ? status.getValue() : -1);
        startActivity(intent);
    }

    public void excluir(View v) {
        new AlertDialog.Builder(this)
                .setTitle("Excluir Livro")
                .setMessage("Tem certeza que deseja excluir este livro?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    if (bookId != -1) {
                        bookRepository.deleteBook(bookId);
                        Toast.makeText(this, "Livro excluído com sucesso", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Erro ao excluir o livro", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    public void voltarParaHome(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
