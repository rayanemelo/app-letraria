package com.example.letraria;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.letraria.entities.BookEntity;
import com.example.letraria.entities.UserEntity;
import com.example.letraria.enums.BookStatus;
import com.example.letraria.global.UserSession;
import com.example.letraria.repositories.BookRepository;

import java.time.LocalDateTime;

public class CadastroLivroActivity extends AppCompatActivity {

    private EditText inputTitulo;
    private EditText inputAutor;
    private Spinner spinnerStatus;
    private BookRepository bookRepository;

    private boolean isEditing = false;
    private int bookId = -1;

    private TextView textViewHeader;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_livro);

        inputTitulo = findViewById(R.id.inputTitulo);
        inputAutor = findViewById(R.id.inputAutor);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        textViewHeader = findViewById(R.id.textViewHeader);
        button = findViewById(R.id.button);

        // Adapter com estilo personalizado para fonte, cor e tamanho
        String[] statusArray = getResources().getStringArray(R.array.status_livro);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, statusArray) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view;
                text.setTextSize(14);
                text.setTypeface(ResourcesCompat.getFont(getContext(), R.font.merriweather));
                text.setTextColor(ContextCompat.getColor(getContext(), R.color.text_primary));
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView text = (TextView) view;
                text.setTextSize(14);
                text.setTypeface(ResourcesCompat.getFont(getContext(), R.font.merriweather));
                text.setTextColor(ContextCompat.getColor(getContext(), R.color.text_primary));
                return view;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);

        bookRepository = new BookRepository(this);

        // Verifica se está em modo de edição
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("id")) {
            isEditing = true;
            bookId = intent.getIntExtra("id", -1);
            String title = intent.getStringExtra("title");
            String author = intent.getStringExtra("author");
            int status = intent.getIntExtra("status", 0);

            inputTitulo.setText(title);
            inputAutor.setText(author);

            // Corrige a seleção do Spinner baseado no enum
            BookStatus bookStatus = BookStatus.fromValue(status);
            if (bookStatus != null) {
                String label = bookStatus.getLabel();
                for (int i = 0; i < statusArray.length; i++) {
                    if (statusArray[i].equalsIgnoreCase(label)) {
                        spinnerStatus.setSelection(i);
                        break;
                    }
                }
            }

            // Atualiza o cabeçalho e botão
            textViewHeader.setText("Editar Livro");
            button.setText("Salvar Alterações");
        }
    }

    public void cadastrarLivro(View v) {
        String titulo = inputTitulo.getText().toString().trim();
        String autor = inputAutor.getText().toString().trim();
        String statusLabel = spinnerStatus.getSelectedItem().toString();
        BookStatus statusEnum = BookStatus.fromLabel(statusLabel);

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

        if (statusEnum == null) {
            Toast.makeText(this, "Status inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        UserEntity user = UserSession.getInstance(this).getUser();
        int userId = user.getUserId();

        BookEntity book = new BookEntity();
        book.setUserId(userId);
        book.setTitle(titulo);
        book.setAutor(autor);
        book.setStatus(statusEnum.getValue());
        book.setUpdatedAt(LocalDateTime.now());

        try {
            if (isEditing) {
                book.setBookId(bookId);
                boolean updated = bookRepository.updateBook(book);
                if (updated) {
                    Toast.makeText(this, "Livro atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Erro ao atualizar o livro.", Toast.LENGTH_SHORT).show();
                }
            } else {
                book.setCreatedAt(LocalDateTime.now());
                bookRepository.save(book);
                Toast.makeText(this, "Livro cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
            }

            startActivity(new Intent(this, HomeActivity.class));
            finish();

        } catch (Exception e) {
            Toast.makeText(this, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void voltar(View v) {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}
