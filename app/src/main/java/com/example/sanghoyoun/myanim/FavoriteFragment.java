package com.example.sanghoyoun.myanim;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sanghoyoun on 2017. 10. 3..
 */

public class FavoriteFragment extends Fragment implements RecyclerView.OnScrollChangeListener {
    private static final String TAG = "FavoriteFragment Json";
    ListView favorListView;
    ListAdapter myListAdapter;
    ArrayList<ListItem> favorList;
    private View view;
    private String loginID; //로그인 정보를 저장

    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    //스크롤시 갱신
    SwipeRefreshLayout swipeRefreshLayout;


    //Volley Request Queue
    private RequestQueue requestQueue;

    //The request counter to send ?page=1, ?page=2  requests
    private int requestCount = 1;


    public static FavoriteFragment newInstance() {
        Bundle args = new Bundle();
        FavoriteFragment fragment = new FavoriteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(getActivity() != null && getActivity() instanceof MainActivity){
            loginID = ((MainActivity)getActivity()).getData();
            Log.e(TAG,"loginID = " + loginID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favorite, container, false);


        //Initializing Views
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        //favorListView = (ListView) view.findViewById(R.id.favorListview);

        //myListAdapter = new ListAdapter(view.getContext(),favorList);

        //스크롤 갱신
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiftRefreshFavor);


        //favorListView.setAdapter(myListAdapter);

        //list클릭 리스를 추가

        //Initializing our favor list

        //Initializing our superheroes list
        favorList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(view.getContext());

        //Calling method to get data to fetch data
        getData();

        //Adding an scroll change listener to recyclerview
        recyclerView.setOnScrollChangeListener(this);

        //initializing our adapter
        adapter = new ListAdapter(favorList, view.getContext());

        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);


        requestQueue = Volley.newRequestQueue(view.getContext());

        //Calling method to get data to fetch data
        getData();


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                favorList.clear();
                requestCount = 1;
                getData();



                //갱신후 로딩 화면 제거
                swipeRefreshLayout.setRefreshing(false);
            }
        });








        return view;
    }


    //Request to get json from server we are passing an integer here
    //This integer will used to specify the page number for the request ?page = requestcount
    //This method would return a JsonArrayRequest that will be added to the request queue
    private JsonArrayRequest getDataFromServer(int requestCount) {
        //Initializing ProgressBar
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);

        //Displaying Progressbar
        progressBar.setVisibility(View.VISIBLE);
        getActivity().setProgressBarIndeterminateVisibility(true);

        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.DATA_URL + loginID + "&page="+String.valueOf(requestCount),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e(TAG,"response" + response.toString());
                        //Calling method parseData to parse the json response
                        parseData(response);
                        //Hiding the progressbar
                        progressBar.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        //If an error occurs that means end of the list has reached
                        Toast.makeText(view.getContext(), "No More Items Available", Toast.LENGTH_SHORT).show();
                    }
                });
        Log.e("RECV Json",jsonArrayRequest.toString());
        //Returning the request
        return jsonArrayRequest;
    }


    //This method will get data from the web api
    private void getData() {
        //Adding the method to the queue by calling the method getDataFromServer
        requestQueue.add(getDataFromServer(requestCount));
        //Incrementing the request counter
        requestCount++;
    }

    //This method will parse json data
    private void parseData(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            //Creating the superhero object
            ListItem myanim = new ListItem();
            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);
                Log.e(TAG,json.toString());
                //Adding data to the superhero object
                myanim.setProfileImage(json.getString(Config.TAG_IMAGE_URL));
                myanim.setAnimTitle(json.getString(Config.TAG_NAME));
                myanim.setAnimYear(json.getString(Config.TAG_YEAR));
                myanim.setAnim_ID(json.getString(Config.TAG_ANIM_ID));
                myanim.setLoginID(loginID);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Adding the superhero object to the list
            favorList.add(myanim);
        }

        //Notifying the adapter that data has been added or changed
        adapter.notifyDataSetChanged();
    }

    //This method would check that the recyclerview scroll has reached the bottom or not
    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                return true;
        }
        return false;
    }

    //Overriden method to detect scrolling
    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        //Ifscrolled at last then
        if (isLastItemDisplaying(recyclerView)) {
            //Calling the method getdata again
            getData();
        }
    }

}
