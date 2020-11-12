package com.js.library.widget.roundimageview;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.js.library.R;


/**
 * 图片圆角描边说明
 * <p>
 * 调用说明： 在布局直接引用： <com.zdit.advert.util.view.RoundedImageView android:layout_width="70dp"
 * android:layout_height="70dp" android:scaleType="centerCrop" />
 * <p>
 * com.zdit.advert.util.view.RoundedImageView
 *
 * @author ChengChangmu <br/> Create at 2014-10-30 下午4:28:26
 */
@SuppressLint("AppCompatCustomView")
public class RoundedImageView extends ImageView {

  public static final String TAG = "RoundedImageView";
  public static final float DEFAULT_RADIUS = 20;
  public static final float DEFAULT_BORDER_WIDTH = 0f;

  public static final int GRAVITY_LEFT = 1 << 1;
  public static final int GRAVITY_TOP = 1 << 2;
  public static final int GRAVITY_RIGHT = 1 << 3;
  public static final int GRAVITY_BOTTOM = 1 << 4;
  public static final int GRAVITY_CENTER_HORIZONTAL = 1 << 5;
  public static final int GRAVITY_CENTER_VITICAL = 1 << 6;
  public static final int GRAVITY_CENTER = GRAVITY_CENTER_VITICAL | GRAVITY_CENTER_HORIZONTAL;

  /**
   * ImageView的属性android:scaleType，即ImageView.setScaleType(ImageView.ScaleType
   * )。android:scaleType是控制图片如何resized/moved来匹对ImageView的size。 ImageView.ScaleType /
   * android:scaleType值的意义区别： CENTER /center 按图片的原来size居中显示，当图片长/宽超过View的长/宽，则截取图片的居中部分显示
   * CENTER_CROP / centerCrop 按比例扩大图片的size居中显示，使得图片长(宽)等于或大于View的长(宽) CENTER_INSIDE / centerInside
   * 将图片的内容完整居中显示，通过按比例缩小或原来的size使得图片长/宽等于或小于View的长/宽 FIT_CENTER / fitCenter
   * 把图片按比例扩大/缩小到View的宽度，居中显示 FIT_END / fitEnd 把图片按比例扩大/缩小到View的宽度，显示在View的下部分位置 FIT_START /
   * fitStart 把图片按比例扩大/缩小到View的宽度，显示在View的上部分位置 FIT_XY / fitXY 把图片不按比例 扩大/缩小到View的大小显示 MATRIX /
   * matrix 用矩阵来绘制
   */
  // 这个还是需要，这个是本来就是系统原生的，在这里定义主要是方便xml可配置
  private static final ScaleType[] SCALE_TYPES = {ScaleType.MATRIX, ScaleType.FIT_XY,
          ScaleType.FIT_START,
          ScaleType.FIT_CENTER, ScaleType.FIT_END, ScaleType.CENTER, ScaleType.CENTER_CROP,
          ScaleType.CENTER_INSIDE};

  private float cornerRadius = DEFAULT_RADIUS;
  private float borderWidth = DEFAULT_BORDER_WIDTH;
  // private ColorStateList borderColor =
  // ColorStateList.valueOf(getResources()
  // .getColor(RoundedDrawable.DEFAULT_BORDER_COLOR));

  private int borderColor = RoundedDrawable.DEFAULT_BORDER_COLOR;
  private boolean isOval = false;
  private boolean mutateBackground = false;

  private int mResource;
  private Drawable mDrawable;
  private Drawable mBackgroundDrawable;

  private ScaleType mScaleType;

  private String mErrorMsg;

  private Bitmap mErrorBitmap;
  private Paint mErrorPaint;
  private int leftPosition;

  private Drawable mLabelDrawable;
  private boolean mLabelVisible = true;
  private int mLabelOffsetLeft;
  private int mLabelOffsetRight;
  private int mLabelOffsetTop;
  private int mLabelOffsetBottom;
  private Point mLabelSize;
  private Point mLabelOffset;
  private int mLabelGravity = GRAVITY_LEFT | GRAVITY_TOP;
  private int mMaskColor = 0;

  public RoundedImageView(Context context) {
    super(context);
  }

