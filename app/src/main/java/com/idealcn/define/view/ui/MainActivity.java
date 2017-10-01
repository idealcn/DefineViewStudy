package com.idealcn.define.view.ui;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.idealcn.define.view.listener.OnFlowChildClickListener;
import com.idealcn.define.view.listener.OnFragmentChangeListener;
import com.idealcn.define.view.R;

public class MainActivity extends AppCompatActivity implements OnFragmentChangeListener,OnFlowChildClickListener {

    private static final String TAG = "main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        MainFragment mainFragment = new MainFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.root,mainFragment).commit();


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.d(TAG, "onWindowFocusChanged: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
    }

    @Override
    public void onAttachFragment(android.support.v4.app.Fragment fragment) {
        super.onAttachFragment(fragment);
            if (fragment instanceof MainFragment){
                ((MainFragment) fragment).setListener(this);
            }
    }


    @Override
    public void change(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onChildClick(int index) {
        switch (index){
            case 0:
//                listener.change("进度条");
                startActivity(new Intent(this, ProgressBarActivity.class));
                break;
//            case R.id.clock:
//                listener.change("时钟");
//                startActivity(new Intent(mainActivity, ClockActivity.class));
//                break;
//            case R.id.round_cake:
//                listener.change("饼状图");
//                startActivity(new Intent(mainActivity,RoundCakeActivity.class));
//                break;
//            case R.id.drag:
//                listener.change("拖拽");
//                startActivity(new Intent(mainActivity,DragActivity.class));
//                break;
//            case R.id.picSelector:
//                listener.change("图片选择器");
//                startActivity(new Intent(mainActivity,TableActivity.class));
//                break;
//            case R.id.flowLayout:
//                listener.change("流式布局");
//                startActivity(new Intent(mainActivity,FlowLayoutActivity.class));
//                break;
//            case R.id.padding:
//                listener.change("padding");
//                startActivity(new Intent(mainActivity,PaddingActivity.class));
//                break;
            default:break;
        }
    }
}
