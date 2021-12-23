package com.example.mvvmproject.RetroApi

import com.example.mvvmproject.DataModels.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitApi {



    @POST("index.php?mod=login&act=do_login")
    suspend fun getLoginStatus(@Body userModel: LoginRequestModel): Response<UserDataReposnse>

    @POST("index.php?mod=Long_questionnaire&act=get_initial_questionnaire")
    suspend fun getUsersData(@Body patient_id:Fetch_UsersDetails) : Response<User_Details>


    @POST("index.php?mod=profile&act=set_patient_profile")
    suspend fun getUserUpdate(@Body updateProfile:UserPofileUpdate) : Response<GetUserDataUpdates>


}