package com.example.sanghoyoun.myanim;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sanghoyoun on 2017. 10. 4..
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>{

    //Imageloader to load image
    private ImageLoader imageLoader;
    private Context context;

    //list all store animation
    List<ListItem> animList;

    ViewHolder viewHolder;



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
        ListItem anim =  animList.get(position);

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
                Toast.makeText(view.getContext(),"이미지가 눌러졌습니다",Toast.LENGTH_SHORT).show();

                view.getContext().startActivity(intent);
            }
        });

    }
    @Override
    public int getItemCount() {
        //이 리스트뷰가 몇개의 아이템을 가지고 있는지 알려주는 카운트
        return animList.size();
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
