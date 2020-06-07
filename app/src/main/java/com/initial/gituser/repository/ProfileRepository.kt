package com.initial.gituser.repository

import android.util.Log
import com.initial.gituser.data.UserDetails
import com.initial.gituser.data.UserRepo
import com.initial.gituser.data.UserRepoItem
import com.initial.gituser.ui.ProfileDetailsActivity
import com.initial.gituser.utils.MyApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProfileRepository {

    fun getData(profileDetailsActivity: ProfileDetailsActivity, text1 : String?) {
        GlobalScope.launch(Dispatchers.IO){

            launch {   val  response = MyApi().getUserDetail(text1)
                try {
                    if(response.isSuccessful){
                        response.body().let { profileDetailsActivity.run { onSucess1(response.body() as UserDetails) } }
                    }else{
                        profileDetailsActivity.onError1(response.errorBody().toString())
                    }
                }catch (e : Exception){
                    profileDetailsActivity.onError1(e.toString())
                } }

            launch {   val  response = MyApi().getUserRepository(text1)
                try {
                    if(response.isSuccessful){
                        response.body().let { profileDetailsActivity.run { onSucess2(it as ArrayList<UserRepoItem?>) } }
                    }else{
                        profileDetailsActivity.onError2(response.errorBody().toString())
                    }
                }catch (e : Exception){
                    profileDetailsActivity.onError2(e.toString())
                } }


        }
    }
}