package com.example.diego.testecinq.retrofit;

import com.example.diego.testecinq.models.Album;
import com.example.diego.testecinq.models.response.AlbumResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RequestInterfaceUser {
    //  <---------------  POST requests --------------------->
//    @POST("usuario/login")
//    Call<LoginResponse> login(@Body RequestBody params);
//
//    @POST("usuario/cadastro")
//    Call<CadastroResponse> cadastro(@Body UsuarioRequest params);
//
//    @POST("usuario/buscarUsuario")
//    Call<UsuarioResponse> buscarUsuario(@Body RequestBody params);
//
//    @POST("estabelecimento/buscarAtributos")
//    Call<EstabelecimentoResponse> buscarEstabelecimento(@Body RequestBody params);
//
//    @POST("estabelecimento/cardEstabelecimento")
//    Call<ArrayList<Estabelecimento>> cardsEstabelecimentos (@Body CardEstabelecimentoRequest params);

    //  <---------------  GET requests --------------------->
    @GET("photos")
    Call<ArrayList<Album>> albuns();

//    @GET("url")
//    Call<ObjResponse> getPath(@Path("id") String id);

//    @GET("url/{id}")
//    Call<ObjResponse> getQuery(@Path("id") String id, @Query("limit") int limit);


    //  <--------------- PUT requests --------------------->

//    @PUT("url")
//    Call<ObjResponse> put(@Body RequestBody params);


    //  <--------------- DELETE requests --------------------->
//    @DELETE("url/{id}")
//    Call<ObjResponse> delete(@Path("id") String id);
}
