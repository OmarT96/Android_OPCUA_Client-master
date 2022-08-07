package org.twistedappdeveloper.opcclient.PianoTerra;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.ReadResponse;
import org.opcfoundation.ua.core.TimestampsToReturn;
import org.twistedappdeveloper.opcclient.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import OpcUtils.ConnectionThread.ThreadRead;
import OpcUtils.ConnectionThread.ThreadWrite;
import OpcUtils.ManagerOPC;
import OpcUtils.SessionElement;

public class Salone extends AppCompatActivity {
    ImageView imageView;
    Button btnLuce32, btnLuce33, btnLuce34, btnLuce35, btnLuce53, btnLuce54, btnLuce55, btnLuce56, btnTapp15, btnTapp16;
    ManagerOPC managerOPC;
    SessionElement sessionElement;
    int session_position;
    Variant value_write;
    String url1a = "|var|CODESYS Control Win V3 x64.Application.PLC_VAR.LUCI[";
    String url1b = "|var|CODESYS Control Win V3 x64.Application.PLC_VAR.TAPP[";
    String url2a = "].SR";
    String url2b = "].ATTUALE";
    String url2c = "].APERTURA";
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 10;
    Variant Token;
    int Dim = 0;
    int Type = 0;
    int Counter = 0;
    int idTag = 0;
    int Step = 0;
    List<String> txtButton = new ArrayList<String>();

    List<List<Integer>> dmtStanza = Arrays.asList(Arrays.asList(0, 32),
                                    Arrays.asList(0, 33),
                                    Arrays.asList(0, 34),
                                    Arrays.asList(0, 35),
                                    Arrays.asList(0, 53),
                                    Arrays.asList(0, 54),
                                    Arrays.asList(0, 55),
                                    Arrays.asList(0, 56),
                                    Arrays.asList(1, 15),
                                    Arrays.asList(1, 16),
                                    Arrays.asList(1, 17));


    // |var|CODESYS Control Win V3 x64.Application.PLC_VAR.LUCI[55].PB_PC

    public void iniz (List a){
        for (int i=0; i<15; i++){
            a.add(i, "###");
        }
    }



    public void createWrite (String a, Boolean b){
        value_write = new Variant(b);
        ThreadWrite t;
        t = new ThreadWrite(sessionElement.getSession(), 4, url1a + a + url2a , Attributes.Value, value_write);
        @SuppressLint("HandlerLeak") Handler handler = new Handler() {};
        t.start(handler);
    }

    public void createWrite (String a, short b){
        value_write = new Variant(b);
        ThreadWrite t;
        t = new ThreadWrite(sessionElement.getSession(), 4, url1b + a + url2c , Attributes.Value, value_write);
        @SuppressLint("HandlerLeak") Handler handler = new Handler() {};
        t.start(handler);
    }

    public void readingThread (final int type, int id){
        ThreadRead t;
        if (type==0) {
            t = new ThreadRead(sessionElement.getSession(), 1000, TimestampsToReturn.Both, 4, url1a + Integer.toString(id) + url2a, Attributes.Value);
            @SuppressLint("HandlerLeak") Handler handler = new Handler() {
                public void handleMessage(Message msg) {
                    ReadResponse res = (ReadResponse) msg.obj;
                    Token = res.getResults()[0].getValue();
                }
            };
            t.start(handler);
        }
        else {
            t = new ThreadRead(sessionElement.getSession(), 1000, TimestampsToReturn.Both, 4, url1b + Integer.toString(id) + url2b, Attributes.Value);
            @SuppressLint("HandlerLeak") Handler handler = new Handler() {
                public void handleMessage(Message msg) {
                    ReadResponse res = (ReadResponse) msg.obj;
                    Token = res.getResults()[0].getValue();
                }
            };
            t.start(handler);
        }
    }

    public void changeState (String a, String b){
        if (a=="ON")
            createWrite(b, false);
        else
            createWrite(b, true);
    }

