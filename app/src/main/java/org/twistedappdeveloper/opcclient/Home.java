package org.twistedappdeveloper.opcclient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class Home extends AppCompatActivity {

    int session_position;
    CardView buttonPT, buttonPP, buttonComRap;
    String Url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_session);

        session_position = getIntent().getIntExtra("sessionPosition", -1);
        Url = getIntent().getStringExtra("url");

        if (session_position < 0) {
            Toast.makeText(Home.this, R.string.Errore, Toast.LENGTH_SHORT).show();
            finish();
        }


        buttonPT = findViewById(R.id.buttonPT);
        buttonPP = findViewById(R.id.buttonPP);
        buttonComRap = findViewById(R.id.buttonComRap);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.buttonPT:
                        Intent intent_pt = new Intent(Home.this, SessionPT.class);
                        intent_pt.putExtra("sessionPosition", session_position);
                        startActivity(intent_pt);
                        break;

                    case R.id.buttonPP:
                        Intent intent_pp = new Intent(Home.this, SessionPP.class);
                        intent_pp.putExtra("sessionPosition", session_position);
                        startActivity(intent_pp);
                        break;

                    case R.id.buttonComRap:
                        Intent intent_ComRap = new Intent(Home.this, ComandiRapidi.class);
                        intent_ComRap.putExtra("sessionPosition", session_position);
                        startActivity(intent_ComRap);
                        break;
                }
            }
        };

        buttonPT.setOnClickListener(listener);
        buttonPP.setOnClickListener(listener);
        buttonComRap.setOnClickListener(listener);
    }
}
