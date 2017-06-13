package pj.uacnix.pointrunner;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView de,du;
    private int navid,oldId,Rid,oldRid;
    private NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        du = (TextView) header.findViewById(R.id.drawer_username);
        de = (TextView) header.findViewById(R.id.drawer_email);


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Toast.makeText(MainActivity.this,"User LOGGED IN", Toast.LENGTH_SHORT).show();
                    updateUI();
                } else {
                    Toast.makeText(MainActivity.this,"User LOGGED OUT", Toast.LENGTH_SHORT).show();
                }
            }
        };
        updateUI();
        mapFrag def = new mapFrag();
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.mainFrame, def);
        tx.commit();
        navid = 0;
        Rid = R.id.nav_Map;
        navigationView.getMenu().getItem(navid).setChecked(true);
    }

    private void updateUI() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null) {
            de.setText(user.getEmail());
            du.setText(user.getEmail().replaceAll("@.*", ""));
        }else{
            de.setText("USER IS NULL");
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            int count = getSupportFragmentManager().getBackStackEntryCount();
            if (count == 0) {
                Toast.makeText(this,"Cant go more back lol",Toast.LENGTH_LONG).show();
                moveTaskToBack(true);
            } else {
                getSupportFragmentManager().popBackStack();
                navid = oldId;
                Rid = oldRid;
                oldRid = 0;
                navigationView.getMenu().getItem(navid).setChecked(true);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Rid = item.getItemId();
        if(oldRid == Rid) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }else if(Rid == R.id.nav_Map){
            navid = 0;
            oldRid = Rid;
            mapFrag frag = new mapFrag();
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.replace(R.id.mainFrame, frag,"mapFrag").addToBackStack("old");
            tx.commit();
        } else if (Rid == R.id.nav_userAssignments) {
            navid = 1;
            oldRid = Rid;
            MyAssignments frag = new MyAssignments();
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.replace(R.id.mainFrame, frag).addToBackStack("old");
            tx.commit();
        } else if (Rid == R.id.nav_userMadeAssigments) {
            navid = 2;
            oldRid = Rid;

        } else if (Rid == R.id.nav_settings) {
            navid = 3;
            oldRid = Rid;
        } else if (Rid == R.id.nav_logout) {
            navid = 4;
            oldRid = Rid;
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(MainActivity.this,"Logging off...",Toast.LENGTH_SHORT).show();
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
