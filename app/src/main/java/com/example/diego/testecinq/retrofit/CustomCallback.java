package com.example.diego.testecinq.retrofit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;

import com.example.diego.testecinq.R;
import com.example.diego.testecinq.retrofit.utils.NetworkUtil;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;

public class CustomCallback<T> implements Callback<T> {
    Context context;
    ProgressDialog dialog;
    OnResponse onResponse;
    View viewLayout;
    boolean loadDialog = true;
    boolean showAlert = true;
    Class<T> type;

    public CustomCallback(Activity context, OnResponse<T> onResponse, View viewLayout){
        this(context,onResponse);
        this.viewLayout = viewLayout;
    }

    public CustomCallback(Context context, OnResponse<T> onResponse){
        this.context = context;
        this.onResponse = onResponse;
    }

    private void requestUsuario (){

    }
    @Override
    public void onResponse(Call<T> call, retrofit2.Response<T> response) {
        try {
            if(dialog!=null)
                dialog.dismiss();
        }catch (Throwable ex){
            Log.e("dialog", "CustomCallback: ",ex);
        }

        if(response.isSuccessful()){
            onResponse.onResponse(response.body());
        }
        else if(response.code() == 404){
            try {
                onResponse.onFailure(new Throwable(response.errorBody().string()),response.code());
            }catch (Exception ex){
                onResponse.onFailure(new Throwable(context.getResources().getString(R.string.geral_mensagem_naoEncontrado)),response.code());
            }

        }
        else if(response.code() == 500){
            try {
                onResponse.onFailure(new Throwable(response.errorBody().string()),response.code());
            }catch (Exception ex){
                onResponse.onFailure(new Throwable(context.getResources().getString(R.string.geral_mensagem_erroServidor)),response.code());
            }
        }
        else if(response.code() == 401){
            onResponse.onFailure(new Throwable(context.getResources().getString(R.string.geral_mensagem_naoAutorizado)),response.code());
        }
        else{
            try {
                onResponse.onFailure(new Throwable(response.errorBody().string()),response.code());
            }catch (Exception ex){
                ex.printStackTrace();
                onResponse.onFailure(new Throwable(context.getResources().getString(R.string.geral_mensagem_erroProblema)),response.code());
            }
        }
    }

    @Override
    public void onFailure(Call<T> call, final Throwable t) {
        try {
            if(dialog!=null)
                dialog.dismiss();
        }catch (Throwable ex){
            Log.e("dialog", "CustomCallback: ",ex);
        }
        if(viewLayout != null){
            try {
                if(t instanceof TimeoutException || t instanceof SocketTimeoutException){
                    Snackbar snackbar = Snackbar
                            .make(viewLayout, context.getResources().getString(R.string.geral_mensagem_erroConexao), Snackbar.LENGTH_LONG)
                            .setAction(context.getResources().getString(R.string.geral_mensagem_erroNovamente), new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    try {
                                        onResponse.onRetry(t);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                    snackbar.show();
                }
                else if(t instanceof UnknownHostException && !NetworkUtil.isConnected(context)){
                    Snackbar snackbar = Snackbar
                            .make(viewLayout, context.getResources().getString(R.string.geral_mensagem_conexaoInternet), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                else if(t instanceof ConnectException && !NetworkUtil.isConnected(context)){
                    Snackbar snackbar = Snackbar
                            .make(viewLayout, context.getResources().getString(R.string.geral_mensagem_conexaoInternet), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                else{
                    t.printStackTrace();
                    onResponse.onFailure(new Throwable(context.getResources().getString(R.string.geral_mensagem_erroProblema)),999);
                }
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }

        }else {
            if(t instanceof TimeoutException || t instanceof SocketTimeoutException){

                try {
                    //Cria o gerador do AlertDialog
                    AlertDialog.Builder builder = new  AlertDialog.Builder(context, R.style.AlertDialogTheme);
                    //define o titulo
                    builder.setTitle(context.getResources().getString(R.string.geral_mensagem_erroConexao));
                    //define a mensagem
                    builder.setMessage(context.getResources().getString(R.string.geral_mensagem_erroGostariaNovamente));
                    //define um botão como positivo
                    builder.setPositiveButton(context.getResources().getString(R.string.geral_button_sim), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            arg0.dismiss();
                            try {
                                onResponse.onRetry(t);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    //define um botão como negativo.
                    builder.setNegativeButton(context.getResources().getString(R.string.geral_button_nao), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            arg0.dismiss();
                            onResponse.onFailure(t,999);
                        }
                    });
                    //cria o AlertDialog
                    AlertDialog alerta = builder.create();
                    //Exibe
                    //Caso definido, nao exibe o dialog
                    if(showAlert)
                        alerta.show();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
            }
            else if(t instanceof UnknownHostException && !NetworkUtil.isConnected(context)){
                try {
                    //Cria o gerador do AlertDialog
                    AlertDialog.Builder builder = new  AlertDialog.Builder(context, R.style.AlertDialogTheme);
                    //define o titulo
                    builder.setTitle(context.getResources().getString(R.string.geral_mensagem_erroConexao));
                    //define a mensagem
                    builder.setMessage(context.getResources().getString(R.string.geral_mensagem_conexaoInternet));
                    //define um botão como positivo
                    builder.setPositiveButton(context.getResources().getString(R.string.geral_button_ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            arg0.dismiss();
                        }
                    });
                    //cria o AlertDialog
                    AlertDialog alerta = builder.create();
                    //Exibe
                    if(showAlert)
                        alerta.show();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
            }
            else if(t instanceof ConnectException && !NetworkUtil.isConnected(context)){
                try {
                    //Cria o gerador do AlertDialog
                    AlertDialog.Builder builder = new  AlertDialog.Builder(context, R.style.AlertDialogTheme);
                    //define o titulo
                    builder.setTitle(context.getResources().getString(R.string.geral_mensagem_erroConexao));
                    //define a mensagem
                    builder.setMessage(context.getResources().getString(R.string.geral_mensagem_conexaoInternet));
                    //define um botão como positivo
                    builder.setPositiveButton(context.getResources().getString(R.string.geral_button_ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            arg0.dismiss();
                        }
                    });
                    //cria o AlertDialog
                    AlertDialog alerta = builder.create();
                    //Exibe
                    if(showAlert)
                        alerta.show();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
            }
            else{
                try {
                    onResponse.onFailure(t,999);
                }catch (Exception ex){
                    ex.printStackTrace();
                    Log.e("Error", ex.getMessage());
                    onResponse.onFailure(new Throwable(context.getResources().getString(R.string.geral_mensagem_erroProblema)),999);
                }

            }

        }
    }

    public void useSnackBar(View view){
        viewLayout = view;
    }

    public interface OnResponse<T>{
        void onResponse(T response);
        void onFailure(Throwable t, int errorcode);
        void onRetry(Throwable t) throws JSONException;
    }
}
