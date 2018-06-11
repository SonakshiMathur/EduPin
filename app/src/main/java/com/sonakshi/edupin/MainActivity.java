package com.sonakshi.edupin;


import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import android.support.design.widget.TextInputLayout;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    public class GetApi extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            String urlStr=strings[0];

            try {

                HttpClient httpclient = new DefaultHttpClient();

                HttpPost httpPost = new HttpPost(urlStr);
                StringEntity se = new StringEntity(strings[1]);
                httpPost.setEntity(se);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                HttpResponse httpResponse = httpclient.execute(httpPost);
                InputStream inputStream = httpResponse.getEntity().getContent();
                String result="";
                if(inputStream != null)
                    result = convertInputStreamToString(inputStream);
                else
                    result = "Did not work!";

                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return  null;
        }


    }
    private static String convertInputStreamToString(InputStream inputStream) throws
            IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result2 = "";
        while((line = bufferedReader.readLine()) != null)
            result2 += line;
        inputStream.close();
        return result2;
    }
    public void login(View view){
        JSONObject postData = new JSONObject();
        String jsonData="";
//        ProgressDialog progress = ProgressDialog.show(this, null, null, true);
//
//        progress.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//
//
//        progress.show();

        try {
            postData.accumulate("username",((TextView) findViewById(R.id.usrusr)).getText().toString());
            postData.accumulate("password", ((TextView) findViewById(R.id.passwrd)).getText().toString());
            postData.accumulate("user_type","admin");
            jsonData=new GetApi().execute("http://13.127.45.172/api/account/authenticate", postData.toString()).get();
           if(jsonData==null){
               Log.i("ccc","null");
           }else
               Log.i("ccc",jsonData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            JSONObject jsonObject=new JSONObject(jsonData);
            String token=jsonObject.getString("token");
            if(token.equals("null")){
                Toast.makeText(this, "Invalid Details! Try Again!", Toast.LENGTH_SHORT).show();
            }
            else {
                SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("token",token);
                Log.i("rr",token);
                editor.commit();
                String message=jsonObject.getString("message");
                JSONObject msgObject=new JSONObject(message);
                String username=msgObject.getString("school_name");
                String phone=msgObject.getString("school_phone");
                String email=msgObject.getString("school_email");

                Intent intent=new Intent(getApplicationContext(),Main2Activity.class);
                intent.putExtra("username",username);
                intent.putExtra("phone",phone);
                intent.putExtra("email",email);
                intent.putExtra("token",token);
                startActivity(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        String tok=sharedPreferences.getString("token","null");
        if(!tok.equals("null")){
            Toast.makeText(this, "The token used last time :\n"+tok, Toast.LENGTH_LONG).show();
        }
    }
}
