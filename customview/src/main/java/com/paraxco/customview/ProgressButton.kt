package com.paraxco.customview

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RotateDrawable
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.progressbar_button.view.*
import java.util.concurrent.atomic.AtomicBoolean

/**
 * button with progress bar for time consuming actions to show progress bar  after click
 * helps to avoid multiple uncontrolled click
 */
class ProgressButton(context: Context, var attrs: AttributeSet) : FrameLayout(context, attrs) {

    init {
        val view = View.inflate(getContext(), R.layout.progressbar_button, null)
        addView(view)
        setAttributes()
        initView()
    }

    private fun setAttributes() {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.ProgressButton,
                0, 0)
        val buttonText = typedArray.getString(R.styleable.ProgressButton_text)
        val buttonTextColor = typedArray.getColor(R.styleable.ProgressButton_textColor, button.textColors.defaultColor)
        val buttonTextStyle = typedArray.getInt(R.styleable.ProgressButton_textStyle, 0)
//        var typeFace = typedArray.getInt(R.styleable.ProgressButton_textColor, -1)

        val indeterminateDrawable = typedArray.getDrawable(R.styleable.ProgressButton_indeterminateDrawable)

        val startColor = typedArray.getColor(R.styleable.ProgressButton_gradientStartColor, resources.getColor(android.R.color.transparent))
        val centerColor = typedArray.getColor(R.styleable.ProgressButton_gradientCenterColor, resources.getColor(android.R.color.transparent))
        val endColor = typedArray.getColor(R.styleable.ProgressButton_gradientEndColor, getThemeAccentColor())

        button.text = buttonText

        button.background = background
        background = null
        var buttonTypeFace: Typeface? = null
//        if (typeFace == -1)
        buttonTypeFace = button.typeface
//        else{
//            when(typeFace){
//                0->{}
//            }
//        }

        when (buttonTextStyle) {
            0 -> {
                button.setTypeface(buttonTypeFace, Typeface.NORMAL)
            }
            1 -> {
                button.setTypeface(buttonTypeFace, Typeface.BOLD)
            }
            2 -> {
                button.setTypeface(buttonTypeFace, Typeface.ITALIC)
            }
            3 -> {
                button.setTypeface(buttonTypeFace, Typeface.BOLD_ITALIC)
            }
        }
//        if (buttonTextColor != -1)
        button.setTextColor(buttonTextColor)

        if (indeterminateDrawable != null)
            progressBar.indeterminateDrawable = indeterminateDrawable
        else
            progressBar.indeterminateDrawable = getIndeterminateDrawable(startColor, centerColor, endColor)
    }

    private val inProgressMode = AtomicBoolean(false)

    private fun initView() {
        button.setOnClickListener {
            if (inProgressMode.compareAndSet(false, true)) {
                showProgressBar()
                this.performClick()
            }
        }
    }

    fun isInProgressMode():Boolean{
        return inProgressMode.get()
    }

    private fun getIndeterminateDrawable(startColor: Int, centerColor: Int, endcolor: Int): Drawable {
        val indeterminateDrawable = ContextCompat.getDrawable(context, R.drawable.indeterminate_progress) as RotateDrawable
//        val bgShape = indeterminateDrawable.findDrawableByLayerId(R.id.gradient) as RotateDrawable
//        bgShape.get
        val shapeGradient = indeterminateDrawable.drawable as GradientDrawable

        shapeGradient.mutate()
        shapeGradient.colors = intArrayOf(startColor, centerColor, endcolor)

//        shapeGradient.color= ColorStateList.valueOf(startColor)
        return indeterminateDrawable

//        var rotate= RotateDrawable()
//        rotate.mutate()
//        rotate.drawable=GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, intArrayOf(startColor, centerColor,endCenter))


    }

    fun showProgressBar() {
        post {
            //            button.visibility = View.INVISIBLE
            hideWithAnim(button)
            progressBar.visibility = View.VISIBLE
        }

    }

    fun showButton() {
        inProgressMode.set(false)
        post {
            //            button.visibility = View.VISIBLE
            showWithAnim(button)
            progressBar.visibility = View.INVISIBLE
        }

    }

    private fun hideWithAnim(view: View) {
        view.visibility = View.VISIBLE
        val anim = AnimationUtils.loadAnimation(context, R.anim.fade_out_anim)
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                view.visibility = View.INVISIBLE
            }

            override fun onAnimationStart(animation: Animation?) {
            }

        })

        view.startAnimation(anim)
    }

    private fun showWithAnim(view: View) {
        view.visibility = View.INVISIBLE

        val anim = AnimationUtils.loadAnimation(context, R.anim.fade_in_anim)
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                view.visibility = View.VISIBLE
            }

            override fun onAnimationStart(animation: Animation?) {
            }

        })

        view.startAnimation(anim)
    }

    private fun getThemePrimaryColor(): Int {
        val value = TypedValue()
        context.theme.resolveAttribute(R.attr.colorPrimary, value, true)
        return value.data
    }

    private fun getThemeAccentColor(): Int {
        val value = TypedValue()
        context.theme.resolveAttribute(R.attr.colorAccent, value, true)
        return value.data
    }
}