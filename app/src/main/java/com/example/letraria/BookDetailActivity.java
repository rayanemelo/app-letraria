package com.example.letraria;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.letraria.entities.BookEntity;
import com.example.letraria.enums.BookStatus;
import com.example.letraria.repositories.BookRepository;

public class BookDetailActivity extends AppCompatActivity {

    private TextView textTitle, textAuthor, textStatus;
    private ImageView[] stars;
    private BookRepository bookRepository;

    private int bookId;
    private String title, author;
    private int nota;
    private BookStatus status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        textTitle = findViewById(R.id.textTitle);
        textAuthor = findViewById(R.id.textAuthor);
        textStatus = findViewById(R.id.textStatus);

        stars = new ImageView[]{
                findViewById(R.id.star1),
                findViewById(R.id.star2),
                findViewById(R.id.star3),
                findViewById(R.id.star4),
                findViewById(R.id.star5)
        };

        bookRepository = new BookRepository(this);

        bookId = getIntent().getIntExtra("id", -1);
        title = getIntent().getStringExtra("title");
        author = getIntent().getStringExtra("author");
        String statusString = getIntent().getStringExtra("status");
        nota = getIntent().getIntExtra("nota", 0);

        status = BookStatus.fromLabel(statusString);

        textTitle.setText(title);
        textAuthor.setText(author);
        textStatus.setText("Status: " + (status != null ? status.getLabel() : "Desconhecido"));

        updateStarIcons(nota);
    }

    public void editar(View v) {
        Intent intent = new Intent(this, CadastroLivroActivity.class);
        intent.putExtra("id", bookId);
        intent.putExtra("title", title);
        intent.putExtra("author", author);
        intent.putExtra("status", status != null ? status.getValue() : -1);
        intent.putExtra("nota", nota);
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

    private void updateStarIcons(int rating) {
        Log.d("CadastroLivro", "Nota teste: " + rating);
        for (int i = 0; i < stars.length; i++) {
            if (i < rating) {
                stars[i].setImageResource(R.drawable.ic_star_filled);
            } else {
                stars[i].setImageResource(R.drawable.ic_star_outline);
            }
        }
    }
}
