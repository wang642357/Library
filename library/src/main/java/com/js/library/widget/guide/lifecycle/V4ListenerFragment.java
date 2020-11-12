package com.js.library.widget.guide.lifecycle;


import android.util.Log;

import androidx.fragment.app.Fragment;

/**
 * Created by hubert
 * <p>
 * Created on 2017/9/13.
 */

public class V4ListenerFragment extends Fragment {

    FragmentLifecycle mFragmentLifecycle;

    public void setFragmentLifecycle(FragmentLifecycle lifecycle) {
        mFragmentLifecycle = lifecycle;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("NewbieGuide", "onStart");
        if (mFragmentLifecycle != null) {
            mFragmentLifecycle.onStart();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mFragmentLifecycle != null) {
            mFragmentLifecycle.onStop();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mFragmentLifecycle != null) {
            mFragmentLifecycle.onDestroyView();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("NewbieGuide", "onDestroy");
        if (mFragmentLifecycle != null) {
            mFragmentLifecycle.onDestroy();
        }
    }
}
