package ml223vz.dv606.rssfeeder;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import ml223vz.dv606.rssfeeder.provider.FeedDatabaseHelper;
import ml223vz.dv606.rssfeeder.views.FeedAdapter;

/**
 * TODO: Connect to content resolver and fetch mData for list
 * TODO: Add button functionality for the drawer
 * TODO: Add grouping functionality for the drawer
 */
public class FeedActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "Feedr.Main";

    private FeedAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.tip_navigation_drawer_open,
                R.string.tip_navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.view_feed_drawer);
        navigationView.setNavigationItemSelectedListener(this);

        ListView listView = (ListView) findViewById(R.id.list_feed);
        mAdapter = new FeedAdapter(this, new ArrayList<PostData>());
        listView.setAdapter(mAdapter);

        try{
            URL url = new URL("http://www.sweclockers.com/feeds/nyheter");
            AsyncTask task = new parseRSS().execute(url);
        } catch (MalformedURLException mfe){
            Log.e(TAG, "URL is wrong");
        }
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
        getMenuInflater().inflate(R.menu.main, menu);
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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // TODO: Handle clicks
        if (id == R.id.drawer_item_home) {

        } else if (id == R.id.drawer_item_saved) {

        } else if (id == R.id.drawer_item_add) {

        } else if (id == R.id.drawer_item_all) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // TODO: This should not be in this class
    private class parseRSS extends AsyncTask<URL, Void, ArrayList<PostData>> {
        protected ArrayList<PostData> doInBackground(URL... urls) {
            try {
                return RSSHandler.parseStream(urls[0]);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        protected void onProgressUpdate(Void... progress) {

        }

        protected void onPostExecute(ArrayList<PostData> result) {
            for(PostData post : result){
                mAdapter.add(post);
            }
            mAdapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), "RSS document parsed", Toast.LENGTH_LONG).show();
            // printResults(data);
        }
    }

    private void printResults(ArrayList<PostData> data) {
        if(data != null){
            int count = 0;
            for(PostData post : data){
                count++;
                Log.i(TAG, "Post #" + count);
                Log.i(TAG, "Title: " + post.getTitle());
                Log.i(TAG, "Description: " + post.getDescription());
                Log.i(TAG, "URL: " + post.getLink().toString());
            }
        } else {
            Log.e(TAG, "RSS was not parsed");
        }
    }
}
