package com.example.diego.testecinq.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diego.testecinq.CadastroActivity;
import com.example.diego.testecinq.EditarUsuarioActivity;
import com.example.diego.testecinq.LoginActivity;
import com.example.diego.testecinq.R;
import com.example.diego.testecinq.SplashActivity;
import com.example.diego.testecinq.adapter.AlbumAdapter;
import com.example.diego.testecinq.adapter.UsuarioAdapter;
import com.example.diego.testecinq.dao.LocalDbImplement;
import com.example.diego.testecinq.dao.sqlite.UserContract;
import com.example.diego.testecinq.dao.sqlite.UserDBHelper;
import com.example.diego.testecinq.extras.Utils;
import com.example.diego.testecinq.models.EventBus.FinishEvent;
import com.example.diego.testecinq.models.User;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textfield.TextInputEditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SearchView.OnQueryTextListener;
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
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements OnQueryTextListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.nomeUsuario)
    TextView nomeUsuario;
    @BindView(R.id.btnAdicionarUsuario)
    TextView btnAdicionarUsuario;
    @BindView(R.id.listaUsuarios)
    RecyclerView listaUsuarios;
    Unbinder unbinder;


    UsuarioAdapter adapter;
    User usuario, user;
    List<User> users = new ArrayList<>();
    List<String> names = new ArrayList<>();


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init(){
        user = new LocalDbImplement<User>(getActivity()).getDefault(User.class);
        //USUÁRIO LOGADO
        if (user != null) {
            nomeUsuario.setText("Olá, "+user.getNome());
        }

        //LOAD USUÁRIO
        getListaUsuarios();

        //BTN ADICIONAR CONTATO
        btnAdicionarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CadastroActivity.class)
                        .putExtra("isAdd", true));
            }
        });
    }

    private void getListaUsuarios() {
        UserDBHelper userDBHelper = new UserDBHelper(getActivity());
        SQLiteDatabase db = userDBHelper.getReadableDatabase();

        Cursor cursor = userDBHelper.getUsers(db);

        User user;
        while (cursor.moveToNext()){
            String id = Integer.toString(cursor.getInt(cursor.getColumnIndex(UserContract.UserEntry.ID)));
            String name = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.NOME));
            String email = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.EMAIL));
            String senha = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.SENHA));

            user = new User(Integer.valueOf(id), name, email, senha);
            users.add(user);
        }

        userDBHelper.close();

        //set adapter
        setAdapter(users);
    }

    private void setAdapter(final List<User> users) {
        if (users != null) {
            for (int i = 0; i < users.size(); i++) {
                names.add(users.get(i).getNome());
            }

            RecyclerView.LayoutManager layout = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
            adapter = new UsuarioAdapter(names, getActivity(), new UsuarioAdapter.UserInterface() {
                @Override
                public void onEditUserClicked(int position) {
                    for (int i = 0; i < users.size(); i++) {
                        if (i == position) {
                            usuario = new User(users.get(i).getId(), users.get(i).getNome(), users.get(i).getEmail(), users.get(i).getSenha());

                            startActivity(new Intent(getActivity(), EditarUsuarioActivity.class)
                                    .putExtra("usuario", usuario));

                            break;
                        }
                    }
                }

                @Override
                public void onDeleteUserClicked(int position) {
                    dialogDelete(position);
                }
            });
            listaUsuarios.setLayoutManager(layout);
            listaUsuarios.setAdapter(adapter);
        }
    }

    //DIALOGS
    private void dialogSair() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_sair);

        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView btnCancel = dialog.findViewById(R.id.btnCancelarSair);
        TextView btnSair = dialog.findViewById(R.id.btnSair);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LocalDbImplement<User>(getActivity()).clearObject(User.class);
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        dialog.show();

    }

    private void dialogDelete(final int position) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_delete);

        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView btnCancel = dialog.findViewById(R.id.btnCancelar);
        TextView btnExcluir = dialog.findViewById(R.id.btnExcluir);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GET USER ID
                int id = 0;
                for (int i = 0; i < users.size(); i++) {
                    if (i == position) {
                        if (user.getId() == users.get(i).getId()) {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), "Você não pode excluir um usuário que está logado!!", Toast.LENGTH_SHORT).show();
                            break;
                        }else{
                            id = users.get(i).getId();
                            //DELETE DA LISTA
                            UserDBHelper userDBHelper = new UserDBHelper(getActivity());
                            SQLiteDatabase db = userDBHelper.getWritableDatabase();

                            userDBHelper.deleteUser(id, db);
                            userDBHelper.close();

                            Toast.makeText(getActivity(), "Usuário excluído!!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            users.clear();
                            names.clear();
                            getListaUsuarios();
                            break;
                        }
                    }
                }
            }
        });

        dialog.show();
    }

    //TOOLBAR MENU
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.sair:{
                dialogSair();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //FILTRO
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput = newText.toLowerCase();
        List<String> newList = new ArrayList<>();

        for (String name : names) {
            if(name.toLowerCase().contains(userInput)){
                newList.add(name);
            }
        }
        adapter.updateList(newList);

        return false;
    }

    //EventBus
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFinishEvent(FinishEvent finishEvent) {
        if (finishEvent != null) {
            if (finishEvent.isFinished()){
                users.clear();
                names.clear();

                init();
            }
        }
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
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }
}