  public RoundedImageView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  @SuppressLint("ResourceAsColor")
  public RoundedImageView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);

    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundedImageView, defStyle, 0);

    int index = a.getInt(R.styleable.RoundedImageView_android_scaleType, -1);
    if (index >= 0) {
      setScaleType(SCALE_TYPES[index]);
    } else {
      // default scaletype to FIT_CENTER
      setScaleType(ScaleType.FIT_CENTER);
    }

    cornerRadius = a.getDimensionPixelSize(R.styleable.RoundedImageView_corner_radius, -1);
    borderWidth = a.getDimensionPixelSize(R.styleable.RoundedImageView_border_width, -1);

    mLabelDrawable = a.getDrawable(R.styleable.RoundedImageView_label_src);
    mLabelOffsetLeft = Math
            .max(0, a.getDimensionPixelOffset(R.styleable.RoundedImageView_label_offsetleft, -1));
    mLabelOffsetRight = Math
            .max(0, a.getDimensionPixelOffset(R.styleable.RoundedImageView_label_offsetright, -1));
    mLabelOffsetTop = Math
            .max(0, a.getDimensionPixelOffset(R.styleable.RoundedImageView_label_offsettop, -1));
    mLabelOffsetBottom = Math.max(0,
            a.getDimensionPixelOffset(R.styleable.RoundedImageView_label_offsetbottom, -1));
    mLabelGravity = a
            .getInteger(R.styleable.RoundedImageView_label_gravity, GRAVITY_LEFT | GRAVITY_TOP);
    // don't allow negative values for radius and border
    if (cornerRadius < 0) {
      cornerRadius = DEFAULT_RADIUS;
    }
    if (borderWidth < 0) {
      borderWidth = DEFAULT_BORDER_WIDTH;
    }

    mMaskColor = a.getColor(R.styleable.RoundedImageView_mask_color, 0);
    // borderColor = a
    // .getColorStateList(R.styleable.RoundedImageView_border_color);
    // if (borderColor == null) {
    // borderColor = ColorStateList
    // .valueOf(RoundedDrawable.DEFAULT_BORDER_COLOR);
    // }

    mutateBackground = a.getBoolean(R.styleable.RoundedImageView_mutate_background, false);
    isOval = a.getBoolean(R.styleable.RoundedImageView_oval, false);
    mLabelVisible = a.getBoolean(R.styleable.RoundedImageView_label_visible, false);
    borderColor = a
            .getColor(R.styleable.RoundedImageView_border_color, RoundedDrawable.DEFAULT_BORDER_COLOR);

    updateDrawableAttrs();
    updateBackgroundDrawableAttrs(true);
    a.recycle();

  }

  @Override
  public void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    canvas.save();
    if (mErrorBitmap != null) {
      canvas.drawBitmap(mErrorBitmap, leftPosition, 5, mErrorPaint);
    }

    if (mLabelDrawable != null && mLabelVisible) {
      mLabelDrawable.setBounds(0, 0, mLabelSize.x, mLabelSize.y);
      canvas.translate(mLabelOffset.x, mLabelOffset.y);
      mLabelDrawable.draw(canvas);
    }
    if (mMaskColor != 0) {
      canvas.drawColor(mMaskColor);
    }
    canvas.restore();
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    initLabel();
  }


  @Override
  protected void drawableStateChanged() {
    super.drawableStateChanged();
    invalidate();
  }

  /**
   * Return the current scale type in use by this ImageView.
   *
   * @attr ref android.R.styleable#ImageView_scaleType
   * @see ScaleType
   */
  @Override
  public ScaleType getScaleType() {
    return mScaleType;
  }

  /**
   * Controls how the image should be resized or moved to match the size of this ImageView.
   *
   * @param scaleType The desired scaling mode.
   * @attr ref android.R.styleable#ImageView_scaleType
   */
  @Override
  public void setScaleType(ScaleType scaleType) {
    assert scaleType != null;

    if (mScaleType != scaleType) {
      mScaleType = scaleType;

      switch (scaleType) {
        case CENTER:
        case CENTER_CROP:
        case CENTER_INSIDE:
        case FIT_CENTER:
        case FIT_START:
        case FIT_END:
        case FIT_XY:
          super.setScaleType(ScaleType.FIT_XY);
          break;
        default:
          super.setScaleType(scaleType);
          break;
      }

      updateDrawableAttrs();
      updateBackgroundDrawableAttrs(false);
      invalidate();
    }
  }

  @Override
  public void setImageDrawable(Drawable drawable) {
    mResource = 0;
    mDrawable = RoundedDrawable.fromDrawable(drawable);
    updateDrawableAttrs();
    super.setImageDrawable(mDrawable);
  }

  @Override
  public void setImageBitmap(Bitmap bm) {
    mResource = 0;
    mDrawable = RoundedDrawable.fromBitmap(bm);
    updateDrawableAttrs();
    super.setImageDrawable(mDrawable);
  }

  @Override
  public void setImageResource(int resId) {
    if (mResource != resId) {
      mResource = resId;
      mDrawable = resolveResource();
      updateDrawableAttrs();
      super.setImageDrawable(mDrawable);
    }
  }

  @Override
  public void setImageURI(Uri uri) {
    super.setImageURI(uri);
    setImageDrawable(getDrawable());
  }

  private Drawable resolveResource() {
    Resources rsrc = getResources();
    if (rsrc == null) {
      return null;
    }

    Drawable d = null;

    if (mResource != 0) {
      try {
        d = rsrc.getDrawable(mResource);
      } catch (Exception e) {
        Log.w(TAG, "Unable to find resource: " + mResource, e);
        // Don't try again.
        mResource = 0;
      }
    }
    return RoundedDrawable.fromDrawable(d);
  }

  private void updateDrawableAttrs() {
    updateAttrs(mDrawable);
  }

  private void updateBackgroundDrawableAttrs(boolean convert) {
    if (mutateBackground) {
      if (convert) {
        mBackgroundDrawable = RoundedDrawable.fromDrawable(mBackgroundDrawable);
      }
      updateAttrs(mBackgroundDrawable);
    }
  }

  private void updateAttrs(Drawable drawable) {
    if (drawable == null) {
      return;
    }

    if (drawable instanceof RoundedDrawable) {
      ((RoundedDrawable) drawable).setScaleType(mScaleType).setCornerRadius(cornerRadius)
              .setBorderWidth(borderWidth).setBorderColor(borderColor).setOval(isOval);
    } else if (drawable instanceof LayerDrawable) {
      // loop through layers to and set drawable attrs
      LayerDrawable ld = ((LayerDrawable) drawable);
      for (int i = 0, layers = ld.getNumberOfLayers(); i < layers; i++) {
        updateAttrs(ld.getDrawable(i));
      }
    }
  }

  @Override
  @Deprecated
  public void setBackgroundDrawable(Drawable background) {
    mBackgroundDrawable = background;
    updateBackgroundDrawableAttrs(true);
    super.setBackgroundDrawable(mBackgroundDrawable);
  }

  public float getCornerRadius() {
    return cornerRadius;
  }

  public void setCornerRadius(int resId) {
    setCornerRadius(getResources().getDimension(resId));
  }

  public void setCornerRadius(float radius) {
    if (cornerRadius == radius) {
      return;
    }

    cornerRadius = radius;
    updateDrawableAttrs();
    updateBackgroundDrawableAttrs(false);
  }

  public float getBorderWidth() {
    return borderWidth;
  }

  /**
   * 描边宽度
   */
  public void setBorderWidth(int resId) {
    setBorderWidth(getResources().getDimension(resId));
  }

  public void setBorderWidth(float width) {
    if (borderWidth == width) {
      return;
    }

    borderWidth = width;
    updateDrawableAttrs();
    updateBackgroundDrawableAttrs(false);
    invalidate();
  }

  public int getBorderColor() {
    return borderColor;
  }

  public void setBorderColor(int color) {
    if (borderColor == color) {
      return;
    }
    borderColor = color;
    updateDrawableAttrs();
    updateBackgroundDrawableAttrs(false);
    if (borderWidth > 0) {
      invalidate();
    }
  }

  // public ColorStateList getBorderColors() {
  // return borderColor;
  // }
  //
  // public void setBorderColor(ColorStateList colors) {
  // if (borderColor.equals(colors)) {
  // return;
  // }
  //
  // borderColor = (colors != null) ? colors : ColorStateList
  // .valueOf(RoundedDrawable.DEFAULT_BORDER_COLOR);
  // updateDrawableAttrs();
  // updateBackgroundDrawableAttrs(false);
  // if (borderWidth > 0) {
  // invalidate();
  // }
  // }

  public boolean isOval() {
    return isOval;
  }

  public void setOval(boolean oval) {
    isOval = oval;
    updateDrawableAttrs();
    updateBackgroundDrawableAttrs(false);
    invalidate();
  }

  public boolean isMutateBackground() {
    return mutateBackground;
  }

  public void setMutateBackground(boolean mutate) {
    if (mutateBackground == mutate) {
      return;
    }

    mutateBackground = mutate;
    updateBackgroundDrawableAttrs(true);
    invalidate();
  }

  public void setLabelDrawable(Drawable drawable) {
    if (drawable != null) {
      mLabelDrawable = drawable;
      mLabelVisible = true;
      initLabel();
      postInvalidate();
    }
  }

  public void setLabelDrawable(int res) {
    if (res > 0) {
      Drawable drawable = getContext().getResources().getDrawable(res);
      setLabelDrawable(drawable);
    } else {
      setLabelVisible(false);
    }
  }

  public void setLabelDrawable(Bitmap bitmap) {
    if (bitmap != null) {
      setLabelDrawable(new BitmapDrawable(bitmap));
    }
  }

  public void setLabelVisible(boolean visible) {
    if (mLabelVisible && visible || !mLabelVisible && !visible) {
      return;
    }
    mLabelVisible = visible;
    postInvalidate();
  }

  public void setLabelGravity(int gravity) {
    mLabelGravity = gravity;
    initLabelOffset();
  }

  public void setLabelOffset(int offsetLeft, int offsetTop, int offsetRight, int offsetBottom) {
    mLabelOffsetLeft = offsetLeft;
    mLabelOffsetTop = offsetTop;
    mLabelOffsetRight = offsetRight;
    mLabelOffsetBottom = offsetBottom;
    initLabelOffset();
  }

  private void initLabel() {
    if (mLabelSize == null) {
      mLabelSize = new Point();
    }

    if (mLabelDrawable == null) {
      return;
    }

    int viewW = getMeasuredWidth();
    int viewH = getMeasuredHeight();
    int labelW = mLabelDrawable.getIntrinsicWidth();
    int labelH = mLabelDrawable.getIntrinsicHeight();
    float scale = Math.min((float) viewW / (float) labelW, (float) viewH / (float) labelH);
    if (scale < 1.0) {
      mLabelSize.x = (int) (labelW * scale);
      mLabelSize.y = (int) (labelH * scale);
    } else {
      mLabelSize.x = labelW;
      mLabelSize.y = labelH;
    }

    initLabelOffset();
  }

  private void initLabelOffset() {
    if (mLabelOffset == null) {
      mLabelOffset = new Point();
    }

    if (mLabelDrawable == null) {
      return;
    }

    int viewW = getMeasuredWidth();
    int viewH = getMeasuredHeight();
    // label默认优先靠left/top
    if ((mLabelGravity & GRAVITY_CENTER_HORIZONTAL) != 0
            || (mLabelGravity & GRAVITY_CENTER_VITICAL) != 0) {
      if ((mLabelGravity & GRAVITY_CENTER) != 0) {
        // 水平垂直居中
        mLabelOffset.x = Math.max(0, (viewW - mLabelSize.x) / 2);
        mLabelOffset.y = Math.max(0, (viewH - mLabelSize.y) / 2);
      } else if ((mLabelGravity & GRAVITY_CENTER_HORIZONTAL) != 0) {
        mLabelOffset.x = Math.max(0, (viewW - mLabelSize.x) / 2);
        if ((mLabelGravity & GRAVITY_BOTTOM) != 0 && (mLabelGravity & GRAVITY_TOP) == 0) {
          mLabelOffset.y = viewH - mLabelOffsetBottom - mLabelSize.y;
        } else {
          mLabelOffset.y = mLabelOffsetTop;
        }
      } else {
        if ((mLabelGravity & GRAVITY_RIGHT) != 0 && (mLabelGravity & GRAVITY_LEFT) == 0) {
          mLabelOffset.x = viewW - mLabelOffsetRight - mLabelSize.x;
        } else {
          mLabelOffset.x = mLabelOffsetLeft;
        }
        mLabelOffset.y = Math.max(0, (viewH - mLabelSize.y) / 2);
      }
    } else {
      if ((mLabelGravity & GRAVITY_RIGHT) != 0 && (mLabelGravity & GRAVITY_LEFT) == 0) {
        mLabelOffset.x = viewW - mLabelOffsetRight - mLabelSize.x;
        if ((mLabelGravity & GRAVITY_BOTTOM) != 0 && (mLabelGravity & GRAVITY_TOP) == 0) {
          mLabelOffset.y = viewH - mLabelOffsetBottom - mLabelSize.y;
        } else {
          mLabelOffset.y = mLabelOffsetTop;
        }
      } else {
        mLabelOffset.x = mLabelOffsetLeft;
        if ((mLabelGravity & GRAVITY_BOTTOM) != 0 && (mLabelGravity & GRAVITY_TOP) == 0) {
          mLabelOffset.y = viewH - mLabelOffsetBottom - mLabelSize.y;
        } else {
          mLabelOffset.y = mLabelOffsetTop;
        }
      }
    }

  }

}
