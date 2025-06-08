package com.example.letraria.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.letraria.entities.BookEntity;
import com.example.letraria.entities.UserEntity;
import com.example.letraria.helpers.DBHelper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookRepository {
    private SQLiteDatabase database;
    private DBHelper dbHelper;

    public BookRepository(Context context) {
        dbHelper = new DBHelper(context);
        this.open();
    }

    private void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    private void close() {
        dbHelper.close();
    }

    private static final String tableName = "books";

    private static final String[] tableColumns = {
            "book_id",
            "user_id",
            "title",
            "autor",
            "status",
            "created_at",
            "updated_at"
    };

    public BookEntity save(BookEntity book) {
        ContentValues values = new ContentValues();
        values.put(tableColumns[1], book.getUserId());
        values.put(tableColumns[2], book.getTitle());
        values.put(tableColumns[3], book.getAutor());
        values.put(tableColumns[4], book.getStatus());
        values.put(tableColumns[5], book.getCreatedAt().toString());
        values.put(tableColumns[6], book.getUpdatedAt().toString());
        long id = database.insert(tableName, null, values);
        book.setBookId((int) id);
        return book;
    }

    public List<BookEntity> getBookByUserId(int userId) {
        Cursor cursor = database.query(tableName,
                tableColumns, String.format("%s = ?", tableColumns[1]), new String[]{String.valueOf(userId)}, null, null, null);

        List<BookEntity> books = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                BookEntity book = new BookEntity();

                book.setBookId(cursor.getInt(cursor.getColumnIndexOrThrow(tableColumns[0])));
                book.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(tableColumns[1])));
                book.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(tableColumns[2])));
                book.setAutor(cursor.getString(cursor.getColumnIndexOrThrow(tableColumns[3])));
                book.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow(tableColumns[4])));
                book.setCreatedAt(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(tableColumns[5]))));
                book.setUpdatedAt(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(tableColumns[6]))));
                books.add(book);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return books;
    }

    public BookEntity getBookById(int bookId) {
        Cursor cursor = database.query(tableName,
                tableColumns, String.format("%s = ?", tableColumns[0]), new String[]{String.valueOf(bookId)}, null, null, null);

        BookEntity book = null;

        if (cursor.moveToFirst()) {
            book = new BookEntity();
            book.setBookId(cursor.getInt(cursor.getColumnIndexOrThrow(tableColumns[0])));
            book.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(tableColumns[1])));
            book.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(tableColumns[2])));
            book.setAutor(cursor.getString(cursor.getColumnIndexOrThrow(tableColumns[3])));
            book.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow(tableColumns[4])));
            book.setCreatedAt(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(tableColumns[5]))));
            book.setUpdatedAt(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(tableColumns[6]))));
        }

        cursor.close();

        return book;
    }


}
