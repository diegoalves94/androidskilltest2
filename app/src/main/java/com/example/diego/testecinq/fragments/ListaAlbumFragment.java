package com.example.diego.testecinq.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.diego.testecinq.R;
import com.example.diego.testecinq.adapter.AlbumAdapter;
import com.example.diego.testecinq.models.Album;
import com.example.diego.testecinq.retrofit.ApiManager;
import com.example.diego.testecinq.retrofit.CustomCallback;
import com.example.diego.testecinq.retrofit.RequestInterfaceUser;
import com.google.android.material.appbar.AppBarLayout;

import org.json.JSONException;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListaAlbumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListaAlbumFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final Object TAG = "";
    @BindView(R.id.listaAlbum)
    RecyclerView listaAlbum;
    Unbinder unbinder;

    ProgressDialog pd;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    AlbumAdapter adapter;
    private ArrayList<Album> albuns = new ArrayList<>();

    public ListaAlbumFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListaAlbumFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListaAlbumFragment newInstance(String param1, String param2) {
        ListaAlbumFragment fragment = new ListaAlbumFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lista_album, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        pd = new ProgressDialog(getActivity());
        requestGetListaAlbums();
    }

    private void requestGetListaAlbums() {
        try {
            pd.setMessage("Carregando...");
            pd.setCancelable(false);
            pd.show();

            new ApiManager(getActivity())
                    .getRetrofit()
                    .create(RequestInterfaceUser.class)
                    .albuns()
                    .enqueue(new CustomCallback<ArrayList<Album>>(getActivity(), new CustomCallback.OnResponse<ArrayList<Album>>() {
                        @Override
                        public void onResponse(ArrayList<Album> response) {
                            if (response != null) {
                                for (int i = 0; i < response.size(); i++) {
                                    albuns.add(response.get(i));
                                }

                                setAdapter(albuns);
                            }
                        }

                        @Override
                        public void onFailure(Throwable t, int errorcode) {
                            t.printStackTrace();
                            Toast.makeText(getActivity(), "Ocorreu um erro, tente novamente", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onRetry(Throwable t) throws JSONException {
                            requestGetListaAlbums();
                        }
                    }));

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Ocorreu um erro, tente novamente", Toast.LENGTH_SHORT).show();
        }
    }

    private void setAdapter(ArrayList<Album> albums) {
        RecyclerView.LayoutManager layout = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        adapter = new AlbumAdapter(albums, getContext());
        listaAlbum.setLayoutManager(layout);
        listaAlbum.setAdapter(adapter);

        pd.dismiss();
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
