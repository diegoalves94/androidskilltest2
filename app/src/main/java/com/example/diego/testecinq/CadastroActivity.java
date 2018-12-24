package com.example.diego.testecinq;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.diego.testecinq.dao.sqlite.UserDBHelper;
import com.example.diego.testecinq.extras.Utils;
import com.example.diego.testecinq.models.EventBus.FinishEvent;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.greenrobot.eventbus.EventBus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CadastroActivity extends AppCompatActivity {

    //region Views
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.nomeCadastro)
    TextInputEditText nomeCadastro;
    @BindView(R.id.tilNome)
    TextInputLayout tilNome;
    @BindView(R.id.emailCadastro)
    TextInputEditText emailCadastro;
    @BindView(R.id.tilEmail)
    TextInputLayout tilEmail;
    @BindView(R.id.senhaCadastro)
    TextInputEditText senhaCadastro;
    @BindView(R.id.tilSenha)
    TextInputLayout tilSenha;
    @BindView(R.id.confirmaSenhaCadastro)
    TextInputEditText confirmaSenhaCadastro;
    @BindView(R.id.tilConfirmaSenha)
    TextInputLayout tilConfirmaSenha;
    @BindView(R.id.btnCadastrar)
    MaterialButton btnCadastrar;
    @BindView(R.id.constraintLayout)
    ConstraintLayout constraintLayout;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    //endregion

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        ButterKnife.bind(this);
        Utils.toolbarSettings(this, "Cadastro", toolbar, false);

        init();
    }

    private void init() {
        pd = new ProgressDialog(CadastroActivity.this);

        //CADASTRAR
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarDados();
            }
        });

    }

    private void validarDados() {
        boolean error = false;

        if (nomeCadastro.getText().toString().isEmpty()) {
            error = true;
            nomeCadastro.setError(getString(R.string.campo_vazio_error));
        }

        if (!Utils.isValidEmail(emailCadastro.getText().toString())) {
            error = true;
            emailCadastro.setError(getString(R.string.email_invalido_error));
        }

        if (senhaCadastro.getText().toString().isEmpty() || confirmaSenhaCadastro.getText().toString().isEmpty()) {
            error = true;
            senhaCadastro.setError(getString(R.string.campo_vazio_error));
            confirmaSenhaCadastro.setError(getString(R.string.campo_vazio_error));
        } else if (!senhaCadastro.getText().toString().equalsIgnoreCase(confirmaSenhaCadastro.getText().toString())) {
            error = true;
            senhaCadastro.setError(getString(R.string.senhas_diferenter_error));
            confirmaSenhaCadastro.setError(getString(R.string.senhas_diferenter_error));
        }

        if (!error) {
            cadastrar();
        }
    }

    private void cadastrar() {
        pd.setMessage("Cadastrando...");
        pd.show();

        //INSERT NO DATABASE
        UserDBHelper userDBHelper = new UserDBHelper(CadastroActivity.this);
        SQLiteDatabase db = userDBHelper.getWritableDatabase();

        userDBHelper.addUser(
                nomeCadastro.getText().toString().trim(),
                emailCadastro.getText().toString().trim(),
                senhaCadastro.getText().toString().trim(),
                db);

        userDBHelper.close();

        pd.dismiss();
        Toast.makeText(this, "Usuário cadastrado!!", Toast.LENGTH_SHORT).show();
        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {
            @Override
                public void run() {
                EventBus.getDefault().post(new FinishEvent(true));
                finish();
            }
        }, 1500);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
