package com.idealcn.define.view.ui;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.idealcn.define.view.R;
import com.idealcn.define.view.databinding.FragmentMainBinding;

/**
 * Created by ideal-gn on 2017/8/19.
 */

public class MainFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "fragment";

    private FragmentMainBinding  binding;
    private MainActivity mainActivity;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
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
                startActivity(new Intent(mainActivity, ProgressBarActivity.class));
                break;
            case R.id.clock:
                startActivity(new Intent(mainActivity, ClockActivity.class));
                break;
            default:break;
        }
    }
}
