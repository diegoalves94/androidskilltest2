package com.example.diego.testecinq.adapter;

import android.content.Context;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.diego.testecinq.R;
import com.example.diego.testecinq.models.Album;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class AlbumAdapter extends RecyclerView.Adapter{
    Context context;
    LayoutInflater inflater;
    ArrayList<Album> albums = new ArrayList<>();

    public AlbumAdapter(ArrayList<Album> albumList, Context context) {
        this.albums = albumList;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        final ImageView image;
        final AppCompatTextView nomeAlbum;

        public MyViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.iconAlbumItem);
            nomeAlbum = (AppCompatTextView) view.findViewById(R.id.nomeAlbumItem);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_album, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        AlbumAdapter.MyViewHolder vHolder = (AlbumAdapter.MyViewHolder) holder;

        final Album album = albums.get(position);

        if (album != null){
            Glide.with(context).load(album.getThumbnailUrl()).apply(new RequestOptions().centerCrop()
                    .placeholder(R.drawable.placeholder)).into(vHolder.image);
        }

        vHolder.nomeAlbum.setText(album.getTitle());
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
        return albums.size();
    }

}
