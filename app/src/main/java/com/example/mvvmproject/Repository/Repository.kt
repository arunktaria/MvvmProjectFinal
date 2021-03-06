package com.example.mvvmproject.Repository

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.text.createTextLayoutResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvmproject.DataModels.*
import com.example.mvvmproject.RetroApi.RetrofitApi
import com.example.mvvmproject.Database.UserDatabase
import com.example.mvvmproject.utils.ProgressBuilder
import com.example.mvvmproject.UserDataEntity2
import com.example.mvvmproject.UI.Users_Address
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Response

class Repository(val retrofitApi: RetrofitApi, val database: UserDatabase) {
    val progress = ProgressBuilder()
    var usersdata = MutableLiveData<User_Details>()
     var TAG="tag"
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

            progress.show(context, "please wait...")
        })
        val result = retrofitApi.getLoginStatus(userModel)
        if (result.body() != null) {
            userLivedata.postValue(result.body())
            val userresponsedata = result.body()




           var ob=UserDataEntity2()
            var userentity = userresponsedata?.data?.apply {
             ob =  UserDataEntity2()
                ob.doctor_name=   doctor_name.toString()
                ob.patient_id=   patient_id.toString()
                ob.first_name=   first_name.toString()
                ob.code=     code.toString()
                ob.mobile=     mobile.toString()
                ob.dob=     dob.toString()
                ob.zip_code=     zip_code.toString()
                ob.country=      country.toString()
                ob.state=    state_id
                ob.state=     state
                ob.status=     status
                ob.last_name=     last_name
                ob.email=     email
                ob.address=     address
                ob.city=     city.toString()
                ob.state=state

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

                        progress.dismissDialog()

                    })
                }
                if (!userresponsedata?.data?.email.equals(null)) {
                    Log.d(TAG, "in database insertion: "+ob.email.toString())
                    database.getDao().insertUserData(ob)
                    Handler(Looper.getMainLooper()).post(Runnable {
                        context.startActivity(Intent(context, Users_Address::class.java))
                    })
                }
            }
            if (userresponsedata?.data?.email.equals(null)) {
                Handler(Looper.getMainLooper()).post(Runnable {
                    Toast.makeText(
                        context,
                        "user not found!",
                        Toast.LENGTH_SHORT
                    ).show()

                    progress.dismissDialog()
                })
            }
        } else {
            Handler(Looper.getMainLooper()).post(Runnable {
                Toast.makeText(context, "something went wrong!", Toast.LENGTH_SHORT).show()

                progress.dismissDialog()
            })
        }
        return result.body()!!
    }

var mutableupdate=MutableLiveData<UserDataReposnse>()

    suspend  fun getUserUpdates(userupdate : UserPofileUpdate): UserDataReposnse {
       // retrofitApi.getUserUpdate(userupdate)
      val result = retrofitApi.getUserUpdate(userupdate)
      CoroutineScope(Dispatchers.IO)
          .launch {
              mutableupdate.postValue(result.body())
          }
      return result.body()!!
  }
  }


