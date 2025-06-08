package com.example.letraria.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.letraria.R;
import com.example.letraria.entities.BookEntity;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private List<BookEntity> livros;

    public BookAdapter(List<BookEntity> livros) {
        this.livros = livros;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);
        return new BookViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        BookEntity livro = livros.get(position);
        holder.titulo.setText(livro.getTitle() + " - " + livro.getAutor());
        String status;
        switch (livro.getStatus()) {
            case 0:
                status = "Quero Ler";
                break;
            case 1:
                status = "Lendo";
                break;
            case 2:
                status = "Lido";
                break;
            default:
                status = "Desconhecido";
        }
        holder.status.setText(status);
    }

    @Override
    public int getItemCount() {
        return livros.size();
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView titulo, status;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.textTitulo);
            status = itemView.findViewById(R.id.textStatus);
        }
    }
}