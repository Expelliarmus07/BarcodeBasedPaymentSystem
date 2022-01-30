package com.fyp.barcodebasedpaymentsystem.all.activity.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayoutStates
import androidx.viewpager.widget.PagerAdapter
import com.fyp.barcodebasedpaymentsystem.R
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.media.Image
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat

import androidx.core.content.ContextCompat.getSystemService


open class SliderAdapter(
    private val context: Context
) : PagerAdapter() {

    var images: Array<Int> = arrayOf<Int>(
        R.drawable.artwork_one,
        R.drawable.artwork_two,
        R.drawable.artwork_three
    )

    var headings: Array<Int> = arrayOf<Int>(
        R.string.first_slide_title,
        R.string.second_slide_title,
        R.string.third_slide_title
    )

    var description: Array<Int> = arrayOf<Int>(
        R.string.first_slide_desc,
        R.string.second_slide_desc,
        R.string.third_slide_desc
    )

    override fun getCount(): Int {
        return headings.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`

    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val view:View = LayoutInflater.from(context).inflate(R.layout.slides_layout, null)

        val imageView: ImageView = view.findViewById(R.id.slider_image)
        val heading: TextView = view.findViewById(R.id.slider_heading)
        val desc: TextView = view.findViewById(R.id.slider_desc)


        imageView.setImageResource(images[position])
        heading.setText(headings[position])
        desc.setText(description[position])

        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
       container.removeView(`object` as View)
    }
}