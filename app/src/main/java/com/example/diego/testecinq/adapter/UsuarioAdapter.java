package com.example.diego.testecinq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.diego.testecinq.R;
import com.example.diego.testecinq.fragments.ListaAlbumFragment;
import com.example.diego.testecinq.models.Album;
import com.example.diego.testecinq.models.User;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

public class UsuarioAdapter extends RecyclerView.Adapter  {
    Context context;
    LayoutInflater inflater;
    List<String> usersFilter = new ArrayList<>();
    UserInterface inteface;

    public UsuarioAdapter(List<String> userFilter, Context context, UserInterface userInterface) {
        this.usersFilter = userFilter;
        this.context = context;
        this.inteface = userInterface;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        final ImageView edit;
        final ImageView delete;
        final AppCompatTextView nomeUsuario;

        public MyViewHolder(View view) {
            super(view);
            edit = (ImageView) view.findViewById(R.id.editItem);
            delete = (ImageView) view.findViewById(R.id.deleteItem);
            nomeUsuario = (AppCompatTextView) view.findViewById(R.id.nomeUsuarioItem);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_usuario, parent, false);
        UsuarioAdapter.MyViewHolder holder = new UsuarioAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final UsuarioAdapter.MyViewHolder vHolder = (UsuarioAdapter.MyViewHolder) holder;

        final String nome = usersFilter.get(position);

        vHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inteface.onEditUserClicked(position);
            }
        });

        vHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inteface.onDeleteUserClicked(position);
            }
        });

        vHolder.nomeUsuario.setText(nome);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return usersFilter.size();
    }

    public interface UserInterface{
        void onEditUserClicked(int position);
        void onDeleteUserClicked(int position);
    }

    public void updateList(List<String> newList){
        usersFilter = new ArrayList<>();
        usersFilter.addAll(newList);
        notifyDataSetChanged();
    }
}
