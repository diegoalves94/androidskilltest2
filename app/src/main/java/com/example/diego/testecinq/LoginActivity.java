package com.example.diego.testecinq;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diego.testecinq.dao.LocalDbImplement;
import com.example.diego.testecinq.dao.sqlite.UserDBHelper;
import com.example.diego.testecinq.extras.Utils;
import com.example.diego.testecinq.models.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    //region Views
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.emailLogin)
    TextInputEditText emailLogin;
    @BindView(R.id.tilEmail)
    TextInputLayout tilEmail;
    @BindView(R.id.senhaLogin)
    TextInputEditText senhaLogin;
    @BindView(R.id.tilSenha)
    TextInputLayout tilSenha;
    @BindView(R.id.btnLogin)
    MaterialButton btnLogin;
    @BindView(R.id.btnCadastrarLogin)
    MaterialButton btnCadastrarLogin;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ButterKnife.bind(this);

        init();
    }

    private void init() {

        //LOGIN
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarLogin();
            }
        });

        //CADASTRAR
        btnCadastrarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, CadastroActivity.class));
            }
        });
    }

    private void validarLogin() {
        boolean error = false;

        if (!Utils.isValidEmail(emailLogin.getText().toString())) {
            error = true;
            emailLogin.setError(getString(R.string.email_invalido_error));
        }

        if (senhaLogin.getText().toString().isEmpty()) {
            error = true;
            senhaLogin.setError(getString(R.string.senha_invalida));
        }

        if (!error) {
            entrar();
        }
    }

    private void entrar() {
        UserDBHelper userDBHelper = new UserDBHelper(LoginActivity.this);
        User user = userDBHelper.queryUser(emailLogin.getText().toString().trim(), senhaLogin.getText().toString());
        
        if (user != null) {
            new LocalDbImplement<User>(LoginActivity.this).save(user);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else {
            Toast.makeText(this, "Usuário não encontrado.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
