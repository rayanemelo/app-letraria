package com.example.letraria;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.letraria.entities.UserEntity;
import com.example.letraria.global.UserSession;
import com.example.letraria.repositories.UserRepository;

public class MainActivity extends AppCompatActivity {
    EditText emailInput, passwordInput;
    private UserRepository userRepository;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userRepository = new UserRepository(this);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
    }


    public void logar(View v) {
        String email = emailInput.getText().toString().trim();
        String senha = passwordInput.getText().toString().trim();

        if (email.isEmpty()) {
            emailInput.setError("Informe o e-mail");
            emailInput.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("E-mail inválido");
            emailInput.requestFocus();
            return;
        }

        if (senha.isEmpty()) {
            passwordInput.setError("Informe a senha");
            passwordInput.requestFocus();
            return;
        }

        if (senha.length() < 6) {
            passwordInput.setError("A senha deve ter no mínimo 6 caracteres");
            passwordInput.requestFocus();
            return;
        }

        UserEntity userExists = userRepository.findByEmail(email);
        Log.d("LOGIN", "Usuário encontrado: " + userExists);
        if (userExists == null) {
            emailInput.setError("Usuário não encontrado");
            return;
        }

        if (!userExists.getPassword().equals(senha)) {
            passwordInput.setError("Senha incorreta");
            return;
        }

        UserSession.getInstance(this).setUser(userExists);

        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void acessarCadastro(View v) {
        Intent intent = new Intent(this, CadastroActivity.class);
        startActivity(intent);
    }
}