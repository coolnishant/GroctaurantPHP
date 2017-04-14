package com.shubhangi.groctaurantphp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shubhangi Bharti on 03-Apr-17.
 */
public class MainActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private ListView lv;


    ArrayList<String> recipeList;
    HashMap<String, String> recipe;

    private static String url = "http://karmakitchen.in/groctaurant_merchant/grocem/a/b/z/merchant/recipelist.php";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recipeList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.recipeListView);

        new GetRecipes().execute();

    }

    private class GetRecipes extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {


            // Making a request to url and getting response
            String jsonStr ="";

                    Log.e(MainActivity.class.getSimpleName(), "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray recipe_name = jsonObj.getJSONArray("recipe_name");
                    for (int i = 0; i < recipe_name.length(); i++) {
                        JSONObject c = recipe_name.getJSONObject(i);

                        String name = c.getString("recipe_name");
                        String serving = c.getString("serving");
                        String price = c.getString("price");

                        recipe = new HashMap<>();

                        // adding each child node to HashMap key => value
                        recipe.put("name", name);
                        recipe.put("serving", serving);
                        recipe.put("price", price);

                        //recipeList.add(recipe);
                        return null;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        /**
         * Updating parsed JSON data into ListView
         * */
//        ListAdapter adapter = new ArrayAdapter<>()(
//                MainActivity.this, ,
//                R.layout.list_item, new String[]{"name", "serving",
//                "price"}, new int[]{R.id.name,
//                R.id.serving, R.id.price});

       // lv.setAdapter(adapter);
    }

}
}