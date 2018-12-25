package tokyo.mp015v.async;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Function;

/*
* How To

  HttpResponseAsync async = new HttpResponseAsync( String url , String table_name );
  async.setFunction(new Function<String,Void>(){
      @Override
            public Void apply(String s) {
                txtSample.setText( s );
                return null;
            }
        });

        async.execute();
  }
 */

//非同期通信用クラス
public class HttpResponseAsync extends AsyncTask<Void,Void,String> {
    private Function<String , Void> func;
    private String urlSt = null;
    private String sendWord = null;
    //コンストラクタ
    public HttpResponseAsync(){}
    public HttpResponseAsync(String urlSt){
        this.urlSt = urlSt;
    }
    public HttpResponseAsync(String urlSt,String sendWord){
        this.urlSt = urlSt;
        this.sendWord = sendWord;
    }
    //メソッド
    //URLのセッティング
    public void setUrlSt( String urlSt){
        this.urlSt = urlSt;
    }

    @Override
    protected void onPreExecute(){ }

    //非同期処理
    @Override
    protected String doInBackground( Void... params){
        StringBuilder sb = new StringBuilder();
        InputStream in = null;
        HttpURLConnection con = null;
        String readSt = null;
        try{
            URL url = new URL(urlSt);
            con = (HttpURLConnection)url.openConnection();
            con.setConnectTimeout( 3000 );
            con.setReadTimeout( 3000 );
            con.setRequestMethod("POST");
            con.setDoInput(true);   //読込の許可
            con.setDoOutput(true);  //書き込みの許可
            con.connect();
            //書き込み処理
            OutputStream out = null;

            try{
                out = con.getOutputStream();
                out.write(sendWord.getBytes("UTF-8"));
                out.flush();
            }catch(IOException e){
                e.printStackTrace();
                readSt = "送信エラー";
            }finally{
                if( out != null) {
                    out.close();
                }
            }

            int resCode = con.getResponseCode();
            if( resCode != HttpURLConnection.HTTP_OK){
                throw new IOException("HTTP responseCode:"  + resCode );
            }else {
                //con.setInstanceFollowRedirects(true); //リダイレクトする
                //読込処理
                in = con.getInputStream();
                readSt = readInputStream(in);

            }
        }catch(Exception e){
            //Log.d("error",e.toString() );
            e.printStackTrace();
        }finally{
            if(con != null ){
                con.disconnect();
            }
        }

        return readSt;
    }
    //途中経過をメインスレッドに返す
    protected void onProgressUpdate(Void... progress){}

    //非同期処理が終了後、結果をメインスレッドに返す
    @Override
    protected void onPostExecute( String result ){
        super.onPostExecute( result );
        func.apply((String) result);
    }

    //ファンクションの設定
    public void setFunction( Function<String,Void> func ){
        this.func = func;
    }

    //ByteをStringに変換
    public String readInputStream( InputStream in) throws IOException, UnsupportedEncodingException {
        StringBuffer sb = new StringBuffer();
        String st = "";

        BufferedReader br = new BufferedReader( new InputStreamReader(in,"UTF-8"));
        while((st=br.readLine()) != null){
            sb.append( st);}
        try{
            in.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return sb.toString();
    }
}