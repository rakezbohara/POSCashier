package com.app.rakez.poscashier;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CashierHome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TableAdapter adapter;
    private RecyclerView rv;
    private ArrayList<String> tableNo = new ArrayList<>();
    private List<TableView> tableList;

    private SwipeRefreshLayout swipeRefreshLayout;
    String ipAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences ipPref = getApplicationContext().getSharedPreferences("MyIP", 0);
        ipAddress = ipPref.getString("IPAddress"," ");

        tableList = new ArrayList<>();
        makeJsonArrayRequest();
        rv = (RecyclerView)findViewById(R.id.rv);
        adapter = new TableAdapter(tableList,this);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        rv.setLayoutManager(mLayoutManager);
        rv.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                makeJsonArrayRequest();
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cashier_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.logout){
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPrefCashier", 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.commit();
            Intent in = new Intent(getApplicationContext(),ScrollingActivity.class);
            startActivity(in);
            finish();
        }
        if (id == R.id.setIP) {
            Intent in = new Intent(getApplicationContext(),setIP.class);
            startActivity(in);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void makeJsonArrayRequest(){
       /* final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading");
        pDialog.show();
     */

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, "http://"+ipAddress+"/orderapp/tableCheckOut.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                tableList.clear();
                tableNo.clear();
                Log.d("size of the","Sizw is rakjsdifns response "+response.length());
                for(int i = 0;i<response.length();i++){
                    try {
                        JSONObject table = (JSONObject) response.get(i);
                        String num = table.getString("id");
                        Log.d("size of the","Data is "+num );
                        tableNo.add(num);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                Log.d("size of the","Data is "+tableNo.size());
                //pDialog.hide();
                prepareData();
                swipeRefreshLayout.setRefreshing(false);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //pDialog.hide();
                swipeRefreshLayout.setRefreshing(false);

            }
        });
        AppController.getInstance().addToRequestQueue(req);


    }

    private void prepareData() {
        for(int  i = 0 ; i < tableNo.size() ; i++){
            tableList.add(new TableView(tableNo.get(i)));
        }
        adapter.notifyDataSetChanged();
    }


    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
