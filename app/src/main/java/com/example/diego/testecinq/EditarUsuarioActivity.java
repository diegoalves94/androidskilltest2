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

import com.example.diego.testecinq.dao.LocalDbImplement;
import com.example.diego.testecinq.dao.sqlite.UserDBHelper;
import com.example.diego.testecinq.extras.Utils;
import com.example.diego.testecinq.models.EventBus.FinishEvent;
import com.example.diego.testecinq.models.User;
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

public class EditarUsuarioActivity extends AppCompatActivity {

    //region Views
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.nomeEditar)
    TextInputEditText nomeEditar;
    @BindView(R.id.tilNome)
    TextInputLayout tilNome;
    @BindView(R.id.emailEditar)
    TextInputEditText emailEditar;
    @BindView(R.id.tilEmail)
    TextInputLayout tilEmail;
    @BindView(R.id.senhaEditar)
    TextInputEditText senhaEditar;
    @BindView(R.id.tilSenha)
    TextInputLayout tilSenha;
    @BindView(R.id.confirmaSenhaEditar)
    TextInputEditText confirmaSenhaEditar;
    @BindView(R.id.tilConfirmaSenha)
    TextInputLayout tilConfirmaSenha;
    @BindView(R.id.btnEditar)
    MaterialButton btnEditar;
    @BindView(R.id.constraintLayout)
    ConstraintLayout constraintLayout;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    //endregion

    ProgressDialog pd;
    User usuario;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_usuario);
        ButterKnife.bind(this);
        Utils.toolbarSettings(this, "Editar usuário", toolbar, false);
        usuario = new LocalDbImplement<User>(EditarUsuarioActivity.this).getDefault(User.class);

        if (getIntent().hasExtra("usuario"))
            user = getIntent().getParcelableExtra("usuario");

        init();
    }

    private void init() {
        pd = new ProgressDialog(EditarUsuarioActivity.this);

        if (user != null) {
            //SET INFO
            nomeEditar.setText(user.getNome());
            emailEditar.setText(user.getEmail());
            senhaEditar.setText(user.getSenha());
            confirmaSenhaEditar.setText(user.getSenha());
        }

        //EDITAR
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarDados();
            }
        });

    }

    private void validarDados() {
        boolean error = false;

        if (nomeEditar.getText().toString().isEmpty()) {
            error = true;
            nomeEditar.setError(getString(R.string.campo_vazio_error));
        }

        if (senhaEditar.getText().toString().isEmpty() || confirmaSenhaEditar.getText().toString().isEmpty()) {
            error = true;
            senhaEditar.setError(getString(R.string.campo_vazio_error));
            confirmaSenhaEditar.setError(getString(R.string.campo_vazio_error));
        } else if (!senhaEditar.getText().toString().equalsIgnoreCase(confirmaSenhaEditar.getText().toString())) {
            error = true;
            senhaEditar.setError(getString(R.string.senhas_diferenter_error));
            confirmaSenhaEditar.setError(getString(R.string.senhas_diferenter_error));
        }

        if (!error) {
            editar();
        }
    }

    private void editar() {
        pd.setMessage("Editando usuário...");
        pd.show();

        //UPDATE NO DATABASE
        UserDBHelper userDBHelper = new UserDBHelper(EditarUsuarioActivity.this);
        SQLiteDatabase db = userDBHelper.getWritableDatabase();

        userDBHelper.updateUser(
                user.getId(),
                nomeEditar.getText().toString().trim(),
                senhaEditar.getText().toString().trim(),
                db);

        userDBHelper.close();

        pd.dismiss();
        Toast.makeText(this, "Usuário editado!!", Toast.LENGTH_SHORT).show();
        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (user.getId() == usuario.getId()){
                    updateLoggedUser();
                }
                EventBus.getDefault().post(new FinishEvent(true));
                finish();
            }
        }, 1500);
    }

    private void updateLoggedUser(){
        new LocalDbImplement<User>(EditarUsuarioActivity.this).clearObject(User.class);

        UserDBHelper userDBHelper = new UserDBHelper(EditarUsuarioActivity.this);
        User user = userDBHelper.queryUser(emailEditar.getText().toString().trim(), senhaEditar.getText().toString().trim());

        new LocalDbImplement<User>(EditarUsuarioActivity.this).save(user);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
