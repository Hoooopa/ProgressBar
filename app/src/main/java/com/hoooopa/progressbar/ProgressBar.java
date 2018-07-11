package com.hoooopa.progressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class ProgressBar extends View {


    private int bottomLineHeight;  //底部高度    def:5dp
    private int bottomLineLong;    //底部长度
    private int bottomLineColor;   //底部颜色    def:blue

    private int aboveLineHeight;   //高度       def:5
    private float aboveLineLong;   //长度
    private int aboveLineColor;    //颜色       def:pink

    private int indicatorColor;    //指示器颜色  def:pink
    private int indicatorRadius;   //指示器圆角  def:3

    private int textColor;   //文字颜色  def:白色
    private int textSize;    //文字大小  def:18sp

    private int progressFinished ;   //已经完成多少个 def：3
    private int progressTotal ;      //总共多少个    def:5

    private int width;
    private int height;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public ProgressBar(Context context) {
        super(context);
        init();
    }

    public ProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.ProgressBar);

        bottomLineHeight = a.getDimensionPixelOffset(R.styleable.ProgressBar_bottom_line_height,5);
        bottomLineColor = a.getColor(R.styleable.ProgressBar_bottom_line_color, Color.BLUE);

        aboveLineHeight = a.getDimensionPixelOffset(R.styleable.ProgressBar_above_line_height,5);
        aboveLineColor = a.getColor(R.styleable.ProgressBar_above_line_color,Color.RED);

        indicatorColor = a.getColor(R.styleable.ProgressBar_indicator_color,Color.BLACK);
        indicatorRadius = a.getDimensionPixelOffset(R.styleable.ProgressBar_indicator_radius,2);

        textColor = a.getColor(R.styleable.ProgressBar_text_color,Color.WHITE);
        textSize = a.getDimensionPixelSize(R.styleable.ProgressBar_text_size,18);

        a.recycle();
        init();
    }


    public ProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){

    }

    private float textWidth;
    private float textHight;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();

        String progress = "00/00";
        mPaint.setTextSize(textSize);
        textWidth = mPaint.measureText(progress);
        textHight = getTxtHeight(mPaint);

    }

    private Path path = new Path();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画指示器
        initRectF(0);
        mPaint.setColor(indicatorColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(rectF,indicatorRadius,indicatorRadius,mPaint);
        //画三角形
        drawTriangle(canvas);
        //画字
        String progress = a + "/" + b;
        mPaint.setColor(Color.WHITE);
        canvas.drawText(progress,indicatorRadius + indicatorStart, textHight,mPaint);
        //画背景线
        initRectF(1);
        mPaint.setColor(bottomLineColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(rectF,bottomLineHeight/2,bottomLineHeight/2,mPaint);
        //画进度线
        initRectF(2);
        mPaint.setColor(aboveLineColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(rectF,aboveLineHeight/2,aboveLineHeight/2,mPaint);


    }

    private void drawTriangle(Canvas canvas){
        float rate = (float) progressFinished / progressTotal;
        float triangleStartX = textWidth/2 +indicatorRadius - textWidth/4 + (float) (width - indicatorRadius*2 - textWidth) * rate;
        float triangleStartY = indicatorRadius * 2 + textHight ;
        float triangleEndX = textWidth/2 + indicatorRadius + (float) (width - indicatorRadius*2 - textWidth) * rate;
        float triangleEndY = triangleStartY + textWidth/4;
        path.moveTo(triangleStartX,triangleStartY);
        path.lineTo(triangleStartX + textWidth/2,triangleStartY);
        path.lineTo(triangleEndX,triangleEndY);
        path.close();
        canvas.drawPath(path,mPaint);
        path.reset();

    }

    private RectF rectF;

    private float indicatorStart = 0.0f;

    private void initRectF(int type ){
        float rate = (float) progressFinished / progressTotal;
        if (type == 0){
            indicatorStart = (float) (width - indicatorRadius*2 - textWidth) * rate ;
            float indicatorEnd = indicatorStart + textWidth + indicatorRadius * 2;
            rectF = new RectF(indicatorStart,0, indicatorEnd, textHight + indicatorRadius*2);
        }

        if (type ==1){
            float backLineStartLeft = textWidth/2 + indicatorRadius - aboveLineHeight/2;
            float backLineStartTop =         indicatorRadius * 2 + textHight + textWidth/4 + aboveLineHeight/2 - bottomLineHeight/2;
            float backLineEndRight = width - textWidth/2 - indicatorRadius + aboveLineHeight/2;
            float backLineEndBottom = backLineStartTop + bottomLineHeight;
            rectF = new RectF(backLineStartLeft,backLineStartTop,backLineEndRight,backLineEndBottom);
        }

        if (type == 2){
            float aboveStartLeft = textWidth/2 + indicatorRadius - aboveLineHeight/2;
            float aboveStartTop =  indicatorRadius * 2 + textHight + textWidth/4;;
            float aboveEndRight = textWidth/2 + indicatorRadius + aboveLineHeight/2 + (width - indicatorRadius*2 -textWidth) * rate;
            float aboveEndBottom = aboveStartTop + aboveLineHeight;
            rectF = new RectF(aboveStartLeft,aboveStartTop,aboveEndRight,aboveEndBottom);
        }

    }

    public float getTxtHeight(Paint mPaint) {
        Paint.FontMetrics fm = mPaint.getFontMetrics();
        return (float) Math.ceil(fm.descent - fm.ascent);
    }

    public void setTotalNum(int num){
        progressTotal = num ;
    }

    private String a = progressFinished +"";
    private String b = progressTotal +"";

    public void setFinishedNum(int num) {
        progressFinished = num;
        if (progressTotal != 0 && num <= progressTotal){

            if (progressTotal < 10){
                a = "0" + progressFinished;
                b = "0" + progressTotal;
            }
            if (10 <= progressTotal && progressTotal < 100){
                b = "" +progressTotal;
                if (progressFinished <10){
                    a = "0" + progressFinished;
                }else {
                    a = progressFinished +"";
                }

            }
        }
        invalidate();
    }
}
