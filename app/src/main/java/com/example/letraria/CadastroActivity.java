package com.example.letraria;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.letraria.entities.UserEntity;
import com.example.letraria.repositories.UserRepository;

public class CadastroActivity extends AppCompatActivity {
    private EditText emailInput, passwordInput, confirmPasswordInput;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        userRepository = new UserRepository(this);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
    }


    public void cadastrar(View v) {
        String email = emailInput.getText().toString().trim();
        String senha = passwordInput.getText().toString().trim();
        String confirmarSenha = confirmPasswordInput.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            emailInput.setError("Preencha o e-mail");
            emailInput.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("E-mail inválido");
            emailInput.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(senha)) {
            passwordInput.setError("Preencha a senha");
            passwordInput.requestFocus();
            return;
        }

        if (senha.length() < 6) {
            passwordInput.setError("A senha deve ter pelo menos 6 caracteres");
            passwordInput.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(confirmarSenha)) {
            confirmPasswordInput.setError("Confirme a senha");
            confirmPasswordInput.requestFocus();
            return;
        }

        if (!senha.equals(confirmarSenha)) {
            confirmPasswordInput.setError("As senhas não coincidem");
            confirmPasswordInput.requestFocus();
            return;
        }

        UserEntity userWithSameEmail = userRepository.findByEmail(email);

        if (userWithSameEmail != null) {
            emailInput.setError("E-mail já cadastrado");
            return;
        }

        UserEntity newUser = new UserEntity();
        newUser.setEmail(email);
        newUser.setPassword(senha);

        userRepository.save(newUser);

        Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
        acessarLogin(v);
    }

    public void acessarLogin(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
