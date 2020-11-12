package com.js.library.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.js.library.R;

import java.util.List;

public class UPMarqueeView extends ViewFlipper {

    private Context mContext;
    private boolean isSetAnimDuration = false;
    private int interval = 2000;
    /**
     * 动画时间
     */
    private int animDuration = 500;

    public UPMarqueeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.mContext = context;
        setFlipInterval(interval);
        Animation animIn = AnimationUtils.loadAnimation(mContext, R.anim.anim_marquee_in);
        if (isSetAnimDuration) animIn.setDuration(animDuration);
        setInAnimation(animIn);
        Animation animOut = AnimationUtils.loadAnimation(mContext, R.anim.anim_marquee_out);
        if (isSetAnimDuration) animOut.setDuration(animDuration);
        setOutAnimation(animOut);
    }

    /**
     * 设置循环滚动的View数组
     *
     * @param views
     */
    public void setViews(List<View> views) {
        if (views == null || views.size() == 0)
            return;
        removeAllViews();
        for (int i = 0; i < views.size(); i++) {
            addView(views.get(i));
        }
        startFlipping();
    }

    public void setData(List<String> data) {
        if (data == null || data.size() <= 0)
            return;
        removeAllViews();
        for (int i = 0; i < data.size(); i++) {
            //设置滚动的单个布局
            TextView text = new TextView(getContext());
            text.setTextColor(Color.parseColor("#636363"));
            text.setMaxLines(1);
            text.setEllipsize(TextUtils.TruncateAt.END);
            text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            //进行对控件赋值
            String str = data.get(i);
            text.setText(str);
            //添加到循环滚动数组里面去
            addView(text);
        }
        startFlipping();
    }
}
