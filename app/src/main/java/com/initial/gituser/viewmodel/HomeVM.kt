package com.initial.gituser.viewmodel

import androidx.lifecycle.ViewModel
import com.initial.gituser.data.Item
import com.initial.gituser.repository.HomeRepository
import com.initial.gituser.ui.HomeActivity


class HomeVM() : ViewModel() {


     fun getload(homeActivity: HomeActivity,text :String) {
        HomeRepository().getData(homeActivity,text)
    }

    fun onRetry(homeActivity: HomeActivity,text: String){
        getload(homeActivity,text)
    }

    interface HomeAuthlistner {
        fun onSucess(detailsList: ArrayList<Item>);
        fun onError(message : String);
    }
}
