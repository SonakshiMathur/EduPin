package com.sonakshi.edupin;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;



import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DashFragment extends Fragment {


    String token;
    public int flag = 0;

    public class GetSchoolStats extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String urlStr = strings[0];

            try {
                HttpClient httpclient = new DefaultHttpClient();

                HttpGet httpGet = new HttpGet(urlStr);
                httpGet.setHeader("x-access-token", token);
                HttpResponse httpResponse = httpclient.execute(httpGet);
                InputStream inputStream = httpResponse.getEntity().getContent();
                String result = "";
                if (inputStream != null)
                    result = convertInputStreamToString(inputStream);
                else
                    result = "Did not work!";
                Log.i("resultInAsync", result);
                flag = 1;
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    private static String convertInputStreamToString(InputStream inputStream) throws
            IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result2 = "";
        while ((line = bufferedReader.readLine()) != null)
            result2 += line;
        inputStream.close();
        return result2;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.app_bar_user_dash_board, container, false);

        if (getArguments() != null) {
            token = getArguments().getString("token");
        }

        String jsonData = "";
        GetSchoolStats stats = new GetSchoolStats();
        try {
            jsonData = stats.execute("http://13.127.45.172/api/school/getSchoolStats").get();
            while (flag == 0)
                continue;
            Log.i("jsondata", jsonData);
            JSONObject jsonObject = new JSONObject(jsonData);
            String message = jsonObject.getString("message");
            JSONArray arr = new JSONArray(message);
            JSONObject msgObject = arr.getJSONObject(0);
            String stud_count = msgObject.getString("net_stu_count");
            String net_fac_count = msgObject.getString("net_fac_count");
            String sms_balance = msgObject.getString("balance");
            String sent_sms = msgObject.getString("net_comm_count");
            String sms_perc = String.format("%.2f", ((Float.parseFloat(sent_sms)) / (Float.parseFloat(sms_balance)) * 100));
            float num = Float.parseFloat(msgObject.getString("space"));
            String cloud_used = String.format("%.2f", (num / 1024) / 1024);
            float num1 = Float.parseFloat(msgObject.getString("quota"));
            Log.i("quota", Float.toString(num1));
            String quota = String.format("%.2f", (num1 / 1024) / 1024);
            String storage_percent = String.format("%.2f", (num / num1) * 100);
            TextView tx = rootView.findViewById(R.id.storage_used);
            tx.setText("Used: " + cloud_used + " MB");
            TextView a = rootView.findViewById(R.id.storage_rem);
            a.setText("Total: " + quota + " MB");
            TextView b = rootView.findViewById(R.id.Storage_perc);
            b.setText(storage_percent + "%");
            ProgressBar progressBar1 = rootView.findViewById(R.id.progressBar1);
            progressBar1.setProgress((int) Float.parseFloat(storage_percent));

            Log.i("in ac","in dash");

            TextView q = rootView.findViewById(R.id.sms_used);
            q.setText("Sent: " + sent_sms);
            TextView g = rootView.findViewById(R.id.sms_rem);
            g.setText("Remaining: " + sms_balance);
            TextView d = rootView.findViewById(R.id.mmy_perc);
            d.setText(sms_perc + "%");
            ProgressBar progressBar = rootView.findViewById(R.id.progressBar);
            progressBar.setProgress((int) Float.parseFloat(sms_perc));

            TextView fac = rootView.findViewById(R.id.no_of_FAC);
            fac.setText(net_fac_count);
            TextView stud = rootView.findViewById(R.id.no_of_stud);
            stud.setText(stud_count);
            ProgressBar p=rootView.findViewById(R.id.progressBar);
            ProgressBar p1=rootView.findViewById(R.id.progressBar1);

            p.getProgressDrawable().setColorFilter(Color.parseColor("#30A7CD"), PorterDuff.Mode.SRC_IN);
            p1.getProgressDrawable().setColorFilter(Color.parseColor("#30A7CD"), PorterDuff.Mode.SRC_IN);
            GraphView graph = (GraphView) rootView.findViewById(R.id.graph);
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                    new DataPoint(0, 1),
                    new DataPoint(1, 5),
                    new DataPoint(2, 3),
                    new DataPoint(3, 2),
                    new DataPoint(4, 6)
            });
            graph.addSeries(series);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootView;
    }
}



