package com.js.library.widget.captcha;

import android.animation.Animator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.js.library.R;

import java.math.RoundingMode;
import java.text.NumberFormat;

/**
 * Created by luozhanming on 2018/1/17.
 */

public class Captcha extends LinearLayout {

    //控件成员
    private PictureVerifyView vertifyView;         //拼图块
    private TextSeekBar mSeekbar;                    //滑动条块
    private View accessSuccess, accessFailed;       //验证成功/失败显示的视图
    private TextView accessText, accessFailedText;  //验证成功/失败显示的文字
    private ImageView refreshView;                  //刷新按钮
    private ImageView closeView;                     //关闭按钮
    //控件属性
    private int drawableId = -1;          //验证图片资源id
    private int progressDrawableId;  //滑动条背景id
    private int thumbDrawableId;     //滑动条滑块id
    private int mMode;               //控件验证模式(有滑动条/无滑动条)
    private int maxFailedCount;      //最大失败次数
    private int failCount;           //已失败次数
    private int blockSize;           //拼图缺块大小
    private String mUrl;
    //处理滑动条逻辑
    private boolean isResponse;
    private boolean isDown;
    private Bitmap mBitmap;
    private CaptchaListener mListener;

    private BitmapLoaderTask mTask;
    /**
     * 带滑动条验证模式
     */
    public static final int MODE_BAR = 1;
    /**
     * 不带滑动条验证，手触模式
     */
    public static final int MODE_NONBAR = 2;

    @IntDef(value = {MODE_BAR, MODE_NONBAR})
    public @interface Mode {

    }


    public interface CaptchaListener {

        void onClose();

        /**
         * Called when captcha access.
         *
         * @param time cost of access time
         * @return text to show,show default when return null
         */
        String onAccess(long time);

        /**
         * Called when captcha failed.
         *
         * @param failCount fail count
         * @return text to show,show default when return null
         */
        String onFailed(int failCount);

        /**
         * Called when captcha failed
         *
         * @return text to show,show default when return null
         */
        String onMaxFailed();

    }


    public Captcha(@NonNull Context context) {
        super(context);
    }

    public Captcha(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Captcha(@NonNull final Context context, @Nullable AttributeSet attrs,
                   @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Captcha);
        drawableId = typedArray.getResourceId(R.styleable.Captcha_src, R.drawable.icon_captcha_move);
        progressDrawableId = typedArray
                .getResourceId(R.styleable.Captcha_progressDrawable, R.drawable.shape_captcha_po_seekbar);
        thumbDrawableId = typedArray
                .getResourceId(R.styleable.Captcha_thumbDrawable, R.drawable.icon_captcha_seekbar);
        mMode = typedArray.getInteger(R.styleable.Captcha_mode, MODE_BAR);
        maxFailedCount = typedArray.getInteger(R.styleable.Captcha_max_fail_count, 3);
        blockSize = typedArray
                .getDimensionPixelSize(R.styleable.Captcha_blockSize, dp2px(getContext(), 50));
        typedArray.recycle();
        init();
    }


