package org.twistedappdeveloper.opcclient;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.opcfoundation.ua.application.Client;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.core.EndpointDescription;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import OpcUtils.ConnectionThread.ThreadCreateSession;
import OpcUtils.ConnectionThread.ThreadDiscoveryEndpoints;
import OpcUtils.ManagerOPC;
import OpcUtils.SessionElement;
import tool.ui.EndpointsAdapter;

import static org.opcfoundation.ua.utils.EndpointUtil.selectByProtocol;
import static org.opcfoundation.ua.utils.EndpointUtil.sortBySecurityLevel;

public class MainActivity extends AppCompatActivity {
    ImageButton accessID;
    ManagerOPC manager;
    ProgressDialog dialog;
    EndpointDescription[] endpoints;
    List<EndpointDescription> endpoints_list;
    EndpointsAdapter adapter;
    String url = "opc.tcp://192.168.2.113:4840";

    // 192.168.2.113

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File certFile = new File(getFilesDir(), "OPCCert.der");
        File privKeyFile = new File(getFilesDir(), "OPCCert.pem");

        manager = ManagerOPC.CreateManagerOPC(certFile, privKeyFile);

        accessID = findViewById(R.id.accessID);

        endpoints_list = new ArrayList<>();
        adapter = new EndpointsAdapter(getApplicationContext(), R.layout.list_endpoints, endpoints_list);

        accessID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endpoints_list.clear();
                Client client = manager.getClient();
                dialog = ProgressDialog.show(MainActivity.this, getString(R.string.TentativoDiConnessione), getString(R.string.RichiestaEndpoints), true);
                ThreadDiscoveryEndpoints t = new ThreadDiscoveryEndpoints(client, url);

                @SuppressLint("HandlerLeak") Handler handler_discovery = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        dialog.dismiss();
                        if (msg.what == -1) {
                            Toast.makeText(getApplicationContext(), getString(R.string.EndpointsNontrovati) + ((StatusCode) msg.obj).getDescription() + "\nCode: " + ((StatusCode) msg.obj).getValue().toString(), Toast.LENGTH_LONG).show();
                        } else if (msg.what == -2) {
                            Toast.makeText(getApplicationContext(), R.string.ServerDown, Toast.LENGTH_LONG).show();
                        } else {
                            endpoints = selectByProtocol(sortBySecurityLevel((EndpointDescription[]) msg.obj), "opc.tcp");
                            ThreadCreateSession g = new ThreadCreateSession(manager, url, endpoints[0]);
                            dialog = ProgressDialog.show(MainActivity.this, getString(R.string.TentativoDiConnessione), getString(R.string.CreazioneSessione), true);
                            @SuppressLint("HandlerLeak") Handler handler_createsession = new Handler() {
                                @Override
                                public void handleMessage(Message msg) {
                                    dialog.dismiss();
                                    int session_position = 0;
                                    Intent intent = new Intent(MainActivity.this, Home.class);
                                    intent.putExtra("sessionPosition", session_position);
                                    intent.putExtra("url", manager.getSessions().get(session_position).getUrl());
                                    startActivity(intent);
                                }
                            };
                            g.start(handler_createsession);
                        }
                        adapter.notifyDataSetChanged();
                    }
                };
                t.start(handler_discovery);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (SessionElement session : manager.getSessions()) {
            session.getSession().closeAsync();
        }
        manager.getSessions().clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }
}
