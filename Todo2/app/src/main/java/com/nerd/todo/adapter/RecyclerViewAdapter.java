package com.nerd.todo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.nerd.todo.MainActivity;
import com.nerd.todo.R;
import com.nerd.todo.model.Todo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    Context context;
    ArrayList<Todo> todoArrayList;

    public RecyclerViewAdapter(Context context, ArrayList<Todo> todoArrayList) {
        this.context = context;
        this.todoArrayList = todoArrayList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_row,parent,false); //inflate=만들라는 뜻
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Todo todo = todoArrayList.get(position);
        String title = todo.getTitle();
        String date = todo.getDate();

        holder.title.setText(title);

        date = date.replace("T", " ");
        SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm", Locale.getDefault());
        df.setTimeZone(TimeZone.getTimeZone("UTC"));    // 위의 시간을 utc로 맞추는것.(우리는 이미 서버에서 utc로 맞춰놔서 안해도 되는데 혹시몰라서 해줌)
        try {
            Date d = df.parse(date);
            df.setTimeZone(TimeZone.getDefault());      // 내 폰의 로컬 타임존으로 바꿔줌.
            String strDate = df.format(d);
            holder.date.setText(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (todo.getCompleted() == 1){
            holder.completed.setImageResource(android.R.drawable.checkbox_on_background);
        }else {
            holder.completed.setImageResource(android.R.drawable.checkbox_off_background);
        }
    }

    @Override
    public int getItemCount() {
        return todoArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView date;
        public ImageView completed;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            completed = itemView.findViewById(R.id.completed);

            completed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    int completed = todoArrayList.get(position).getCompleted();
                    if (completed == 1){
                        ((MainActivity)context).not_completed(position);
                    }else {
                        ((MainActivity)context).completed(position);
                    }
                }
            });

        }
    }
}
