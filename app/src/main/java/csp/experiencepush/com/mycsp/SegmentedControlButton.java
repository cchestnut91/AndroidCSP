package csp.experiencepush.com.mycsp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.RadioButton;

/**
 * Created by cchestnut on 10/1/14.
 */
public class SegmentedControlButton extends RadioButton {
    private float mX;

    public SegmentedControlButton(Context context) {
        super(context);
    }

    public SegmentedControlButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SegmentedControlButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private static final float TEXT_SIZE = 16.0f;

    @Override
    public void onDraw(Canvas canvas) {

        String text = this.getText().toString();
        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);
        float currentWidth = textPaint.measureText(text);
        float currentHeight = textPaint.measureText("x");

        // final float scale =
        // getContext().getResources().getDisplayMetrics().density;
        // float textSize = (int) (TEXT_SIZE * scale + 0.5f);
        textPaint.setTextSize(this.getTextSize());
        textPaint.setTextAlign(Paint.Align.CENTER);

        float canvasWidth = canvas.getWidth();
        float textWidth = textPaint.measureText(text);

        if (isChecked()) {
            textPaint.setColor(Color.WHITE);
            GradientDrawable grad = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] { 0xff6f7dda, 0xff6f7dda });
            grad.setBounds(0, 0, this.getWidth(), this.getHeight());
            grad.draw(canvas);
        } else {
            textPaint.setColor(0xff6f7dda);
            GradientDrawable grad = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] { Color.WHITE, Color.WHITE});
            grad.setBounds(0, 0, this.getWidth(), this.getHeight());
            grad.draw(canvas);
        }

        float w = (this.getWidth() / 2) - currentWidth;
        float h = (this.getHeight() / 2) + currentHeight;
        canvas.drawText(text, mX, h, textPaint);

        Paint paint = new Paint();
        paint.setColor(0xff6f7dda);
        paint.setStyle(Paint.Style.STROKE);
        Rect rect = new Rect(0, 0, this.getWidth(), this.getHeight());
        canvas.drawRect(rect, paint);

    }

    @Override
    protected void onSizeChanged(int w, int h, int ow, int oh) {
        super.onSizeChanged(w, h, ow, oh);
        mX = w * 0.5f; // remember the center of the screen
    }
}
