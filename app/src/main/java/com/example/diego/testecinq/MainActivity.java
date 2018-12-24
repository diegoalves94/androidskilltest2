package com.example.diego.testecinq;

import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.diego.testecinq.adapter.UsuarioAdapter;
import com.example.diego.testecinq.extras.Utils;
import com.example.diego.testecinq.fragments.HomeFragment;
import com.example.diego.testecinq.fragments.ListaAlbumFragment;
import com.example.diego.testecinq.models.User;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        HomeFragment.OnFragmentInteractionListener,
        ListaAlbumFragment.OnFragmentInteractionListener {

    //region Views
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.main_content)
    FrameLayout mainContent;
    @BindView(R.id.view6)
    View view6;
    @BindView(R.id.linearLayout2)
    LinearLayout linearLayout2;
    @BindView(R.id.homeMenuItem)
    ImageButton homeMenuItem;
    @BindView(R.id.albumMenuItem)
    ImageButton albumMenuItem;
    //endregion

    boolean HOME = false;
    boolean ALBUM = false;

    UsuarioAdapter adapter;
    List<User> users = new ArrayList<>();
    List<String> names = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        FragmentManager fm = getSupportFragmentManager();

        HomeFragment homeFragment = HomeFragment.newInstance("", "");
        fm.beginTransaction().replace(R.id.main_content, homeFragment).commit();
        HOME = true;
        menuHandler();

        homeMenuItem.setOnClickListener(this);
        albumMenuItem.setOnClickListener(this);
    }


    private void menuHandler() {
        if (HOME) {
            homeMenuItem.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_home_on));
            homeMenuItem.setBackgroundColor(ContextCompat.getColor(this, R.color.lightPrimary));
            Utils.toolbarMain(this, "Home", toolbar, false);
        } else {
            homeMenuItem.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_home_off));
            homeMenuItem.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        }

        if (ALBUM) {
            albumMenuItem.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_photo_library_on));
            albumMenuItem.setBackgroundColor(ContextCompat.getColor(this, R.color.lightPrimary));
            Utils.toolbarMain(this, "Lista de albums", toolbar, false);
        } else {
            albumMenuItem.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_photo_library_off));
            albumMenuItem.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        }
    }

    @Override
    public void onClick(View v) {
        FragmentManager fm = getSupportFragmentManager();

        //HOME MENU
        if (v.getId() == R.id.homeMenuItem) {
            HOME = true;
            ALBUM = false;

            menuHandler();

            HomeFragment homeFragment = HomeFragment.newInstance("", "");
            fm.beginTransaction().replace(R.id.main_content, homeFragment).commit();
        }

        //ALBUM MENU
        if (v.getId() == R.id.albumMenuItem) {
            HOME = false;
            ALBUM = true;

            menuHandler();

            ListaAlbumFragment albumFragment = ListaAlbumFragment.newInstance("", "");
            fm.beginTransaction().replace(R.id.main_content, albumFragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

//        MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
//            @Override
//            public boolean onMenuItemActionExpand(MenuItem item) {
//                return false;
//            }
//
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem item) {
//                return false;
//            }
//        };
//
//        MenuItem menuItem = menu.findItem(R.id.search);
//        SearchView searchView = (SearchView) menuItem.getActionView();
//        searchView.setOnQueryTextListener(this);
//        searchItem.setOnActionExpandListener(onActionExpandListener);

        return false;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle item selection
//        switch (item.getItemId()) {
//            case R.id.sair:
//                Toast.makeText(this, "saiu", Toast.LENGTH_SHORT).show();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

//    //FILTRO
//    @Override
//    public boolean onQueryTextSubmit(String query) {
//        return false;
//    }
//
//    @Override
//    public boolean onQueryTextChange(String newText) {
////        String userInput = newText.toLowerCase();
////        List<String> newList = new ArrayList<>();
////
////        for (String name : names) {
////            if(name.toLowerCase().contains(userInput)){
////                newList.add(name);
////            }
////        }
////        adapter.updateList(newList);
//
//        return false;
//    }
}
