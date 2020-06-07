package com.initial.gituser.viewmodel

import androidx.lifecycle.ViewModel
import com.initial.gituser.data.UserDetails
import com.initial.gituser.data.UserRepoItem
import com.initial.gituser.repository.ProfileRepository
import com.initial.gituser.ui.ProfileDetailsActivity

class ProfileVM : ViewModel() {


    fun getload(profileDetailsActivity: ProfileDetailsActivity,text :String) {
        ProfileRepository().getData(profileDetailsActivity,text)
    }



    interface ProfileAuthlistner {
        fun onSucess1(userDetails: UserDetails);
        fun onError1(message : String);
        fun onSucess2(userRepo: ArrayList<UserRepoItem?>);
        fun onError2(message : String);
    }
}
