package com.example.mvvmproject.Repository

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvmproject.DataModels.*
import com.example.mvvmproject.RetroApi.RetrofitApi
import com.example.mvvmproject.Database.UserDatabase
import com.example.mvvmproject.ProgressBuilder
import com.example.mvvmproject.UserDataEntity2
import com.example.mvvmproject.UI.Users_Address
import retrofit2.Response

class Repository(val retrofitApi: RetrofitApi, val database: UserDatabase) {

    var usersdata = MutableLiveData<User_Details>()

    val livedatao: LiveData<User_Details>
        get() = usersdata

    suspend fun getUsers_Details(
        patient_id: Fetch_UsersDetails,
        context: Context
    ): Response<User_Details> {
        val result = retrofitApi.getUsersData(patient_id)
        Handler(Looper.getMainLooper()).post(Runnable {
            Toast.makeText(
                context,
                result.body()?.email.toString() + "hello world",
                Toast.LENGTH_SHORT
            ).show()
        })
        return result
    }

    var userLivedata = MutableLiveData<UserDataReposnse>()
    val dataob: LiveData<UserDataReposnse>
        get() = userLivedata


    suspend fun getLoginStatusRepo(
        userModel: LoginRequestModel,
        context: Context
    ): UserDataReposnse {

        Handler(Looper.getMainLooper()).post(Runnable {
            val progress = ProgressBuilder()
            progress.show(context, true, "please wait...")
        })
        val result = retrofitApi.getLoginStatus(userModel)
        if (result.body() != null) {
            userLivedata.postValue(result.body())
            val userresponsedata = result.body()
            var userentity = userresponsedata?.data?.let {
                UserDataEntity2(
                    it.doctor_name.toString(),
                    it.first_name.toString(),
                    it.code.toString(),
                    it.mobile.toString(),
                    it.dob.toString(),
                    it.zip_code.toString(),
                    it.country,
                    it.state_id,
                    it.state,
                    it.status
                )
            }
            if (userentity != null) {
                var email = result.body()?.data?.email.toString()

                if (email.equals(null)) {
                    Handler(Looper.getMainLooper()).post(Runnable {
                        Toast.makeText(
                            context,
                            "User not found!",
                            Toast.LENGTH_SHORT
                        ).show()
                        val progress = ProgressBuilder()
                        progress.show(context, false, "please wait...")

                    })
                }
                if (!userresponsedata?.data?.email.equals(null)) {
                    database.getDao().insertUserData(userentity)
                    Handler(Looper.getMainLooper()).post(Runnable {
                        context.startActivity(Intent(context, Users_Address::class.java))
                    })
                }
            } else if (!userresponsedata?.data?.email.equals(null)) {
                Handler(Looper.getMainLooper()).post(Runnable {
                    Toast.makeText(
                        context,
                        "user not found!",
                        Toast.LENGTH_SHORT
                    ).show()
                    val progress = ProgressBuilder()
                    progress.show(context, false, "please wait...")
                })
            }
        } else {
            Handler(Looper.getMainLooper()).post(Runnable {
                Toast.makeText(context, "something went wrong!", Toast.LENGTH_SHORT).show()
                val progress = ProgressBuilder()
                progress.show(context, false, "please wait...")
            })
        }
        return result.body()!!
    }

}


