package com.idealcn.define.view.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.idealcn.define.view.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ideal-gn on 2017/9/7.
 */

public class RoundCakeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp);
//        RelativeLayout root = (RelativeLayout) findViewById(R.id.root);
//        ViewGroup.LayoutParams lp = root.getLayoutParams();
//        System.out.println(lp.getClass().getSimpleName());
//        MyView myView = (MyView) findViewById(R.id.myView);
//        ViewGroup.LayoutParams layoutParams = myView.getLayoutParams();
//        System.out.println(layoutParams.getClass().getSimpleName());

        List<String> dataList = new ArrayList<>();
        for (int x = 0; x < 10; x++) {
            dataList.add("data---"+x);
        }
       final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)
       /* {
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                        RecyclerView.LayoutParams.WRAP_CONTENT);
                lp.topMargin = dp2px(10);
                lp.leftMargin = dp2px(10);
                lp.bottomMargin = dp2px(10);
                return lp;
            }
        }*/
        );
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(new MyAdapter(dataList));


       /* recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                ViewGroup.LayoutParams layoutParams = recyclerView.getChildAt(0).getLayoutParams();
                System.out.println("");
            }

        });*/

    }

    private int dp2px(int dp){
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return (int) (metrics.scaledDensity * dp + 0.5f);
    }



    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder>{
        List<String> dataList;
         MyAdapter(List<String> dataList) {
            this.dataList = dataList;
        }

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = View.inflate(RoundCakeActivity.this, R.layout.item, null);
            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder holder, int position) {
            holder.tvItem.setText(dataList.get(position));
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        class MyHolder extends RecyclerView.ViewHolder{
            TextView tvItem;
             MyHolder(View itemView) {
                super(itemView);
                tvItem = (TextView) itemView.findViewById(R.id.content);
            }
        }
    }
}