    public void dialogTapp (final String a){
        final Dialog dialog_write = new Dialog(Salone.this, R.style.AppAlert);
        dialog_write.setContentView(R.layout.dialog_datatapp);
        final EditText edtvalue_write = dialog_write.findViewById(R.id.edtWriteData);
        Button btnokdata = dialog_write.findViewById(R.id.btnOkData);
        Button btnopen = dialog_write.findViewById(R.id.btnOpen);
        Button btnclose = dialog_write.findViewById(R.id.btnClose);
        btnokdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtvalue_write.getText().toString().length() == 0) {
                    Toast.makeText(Salone.this, R.string.InserisciValoriValidi, Toast.LENGTH_SHORT).show();
                }
                else{
                    int value = Integer.parseInt(edtvalue_write.getText().toString());
                    createWrite(a, (short) value);
                }
                dialog_write.dismiss();
            }
        });
        btnopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createWrite(a, (short) 100);
                dialog_write.dismiss();
            }
        });
        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createWrite(a, (short) 0);
                dialog_write.dismiss();
            }
        });
        dialog_write.show();
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salone);

        session_position = getIntent().getIntExtra("sessionPosition", -1);
        if (session_position < 0) {
            Toast.makeText(Salone.this, R.string.Errore, Toast.LENGTH_SHORT).show();
            finish();
        }

        imageView = findViewById(R.id.imageSalone);

        btnLuce32 = findViewById(R.id.btnLuce32);
        btnLuce33 = findViewById(R.id.btnLuce33);
        btnLuce34 = findViewById(R.id.btnLuce34);
        btnLuce35 = findViewById(R.id.btnLuce35);
        btnLuce53 = findViewById(R.id.btnLuce53);
        btnLuce54 = findViewById(R.id.btnLuce54);
        btnLuce55 = findViewById(R.id.btnLuce55);
        btnLuce56 = findViewById(R.id.btnLuce56);
        btnTapp15 = findViewById(R.id.btnTapp15);
        btnTapp16 = findViewById(R.id.btnTapp16);



        managerOPC = ManagerOPC.getIstance();
        sessionElement = managerOPC.getSessions().get(session_position);

        iniz(txtButton);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnLuce32:
                        changeState(btnLuce32.getText().toString(), "32");
                        break;
                    case R.id.btnLuce33:
                        changeState(btnLuce33.getText().toString(), "33");
                        break;
                    case R.id.btnLuce34:
                        changeState(btnLuce34.getText().toString(), "34");
                        break;
                    case R.id.btnLuce35:
                        changeState(btnLuce35.getText().toString(), "35");
                        break;
                    case R.id.btnLuce53:
                        changeState(btnLuce53.getText().toString(), "53");
                        break;
                    case R.id.btnLuce54:
                        changeState(btnLuce54.getText().toString(), "54");
                        break;
                    case R.id.btnLuce55:
                        changeState(btnLuce55.getText().toString(), "55");
                            break;
                    case R.id.btnLuce56:
                        changeState(btnLuce56.getText().toString(), "56");
                        break;
                    case R.id.btnTapp15:
                        dialogTapp("15");
                        break;
                    case R.id.btnTapp16:
                        dialogTapp("16");
                        break;
                }
            }
        };

        btnLuce32.setOnClickListener(listener);
        btnLuce34.setOnClickListener(listener);
        btnLuce34.setOnClickListener(listener);
        btnLuce35.setOnClickListener(listener);
        btnLuce53.setOnClickListener(listener);
        btnLuce54.setOnClickListener(listener);
        btnLuce55.setOnClickListener(listener);
        btnLuce56.setOnClickListener(listener);
        btnTapp15.setOnClickListener(listener);
        btnTapp16.setOnClickListener(listener);
    }

    @Override
    protected void onResume() {
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, delay);
                switch (Step) {
                    case 0:
                        Dim = dmtStanza.size();
                        Type = dmtStanza.get(Counter).get(0);
                        idTag = dmtStanza.get(Counter).get(1);
                        Step++;
                        break;
                    case 1:
                        Token = null;
                        readingThread(Type, idTag);
                        Step++;
                        break;
                    case 2:
                        if (Token != null) {
                            if(dmtStanza.get(Counter).get(0)==0){
                                if(Token.booleanValue()==true){
                                    txtButton.set(Counter, "ON");
                                }
                                if(Token.booleanValue()==false){
                                    txtButton.set(Counter, "OFF");
                                }
                            } else
                                txtButton.set(Counter, Token.toString()+"%");
                            Step++;
                        }
                        break;
                    case 3:
                        if (Counter < Dim-1)
                            Counter++;
                        else {
                            Counter = 0;
                            btnLuce32.setText(txtButton.get(0));
                            btnLuce33.setText(txtButton.get(1));
                            btnLuce34.setText(txtButton.get(2));
                            btnLuce35.setText(txtButton.get(3));
                            btnLuce53.setText(txtButton.get(4));
                            btnLuce54.setText(txtButton.get(5));
                            btnLuce55.setText(txtButton.get(6));
                            btnLuce56.setText(txtButton.get(7));
                            btnTapp15.setText(txtButton.get(8));
                            btnTapp16.setText(txtButton.get(9));
                        }
                        Step = 0;
                        break;
                }
            }
        }, delay);
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable); //stop handler when activity not visible super.onPause();
    }
}