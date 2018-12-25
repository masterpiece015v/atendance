package tokyo.mp015v.atendance;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
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

    //コンストラクタ
    public ListAlert( Context context) {
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
    //コンストラクタ Viewは親のビュー
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

    //ダイアログを表示するメソッド
    public void show(){
        this.dialog.show();
    }

    //リストに追加するメソッド
    public void add( String item ){
        Log.d("add",item);
        this.itemList.add( item );
    }

    //リストにリストを設定するメソッド
    public void setList( ArrayList<String> list ){

        for(String item : list )
            this.itemList.add( item );
    }

    //リストボックスのテキストボックスにイベントを登録する
    public void setTextViewOnClick( View.OnClickListener listener ){
        adapter.setTextViewOnClickListener( listener );
    }

    //リストのアダプタ
    public class CustomAdapter extends ArrayAdapter<String>{
        private LayoutInflater inflater;
        private String item;
        private View.OnClickListener listener;

        //セッター
        public void setTextViewOnClickListener( View.OnClickListener listener ){
            this.listener = listener;
        }

        //コンストラクタ
        public CustomAdapter( Context context ,
                              int resource,
                              List<String> objects ){
            super( context , resource, objects );
            inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public View getView(int position , View v, ViewGroup parent){
            if( null == v)
                v = this.inflater.inflate(tokyo.mp015v.atendance.R.layout.custom_listview,null);

            TextView textView = (TextView)v.findViewById(tokyo.mp015v.atendance.R.id.string_item);
            textView.setText( (String)getItem(position) );

            //イベントの登録
            textView.setOnClickListener( listener );

            Log.d("getView", (String)getItem(position));
            return v;
        }

    }
    //ダイアログを閉じる
    public void dismiss(){
        this.dialog.dismiss();
    }
}
