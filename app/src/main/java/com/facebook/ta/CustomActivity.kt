package com.facebook.ta

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.ValueCallback
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RatingBar
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.facebook.mycustommodule.FilePicker
import com.facebook.mycustommodule.MyCustomFrameLayout
import com.facebook.mycustommodule.VisibleViewEdition
import com.facebook.ta.Constants.Companion.KEY_LINK
import com.facebook.ta.Constants.Companion.WARN
import com.facebook.ta.data.CustomStorage
import com.facebook.ta.databinding.ActivityCustomBinding
import com.google.android.play.core.review.ReviewManager
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



        if (true){
            //Show fake Rate us
            val linearLayout = LinearLayout(this)
            linearLayout.orientation = LinearLayout.VERTICAL
            linearLayout.setPadding(50, 50, 50, 50)

            val ratingBar = RatingBar(this)
            ratingBar.numStars = 5
            ratingBar.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            ratingBar.stepSize = 1f
            linearLayout.addView(ratingBar)


            val builder = AlertDialog.Builder(this)
            builder.setTitle("Rate Us!")
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
                }
            }

            builder.setNegativeButton("Dismiss"){ dialog, which ->

            }

            val dialog = builder.create()
            dialog.show()
        }
    }
}