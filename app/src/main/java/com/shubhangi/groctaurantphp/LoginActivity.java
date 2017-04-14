package com.shubhangi.groctaurantphp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class LoginActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    private static String url_login = "http://karmakitchen.in/groctaurant_merchant/grocem/a/b/z/merchant/login.php";

    EditText MPIN, MID;
    Button bLogin;
    JSONParser jsonParser = new JSONParser();

    String merchant_id, merchant_pin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        MID = (EditText) findViewById(R.id.etMID);
        MPIN = (EditText) findViewById(R.id.etMPIN);
        bLogin = (Button) findViewById(R.id.bSignIn);

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                merchant_id = MID.getText().toString();
                merchant_pin = MPIN.getText().toString();
                new Login().execute(merchant_id,merchant_pin);
            }
        });

    }


    class Login extends AsyncTask<String,String ,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Login In..");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String id = strings[0];
            String pin = strings[1];

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("mid", id));
            params.add(new BasicNameValuePair("mpin", pin));
            Log.e("heyNish: id pin", id + " " + pin);

//            HttpClient httpClient = new DefaultHttpClient();
//
//            HttpPost httpPost = new HttpPost(url_login);
//
//            try {
//                httpPost.setEntity(new UrlEncodedFormEntity(params));
//
//                HttpResponse response = httpClient.execute(httpPost);
//                Log.e("jsonstring ", response.toString());
//                return response.toString();
//
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            } catch (ClientProtocolException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//

            URL url;
            StringBuilder response = new StringBuilder();
            try {
                url = new URL(url_login);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
//                conn.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`
                conn.connect();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os));

//                Log.e("json",jsonObject.toString());
                writer.write(params.toString());
                writer.flush();
                writer.close();
                os.close();
                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    Log.e("testerhere","HttpOk");
                    BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line =null;
                    while((line = br.readLine()) != null)
                    {
                        // Append server response in string
                        response.append(line + "\n");
                    }
//                response = br.readLine();
                    Log.e("hereea",response.toString());
                    conn.disconnect();
                }
                else {
                    response.append("Error while Sending");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("testernishant","herere"+e);
            }

            return response.toString().trim();
//            return null;
    }

        @Override
        protected void onPostExecute(String feed) {
            progressDialog.dismiss();
            Log.e("jsonstring1 ",feed);

            try {
//                JSONObject JSON = new JSONObject(feed);

                org.json.JSONArray resArray = new org.json.JSONArray(feed);
                Log.e("resA:",resArray.toString());
                for (int i=0; i< resArray.length(); i++){
                    JSONObject e = resArray.getJSONObject(i);
//                    String s = e.getString("success");
                    JSONObject object = new JSONObject(e.toString());
                    Log.e("Inside",object.getString("success"));
                    if(object.getString("success").contains("true")){
                        Toast.makeText(LoginActivity.this, "Successful", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("merchant_id",object.getString("merchant_id"));
                        intent.putExtra("merchant_name", object.getString("merchant_name"));
                        intent.putExtra("merchant_pin", object.getString("merchant_pin"));
                        startActivity(intent);
                    }else if(object.getString("success").contains("false")){
                        Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(LoginActivity.this, "NO Internet COnnection!!!!", Toast.LENGTH_LONG).show();
                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}
