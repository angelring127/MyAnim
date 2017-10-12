package com.example.sanghoyoun.myanim;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sanghoyoun on 2017. 10. 4..
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>{

    //TAG
    static final String TAG = "ListAdapter";

    //Imageloader to load image
    private ImageLoader imageLoader;
    private Context context;

    //list all store animation
    List<ListItem> animList;

    ViewHolder viewHolder;

    String result = "-1";
    private String loginID = null;



    //Construct of this class
    public ListAdapter(List<ListItem> anim,Context context) {
        this.context = context;
        this.animList = anim;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //Getting the particular item from the list
        final ListItem anim =  animList.get(position);

        //Loading image from url
        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(anim.getProfileImage(), ImageLoader.getImageListener(holder.animImageView, R.drawable.profile, android.R.drawable.ic_dialog_alert));

        //Showing data on the views
        holder.animImageView.setImageUrl(anim.getProfileImage(), imageLoader);
        holder.titleTextView.setText(anim.getAnimTitle());
        holder.yearTextView.setText(anim.getAnimYear());

        holder.animImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),DetailPageActivity.class);
                intent.putExtra("anim",anim);
                Toast.makeText(view.getContext(),"이미지가 눌러졌습니다",Toast.LENGTH_SHORT).show();

                view.getContext().startActivity(intent);
            }
        });
        holder.favorRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                loginID = anim.getLoginID();
                if(loginID != null){
                    changeRate CHR = new changeRate(anim.getLoginID(),anim.getAnim_ID(),v);
                    CHR.execute();
                }
            }
        });

    }
    @Override
    public int getItemCount() {
        //이 리스트뷰가 몇개의 아이템을 가지고 있는지 알려주는 카운트
        return animList.size();
    }


    //서버에 평가 값을 전달한다.
    public class changeRate extends AsyncTask<Void, Integer, Void> {
        String data = "";
        private String loginID;
        private String animID;
        private float animRate;

        public changeRate(String loginID, String animID, float animRate) {
            this.loginID = loginID;
            this.animID = animID;
            this.animRate = animRate;
            Log.e(TAG,"loginID: " + this.loginID + "animID: " + this.animID + "animRate: " +animRate);

        }

        @Override
        protected Void doInBackground(Void... unused) {

            /* 인풋 파라메터값 생성 */
            //애니메이션에 대해서 평가하는데 필요한 요소는 user_id, anim_id, rate
            String param = "u_id=" + loginID + "&a_id=" + animID + "&a_rate=" + animRate +"";

            Log.e("POST",param);
            try {
                /* 서버연결 */
                URL url = new URL(
                        Config.DATA_MAINURL+ "update_rate.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.connect();

                /* 안드로이드 -> 서버 파라메터값 전달 */
                OutputStream outs = conn.getOutputStream();
                outs.write(param.getBytes("UTF-8"));
                outs.flush();
                outs.close();

                /* 서버 -> 안드로이드 파라메터값 전달 */

                InputStream is = null;
                BufferedReader in = null;
                String data = "";

                is = conn.getInputStream();
                in = new BufferedReader(new InputStreamReader(is), 8 * 1024);
                String line = null;
                StringBuffer buff = new StringBuffer();
                while ( ( line = in.readLine() ) != null )
                {
                    buff.append(line + "\n");
                }
                data = buff.toString().trim();


                /* 서버에서 응답 */
                Log.e("RECV DATA",data);
                result = data;
                Log.e("RESULT DATA",result);
                /*
                if(data.equals("0"))
                {
                    Log.e("RESULT","성공적으로 처리되었습니다!");
                }
                else
                {
                    Log.e("RESULT","에러 발생! ERRCODE = " + data);
                }
                */
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(result.equalsIgnoreCase("User found"))
            {

            }
            else if(result.equals("0"))
            {

            }
            else
            {

            }

        }
    }


    /*
    @Override
    public Object getItem(int position) {
        return this.listItemArrayList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item,null);

            viewHolder = new ViewHolder();
            viewHolder.titleTextView = (TextView)view.findViewById(R.id.animTitle);
            viewHolder.yearTextView = (TextView)view.findViewById(R.id.animYear);
            viewHolder.favorRatingBar = (RatingBar)view.findViewById(R.id.favorRate);
            viewHolder.animImageView = (ImageView)view.findViewById(R.id.animView);
        } else{

            viewHolder = (ViewHolder)view.getTag();
        }

        viewHolder.titleTextView.setText(listItemArrayList.get(position).getAnimTitle());
        viewHolder.yearTextView.setText(listItemArrayList.get(position).getAnimYear());

        Glide.with(view.getContext()).load(listItemArrayList.get(position).getProfileImage()).into(viewHolder.animImageView);

        //이미지뷰에다가 클릭 리스너를 입력

        viewHolder.animImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //                          (현재화면의 제어권자,다음 넘어갈 화면)
                Intent intent = new Intent(view.getContext(),DetailPageActivity.class);
                Toast.makeText(view.getContext(),"이미지가 눌러졌습니다",Toast.LENGTH_SHORT).show();

                view.getContext().startActivity(intent);
            }
        });
        return view;
    }
    */
    class ViewHolder extends RecyclerView.ViewHolder{
        //viewHolder 패턴을 이용해서 리스트뷰를 최적
        TextView titleTextView;
        TextView yearTextView;
        RatingBar favorRatingBar;
        NetworkImageView animImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.animTitle);
            yearTextView = (TextView) itemView.findViewById(R.id.animYear);
            favorRatingBar = (RatingBar) itemView.findViewById(R.id.favorRate);
            animImageView = (NetworkImageView) itemView.findViewById(R.id.animView);
        }
    }
}
