package com.idealcn.define.view.ui;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.idealcn.define.view.listener.OnFragmentChangeListener;
import com.idealcn.define.view.R;
import com.idealcn.define.view.databinding.FragmentMainBinding;

/**
 * Created by ideal-gn on 2017/8/19.
 */

public class MainFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "fragment";

    private FragmentMainBinding  binding;


    private OnFragmentChangeListener listener;
    private MainActivity mainActivity;

    public void setListener(OnFragmentChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_main, container, false);
        binding = DataBindingUtil.bind(root);
        binding.setClick(this);
        return root;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.progressBar:
                listener.change("进度条");
                startActivity(new Intent(mainActivity, ProgressBarActivity.class));
                break;
            case R.id.clock:
                listener.change("时钟");
                startActivity(new Intent(mainActivity, ClockActivity.class));
                break;
            case R.id.round_cake:
                listener.change("饼状图");
                startActivity(new Intent(mainActivity,RoundCakeActivity.class));
                break;
            case R.id.drag:
                listener.change("拖拽");
                startActivity(new Intent(mainActivity,DragActivity.class));
                break;
            default:break;
        }
    }
}
