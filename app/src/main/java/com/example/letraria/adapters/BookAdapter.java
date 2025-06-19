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

    private final List<BookEntity> livros;
    private OnItemClickListener listener;

    public BookAdapter(List<BookEntity> livros) {
        this.livros = livros;
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_item, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        BookEntity livro = livros.get(position);

        holder.textTitulo.setText(livro.getTitle());
        holder.textAutor.setText(livro.getAutor());

        String statusTexto;
        switch (livro.getStatus()) {
            case 0:
                statusTexto = "Quero ler";
                break;
            case 1:
                statusTexto = "Lendo";
                break;
            case 2:
                statusTexto = "Lido";
                break;
            default:
                statusTexto = "Desconhecido";
        }

        holder.textStatus.setText("Status: " + statusTexto);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(livro);
            }
        });
    }

    @Override
    public int getItemCount() {
        return livros.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(BookEntity livro);
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView textTitulo, textAutor, textStatus;

        public BookViewHolder(View itemView) {
            super(itemView);
            textTitulo = itemView.findViewById(R.id.textTitulo);
            textAutor = itemView.findViewById(R.id.textAutor);
            textStatus = itemView.findViewById(R.id.textStatus);
        }
    }
}
