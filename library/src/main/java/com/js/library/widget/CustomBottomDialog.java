package com.js.library.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import com.js.library.R;
import com.js.library.common.util.ScreenUtils;


public class CustomBottomDialog {

    private View mContentView;// dialog的内容view

    private Dialog mDialog;

    private boolean mAutoDismiss = true;

    private Context mContext;

    private int mTopMargin;

    private int mMaxHeight;

    private int mHeight;

    public interface onDismissListener {

        public void onDismiss();
    }

    /**
     * 加载一个自定义view
     *
     * @param view 自定义view
     */
    public CustomBottomDialog(View view) {
        mContentView = view;
        mContext = view.getContext();
        init();
    }

    /**
     * 加载一个自定义view
     *
     * @param view      自定义view
     * @param topMargin view离屏幕顶部最小距离 单位px 设置之后不要再设置maxHeight
     * @param maxHeight view最大高度 单位px 设置之后不要再设置topMargin
     */
    public CustomBottomDialog(View view, int topMargin, int maxHeight) {
        mContentView = view;
        mContext = view.getContext();
        mTopMargin = topMargin;
        mMaxHeight = maxHeight;
        init();
    }

    /**
     * 加载一个自定义view
     *
     * @param view      自定义view
     * @param topMargin view离屏幕顶部最小距离 单位px 设置之后不要再设置maxHeight
     * @param maxHeight view最大高度 单位px 设置之后不要再设置topMargin
     * @param height    设置view实际高度为自适应WRAP_CONTENT或者填满MATCH_PARENT 或者直接为px
     */
    public CustomBottomDialog(View view, int topMargin, int maxHeight,
                              int height) {
        mContentView = view;
        mContext = view.getContext();
        mTopMargin = topMargin;
        mMaxHeight = maxHeight;
        mHeight = height;
        init();
    }

    public void setOnCancelListener(OnCancelListener l) {
        if (l != null) {
            mDialog.setOnCancelListener(l);
        }
    }

    public void setOnDismissListener(OnDismissListener l) {
        if (l != null) {
            mDialog.setOnDismissListener(l);
        }
    }

    private void init() {
        mDialog = new Dialog(mContentView.getContext(), R.style.common_dialog);
        mDialog.setCancelable(true);
        mDialog.setContentView(getView());
        Window dialogWindow = mDialog.getWindow();
        dialogWindow.setWindowAnimations(R.style.wheelDialogWindowAnim);
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        params.dimAmount = 0.5f;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(params);
        dialogWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialogWindow.setLayout(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        dialogWindow.setGravity(Gravity.BOTTOM);
    }

    private View getView() {
        FrameLayout rootFl = new FrameLayout(mContext);
        rootFl.setBackgroundColor(mContext.getResources().getColor(
                R.color.transparent));
        FrameLayout fl = new FrameLayout(mContext);
        if (mHeight == 0) {
            mHeight = LayoutParams.WRAP_CONTENT;
        }
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, mHeight);
        if (mMaxHeight > 0) {
            mTopMargin = ScreenUtils.getScreenHeight() - mMaxHeight;
        }
        lp.topMargin = mTopMargin;
        lp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        fl.setLayoutParams(lp);
        fl.addView(mContentView);
        rootFl.addView(fl);
        rootFl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAutoDismiss) {
                    mDialog.dismiss();
                }
            }
        });
        return rootFl;
    }

    public void needAutoDismiss(boolean need) {
        mAutoDismiss = need;
    }

    public void show() {
        mDialog.show();
    }

    public void dismiss() {
        mDialog.dismiss();
    }

    public boolean isShowing() {
        return mDialog.isShowing();
    }
}
