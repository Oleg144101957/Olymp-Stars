package com.facebook.ta

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.CalendarContract.Colors
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import androidx.lifecycle.lifecycleScope
import com.facebook.ta.databinding.ActivityMain3Binding
import com.facebook.ta.presentation.OlympViewModel
import com.facebook.ta.presentation.OlympViewModel.Companion.ONE_SIG
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.math.abs

class Main3Activity : AppCompatActivity() {

    private lateinit var binding: ActivityMain3Binding
    private lateinit var imViewList: List<ImageView>
    private val olympViewModel by viewModels<OlympViewModel>()
    private val MIN_DELTA = 150

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        imViewList = listOf(
            binding.imageView00,
            binding.imageView01,
            binding.imageView02,
            binding.imageView03,
            binding.imageView10,
            binding.imageView11,
            binding.imageView12,
            binding.imageView13,
            binding.imageView20,
            binding.imageView21,
            binding.imageView22,
            binding.imageView23,
            binding.imageView30,
            binding.imageView31,
            binding.imageView32,
            binding.imageView33,
            binding.imageView40,
            binding.imageView41,
            binding.imageView42,
            binding.imageView43
        )

        olympViewModel.initListInViewModel()
        setGameContent(imViewList)

        olympViewModel.liveElementsList.observe(this){ data ->
            for (i in data.indices){
                imViewList[i].setImageResource(data[i].draw)
                if (!data[i].isExist){
                    animateImageView(imViewList[i])
                }
            }
        }



        lifecycleScope.launch {
            olympViewModel.liveScores.collect{ score ->
                binding.scoresTV.text = "Score: $score"
            }
        }

        lifecycleScope.launch {
            olympViewModel.timeFlow.collect{
                binding.timeTV.text = "Time: $it"
                if (it > 20){
                    binding.timeTV.setTextColor(Color.RED)
                }

                if (it == 25){
                    val score = olympViewModel.liveScores.first()
                    navigateToWinActivity(score)
                }
            }
        }
    }

    private fun navigateToWinActivity(score: Int) {
        val intent = Intent(this, Main4Activity::class.java)
        intent.putExtra(ONE_SIG, score)
        startActivity(intent)
    }

    private fun animateImageView(imageView: ImageView) {
        // Convert 32dp to pixels
        val pixelsToMove = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 32f, resources.displayMetrics
        )

        // Create an animation to move the ImageView up by 32dp
        val translateYAnimator = ObjectAnimator.ofFloat(imageView, "translationY", -pixelsToMove)
        translateYAnimator.duration = 200 // 300 milliseconds

        // Create an animation to gradually fade out the ImageView
        val fadeOutAnimator = ObjectAnimator.ofFloat(imageView, "alpha", 1f, 0f)
        fadeOutAnimator.duration = 300 // Let's say it fades out in 500 milliseconds for smoothness
        fadeOutAnimator.startDelay = 200 // Start fading out after the move up animation

        // Use AnimatorSet to play the animations in sequence
        val animatorSet = AnimatorSet()
        animatorSet.play(translateYAnimator).before(fadeOutAnimator)
        animatorSet.interpolator = DecelerateInterpolator()
        animatorSet.start()

        //clear all animations on all elements in 1000 ms

        lifecycleScope.launch {
            delay(1000)
            clearAllAnimations(imageView)
        }

    }

    private fun clearAllAnimations(imageView: ImageView) {
        imageView.clearAnimation()
        imageView.animate().cancel()  // Cancel any ongoing view property animations
        // Reset the view properties which were animated
        imageView.translationY = 0f
        imageView.alpha = 1f
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setGameContent(listOfBindings: List<ImageView>) {
        var startX = 0f
        var startY = 0f

        for (i in listOfBindings.indices){
            olympViewModel.liveElementsList.value?.get(i)?.let { listOfBindings[i].setImageResource(it.draw) }
            listOfBindings[i].setOnTouchListener(View.OnTouchListener { view, motionEvent ->
                when (motionEvent.action){

                    MotionEvent.ACTION_DOWN -> {
                        // Store the initial touch position
                        startX = motionEvent.x
                        startY = motionEvent.y
                        true
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val deltaX = motionEvent.x - startX
                        val deltaY = motionEvent.y - startY

                        if (abs(deltaX) > MIN_DELTA) {
                            if (deltaX > 0) {
                                if (olympViewModel.liveIsListAvailable.value == true){
                                    olympViewModel.rightTransfer(i)
                                }
                            } else {
                                if(olympViewModel.liveIsListAvailable.value == true){
                                    olympViewModel.leftTransfer(i)
                                }
                            }
                        }

                        if (abs(deltaY) > MIN_DELTA) {
                            if (deltaY > 0) {
                                if (olympViewModel.liveIsListAvailable.value == true){
                                    olympViewModel.downTransfer(i)
                                }
                            } else {
                                if (olympViewModel.liveIsListAvailable.value == true){
                                    olympViewModel.upTransfer(i)
                                }
                            }
                        }

                        true
                    }
                }
                return@OnTouchListener true
            })
        }
    }
}