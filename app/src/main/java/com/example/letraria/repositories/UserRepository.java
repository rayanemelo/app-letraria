package com.example.letraria.repositories;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.letraria.entities.UserEntity;
import com.example.letraria.helpers.DBHelper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private SQLiteDatabase database;
    private DBHelper dbHelper;

    public UserRepository(Context context) {
        dbHelper = new DBHelper(context);
        this.open();
    }

    private void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    private void close() {
        dbHelper.close();
    }

    private static final String tableName = "users";

    private static final String[] tableColumns = {
            "user_id",
            "email",
            "password",
            "created_at",
            "updated_at"
    };

    public UserEntity save(UserEntity user) {
        ContentValues values = new ContentValues();
        values.put(tableColumns[1], user.getEmail());
        values.put(tableColumns[2], user.getPassword());
        values.put(tableColumns[3], user.getCreatedAt().toString());
        values.put(tableColumns[4], user.getUpdatedAt().toString());
        long id = database.insert(tableName, null, values);
        user.setUserId((int) id);
        return user;
    }

    public List<UserEntity> findAll() {
        Cursor cursor = database.query(tableName,
                tableColumns, null, null, null, null, null);

        List<UserEntity> users = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                UserEntity user = new UserEntity();
                user.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(tableColumns[0])));
                user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(tableColumns[1])));
                user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(tableColumns[2])));
                user.setCreatedAt(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(tableColumns[3]))));
                user.setUpdatedAt(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(tableColumns[4]))));
                users.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return users;
    }

    public UserEntity findByEmail(String email) {
        Cursor cursor = database.query(tableName,
                tableColumns, String.format("%s = ?", tableColumns[1]), new String[]{email}, null, null, null);

        UserEntity user = null;

        if (cursor.moveToFirst()) {
            user = new UserEntity();
            user.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(tableColumns[0])));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(tableColumns[1])));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(tableColumns[2])));
            user.setCreatedAt(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(tableColumns[3]))));
            user.setUpdatedAt(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(tableColumns[4]))));
        }

        cursor.close();

        return user;
    }
}