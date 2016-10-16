package sungkyul.ac.kr.ottocafe.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by HunJin on 2016-09-22.
 * 원형 이미지 뷰
 */
public class RoundImageView extends ImageView{
    public RoundImageView(Context context) {
        super(context);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Drawable drawable = getDrawable();

        if (drawable == null) {
            return;
        }

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        Bitmap mB = ((BitmapDrawable) drawable).getBitmap();
        Bitmap mBitmap = mB.copy(Bitmap.Config.ARGB_8888, true);

        int mWidth = getWidth();
        int mHeight = getHeight();

        Bitmap mRoundBitmap = getCroppedBitmap(mBitmap, mWidth);
        canvas.drawBitmap(mRoundBitmap, 0, 0, null);
    }

    public static Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
        Bitmap mSmallBitmap;

        if (bmp.getWidth() != radius || bmp.getHeight() != radius) {
            float fSmallest = Math.min(bmp.getWidth(), bmp.getHeight());
            float fFactor = fSmallest / radius;

            mSmallBitmap = Bitmap.createScaledBitmap(bmp, (int) (bmp.getWidth() / fFactor), (int) (bmp.getHeight() / fFactor), false);
        } else {
            mSmallBitmap = bmp;
        }

        Bitmap mOutput = Bitmap.createBitmap(radius, radius, Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(mOutput);

        final int mColor = 0xffa19774;
        final Paint mPaint = new Paint();
        final Rect mRect = new Rect(0, 0, radius, radius);

        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        mPaint.setDither(true);
        mCanvas.drawARGB(0, 0, 0, 0);
        mPaint.setColor(Color.parseColor("#BAB399"));
        mCanvas.drawCircle(radius / 2 + 0.7f, radius / 2 + 0.7f, radius / 2 + 0.1f, mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        mCanvas.drawBitmap(mSmallBitmap, mRect, mRect, mPaint);

        return mOutput;
    }
}
