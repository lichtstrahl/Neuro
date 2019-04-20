package root.iv.neuro.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class SimpleCanvas extends View {
    private static final int BACKGROUND_COLOR = android.R.color.white;
    private Paint paint;
    private Path path;
    private boolean needsClear;

    public SimpleCanvas(Context context) {
        super(context);
        init(null, 0);
    }

    public SimpleCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public SimpleCanvas(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int style) {
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(100);
        paint.setStyle(Paint.Style.STROKE);

        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        clear(canvas);
        canvas.drawColor(getResources().getColor(android.R.color.white));
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float tX = event.getX();
        float tY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(tX, tY);
                break;

            case MotionEvent.ACTION_UP:
//                path.lineTo(tX, tY);
                break;

            case MotionEvent.ACTION_MOVE:
                path.lineTo(tX, tY);
                break;
        }
        invalidate();
        return true;
    }

    private void clear(Canvas canvas) {
        if (needsClear) {
            canvas.drawColor(getResources().getColor(BACKGROUND_COLOR));
            path = new Path();
        }
        needsClear = false;
    }

    public void clear() {
        needsClear = true;
    }

    public Path getPath() {
        clear();
        return path;
    }

    public void setPath(Path path) {
        this.path = new Path(path);
    }
}
