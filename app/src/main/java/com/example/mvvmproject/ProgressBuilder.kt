package com.example.mvvmproject

import android.app.ProgressDialog
import android.content.Context
import android.widget.ProgressBar

class ProgressBuilder {

fun show(context:Context,show : Boolean,text:String)
{


    val progressDialogs = ProgressDialog(context)
    progressDialogs.setMessage(text)
    progressDialogs.setCancelable(true)

    if (show==true)
    {
        progressDialogs.show()
    }
    if (show==false)
    {
        progressDialogs.dismiss()
    }



}



}

