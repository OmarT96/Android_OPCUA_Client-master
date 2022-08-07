package org.twistedappdeveloper.opcclient.PrimoPiano;

import androidx.appcompat.app.AppCompatActivity;

import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.ReadResponse;
import org.opcfoundation.ua.core.TimestampsToReturn;
import org.twistedappdeveloper.opcclient.R;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import OpcUtils.ConnectionThread.ThreadRead;
import OpcUtils.ConnectionThread.ThreadWrite;
import OpcUtils.ManagerOPC;
import OpcUtils.SessionElement;

public class CameraSingola extends AppCompatActivity {
    ImageView imageView;
    Button btnLuce3, btnLuce8, btnLuce9, btnLuce17, btnLuce18, btnTapp2, btnTapp4;
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

    List<List<Integer>> dmtStanza = Arrays.asList(Arrays.asList(0, 3),
            Arrays.asList(0, 8),
            Arrays.asList(0, 9),
            Arrays.asList(0, 17),
            Arrays.asList(0, 18),
            Arrays.asList(1, 2),
            Arrays.asList(1, 4));


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
        final Dialog dialog_write = new Dialog(CameraSingola.this, R.style.AppAlert);
        dialog_write.setContentView(R.layout.dialog_datatapp);
        final EditText edtvalue_write = dialog_write.findViewById(R.id.edtWriteData);
        Button btnokdata = dialog_write.findViewById(R.id.btnOkData);
        Button btnopen = dialog_write.findViewById(R.id.btnOpen);
        Button btnclose = dialog_write.findViewById(R.id.btnClose);
        btnokdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtvalue_write.getText().toString().length() == 0) {
                    Toast.makeText(CameraSingola.this, R.string.InserisciValoriValidi, Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_camera_singola);

        session_position = getIntent().getIntExtra("sessionPosition", -1);
        if (session_position < 0) {
            Toast.makeText(CameraSingola.this, R.string.Errore, Toast.LENGTH_SHORT).show();
            finish();
        }

        imageView = findViewById(R.id.imageSalone);

        btnLuce3 = findViewById(R.id.btnLuce3);
        btnLuce8 = findViewById(R.id.btnLuce8);
        btnLuce9 = findViewById(R.id.btnLuce9);
        btnLuce17 = findViewById(R.id.btnLuce17);
        btnLuce18 = findViewById(R.id.btnLuce18);
        btnTapp2 = findViewById(R.id.btnTapp2);
        btnTapp4 = findViewById(R.id.btnTapp4);



        managerOPC = ManagerOPC.getIstance();
        sessionElement = managerOPC.getSessions().get(session_position);

        iniz(txtButton);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnLuce3:
                        changeState(btnLuce3.getText().toString(), "3");
                        break;
                    case R.id.btnLuce8:
                        changeState(btnLuce8.getText().toString(), "8");
                        break;
                    case R.id.btnLuce9:
                        changeState(btnLuce9.getText().toString(), "9");
                        break;
                    case R.id.btnLuce17:
                        changeState(btnLuce17.getText().toString(), "17");
                        break;
                    case R.id.btnLuce18:
                        changeState(btnLuce18.getText().toString(), "18");
                        break;
                    case R.id.btnTapp2:
                        dialogTapp("2");
                        break;
                    case R.id.btnTapp4:
                        dialogTapp("4");
                        break;
                }
            }
        };

        btnLuce3.setOnClickListener(listener);
        btnLuce8.setOnClickListener(listener);
        btnLuce9.setOnClickListener(listener);
        btnLuce17.setOnClickListener(listener);
        btnLuce18.setOnClickListener(listener);
        btnTapp2.setOnClickListener(listener);
        btnTapp4.setOnClickListener(listener);
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
                            btnLuce3.setText(txtButton.get(0));
                            btnLuce8.setText(txtButton.get(1));
                            btnLuce9.setText(txtButton.get(2));
                            btnLuce17.setText(txtButton.get(3));
                            btnLuce18.setText(txtButton.get(4));
                            btnTapp2.setText(txtButton.get(5));
                            btnTapp4.setText(txtButton.get(6));
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