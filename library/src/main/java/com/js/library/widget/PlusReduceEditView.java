package com.js.library.widget;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.js.library.R;


public class PlusReduceEditView extends LinearLayout implements OnClickListener {

    private LayoutInflater mInflater;
    private ImageView mImgPlus;
    private ImageView mImgReduce;
    private EditText mEditText;
    private int max = 99;
    private int min = 0;

    private int mValue = 0;
    private TextWatcher mWatcher;

    public PlusReduceEditView(Context context) {
        super(context);
        mInflater = LayoutInflater.from(context);

        initView();
    }

    public PlusReduceEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = LayoutInflater.from(context);

        initView();
    }

    public void addTextChangedListener(TextWatcher watcher) {
        mWatcher = watcher;
    }

    public void setEnable(boolean enable) {
        mImgPlus.setEnabled(enable);
        mImgPlus.setClickable(enable);
        mImgReduce.setEnabled(enable);
        mImgReduce.setEnabled(enable);
        mEditText.setEnabled(enable);
        super.setEnabled(enable);
    }

    private void initView() {
        mInflater.inflate(R.layout.layout_plus_reduce_edittext, this, true);

        mImgPlus = findViewById(R.id.plus_reduce_edittext_plus);
        mImgPlus.setOnClickListener(this);
        mImgReduce = findViewById(R.id.plus_reduce_edittext_reduce);
        mImgReduce.setOnClickListener(this);
        mEditText = findViewById(R.id.plus_reduce_edittext_edit);

        mEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    mValue = min;
                    mImgReduce.setEnabled(false);
                } else {
                    mValue = Integer.valueOf(s.toString());
                    if (mValue < min) {
                        mEditText.setText(String.valueOf(min));
                        mImgReduce.setEnabled(false);
                        mImgPlus.setEnabled(true);
                    } else if (mValue == min) {
                        mImgReduce.setEnabled(false);
                        mImgPlus.setEnabled(true);
                    } else if (mValue == max) {
                        mImgReduce.setEnabled(true);
                        mImgPlus.setEnabled(false);
                    } else if (mValue > max) {
                        s.delete(s.length() - 1, s.length());
                    } else {
                        mImgReduce.setEnabled(true);
                        mImgPlus.setEnabled(true);
                    }
                    mEditText.setSelection(s.toString().length());
                }
                if (mWatcher != null) {
                    mWatcher.afterTextChanged(s);
                }
            }
        });
        mEditText.setText(String.valueOf(min));
    }

    public int getValue() {
        return mValue;
    }

    /**
     * 没在最大最小范围内会设置不成功
     */
    public void setEditText(int num) {
        if (num >= min && num <= max) {
            mEditText.setText(String.valueOf(num));
        }
    }

    /**
     * 设置编辑框的宽度和编辑框maxlength 建议根据最大的value来设置合理的宽度 需要手动设置max 默认为99
     */
    public void setViewWidth(int pixels) {
        mEditText.setWidth(pixels);
        // if(editTextLength > 1){
        // InputFilter[] filters = {new LengthFilter(editTextLength)};
        // mEditText.setFilters(filters);
        // }
    }

    /**
     * @param max 设置最大值 默认为99
     */
    public void setValueMax(int max) {
        this.max = max;
        if (String.valueOf(max).length() > 0) {
            InputFilter[] filters = {new LengthFilter(String.valueOf(max)
                    .length())};
            mEditText.setFilters(filters);
            if (!TextUtils.isEmpty(mEditText.getText().toString())) {
                int currentValue = Integer.parseInt(mEditText.getText().toString());

                if (currentValue >= max) {
                    mEditText.setText(String.valueOf(max));
                    mImgPlus.setEnabled(false);
                } else {
                    mImgPlus.setEnabled(true);
                }
            }
        }
    }

    /**
     * @param min 设置最小值 默认为0
     */
    public void setValueMin(int min) {
        this.min = min;
        try {
            if (Double.parseDouble(mEditText.getText().toString()) < min) {
                mEditText.setText(String.valueOf(min));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置editext是否可输入 默认为可以输入
     *
     * @param inputable true 为可以输入 false 为不可以
     */
    public void setEditTextInput(boolean inputable) {
        mEditText.setEnabled(inputable);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.plus_reduce_edittext_plus) {
            if (mValue < max) {
                mValue++;
                mEditText.setText(String.valueOf(mValue));
            }
        } else if (id == R.id.plus_reduce_edittext_reduce) {
            if (mValue > min) {
                mValue--;
                mEditText.setText(String.valueOf(mValue));
            }
        }
    }

}
