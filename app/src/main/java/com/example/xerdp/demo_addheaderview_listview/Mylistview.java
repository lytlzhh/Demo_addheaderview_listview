package com.example.xerdp.demo_addheaderview_listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by xerdp on 2016/12/13.
 */

public class Mylistview extends ListView implements AbsListView.OnScrollListener {
    private LayoutInflater layoutInflater = null;
    private MainActivity mainActivity = null;
    private View view = null;
    IRefreshlistener iRfreshlistenner;//刷新数据接口

    int state = 0;
    final int NONE = 0;
    final int PULL = 1;
    final int RELASE = 2;
    final int REFRESH = 3;
    int height;

    int scrollstate;

    int firstVisibleItem;
    boolean ismark;
    int startY;

    public Mylistview(Context context) {
        super(context);
        initview(context);
    }

    public Mylistview(Context context, AttributeSet attrs) {
        super(context, attrs);
        initview(context);
    }

    public Mylistview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initview(context);
    }

    public void initview(Context context) {
        layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.item, null);

        setMeasureView(view);

        height = view.getMeasuredHeight();
        hide_headerview(-height);
        this.addHeaderView(view);
        this.setOnScrollListener(this);
    }

    public void hide_headerview(int height) {
        /*将headerview隐藏*/
        view.setPadding(view.getPaddingLeft(), height, view.getPaddingRight(), view.getPaddingBottom());
        view.invalidate();

    }


    /*告知父类该view所占的空间*/
    public void setMeasureView(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int width = ViewGroup.getChildMeasureSpec(0, 0, params.width);

        int headerview_height;
        int tempheight = params.height;

        if (tempheight > 0) {
            headerview_height = MeasureSpec.makeMeasureSpec(tempheight, MeasureSpec.EXACTLY);
        } else {
            headerview_height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        view.measure(width, headerview_height);

        // Toast.makeText(getContext(), "height" + width, Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.scrollstate = scrollState;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstVisibleItem = firstVisibleItem;
    }

    /*listview在下拉的过程中要经历着几个状态的变化：PULL,DOWN,MOVE.....*/
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (firstVisibleItem == 0) {
                    ismark = true;
                    startY = (int) ev.getY();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (state == RELASE) {
                    state = REFRESH;
                    OnRefresh_Listview();
                    /*这里是刷新的重点*/
                    iRfreshlistenner.onReflash();/////////////////////////////////////////////

                } else if (state == PULL) {
                    state = NONE;
                    ismark = false;
                    OnRefresh_Listview();
                }

                break;

            case MotionEvent.ACTION_MOVE:
                OnMove(ev);
                break;

        }

        return super.onTouchEvent(ev);
    }

    /*各种touch下所要做出的反应*/
    public void OnRefresh_Listview() {
        final TextView textView = (TextView) view.findViewById(R.id.header_textView);
        final ImageView imageView = (ImageView) view.findViewById(R.id.header_imageview);

        RotateAnimation rotateAnimation = new RotateAnimation(180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5F);
        rotateAnimation.setDuration(800);
        rotateAnimation.setFillAfter(true);

        RotateAnimation rotateAnimation1 = new RotateAnimation(0, 180, RotateAnimation.RELATIVE_TO_SELF, 0.5F);
        rotateAnimation1.setDuration(800);
        rotateAnimation1.setFillAfter(true);


        switch (state) {

            case NONE:
                imageView.clearAnimation();
                /*下拉时头部隐藏*/
                hide_headerview(-height);
                break;

            case PULL:
                imageView.setVisibility(VISIBLE);
                textView.setText("下拉刷新!!!");
                imageView.clearAnimation();
                imageView.setAnimation(rotateAnimation);

                break;

            case RELASE:
                imageView.setVisibility(VISIBLE);
                textView.setText("松开刷新!!!");
                //imageView.clearAnimation();
                imageView.setAnimation(rotateAnimation1);
                break;

            case REFRESH:
                hide_headerview(height);
                imageView.setVisibility(VISIBLE);
                textView.setText("正在刷新!!!");
                // imageView.setAnimation(rotateAnimation1);
                imageView.clearAnimation();
                break;
        }
    }


    public void OnMove(MotionEvent event) {

        if (!ismark) {
            return;
        }
        int tempY = (int) event.getY();
        int space = tempY - startY;
        int toppadding = space - height;


        switch (state) {
            case NONE:
                if (space > 0) {
                    state = PULL;
                    OnRefresh_Listview();
                }
                break;
            case PULL:
                hide_headerview(toppadding);
                if (space > height + 30 && scrollstate == SCROLL_STATE_TOUCH_SCROLL) {
                    state = RELASE;
                    OnRefresh_Listview();
                }
                break;
            case RELASE:
                if (space < height + 30) {
                    state = PULL;
                    OnRefresh_Listview();
                } else if (space <= 0) {
                    state = NONE;
                    ismark = false;
                    OnRefresh_Listview();
                }
                break;

        }
    }

    public void RefreshComplete() {
        state = NONE;//刷新完成后变成正常状态
        ismark = false;
        OnRefresh_Listview();//刷新界面

    }

    //???????????????????????????????????????
    public void setInterface(IRefreshlistener iRfreshlistenner) {
        this.iRfreshlistenner = iRfreshlistenner;
    }

    //数据刷新接口  ??????????????????????????????????????????????
    public interface IRefreshlistener {
        public void onReflash();
    }
}
