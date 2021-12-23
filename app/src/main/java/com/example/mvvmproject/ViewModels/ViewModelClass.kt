package com.example.mvvmproject.ViewModels

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvmproject.DataModels.*
import com.example.mvvmproject.UserDataEntity2
import com.example.mvvmproject.Repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelClass(val repository: Repository, val context: Context) : ViewModel() {

    init {
        CoroutineScope(Dispatchers.IO)
            .launch {
                val userModel = LoginRequestModel(
                    "piyush18@gmail.com",
                    "12345678",
                    "patient",
                    "dfdfsasd5",
                    "android"
                )


            }

    }


    val muLivedata=MutableLiveData<UserDataReposnse>()
    val userLiveData:LiveData<UserDataReposnse>
    get() = muLivedata
  fun getUserLogin(ob: LoginRequestModel)
  {
      CoroutineScope(Dispatchers.IO).launch {
          muLivedata.postValue(repository.getLoginStatusRepo(ob,context))
      }
  }



fun getUserDatafromDb() : LiveData<UserDataEntity2>
{

    return repository.database.getDao().getUserdata()
}



    val dataupdates=MutableLiveData<UserDataEntity2>()
    val tempdata:LiveData<UserDataEntity2>
    get()=dataupdates
  fun setUserUpdates(userData:UserPofileUpdate,activity : Activity)
   {

      var ob=UserDataEntity2()
       CoroutineScope(Dispatchers.IO).launch {
           val temp = repository.getUserUpdates(userData)
           val t=temp.data

            ob.apply {

               ob.patient_id=   patient_id.toString()
               ob.first_name=   first_name.toString()
               ob.mobile=     mobile.toString()
               ob.dob=     dob.toString()
               ob.zip_code=     zip_code.toString()
               ob.state=    state_id
               ob.last_name=     last_name
               ob.email=     email
               ob.address=     address
               ob.city=     city.toString()

           }

            repository.database.getDao().insertUserData(ob)

           Log.d("tag", "in view model userdata checking: "+userData.mobile+"  :  "+userData.city)
           dataupdates.postValue(ob)
            repository.database.getDao().updateUserDb(ob)
           }


       }

}