    private void init() {
        View parentView = LayoutInflater.from(getContext())
                .inflate(R.layout.layout_captcha_container, this, true);
        vertifyView = parentView.findViewById(R.id.vertifyView);
        mSeekbar = parentView.findViewById(R.id.seekbar);
        accessSuccess = parentView.findViewById(R.id.accessRight);
        accessFailed = parentView.findViewById(R.id.accessFailed);
        accessText = parentView.findViewById(R.id.accessText);
        accessFailedText = parentView.findViewById(R.id.accessFailedText);
        refreshView = parentView.findViewById(R.id.refresh);
        closeView = parentView.findViewById(R.id.close);
        setMode(mMode);
        if (drawableId != -1) {
            vertifyView.setImageResource(drawableId);
        }
        setBlockSize(blockSize);
        vertifyView.callback(new PictureVerifyView.Callback() {

            @Override
            public void onSuccess(long time) {
                if (mListener != null) {
                    String s = mListener.onAccess(time);
                    if (s != null) {
                        accessText.setText(s);
                    } else {//默认文案
                        accessText
                                .setText(String.format(getResources().getString(R.string.captcha_verify_access), time));
                    }
                }
                accessSuccess.setVisibility(VISIBLE);
                accessFailed.setVisibility(GONE);
            }

            @Override
            public void onFailed() {
                mSeekbar.setEnabled(false);
                vertifyView.setTouchEnable(false);
                failCount = failCount > maxFailedCount ? maxFailedCount : failCount + 1;
                accessFailed.setVisibility(VISIBLE);
                accessSuccess.setVisibility(GONE);
                if (mListener != null) {
                    if (failCount == maxFailedCount) {
                        String s = mListener.onMaxFailed();
                        if (s != null) {
                            accessFailedText.setText(s);
                        } else {//默认文案
                            accessFailedText.setText(String
                                    .format(getResources().getString(R.string.captcha_verify_failed),
                                            maxFailedCount - failCount));
                        }
                    } else {
                        String s = mListener.onFailed(failCount);
                        if (s != null) {
                            accessFailedText.setText(s);
                        } else {//默认文案
                            accessFailedText.setText(String
                                    .format(getResources().getString(R.string.captcha_verify_failed),
                                            maxFailedCount - failCount));
                        }
                    }
                }
            }

        });
        setSeekBarStyle(progressDrawableId, thumbDrawableId);
        //用于处理滑动条渐滑逻辑
        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int progresslength = 12800 / mSeekbar.getSeekBarWidth() + 15;
                if (isDown) {  //手指按下
                    isDown = false;
                    //seekbar宽度 -  thumb宽度
                    if (progress > progresslength) { //按下位置不正确
                        isResponse = false;
                    } else {
                        isResponse = true;
                        accessFailed.setVisibility(GONE);
                        vertifyView.down(0);
                    }
                }
                if (isResponse) {
                    vertifyView.move(progress);
                } else {
                    seekBar.setProgress(0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isDown = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (isResponse) {
                    vertifyView.loose();
                }
            }
        });
        refreshView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startRefresh(v);
            }
        });
        closeView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClose();
            }
        });
    }

    private void startRefresh(View v) {
        //点击刷新按钮，启动动画
        v.animate().rotationBy(360).setDuration(500)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        reset(false);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }


    public void setCaptchaListener(CaptchaListener listener) {
        this.mListener = listener;
    }

    public void setCaptchaStrategy(CaptchaStrategy strategy) {
        if (strategy != null) {
            vertifyView.setCaptchaStrategy(strategy);
        }
    }

    public void setSeekBarStyle(@DrawableRes int progressDrawable, @DrawableRes int thumbDrawable) {
        mSeekbar.setProgressDrawable(getResources().getDrawable(progressDrawable));
        mSeekbar.setThumb(getResources().getDrawable(thumbDrawable));
        mSeekbar.setThumbOffset(0);
    }

    private String doubleToString(double value, int precision,
                                  boolean isUpDown) {
        NumberFormat df = NumberFormat.getInstance();
        df.setMaximumFractionDigits(precision);
        df.setMinimumFractionDigits(precision);
        if (!isUpDown) {
            df.setRoundingMode(RoundingMode.DOWN);
        }
        return df.format(value).replace(",", "");
    }

    /**
     * 设置滑块图片大小，单位px
     */
    public void setBlockSize(int blockSize) {
        vertifyView.setBlockSize(blockSize);
    }

    /**
     * 设置滑块验证模式
     */
    public void setMode(@Mode int mode) {
        this.mMode = mode;
        vertifyView.setMode(mode);
        if (mMode == MODE_NONBAR) {
            mSeekbar.setVisibility(GONE);
            vertifyView.setTouchEnable(true);
        } else {
            mSeekbar.setVisibility(VISIBLE);
            mSeekbar.setEnabled(true);
        }
        hideText();
    }

    public int getMode() {
        return this.mMode;
    }

    public void setMaxFailedCount(int count) {
        this.maxFailedCount = count;
    }

    public int getMaxFailedCount() {
        return this.maxFailedCount;
    }


    public void setCaptchaBitmap(int drawableId) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawableId);
        setCaptchaBitmap(bitmap);
    }

    public void setCaptchaBitmap(Bitmap bitmap) {
        vertifyView.setImageBitmap(bitmap);
        reset(false);
    }

    public void setCaptchaBitmap(String url) {
        if (TextUtils.isEmpty(url) || url.length() <= 0) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawableId);
            vertifyView.setImageBitmap(bitmap);
            reset(false);
            return;
        }
        mUrl = url;
        new Thread(runnable).start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                setCaptchaBitmap(mBitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //图片地址转一下bitmap
            try {
                mBitmap = BitmapUtil.getBitmap(mUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Message msg = new Message();
            handler.sendMessage(msg);
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        if (mTask != null && mTask.getStatus().equals(AsyncTask.Status.RUNNING)) {
            mTask.cancel(true);
        }
        super.onDetachedFromWindow();
    }

    /**
     * 复位
     *
     * @param clearFailed 是否清除失败次数
     */
    public void reset(boolean clearFailed) {
        hideText();
        vertifyView.reset();
        if (clearFailed) {
            failCount = 0;
        }
        if (mMode == MODE_BAR) {
            mSeekbar.setEnabled(true);
            mSeekbar.setProgress(0);
        } else {
            vertifyView.setTouchEnable(true);
        }
    }

    /**
     * 隐藏成功失败文字显示
     */
    public void hideText() {
        accessFailed.setVisibility(GONE);
        accessSuccess.setVisibility(GONE);
    }

    public static int dp2px(Context ctx, float dip) {
        float density = ctx.getResources().getDisplayMetrics().density;
        return (int) (dip * density);
    }
}
