package com.example.git_run;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.git_run.adapters.GitQueryAdapter;
import com.example.git_run.model.QueryModel;
import com.example.git_run.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

   ArrayList<QueryModel>list;
   TextView mErrorMessageDisplay;
     ProgressBar mLoadingIndicator;
   EditText mSearchBoxEditText;
     RecyclerView recyclerView;
    GitQueryAdapter queryAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mErrorMessageDisplay=findViewById(R.id.empty_data);
       mLoadingIndicator=findViewById (R.id.progress);
        mSearchBoxEditText=findViewById(R.id.search_field);
        recyclerView=findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);
        list=new ArrayList<>();
        queryAdapter=new GitQueryAdapter(this,list);
        recyclerView.setAdapter(queryAdapter);
    }
    private void makeGithubSearchQuery() {
        String githubQuery = mSearchBoxEditText.getText().toString();
        URL githubSearchUrl = NetworkUtils.buildUrl(githubQuery);
        new GithubQueryTask().execute(githubSearchUrl);
    }
    class GithubQueryTask extends AsyncTask<URL,Void,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String githubSearchResults = null;
            try {
                githubSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return githubSearchResults;
        }

        @Override
        protected void onPostExecute(String githubSearchResults) {
            super.onPostExecute(githubSearchResults);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (githubSearchResults != null && !githubSearchResults.equals("")) {
                showJsonDataView();
                QueryModel model;
                try {
                    JSONObject basejson=new JSONObject(githubSearchResults);
                   // mSearchResultsTextView.setText(basejson.getString("total_count"));
                    JSONArray basearray=basejson.getJSONArray("items");
                    for(int i=0;i<basearray.length();i++)
                    {
                        model=new QueryModel();
                        JSONObject object=basearray.getJSONObject(i);
                        model.setWeb_url(object.getString("html_url"));
                        model.setDesc(object.getString("description"));
                        model.setProject_name(object.getString("name"));
                        model.setAuthor(object.getString("full_name").split("/")[0]);
                        model.setSearch_text(mSearchBoxEditText.getText().toString());
                        list.add(model);
                    }

                    recyclerView.setAdapter(new GitQueryAdapter(getApplicationContext(),list));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                showErrorMessage();
            }
        }
    }
    private void showJsonDataView() {
        // First, make sure the error is invisible
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        // Then, make sure the JSON data is visible
        recyclerView.setVisibility(View.VISIBLE);
    }
    private void showErrorMessage() {
        // First, hide the currently visible data
        recyclerView.setVisibility(View.INVISIBLE);
        // Then, show the error
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_search) {
           list.clear();
           queryAdapter.notifyDataSetChanged();
            makeGithubSearchQuery();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
