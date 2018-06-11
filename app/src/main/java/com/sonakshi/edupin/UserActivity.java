package com.sonakshi.edupin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class UserActivity extends AppCompatActivity {

    TextView usersname;
    TextView useremail;
    TextView usersphone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        usersname=(TextView)findViewById(R.id.usersname);
        useremail=(TextView)findViewById(R.id.useremail);
        usersphone=(TextView)findViewById(R.id.userphone);
        Toast.makeText(this, "The token is saved in persistent storage!\nSee it in a toast when you restart this app!", Toast.LENGTH_LONG).show();
        Intent intent=getIntent();
        usersname.setText(intent.getStringExtra("username").toString());
        useremail.setText(intent.getStringExtra("email").toString());
        usersphone.setText(intent.getStringExtra("phone").toString());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }
}
