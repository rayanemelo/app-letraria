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

public class CadastroActivity extends AppCompatActivity {
    private EditText emailInput, passwordInput, confirmPasswordInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
    }

    private void validarCampos() {
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

        Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
    }
    public void cadastrar(View v) {
        this.validarCampos();
    }

    public void acessarLogin(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
