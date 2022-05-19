package com.example.SinSenas;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.viewpager2.widget.ViewPager2;

import com.example.SinSenas.Class.ModeloTemario;
import com.example.SinSenas.R;

import java.util.List;


public class ListAdapterTemas extends ArrayAdapter<ModeloTemario> {
    private List<ModeloTemario> mlist;
    private Context mcontext;
    private int resourseLayout;
    ViewPager2 viewPager2;//View

    public ListAdapterTemas(@NonNull Context context, int resource, List<ModeloTemario> objects) {
        super(context, resource, objects);
        this.mlist = objects;
        this.mcontext = context;
        this.resourseLayout = resource;
    }

    @SuppressLint({"ResourceAsColor", "ResourceType"})
    @RequiresApi(api = Build.VERSION_CODES.S)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null)
            view = LayoutInflater.from(mcontext).inflate(R.layout.adapter_layout_temas, null);

        ModeloTemario modelo = mlist.get(position);
        TextView letraTema = view.findViewById(R.id.textViewLetra);
        letraTema.setText(modelo.getLetraNombre());
        TextView nameTema = view.findViewById(R.id.nameTema);
        nameTema.setText(modelo.getNombre().substring(1));
        ImageView disable = view.findViewById(R.id.temaDisable);
        disable.setNextFocusForwardId(R.id.nameTema);

        if(modelo.getEstado().equals("Activo")){
            disable.setVisibility(View.INVISIBLE);
        }

        return view;
    }
}