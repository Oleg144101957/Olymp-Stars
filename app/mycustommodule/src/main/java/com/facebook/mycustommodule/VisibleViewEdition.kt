package com.facebook.mycustommodule

import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.CookieManager
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.result.ActivityResultLauncher


class VisibleViewEdition(private val context: Context, val filePicker: FilePicker) : WebView(context) {

    fun initialVisibleViewEdition(launcher: ActivityResultLauncher<String>, funInCustomActivity: (String) -> Unit){
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.loadWithOverviewMode = false
        settings.userAgentString = settings.userAgentString.evrythigChanger(2, 5)

        webViewClient = object : WebViewClient(){
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                CookieManager.getInstance().flush()


                if (url == null){

                } else {
                    // check india
                    funInCustomActivity(url)
                }

            }
        }

        webChromeClient = object : WebChromeClient(){
            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri?>>,
                fileChooserParams: FileChooserParams?
            ): Boolean {

                filePicker.onPickFileCallBack(filePathCallback)
                launcher.launch("image/*")

                return true
            }
        }
    }


    private fun String.evrythigChanger(number1: Int, number2: Int ): String{
        return if (number1<number2) this.replace("wv", "") else this.replace("sdfhj", "cfs")
    }
}