package com.example.mvvmproject.UI

import android.R.attr
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color.TRANSPARENT
import android.graphics.drawable.ColorDrawable
import android.icu.number.IntegerWidth
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mvvmproject.BaseApplication.ApplicationMain
import com.example.mvvmproject.DataModels.UserPofileUpdate
import com.example.mvvmproject.R

import com.example.mvvmproject.ViewModels.Factories.ViewModelFactoryClass
import com.example.mvvmproject.ViewModels.ViewModelClass
import com.example.mvvmproject.databinding.ActivityUserProfileBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.graphics.Bitmap.CompressFormat

import android.R.attr.bitmap
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File as File


class UserProfile : AppCompatActivity() {
    var TAG = "tag"
    lateinit var userupdatetemp: UserPofileUpdate
    lateinit var patient_id: String
    lateinit var bottomSheet: ChoosePhotoBottomSheet
    lateinit var binding: ActivityUserProfileBinding
    lateinit var bitmapg: Bitmap
    var context=this
    var userdata = UserPofileUpdate()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        /*  Handler(Looper.getMainLooper()).postDelayed(Runnable {

              Glide.with(this).load("").into(binding.shapeableImageView)

          },2000)
  */


        binding.lifecycleOwner = this
        val repositorytemp = (application as ApplicationMain).repository
        val viewmodelinstanse = ViewModelProvider(
            this,
            ViewModelFactoryClass(repositorytemp, this)
        ).get(ViewModelClass::class.java)


        binding.btnupdateprofile.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {
                patient_id = repositorytemp.database.getDao().getUserId()
            }

            userdata.apply {

                patient_id = "120"
                email = binding.emaileditextprofile.text.toString()
                first_name = binding.firstname.text.toString()
                last_name = binding.lastname.text.toString()
                dob = binding.birthday.text.toString()
                address = binding.addressprofile.text.toString()
                city = binding.cityprofilee.text.toString()
                state_id = binding.stateprofilee.text.toString()
                country_id = "16055"
                mobile = binding.mobilenumprofile.text.toString()
                zip_code = binding.zipcodeprofile.text.toString()
                //profile_photo =getFileImage()
            }
            Log.d("TAG", "onCreate: " + userdata.email.toString())

            viewmodelinstanse.setUserUpdates(userdata, this)
        }




        binding.getimagefloatingbtn.setOnClickListener {
            bottomSheet = ChoosePhotoBottomSheet(UserProfile@ this, this)
            //bottomSheet.window?.setBackgroundDrawable()
            bottomSheet.show()
        }

        viewmodelinstanse.getUserDatafromDb().observe(this, Observer {
            Log.d("arun", "in user address " + it?.mobile)
            binding.userprofile = it


        })


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {


            if (requestCode == 100) {
                val bitmap = data?.extras?.get("data") as Bitmap
                binding.shapeableImageView.setImageBitmap(bitmap)
                bottomSheet.dismiss()
                bitmapg = bitmap

            }
            if (requestCode == 101) {
                //working as well
                /*val uri=data?.data
            binding.shapeableImageView.setImageURI(uri)*/

                val uri = data?.data

                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                binding.shapeableImageView.setImageBitmap(bitmapg)
                bottomSheet.dismiss()
                bitmapg = bitmap

            }
        } else {

            Toast.makeText(UserProfile@ this, "declined!", Toast.LENGTH_SHORT).show()
            bottomSheet.dismiss()
        }

    }


    fun getFileImage() : MultipartBody.Part {
        val bos = ByteArrayOutputStream()
        bitmapg.compress(Bitmap.CompressFormat.JPEG,50,bos)
        val bitmapdata: ByteArray = bos.toByteArray()
        val bs = ByteArrayInputStream(bitmapdata)

        val part = MultipartBody.Part.createFormData("pic", "myPic", RequestBody.create(MediaType.parse("image/*"), bs.readBytes()))
        return part

    }




}




