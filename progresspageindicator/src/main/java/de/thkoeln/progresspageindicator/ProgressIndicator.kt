package de.thkoeln.progresspageindicator

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.indicator_container.view.*

open class ProgressIndicator(con : Context, attrs : AttributeSet?)
    : LinearLayout(con, attrs){

    companion object {
        private val TAG = ProgressIndicator::class.java.name
        private const val DEFAULT_SPACE_BETWEEN_INDICATORS_DP = 8f
    }

    private var viewPagerRes : Int = -1
    private var circleCount: Int = -1
    internal var circles : MutableList<CircleIndicator> = mutableListOf()
    private var spaceBetweenIndicators : Int = -1
    private var latestPosition = 0

    init {
        LayoutInflater.from(con).inflate(R.layout.indicator_container, this)

        //viewPager, spaceBetweenIndicators, colors
        attrs?.let {
            val a = con.theme.obtainStyledAttributes(attrs, R.styleable.ProgressIndicator,
                    0 ,0)

            viewPagerRes = a.getResourceId(R.styleable.ProgressIndicator_viewpager, -1)
            if(viewPagerRes == -1) Log.e(TAG, "please provide a viewpager reference")

            val space = a.getDimensionPixelSize(
                    R.styleable.ProgressIndicator_indicator_margin, DimensionHelper
                    .getRoundedPixel(resources.displayMetrics, DEFAULT_SPACE_BETWEEN_INDICATORS_DP))

            spaceBetweenIndicators = (space.toFloat()/2).toInt()

        }
        // TODO initialize fake values; e.g. 3 pager items for ide presentation purpose
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (viewPagerRes == -1) return

        val viewPager = (parent as View).findViewById<ViewPager>(viewPagerRes)

        circleCount = viewPager.adapter?.count ?: -1
        initialize()

        viewPager.addOnAdapterChangeListener({
            _, _, newAdapter -> circleCount = newAdapter?.count ?: -1
            initialize()
        })

        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener(){
            override fun onPageSelected(position: Int) {
                setInactive(latestPosition)
                setActive(position)
                latestPosition = position
            }
        })
    }

    internal open fun initialize(){

        val layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.setMargins(spaceBetweenIndicators, 0, spaceBetweenIndicators, 0)

        for(i in 1..circleCount){
            val circle = CircleIndicator(context, null)
            the_indicator.addView(circle, layoutParams)
            circles.add(circle)
        }
        setActive(0)
    }

    fun setActive(position : Int) = circles[position].setActive()
    fun setInactive(position: Int) = circles[position].setInactive()

}