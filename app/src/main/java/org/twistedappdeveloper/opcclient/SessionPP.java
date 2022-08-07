package org.twistedappdeveloper.opcclient;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SessionPP extends AppCompatActivity {

    RecyclerView recyclerView;

    String s1[];
    int images[] = {R.drawable.imgstudio,R.drawable.imgcamerasingola,
            R.drawable.imgcameramatrimoniale, R.drawable.imgscalecorridoiopp  };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pp);

        recyclerView = findViewById(R.id.idRWPP);

        s1 = getResources().getStringArray(R.array.StanzePrimoPiano);

        MyAdapterPP myAdapterPP = new MyAdapterPP(this, s1, images);

        recyclerView.setAdapter(myAdapterPP);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
