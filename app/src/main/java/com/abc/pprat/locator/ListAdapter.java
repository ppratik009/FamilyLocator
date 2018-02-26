package com.abc.pprat.locator;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ListAdapter extends ArrayAdapter<String>{

    private Activity context;
    String[] name;
    String[] status;
    String[] code;


    public ListAdapter(Activity context,String[] name,String[] status,String[] code)
    {

        super(context,R.layout.card_list_item,name);
        this.name=name;
        this.status=status;
        this.code=code;
        this.context=context;

    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater=context.getLayoutInflater();
        //LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView=layoutInflater.inflate(R.layout.card_list_item,null,true);
        TextView tvName=rowView.findViewById(R.id.tvPName);
        tvName.setText("asad");
        return rowView;
    }
}
