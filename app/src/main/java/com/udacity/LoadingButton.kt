package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.withStyledAttributes
import kotlin.math.roundToInt
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private val valueAnimator = ValueAnimator.ofFloat(0f, 100f)
    private var mainButtonColor = 0
    private var loadingButtonColor = 0
    private var circleColor = 0
    private var progress = 0f

    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when (new) {
            ButtonState.Clicked -> {
                valueAnimator.start()
                valueAnimator.repeatCount = 0

            }
            ButtonState.Completed -> {
                valueAnimator.cancel()
                progress = 0f
                invalidate()
            }
            ButtonState.Loading -> {
                valueAnimator.repeatCount = ValueAnimator.INFINITE
            }
        }
    }


    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        paint.style = Paint.Style.FILL

        valueAnimator.duration = 1000
        valueAnimator.repeatMode = ValueAnimator.RESTART
        valueAnimator.apply {
            addUpdateListener {
                progress = it.animatedValue as Float
                if(progress.roundToInt() == 100 && valueAnimator.repeatCount == 0){
                    progress = 0f
                }
                invalidate()
            }
        }
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            mainButtonColor = getColor(R.styleable.LoadingButton_mainButtonColor, 0)
            loadingButtonColor = getColor(R.styleable.LoadingButton_loadingButtonColor, 0)
            circleColor = getColor(R.styleable.LoadingButton_circleColor, 0)
        }

    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = mainButtonColor
        canvas.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paint)

        paint.color = loadingButtonColor
        canvas.drawRect(0f, 0f, widthSize * (progress / 100), heightSize.toFloat(), paint)

        paint.color = circleColor
        paint.textAlign = Paint.Align.CENTER
        paint.textSize = 48f
        paint.typeface = Typeface.create("", Typeface.BOLD)
        canvas.drawText(
            context.getString(R.string.button_name),
            (1 / 2f) * widthSize,
            (1 / 2f) * heightSize,
            paint
        )

        // draw circle
        paint.color = context.getColor(R.color.colorAccent)
        canvas?.drawArc(
            widthSize - 200f,
            50f,
            widthSize - 100f,
            150f,
            0f,
            progress * 3.6f,
            true,
            paint
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}