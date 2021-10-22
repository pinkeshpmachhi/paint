package com.example.paintappforportfolio;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CustomViewPaint extends View {

    private static final float TOUCH_TOLERANCE = 4;
    private float mX, mY;
    private Path mPath;
    private RectF tempRectF;
    private Paint mPaint;
    private ArrayList<Stroke> paths = new ArrayList<>();
    private ArrayList<Stroke> pathsForRect = new ArrayList<>();
    private int strokeWidth = 20;
    private int currentColor = Color.GREEN;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);
    ArrayList<Stroke> redoPaths = new ArrayList<>();
    private int colorForCanvas = Color.WHITE;
    private String shape = "";
    public boolean isDrawing;
    public float mStartX=0, mStartY=0,mx = 0,my = 0;
    private ArrayList<RectF> rectanglesList = new ArrayList<>();
    private ArrayList<RectF> rectangleListFinal = new ArrayList<>();
    private float xHelper=0, yHelper=0;
    private boolean firstShape = true;
    private int firstRectBugInt = 0;

    public CustomViewPaint(Context context) {
        this (context, null);
    }

    public CustomViewPaint(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(getCurrentColor());
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        // 0xff = 255 in decimal
        mPaint.setAlpha(0xff);


    }

    public void init(int width, int height, String shape) {
        setSHAPE(shape);

        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        currentColor = getCurrentColor();
        strokeWidth = getStrokeWidth();
        setCanvasColor();
    }



    public void undo() {
        if (paths.size() >0){
            redoPaths.add(paths.get(paths.size() - 1));
            paths.remove(paths.size() -1);
            invalidate();
        }
    }

    public void redo() {
        if (redoPaths.size() > 0) {
            paths.add(redoPaths.get(redoPaths.size()-1));
            redoPaths.remove(redoPaths.size() - 1);
            invalidate();

        }
    }

    public Bitmap save() {
        return mBitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();

        init(getWidth(), getHeight(), getShape());

        int backgroundColor = Color.WHITE;
        canvas.drawColor(backgroundColor);

        for (Stroke stroke : paths) {
            mPaint.setStrokeWidth(stroke.strokeWidth);
            mPaint.setColor(stroke.color);

            mCanvas.drawPath(stroke.path, mPaint);
        }

        if (rectanglesList.size() > 0) {
            for (int i = 0; i < rectanglesList.size(); i++) {
                RectF rectF = rectanglesList.get(i);
                Stroke stroke = pathsForRect.get(i);

                mPaint.setColor(stroke.color);
                mPaint.setStrokeWidth(stroke.strokeWidth);

                mCanvas.drawRect(rectF, mPaint);
            }
        }

        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);

        switch (getShape()) {
            case "RECT" :
                onDrawRectangle(canvas);
                break;

            default:
                Log.d("TAG", "onDraw: ");
                break;
        }

        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

            setMx(event.getX());
            setMy(event.getY());



        switch (getShape()) {
            case "RECT" :
                onTouchEventRectangle(event);
                break;

//            case "EARASER" :
//
//                break;

            default:
                setmStartX(0);
                setmStartY(0);

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        touchStart(x,y);
                        invalidate();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        touchMove(x,y);
                        invalidate();
                        break;

                    case MotionEvent.ACTION_UP:
                        touchUp();
                        invalidate();
                        break;
                }
                firstRectBugInt=1;
                break;
        }
        return true;
    }

    private void touchStart(float x, float y) {
        mPath = new Path();
        Stroke stroke = new Stroke(getCurrentColor(), getStrokeWidth(), mPath);
        paths.add(stroke);

        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);

        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void touchUp() {

        mPath.lineTo(mX, mY);

    }

    public void clearCanvas() {
        paths.clear();
        redoPaths.clear();
        invalidate();
    }

    private void onTouchEventRectangle(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDrawing = true;
                setmStartX(getMx());
                setmStartY(getMy());
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                isDrawing = false;
                drawRectangle(mCanvas,mPaint);
                invalidate();
                break;
        }
    }

    public void setSHAPE(String shape){
        this.shape = shape;
    }

    public String getShape() {
        return shape;
    }

    private void onDrawRectangle(Canvas canvas) {
        drawRectangle(canvas,mPaint);
    }

    private void drawRectangle(Canvas canvas,Paint paint){
        float left = getmStartX() > getMx() ? getMx() : getmStartX();
        float top = getmStartY() > getMy() ? getMy() : getmStartY();

        Stroke stroke;
        if (getxHelper() == getMx() && getyHelper() == getMy()) {
            RectF rectF = new RectF();
            rectF.set(left, top , getMx(), getMy());
            stroke = new Stroke(getCurrentColor(), getStrokeWidth());
            pathsForRect.add(stroke);
            mPaint.setStrokeWidth(getStrokeWidth());
            mPaint.setColor(getCurrentColor());
            canvas.drawRect(rectF, mPaint);
            rectanglesList.add(rectF);


        }else {
            tempRectF = new RectF();

            if (getFirstShape()){
                setMy(0);
                setMx(0);
                setmStartX(0);
                setmStartY(0);

                setFirstShape(false);
            }
            if (firstRectBugInt == 0) {
                stroke = new Stroke(getCurrentColor(), getStrokeWidth());
                mPaint.setStrokeWidth(getStrokeWidth());
                mPaint.setColor(getCurrentColor());
                tempRectF.set(left,top,getMx(), getMy());
                canvas.drawRect(tempRectF, mPaint);
            }
            firstRectBugInt = 0;
        }
        setxHelper(getMx());
        setyHelper(getMy());
    }

    public boolean getFirstShape() {
        return firstShape;
    }
    public void setFirstShape(boolean a) {
        firstShape = a;
    }

    public float getmStartX() {
        return mStartX;
    }

    public void setmStartX(float mStartX) {
        this.mStartX = mStartX;
    }

    public float getmStartY() {
        return mStartY;
    }

    public void setmStartY(float mStartY) {
        this.mStartY = mStartY;
    }

    public float getMx() {
        return mx;
    }

    public void setMx(float mx) {
        this.mx = mx;
    }

    public float getMy() {
        return my;
    }

    public void setMy(float my) {
        this.my = my;
    }

    public void setCanvasColor() {
        mCanvas.drawColor(getColorForCanvas());
    }

    public int getColorForCanvas() {
        return colorForCanvas;
    }

    public void setColorForCanvas(int color){
        colorForCanvas = color;
        invalidate();
    }

    public void setCurrentColor(int color) {
        currentColor = color;
    }

    public int getCurrentColor(){
        return currentColor;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public float getxHelper() {
        return xHelper;
    }
    public void setxHelper(float a) {
        xHelper =a;
    }
    public float getyHelper() {
        return yHelper;
    }
    public void setyHelper(float a) {
        yHelper = a;
    }
}






















