package com.example.watabe.atendance;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListAlert {
    //フィールド
    private AlertDialog dialog;
    private ArrayList<String> itemList = new ArrayList<>();
    private ListView listView;
    private CustomAdapter adapter;
    private Context context;
    private View parentView;

    //コンストラクタ Viewは
    public ListAlert( Context context ,View view) {
        this.parentView = view;
        this.context = context;
        this.listView = new ListView(this.context);
        this.adapter = new CustomAdapter(
                this.context,
                0,
                this.itemList
        );

        this.listView.setAdapter( this.adapter );

        dialog = new AlertDialog.Builder(context)
                .setView(this.listView)
                .create();
    }

    public void show(){
        this.dialog.show();
    }

    public void add( String item ){
        Log.d("add",item);
        this.itemList.add( item );
    }

    public void setList( ArrayList<String> list ){
        for(String item : list )
            this.itemList.add( item );
    }

    public class CustomAdapter extends ArrayAdapter<String>{
        private LayoutInflater inflater;
        private String item;
        public CustomAdapter( Context context ,
                              int resource,
                              List<String> objects ){
            super( context , resource, objects );
            inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public View getView(int position , View v, ViewGroup parent){
            if( null == v)
                v = this.inflater.inflate(R.layout.custom_listview,null);

            TextView textView = (TextView)v.findViewById(R.id.string_item);
            textView.setText( (String)getItem(position) );

            textView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    TextView local = (TextView)view;
                    ((TextView)parentView).setText( local.getText() );

                    dialog.dismiss();
                }
            });

            Log.d("getView", (String)getItem(position));
            return v;
        }
    }
}
