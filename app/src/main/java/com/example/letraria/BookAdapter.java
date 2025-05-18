package com.example.letraria;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private List<Book> livros;

    public BookAdapter(List<Book> livros) {
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
        Book livro = livros.get(position);
        holder.titulo.setText(livro.getTitulo() + " - " + livro.getAutor());
        holder.status.setText(livro.getStatus());
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
