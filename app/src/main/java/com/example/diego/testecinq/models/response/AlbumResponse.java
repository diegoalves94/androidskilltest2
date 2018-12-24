package com.example.diego.testecinq.models.response;

import com.example.diego.testecinq.models.Album;

import java.util.ArrayList;
import java.util.List;

public class AlbumResponse {

    private List<Album> data = new ArrayList<>();

    public AlbumResponse(List<Album> data) {
        this.data = data;
    }

    public List<Album> getData() {
        return data;
    }

    public void setData(List<Album> data) {
        this.data = data;
    }
}
