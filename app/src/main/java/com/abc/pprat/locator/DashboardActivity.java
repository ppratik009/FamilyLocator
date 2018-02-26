package com.abc.pprat.locator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    TextView tvName;
    TextView tvEmail;

    // index to identify current nav menu item
    // tags used to attach the fragments
    public static int navItemIndex = 0;
    private static final String TAG_HOME = "Home";
    private static final String TAG_TRACK = "Track";
    private static final String TAG_JOIN = "Join";
    private static final String TAG_SEND_INVITATION = "Invitation";
    private boolean shouldLoadHomeFragOnBackPress = true;
    public static String CURRENT_TAG = TAG_HOME;
    Handler mHandler;
    private SharedPreferences sharedPreferences;

    private String name;
    private String email;
    private String status;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        sharedPreferences=getSharedPreferences("RefLocator",MODE_PRIVATE);

        //setToolbar
        setToolbar();

        mHandler = new Handler();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        View view =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView)view.findViewById(R.id.tvUserName);
        TextView nav_email = (TextView)view.findViewById(R.id.tvUserEmail);
        nav_user.setText(sharedPreferences.getString("Name","Name"));
        nav_email.setText(sharedPreferences.getString("Email","Email"));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        //SetNavigationView
        setUpNavigationView();

        if (savedInstanceState == null) {
           // Toast.makeText(this, "Testing", Toast.LENGTH_SHORT).show();
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }


    }

    private void setUpNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle navigation view item clicks here.
                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    navItemIndex = 0;
                    CURRENT_TAG = TAG_HOME;

                } else if (id == R.id.nav_track) {
                    navItemIndex = 1;
                    CURRENT_TAG = TAG_TRACK;


                } else if (id == R.id.nav_join) {
                    navItemIndex = 2;
                    CURRENT_TAG = TAG_JOIN;
                } else if (id == R.id.nav_invitation) {
                    navItemIndex = 3;
                    CURRENT_TAG = TAG_SEND_INVITATION;
                } else if (id == R.id.nav_share) {

                    Intent sharingIntent=new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT,"Family Locator");
                    sharingIntent.putExtra(Intent.EXTRA_TEXT,"Hey! Just found the top 1 location tracking app available. Download the app from https://play.google.com/apps/testing/com.abc.pprat.locator and then enter the invitation code to join my group.");
                    startActivity(sharingIntent.createChooser(sharingIntent,"Share Via"));

                } else if (id == R.id.nav_rate) {

                    Intent intent=new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://play.google.com/apps/testing/com.abc.pprat.locator"));
                    startActivity(intent);
                }else {
                    navItemIndex = 0;
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

               // loadFragment on navigation item click
                loadHomeFragment();
                return true;
            }
        });
    }

    private void loadHomeFragment() {

        // selecting appropriate nav menu item
        //electNavMenu();

        // set toolbar title
        //setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        // if user selct the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mRunnable=new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments

                Fragment fragment=getHomeFragment();
                FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame,fragment,CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();

            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mRunnable != null) {
            mHandler.post(mRunnable);
        }

        //Closing drawer on item clickf
        drawer.closeDrawers();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex)
        {
            case 0:
                if (shouldLoadHomeFragOnBackPress)
                {
                    if (navItemIndex!=0)
                    {
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        loadHomeFragment();
                    }
                }

                // home
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;

            case 1:
                GroupFragment groupFragment=new GroupFragment();
                return groupFragment;

            case 2:
                JoinFragment joinFragment=new JoinFragment();
                return joinFragment;
            case 3:
                InvitationFragment invitationFragment=new InvitationFragment();
                return invitationFragment;
            default:
               return new HomeFragment();

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_signout) {

            FirebaseAuth.getInstance().signOut();
            sharedPreferences.edit().clear().commit();
            startActivity(new Intent(DashboardActivity.this,SignInActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//
//    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

}
