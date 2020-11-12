package com.js.library.base.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.lang.reflect.Field;

/**
 * Author：wangjianxiong
 * Date：2020/11/11
 * <p>
 * Desc：解决内存泄漏的DialogFragment
 */
public class BaseDialogFragment extends AppCompatDialogFragment implements OnTouchListener {

    private boolean mCancelable = false;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        boolean isShow = this.getShowsDialog();
        this.setShowsDialog(false);
        super.onActivityCreated(savedInstanceState);
        this.setShowsDialog(isShow);

        View view = getView();
        if (view != null) {
            if (view.getParent() != null) {
                throw new IllegalStateException(
                        "DialogFragment can not be attached to a container view");
            }
            this.getDialog().setContentView(view);
        }
        final Activity activity = getActivity();
        if (activity != null) {
            this.getDialog().setOwnerActivity(activity);
        }
        this.getDialog().setCancelable(false);
        this.getDialog().getWindow().getDecorView().setOnTouchListener(this);
        this.getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ESCAPE) {
                    dismissAllowingStateLoss();
                    return true;
                }
                return false;
            }
        });
        if (savedInstanceState != null) {
            Bundle dialogState = savedInstanceState.getBundle("android:savedDialogState");
            if (dialogState != null) {
                this.getDialog().onRestoreInstanceState(dialogState);
            }
        }
    }

    public void setCancelable(boolean mCancelable) {
        this.mCancelable = mCancelable;
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        try {
            Class<?> aClass = Class.forName("androidx.fragment.app.DialogFragment");
            Field mDismissed = aClass.getDeclaredField("mDismissed");
            Field mShownByMe = aClass.getDeclaredField("mShownByMe");
            mDismissed.setAccessible(true);
            mShownByMe.setAccessible(true);
            mDismissed.setBoolean(this, false);
            mShownByMe.setBoolean(this, true);
        } catch (NoSuchFieldException | ClassNotFoundException | IllegalAccessException e) {
            e.printStackTrace();
        }
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Dialog dialog = getDialog();
        if (dialog != null) {
            if (mCancelable && dialog.isShowing()) {
                dismiss();
                return true;
            }
        }
        return false;
    }

}
