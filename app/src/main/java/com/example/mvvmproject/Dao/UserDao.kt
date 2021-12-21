package com.example.mvvmproject.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mvvmproject.UserDataEntity2

@Dao
interface UserDao {

    @Insert
    suspend fun insertUserData(user : UserDataEntity2)

    @Query("select * from tbl_userdata")
     fun getUserdata() : LiveData<UserDataEntity2>


}