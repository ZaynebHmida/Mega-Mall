package com.anypli.megamall.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.anypli.megamall.models.AccountData;
import com.anypli.megamall.models.NavigationRecyclerViewCustomAdapter;
import com.anypli.megamall.R;
import com.anypli.megamall.contracts.NavigationContract.NavigationPresenterItf;
import com.anypli.megamall.contracts.NavigationContract.NavigationViewItf;
import com.anypli.megamall.presenters.NavigationPresenterImpl;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , NavigationViewItf {
    //TODO : change the location of these 2 Attributs
    public static final int KEEPMELOGGEDIN = 0 ;
    public static final int LOGGEDOUT = 1 ;
    //

    private static  final int CURRENTYTASK_NEWS=0;
    private static  final int CURRENTYTASK_SEARCH=1;

    private NavigationPresenterItf mPresenter ;
    private NavigationRecyclerViewCustomAdapter mAdapter ;
    private RecyclerView mRecyclerView ;
    private SearchView mSearchView;
    private int mCurrentTask= CURRENTYTASK_NEWS ;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        this.initscreen() ;

    }


    private void initscreen() {
        //init toolbar
        Toolbar toolbar =  findViewById(R.id.navigationtoolbar);
        setSupportActionBar(toolbar);
        toolbar.addView(getLayoutInflater().inflate(R.layout.navigation_toolbar_custom_view_layout,toolbar,false));

        //init Navigation side menu
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //init search view
        mSearchView = findViewById(R.id.searchbarview);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mPresenter.GetSearchResult("%"+query+"%");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false ;
            }
        });


        //init Presenter
        mPresenter = new NavigationPresenterImpl(this);

        //init the RecyclerView
        mRecyclerView = findViewById(R.id.listcontainer);
        mAdapter = new NavigationRecyclerViewCustomAdapter(null);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mPresenter.GetAccountData();
        mPresenter.GetNewFeed();



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else if(mCurrentTask==CURRENTYTASK_SEARCH){
            mSearchView.clearFocus();
            mSearchView.setQuery("",false);
            mPresenter.GetNewFeed();
        }else {
            setResult(KEEPMELOGGEDIN);
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent ;

        if (id == R.id.LatestFeed) {
            mSearchView.clearFocus();
            mSearchView.setQuery("",false);
            mPresenter.GetNewFeed();
        } else if (id == R.id.BoutiquesListing) {
            intent = new Intent(this,MyBoutiquesActivity.class);
            startActivity(intent);
        } else if (id == R.id.LocateBoutiques) {
            intent=new Intent(this,AR_Activity.class);
            startActivity(intent);
        } else if (id == R.id.MyAccount) {
            intent = new Intent(this,AccountActivity.class);
            startActivity(intent);
        } else if (id == R.id.LogOut) {
            mPresenter.LogOut();
            setResult(LOGGEDOUT);
            finish();
        }

        DrawerLayout drawer =
                findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void showItemList(Bundle[] in) {
        mAdapter.setDataSet(in);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showSearchResult(Bundle[] res) {
        mAdapter.setDataSet(res);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateNavigationMenuHeader(AccountData data) {
        NavigationView sidemenu = findViewById(R.id.nav_view);
        View headerView = sidemenu.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.usernametextview);
        if(data!=null) {
            navUsername.setText(data.getUsername());
            if(data.getIconUrl()!=null && !data.getIconUrl().equals(""))
                Picasso.get().load(data.getIconUrl())
                        .into((CircleImageView) headerView.findViewById(R.id.usericonimageview));
        }
    }
}
