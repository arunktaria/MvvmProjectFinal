package com.example.mvvmproject.UI

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color.TRANSPARENT
import android.graphics.drawable.ColorDrawable
import android.icu.number.IntegerWidth
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mvvmproject.BaseApplication.ApplicationMain
import com.example.mvvmproject.R

import com.example.mvvmproject.ViewModels.Factories.ViewModelFactoryClass
import com.example.mvvmproject.ViewModels.ViewModelClass
import com.example.mvvmproject.databinding.ActivityUserProfileBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class UserProfile : AppCompatActivity() {

lateinit var  bottomSheet:ChoosePhotoBottomSheet
lateinit var binding :ActivityUserProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)



       // Toast.makeText(this,color.toString(),Toast.LENGTH_LONG).show()


        binding.getimagefloatingbtn.setOnClickListener {
          bottomSheet =ChoosePhotoBottomSheet(UserProfile@this,this)
            //bottomSheet.window?.setBackgroundDrawable()

                bottomSheet.show()
        }


        val repository=(application as ApplicationMain ).repository
        val viewmodel=
            ViewModelProvider(this, ViewModelFactoryClass(repository,this)).get(ViewModelClass::class.java)

        viewmodel.getUserDatafromDb().observe(this, Observer {
            Log.d("TAG", "in user address "+it?.mobile)
            binding.userprofile=it


        })



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==100)
        {       val bitmap=data?.extras?.get("data") as Bitmap
            binding.shapeableImageView.setImageBitmap(bitmap)
            bottomSheet.dismiss()
        }
        if(requestCode==101)
        {
            //working as well
            /*val uri=data?.data
            binding.shapeableImageView.setImageURI(uri)*/

                val uri =data?.data
            val bitmam=MediaStore.Images.Media.getBitmap(contentResolver,uri)
                binding.shapeableImageView.setImageBitmap(bitmam)


            bottomSheet.dismiss()
        }




    }



}