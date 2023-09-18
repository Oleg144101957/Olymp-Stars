package com.facebook.mycustommodule

import android.net.Uri
import android.webkit.ValueCallback

interface FilePicker {
    fun onPickFileCallBack(param: ValueCallback<Array<Uri?>>)

}