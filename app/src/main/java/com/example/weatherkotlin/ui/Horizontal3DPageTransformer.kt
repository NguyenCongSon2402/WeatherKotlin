package com.example.weatherkotlin.ui

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.example.weatherkotlin.R

class Horizontal3DPageTransformer : ViewPager2.PageTransformer {
    private val minScale = 0.85f
    private val minAlpha = 0.6f

    override fun transformPage(page: View, position: Float) {
        page.apply {
            val pageWidth = width
            val translationX = -pageWidth * position

            when {
                position < -1 -> { // Page to the left of the screen
                    alpha = minAlpha
                    scaleX = minScale
                    scaleY = minScale
                }
                position <= 1 -> { // Page on the screen or entering/leaving the screen

                    // Gradually reduce depth translation for a slight 3D effect

                    findViewById<View>(R.id.txt_temperature)?.translationX = translationX
                    findViewById<View>(R.id.txt_temperature)?.rotationY = 90f * position // Adjust the rotation based on the position

                    // Calculate the inner scroll position for the fragment's ScrollView

                }
                else -> { // Page to the right of the screen
                    alpha = minAlpha
                    scaleX = minScale
                    scaleY = minScale

                    // Reset translation and rotation for off-screen pages
                    findViewById<View>(R.id.txt_temperature)?.translationX = 0f
                    findViewById<View>(R.id.txt_temperature)?.rotationY = 0f
                }
            }
        }
    }
}