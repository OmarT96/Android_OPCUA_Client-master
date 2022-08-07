package org.twistedappdeveloper.opcclient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.twistedappdeveloper.opcclient.PianoTerra.BagnoEntrata;
import org.twistedappdeveloper.opcclient.PianoTerra.CameraOspiti;
import org.twistedappdeveloper.opcclient.PianoTerra.CorridoioScale;
import org.twistedappdeveloper.opcclient.PianoTerra.Cucina;
import org.twistedappdeveloper.opcclient.PianoTerra.Lavanderia;
import org.twistedappdeveloper.opcclient.PianoTerra.Salone;
import org.twistedappdeveloper.opcclient.PrimoPiano.CameraMatrimoniale;
import org.twistedappdeveloper.opcclient.PrimoPiano.CameraSingola;
import org.twistedappdeveloper.opcclient.PrimoPiano.ScaleCorridoio;
import org.twistedappdeveloper.opcclient.PrimoPiano.Studio;

public class MyAdapterPP extends RecyclerView.Adapter<MyAdapterPP.MyViewHolder> {

    String data[];
    int images[];
    Context context;

    public MyAdapterPP(Context ct, String s1[], int ims[]){
        context = ct;
        data = s1;
        images = ims;
    }

    @NonNull
    @Override
    public MyAdapterPP.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyAdapterPP.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterPP.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.myText.setText(data[position]);
        holder.myImage.setImageResource(images[position]);


        holder.mainRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (position) {
                    case 0:
                        Intent intent0 = new Intent(context, Studio.class);
                        intent0.putExtra("sessionPosition", 0);
                        context.startActivity(intent0);
                        break;
                    case 1:
                        Intent intent1 = new Intent(context, CameraSingola.class);
                        intent1.putExtra("sessionPosition", 0);
                        context.startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(context, CameraMatrimoniale.class);
                        intent2.putExtra("sessionPosition", 0);
                        context.startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(context, ScaleCorridoio.class);
                        intent3.putExtra("sessionPosition", 0);
                        context.startActivity(intent3);
                        break;
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView myText;
        ImageView myImage;
        ConstraintLayout mainRow;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myText = itemView.findViewById(R.id.txtPiano);
            myImage = itemView.findViewById(R.id.imagePTPP);
            mainRow = itemView.findViewById(R.id.mainRow);
        }
    }
}