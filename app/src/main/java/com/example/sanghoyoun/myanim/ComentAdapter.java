package com.example.sanghoyoun.myanim;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sanghoyoun on 2017. 10. 13..
 */

public class ComentAdapter extends BaseAdapter{
    static final String TAG = "ListAdapter";

    // 문자열을 보관 할 ArrayList
    private ArrayList<ComentItem> m_List;

    // 생성자
    public ComentAdapter(ArrayList<ComentItem> list) {
        m_List = list;
    }

    // 현재 아이템의 수를 리턴
    @Override
    public int getCount() {
        return m_List.size();
    }

    // 현재 아이템의 오브젝트를 리턴, Object를 상황에 맞게 변경하거나 리턴받은 오브젝트를 캐스팅해서 사용
    @Override
    public Object getItem(int position) {
        return m_List.get(position);
    }

    // 아이템 position의 ID 값 리턴
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 출력 될 아이템 관리
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // 리스트가 길어지면서 현재 화면에 보이지 않는 아이템은 converView가 null인 상태로 들어 옴
            // view가 null일 경우 커스텀 레이아웃을 얻어 옴
            ComentItem item = m_List.get(pos);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.coment_item, parent, false);

            // TextView에 현재 position의 문자열 추가
            TextView textId = (TextView) convertView.findViewById(R.id.txtId);
            textId.setText(item.getUser_ID());

            TextView textComent = (TextView) convertView.findViewById(R.id.txtViewContent);
            textComent.setText(item.getComent());

            ImageView flagView = (ImageView) convertView.findViewById(R.id.imgFlag);
            flagView.setImageResource(item.getCountry());

            // 버튼을 터치 했을 때 이벤트 발생
            Button btn = (Button) convertView.findViewById(R.id.btnTrans);
            btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // 터치 시 해당 아이템 이름 출력
                    Toast.makeText(context, "번역 버튼이 눌렸습니다", Toast.LENGTH_SHORT).show();
                }
            });


        return convertView;
    }

    // 외부에서 아이템 추가 요청 시 사용
    public void add(ComentItem coment) {
        m_List.add(coment);
    }

    // 외부에서 아이템 삭제 요청 시 사용
    public void remove(int _position) {
        m_List.remove(_position);
    }
}

