package com.js.library.widget.captcha;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatSeekBar;

import com.js.library.R;


/**
 * Created by luozhanming on 2018/1/17.
 */

public class TextSeekBar extends AppCompatSeekBar {

    private Paint textPaint;
    private int mWidth = 0;

    public TextSeekBar(Context context) {
        super(context);
    }

    public TextSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, R.style.Captcha_SeekBar);
    }

    public TextSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        textPaint = new Paint();
        textPaint.setTextAlign(Paint.Align.CENTER);
        int textSize = dp2px(context, 14);
        textPaint.setTextSize(textSize);
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.parseColor("#7d7c7c"));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        mWidth = getWidth();
        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
        int baseLineY = (int) (getHeight() / 2 - top / 2 - bottom / 2);//基线中间点的y轴计算公式
        canvas.drawText("拖动左滑块完成上方拼图", getWidth() / 2, baseLineY, textPaint);
    }

    public int getSeekBarWidth() {
        return mWidth;
    }

    public static int dp2px(Context ctx, float dip) {
        float density = ctx.getResources().getDisplayMetrics().density;
        return (int) (dip * density);
    }
}
