package com.nerd.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nerd.todo.adapter.RecyclerViewAdapter;
import com.nerd.todo.model.Todo;
import com.nerd.todo.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    ArrayList<Todo> todoArrayList = new ArrayList<>();

    RequestQueue requestQueue;

    int offset = 0;
    int limit = 25;
    int cnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        // 페이징 처리
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int totalCount = recyclerView.getAdapter().getItemCount();

                if ((lastPosition + 1) == totalCount){
                    if (cnt == limit){
                        // 네트워크 통해서, 데이터를 더 불러오면 된다.
                        addNetworkData();
                    }
                }

            }
        });
        requestQueue = Volley.newRequestQueue(MainActivity.this);

        getNetworkData();
    }

    // todo 데이터 전부 가져오기
    private void getNetworkData() {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                Utils.BASEURL + "?offset="+offset+"&limit="+limit,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.getBoolean("success");
                            if (success == false){
                                // 유저한테 에러있다고 알리고 리턴.
                                Toast.makeText(MainActivity.this, "success가 false입니다.",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            try {
                                JSONArray rows = response.getJSONArray("rows");
                                for (int i = 0; i < rows.length(); i++) {
                                    JSONObject jsonObject = rows.getJSONObject(i);
                                    int id = jsonObject.getInt("id");
                                    String title = jsonObject.getString("title");
                                    String date = jsonObject.getString("date");
                                    int completed;

                                    if (rows.getJSONObject(i).isNull("completed")){
                                        completed = 0;
                                    }else {
                                        completed = rows.getJSONObject(i).getInt("completed");
                                    }

                                    Log.i("가져와", response.toString());

                                    Todo todo = new Todo(id, title, date, completed);
                                    todoArrayList.add(todo);
                                }
                                recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, todoArrayList);
                                recyclerView.setAdapter(recyclerViewAdapter);

                                // 페이징을 위해서, 오프셋을 증가 시킨다. 그래야 리스트 끝에가서 네트워크 다시 호출할 때,
                                // 해당 offset으로 서버에 요청이 가능하다.
                                offset = offset + response.getInt("count");
                                cnt = response.getInt("count");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("가져와", error.toString());
                    }
                }
        );
        requestQueue.add(request);
    }

    // 영화데이터 전부 가져오기 페이징함수
    private void addNetworkData() {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                Utils.BASEURL + "?offset="+offset+"&limit="+limit,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.getBoolean("success");
                            if (success == false){
                                // 유저한테 에러있다고 알리고 리턴.
                                Toast.makeText(MainActivity.this, "success가 false입니다.",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            try {
                                JSONArray rows = response.getJSONArray("rows");
                                for (int i = 0; i < rows.length(); i++) {
                                    JSONObject jsonObject = rows.getJSONObject(i);
                                    int id = jsonObject.getInt("id");
                                    String title = jsonObject.getString("title");
                                    String date = jsonObject.getString("date");
                                    int completed;

                                    if (rows.getJSONObject(i).isNull("completed")){
                                        completed = 0;
                                    }else {
                                        completed = rows.getJSONObject(i).getInt("completed");
                                    }

                                    Log.i("가져와", response.toString());

                                    Todo todo = new Todo(id, title, date, completed);
                                    todoArrayList.add(todo);
                                }
                                recyclerViewAdapter.notifyDataSetChanged();

                                // 페이징을 위해서, 오프셋을 증가 시킨다. 그래야 리스트 끝에가서 네트워크 다시 호출할 때,
                                // 해당 offset으로 서버에 요청이 가능하다.
                                offset = offset + response.getInt("count");
                                cnt = response.getInt("count");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("가져와", error.toString());
                    }
                }
        );
        requestQueue.add(request);
    }

    public void completed(final int position){
        Todo todo = todoArrayList.get(position);
        int id = todo.getId();

        JSONObject body = new JSONObject();
        try {
            body.put("id", id);
            body.put("completed",1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                Utils.BASEURL,
                body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("AAA","add favorite : "+response.toString());
                        // 어레이리스트의 값을 변경시켜줘야 한다.
                        Todo todo = todoArrayList.get(position);
                        todo.setCompleted(1);

                        recyclerViewAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        Volley.newRequestQueue(MainActivity.this).add(request);
    }

    public void not_completed(final int position){
        Todo todo = todoArrayList.get(position);
        int id = todo.getId();

        JSONObject body = new JSONObject();
        try {
            body.put("id", id);
            body.put("completed",0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                Utils.BASEURL,
                body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("AAA","add favorite : "+response.toString());
                        // 어레이리스트의 값을 변경시켜줘야 한다.
                        Todo todo = todoArrayList.get(position);
                        todo.setCompleted(0);

                        recyclerViewAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        Volley.newRequestQueue(MainActivity.this).add(request);
    }


}
