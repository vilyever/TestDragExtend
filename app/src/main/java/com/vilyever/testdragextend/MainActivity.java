package com.vilyever.testdragextend;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private static String TAG = "MainActivity";

    private ScrollView mTopScrollView, mBottomScrollView;
    private LinearLayout mBottomLayout;
    private ImageView mDragButton;
    private TextView mTopContentTv, mBottomContentTv;


    private int mWindowHeight;
    private float mDownY;
    private int mTopBottom, mTop;
    private LinearLayout mDragLayout;
    private int mDragLayoutHeight;
    private int mMinTop = 0, mMaxTop;
    private String mContent = "4月15日，国务院总理李克强先后考察清华大学、北京大学。细看总理一天的行程安排，可发现与15日下午召开的高等教育改革创新座谈会紧密呼应。共有53所在京的部属、市属、民办高校和有关部门负责人参加会议，其中北京大学、清华大学、北京科技大学主要负责人和中国人民大学、北京第二外国语学院教师代表分别发言。\n" +
            "　　李克强指出：“要加快推进高等教育领域‘放、管、服’改革。结合高校特点，简除烦苛，给学校更大办学自主权。凡高校能够依法自主管理的，相关行政审批权该下放的下放，要抓紧修改或废止不合时宜的行政法规和政策文件，破除制约学校发展的不合理束缚。4月15日，国务院总理李克强先后考察清华大学、北京大学。细看总理一天的行程安排，可发现与15日下午召开的高等教育改革创新座谈会紧密呼应。共有53所在京的部属、市属、民办高校和有关部门负责人参加会议，其中北京大学、清华大学、北京科技大学主要负责人和中国人民大学、北京第二外国语学院教师代表分别发言。\n" +
            "\" +\n" +
            "            \"　　李克强指出：“要加快推进高等教育领域‘放、管、服’改革。结合高校特点，简除烦苛，给学校更大办学自主权。凡高校能够依法自主管理的，相关行政审批权该下放的下放，要抓紧修改或废止不合时宜的行政法规和政策文件，破除制约学校发展的不合理束缚。4月15日，国务院总理李克强先后考察清华大学、北京大学。细看总理一天的行程安排，可发现与15日下午召开的高等教育改革创新座谈会紧密呼应。共有53所在京的部属、市属、民办高校和有关部门负责人参加会议，其中北京大学、清华大学、北京科技大学主要负责人和中国人民大学、北京第二外国语学院教师代表分别发言。\n" +
            "\" +\n" +
            "            \"　　李克强指出：“要加快推进高等教育领域‘放、管、服’改革。结合高校特点，简除烦苛，给学校更大办学自主权。凡高校能够依法自主管理的，相关行政审批权该下放的下放，要抓紧修改或废止不合时宜的行政法规和政策文件，破除制约学校发展的不合理束缚。4月15日，国务院总理李克强先后考察清华大学、北京大学。细看总理一天的行程安排，可发现与15日下午召开的高等教育改革创新座谈会紧密呼应。共有53所在京的部属、市属、民办高校和有关部门负责人参加会议，其中北京大学、清华大学、北京科技大学主要负责人和中国人民大学、北京第二外国语学院教师代表分别发言。\n" +
            "\" +\n" +
            "            \"　　李克强指出：“要加快推进高等教育领域‘放、管、服’改革。结合高校特点，简除烦苛，给学校更大办学自主权。凡高校能够依法自主管理的，相关行政审批权该下放的下放，要抓紧修改或废止不合时宜的行政法规和政策文件，破除制约学校发展的不合理束缚。”";
    private View rootView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        setContentView(rootView);

        rootView.post(new Runnable() {
            @Override
            public void run() {

                initViews(rootView);
            }
        });
    }

    private void initViews(View rootView) {
        mTopScrollView = findView(rootView, R.id.top_sv);
        mBottomLayout = findView(rootView, R.id.bottom_layout);
        mDragButton = findView(rootView, R.id.drag_tv);
        mBottomScrollView = findView(rootView, R.id.bottom_sv);

        mTopContentTv = findView(rootView, R.id.top_content);
        mBottomContentTv = findView(rootView, R.id.bottom_content);
        mDragLayout = findView(rootView, R.id.drag_layout);

        WindowManager window = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        window.getDefaultDisplay().getMetrics(outMetrics);
        mWindowHeight = outMetrics.heightPixels;

        measureDragLayoutHeight(mDragLayout);
        mMaxTop = mWindowHeight - mDragLayoutHeight - getContentViewTop();

        bindingViewListeners();
        initData();
    }

    private <T extends View> T findView(View rootView, int id) {
        return (T) rootView.findViewById(id);
    }

    /**
     * 获取可见content的top
     *
     * @return
     */
    private int getContentViewTop() {
        Rect outRect = new Rect();
        getWindow().findViewById(Window.ID_ANDROID_CONTENT).getDrawingRect(outRect);
        measureDragLayoutHeight(mDragLayout);
        return mWindowHeight - outRect.height();
    }

    /**
     * 获取拖拽按钮的高度
     *
     * @param view
     */
    private void measureDragLayoutHeight(View view) {
        view.measure(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mDragLayoutHeight = view.getMeasuredHeight();
    }

    private void initData() {

        mTopContentTv.setText(mContent);
        mBottomContentTv.setText(mContent);
    }

    private void bindingViewListeners() {

        mDragButton.setOnTouchListener(this);
    }



    private RelativeLayout coverLayout;
    protected RelativeLayout getCoverLayout() { if (this.coverLayout == null) { this.coverLayout = (RelativeLayout) findViewById(R.id.coverLayout); } return this.coverLayout; }

    boolean dragging = false;

    boolean initialTouchSet = false;
    int initialTouchX;
    int initialTouchY;

    int lastTouchX;
    int lastTouchY;

    @Override
    public boolean onTouch(View v, MotionEvent e) {
        final int action = MotionEventCompat.getActionMasked(e);

        if (!this.initialTouchSet) {
            this.initialTouchX = this.lastTouchX = (int) (e.getRawX() + 0.5f);
            this.initialTouchY = this.lastTouchY = (int) (e.getRawY() + 0.5f);
            this.initialTouchSet = true;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE: {
                final int x = (int) (e.getRawX() + 0.5f);
                final int y = (int) (e.getRawY() + 0.5f);

                if (!this.dragging) {
                    final int dx = x - this.initialTouchX;
                    final int dy = y - this.initialTouchY;

                    int touchSlop = ViewConfiguration.get(this).getScaledTouchSlop();

                    if (Math.abs(dx) > touchSlop) {
                        this.lastTouchX = this.initialTouchX + touchSlop * (dx < 0 ? -1 : 1);
                        this.dragging = true;
                    }
                    else if (Math.abs(dy) > touchSlop) {
                        this.lastTouchY = this.initialTouchY + touchSlop * (dy < 0 ? -1 : 1);
                        this.dragging = true;
                    }

                    if (this.dragging) {
                        final int oldAction = e.getAction();
                        e.setAction(MotionEvent.ACTION_CANCEL);
                        v.onTouchEvent(e);
                        e.setAction(oldAction);

                        onDraggingBegin();
                    }
                }
                else {
                    int dx = x - this.lastTouchX;
                    int dy = y - this.lastTouchY;

                    this.lastTouchX = x;
                    this.lastTouchY = y;

                    onDragging(dx, dy);
                }
            }
            break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                if (this.dragging) {
                    onDraggingEnd();

                    this.initialTouchSet = false;
                    this.dragging = false;
                }
            }
        }

        return true;
    }

    private void onDraggingBegin() {

    }

    private void onDragging(int dx, int dy) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mDragLayout.getLayoutParams();
        params.topMargin += dy;

        params.topMargin = Math.max(params.topMargin, 0);
        // TODO: 2016/4/21 最大能拖动到多低的位置
        params.topMargin = Math.min(params.topMargin, getCoverLayout().getHeight() - mDragLayout.getHeight());

        mDragLayout.setLayoutParams(params);
    }

    private void onDraggingEnd() {

    }
}
