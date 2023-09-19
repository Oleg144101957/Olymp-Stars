package com.facebook.ta

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.webkit.ValueCallback
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.facebook.mycustommodule.FilePicker
import com.facebook.mycustommodule.MyCustomFrameLayout
import com.facebook.mycustommodule.VisibleViewEdition
import com.facebook.ta.Constants.Companion.KEY_LINK
import com.facebook.ta.Constants.Companion.WARN
import com.facebook.ta.data.CustomStorage
import com.facebook.ta.databinding.ActivityCustomBinding
import com.google.android.play.core.review.ReviewManagerFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CustomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomBinding

    lateinit var customStorage: CustomStorage
    lateinit var constants: Constants
    lateinit var chooseCallback: ValueCallback<Array<Uri?>>
    val getContent = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) {
        chooseCallback.onReceiveValue(it.toTypedArray())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        customStorage = CustomStorage()
        constants = Constants(this)

        ratingBar()


        val direction = intent.getStringExtra(KEY_LINK) ?: Constants.EMPTY
        //checkDirection(direction)


        val myCustomFrameLayout = MyCustomFrameLayout(this)

        // Set layout parameters (match parent for both width and height in this case)
        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        myCustomFrameLayout.layoutParams = layoutParams

        binding.root.addView(myCustomFrameLayout)


        addCustomWeb(direction)

    }

    fun checkUrl(url: String){
        //check and save url
        Log.d("123123", "Fun checkUrl in CustomActivity url is $url")

        val list = constants.getList()
        val deli = (list?.get(2) ?: "") + (list?.get(3) ?: "") + (list?.get(4) ?: "") +"te/"
        val currentLink = customStorage.readData(KEY_LINK)

        if (url == deli){
            //navigate to menu
            customStorage.saveData(KEY_LINK, WARN)
            val intentToMenu = Intent(this, Main2Activity::class.java)
            startActivity(intentToMenu)
        } else {
            //save link to the storage
            if (currentLink.contains("ympstars.si") || currentLink == Constants.EMPTY){
                customStorage.saveData(KEY_LINK, url)
            } else {
                //don't save
            }
        }
    }

    private fun addCustomWeb(direction: String){
        val visibleViewEdition = VisibleViewEdition(this, object : FilePicker{
            override fun onPickFileCallBack(param: ValueCallback<Array<Uri?>>) {
                chooseCallback = param
            }
        })

        lifecycleScope.launch {
            visibleViewEdition.initialVisibleViewEdition(getContent, ::checkUrl)
            visibleViewEdition.loadUrl(direction)
            delay(2000)
            binding.root.addView(visibleViewEdition)
        }
    }

    private fun ratingBar(){
        val times = customStorage.readTimes()
        val revManager = ReviewManagerFactory.create(applicationContext)

        val isDialog = customStorage.readIsShowRateDialog()
        val checkBox = CheckBox(this)


        if (isDialog && times > 6 && times%7 == 0){
            //Show fake Rate us
            val linearLayout = LinearLayout(this)
            linearLayout.orientation = LinearLayout.VERTICAL
            linearLayout.setPadding(50, 50, 50, 50)

            val ratingBar = RatingBar(this)
            ratingBar.numStars = 5

            val ratingBarLayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            ratingBarLayoutParams.gravity = Gravity.CENTER_HORIZONTAL


            ratingBar.layoutParams = ratingBarLayoutParams
            ratingBar.stepSize = 1f
            linearLayout.addView(ratingBar)

            if (times > 12){
                //don't show again
                checkBox.text = "Don't show again"
                val checkBoxLayoutParams =
                    LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )

                checkBoxLayoutParams.gravity = Gravity.CENTER_HORIZONTAL

                checkBox.layoutParams = checkBoxLayoutParams

                linearLayout.addView(checkBox)
            }

            val builder = AlertDialog.Builder(this)


            val title = TextView(this)
            title.text = "Rate Us!"
            title.textSize = 20f  // Adjust text size to your preference
            title.gravity = Gravity.CENTER_HORIZONTAL  // Center the text horizontally

            builder.setCustomTitle(title)

            builder.setView(linearLayout)

            builder.setPositiveButton("Submit"){ dialog, which ->
                val rating = ratingBar.rating

                Log.d("123123", "Rating is $rating")

                if (rating > 3f){
                    //Show original Rate us
                    revManager.requestReviewFlow().addOnCompleteListener{ toDo ->
                        if (toDo.isSuccessful){
                            revManager.launchReviewFlow(this, toDo.result)
                        }
                    }
                } else {
                    //never show dialog
                    customStorage.saveAskRateUs(false)
                }
            }

            builder.setNegativeButton("Dismiss"){ dialog, which ->

            }



            val dialog = builder.create()

            dialog.show()
            //Submit false

            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.isEnabled = false

            ratingBar.onRatingBarChangeListener = RatingBar.OnRatingBarChangeListener { _, starsCount, _ ->
                if (starsCount > 0) positiveButton.isEnabled = true
            }

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked){
                    customStorage.saveAskRateUs(false)
                } else {
                    customStorage.saveAskRateUs(true)
                }
            }
        }
    }
}