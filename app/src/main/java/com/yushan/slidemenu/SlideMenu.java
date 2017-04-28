package com.yushan.slidemenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by beiyong on 2017-1-16.
 */

public class SlideMenu extends ViewGroup {

    private Scroller scroller;
    private View menuView;
    private int menuViewWidth;
    private View mainView;
    private float downX;
    private boolean hasMove = false;
    private float distanceY;
    private int oldDistanceY;
    private float downY;

    // 当前的state,默认是关闭状态
    private DragState mState = DragState.Open;

    //定义状态常量
    public enum DragState {
        Open, Close;
    }

    public SlideMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public SlideMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SlideMenu(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        scroller = new Scroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        menuView = getChildAt(0);
        menuViewWidth = menuView.getLayoutParams().width;
        menuView.measure(menuViewWidth, heightMeasureSpec);

        mainView = getChildAt(1);
        mainView.measure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean bl, int l, int t, int r, int b) {
        menuView.layout(l - menuViewWidth, t, l, b);

        mainView.layout(l, t, r, b);
    }

    public void scrollTo(int x) {
        super.scrollTo(-x, 0);
    }

    public int getMeasure() {
        return -super.getScrollX();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:

                distanceY = event.getX() - downX;
                if (distanceY > 10) {
                    hasMove = true;
                } else {
                    hasMove = false;
                }

                if (distanceY < 0) {
                    distanceY = 0;
                } else if (distanceY > menuViewWidth) {
                    distanceY = menuViewWidth;
                }

                scrollTo((int) distanceY);
                break;
            case MotionEvent.ACTION_UP:
                if (hasMove) {
                    if (distanceY > menuViewWidth / 2) {
                        oldDistanceY = menuViewWidth;
                        mState = DragState.Open;
                    } else {
                        oldDistanceY = 0;
                        mState = DragState.Close;
                    }

                    int startX = (int) distanceY;
                    int destX = (int) oldDistanceY;

                    animationScroll(startX, destX);
                } else {
                    if (mState != DragState.Close) {
                        showMenu();
                    }
                }
                hasMove = false;
                break;
        }

        return true;
    }

    /**
     * 方法功能：自动归位
     *
     * @param startX
     * @param destX
     */
    private void animationScroll(int startX, int destX) {
        int distanceX = destX - startX;
        int startY = 0;
        int distanceY = 0;
        float maxDuration = 1000f;
        float maxMoveDistance = menuViewWidth;
        float scale = maxDuration / maxMoveDistance;
        int duration = (int) (Math.abs(distanceX) - scale);

        scroller.startScroll(startX, startY, distanceX, distanceY, duration);

        invalidate();
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX());

            invalidate();
        }
    }

    /**
     * 通过拦截Touch事件，解决侧拉菜单左右无法滑动问题
     * @param ev
     * @return
     */
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//
//        switch (ev.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                downX = ev.getX();
//                downY = ev.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                float distanceX = Math.abs(ev.getX() - downX);
//                float distanceY = Math.abs(ev.getY() - downY);
//                if (distanceY < distanceX){
//                    return true;
//                }
//
//                break;
//        }
//
//        return super.onInterceptTouchEvent(ev);
//    }

    /**
     * 方法功能：侧拉菜单的显示与隐藏
     */
    public void showMenu() {
        int startX;
        int destX;
        if (oldDistanceY > 0) {
            startX = menuViewWidth;
            destX = 0;
            mState = DragState.Close;
        } else {
            startX = 0;
            destX = menuViewWidth;
            mState = DragState.Open;
        }

        oldDistanceY = destX;
        animationScroll(startX, destX);
    }
}
