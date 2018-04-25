package de.thkoeln.progresspageindicator

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.indicator_container.view.*

class ProgressIndicator private constructor(con : Context, attrs : AttributeSet?)
    : LinearLayout(con, attrs){

    companion object {
        private const val DEFAULT_SPACE_BETWEEN_INDICATORS_DP = 8f
    }

    private var viewPager : ViewPager? = null
    private var circleCount: Int = -1
    private var circles : MutableList<CircleIndicator> = mutableListOf()
    private var spaceBetweenIndicators : Int = -1

    init {
        LayoutInflater.from(con).inflate(R.layout.indicator_container, this)

        //viewPager, spaceBetweenIndicators, colors
        attrs?.let {
            //get viewPagerAttribute
            //circleCount = viewPager?.childCount
            //throw err, if custom attribute viewPager is not given
        }
        // -> initialize fake values; e.g. 3 pager items for ide presentation purpose
    }

    constructor(con: Context,
                numberOfCircles: Int,
                spaceBeweteenIndicatorsInDp: Float = DEFAULT_SPACE_BETWEEN_INDICATORS_DP)
            : this(con, null){

        circleCount = numberOfCircles
        spaceBetweenIndicators = (getRoundedPixel(spaceBeweteenIndicatorsInDp).toFloat() / 2).toInt()

        initialize()
    }

    private fun initialize(){

        val layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.setMargins(spaceBetweenIndicators, 0, spaceBetweenIndicators, 0)

        for(i in 1..circleCount){
            val circle = CircleIndicator(context, null)
            the_indicator.addView(circle, layoutParams)
            circles.add(circle)
        }
    }

    fun setActive(position : Int){
        circles[position].setActive()
    }

    fun setVisited(position: Int){
        circles[position].setVisited()
    }

    //TODO: redundant code (make static or such)
    private fun getRoundedPixel(dp: Float) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            dp, resources.displayMetrics).toInt()

    private fun getDp(pixel: Int) =
            pixel.toFloat() / (resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
}