package org.twistedappdeveloper.opcclient;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.Attributes;
import org.twistedappdeveloper.opcclient.PianoTerra.Cucina;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import OpcUtils.ConnectionThread.ThreadWrite;
import OpcUtils.ManagerOPC;
import OpcUtils.SessionElement;

public class ComandiRapidi extends AppCompatActivity {
    Button btnopentapp, btnclosetapp, btnopenluciest, btncloseluciest, btncloseluciint;
    ManagerOPC managerOPC;
    SessionElement sessionElement;
    int session_position;
    String url1a = "|var|CODESYS Control Win V3 x64.Application.PLC_VAR.LUCI[";
    String url1b = "|var|CODESYS Control Win V3 x64.Application.PLC_VAR.TAPP[";
    String url2a = "].SR";
    String url2c = "].APERTURA";
    Variant value_write;
    int Token=0;
    List<Integer> luciEsterne = new ArrayList<>();

    public void iniz (List a){
        a.add(31);
        for(int i=41; i<=52; i++){
            a.add(i);
        }
        a.add(57);
        a.add(58);
    }

    public void createWrite (String a, short b){
        value_write = new Variant(b);
        ThreadWrite t;
        t = new ThreadWrite(sessionElement.getSession(), 4, url1b + a + url2c , Attributes.Value, value_write);
        @SuppressLint("HandlerLeak") Handler handler = new Handler() {};
        t.start(handler);
    }

    public void createWrite (String a, Boolean b){
        value_write = new Variant(b);
        ThreadWrite t;
        t = new ThreadWrite(sessionElement.getSession(), 4, url1a + a + url2a , Attributes.Value, value_write);
        @SuppressLint("HandlerLeak") Handler handler = new Handler() {};
        t.start(handler);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comandi_rapidi);

        session_position = getIntent().getIntExtra("sessionPosition", -1);
        if (session_position < 0) {
            Toast.makeText(ComandiRapidi.this, R.string.Errore, Toast.LENGTH_SHORT).show();
            finish();
        }

        iniz(luciEsterne);

        btnopentapp = findViewById(R.id.btnopentapp);
        btnclosetapp = findViewById(R.id.btnclosetapp);
        btnopenluciest = findViewById(R.id.btnopenluciest);
        btncloseluciest = findViewById(R.id.btncloseluciest);
        btncloseluciint = findViewById(R.id.btncloseluciint);

        managerOPC = ManagerOPC.getIstance();
        sessionElement = managerOPC.getSessions().get(session_position);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnclosetapp:
                        for(int i=0; i<20; i++){
                            createWrite(Integer.toString(i), (short) 0);
                        }
                        break;
                    case R.id.btnopentapp:
                        for(int i=0; i<20; i++){
                            createWrite(Integer.toString(i), (short) 100);
                        }
                        break;
                    case R.id.btnopenluciest:
                        for(int i=0; i<luciEsterne.size(); i++){
                            createWrite(Integer.toString(luciEsterne.get(i)), true);
                        }
                        break;
                    case R.id.btncloseluciest:
                        for(int i=0; i<luciEsterne.size(); i++){
                            createWrite(Integer.toString(luciEsterne.get(i)), false);
                        }
                        break;
                    case R.id.btncloseluciint:
                        for(int i=0; i<60; i++){
                            for(int j=0; j<luciEsterne.size(); j++){
                                if(i==luciEsterne.get(j))
                                    Token=1;
                            }
                            if (Token == 0)
                                createWrite(Integer.toString(i), false);
                            else {
                                Token=0;
                            }
                        }
                        break;
                }
            }
        };

        btnopentapp.setOnClickListener(listener);
        btnclosetapp.setOnClickListener(listener);
        btnopenluciest.setOnClickListener(listener);
        btncloseluciest.setOnClickListener(listener);
        btncloseluciint.setOnClickListener(listener);
    }
}