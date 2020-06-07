@file:Suppress("CAST_NEVER_SUCCEEDS")

package com.initial.gituser.repository

import com.initial.gituser.data.Item
import com.initial.gituser.data.UsersPojo
import com.initial.gituser.ui.HomeActivity
import com.initial.gituser.utils.MyApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class HomeRepository {

    fun getData(homeActivity: HomeActivity,text : String?) {
        GlobalScope.launch(Dispatchers.IO){
           val  response = MyApi().searchUsers(text)
            try {
                if(response.isSuccessful){
                    response.body()?.items?.let { homeActivity.onSucess(it as ArrayList<Item>) }
                }else{
                    homeActivity.onError(response.errorBody().toString())
                }
            }catch (e : Exception){
                homeActivity.onError(e.message.toString())
            }

        }
    }
}