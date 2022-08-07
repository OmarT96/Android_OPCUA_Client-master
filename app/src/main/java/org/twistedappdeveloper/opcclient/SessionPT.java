package org.twistedappdeveloper.opcclient;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SessionPT extends AppCompatActivity {

    RecyclerView recyclerView;

    String s1[];
    int images[] = {R.drawable.salone, R.drawable.cucina, R.drawable.bagnoentrata,
            R.drawable.corridoioscale, R.drawable.lavanderia, R.drawable.stanzaospiti };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pt);

        recyclerView = findViewById(R.id.idRW);

        s1 = getResources().getStringArray(R.array.StanzePianoTerra);

        MyAdapterPT myAdapterPT = new MyAdapterPT(this, s1, images);

        recyclerView.setAdapter(myAdapterPT);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }
}
