@file:Suppress("DEPRECATION")

package com.initial.gituser.ui

import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.initial.gituser.R
import com.initial.gituser.data.Item
import com.initial.gituser.ui.adapter.HomeAdapter
import com.initial.gituser.utils.isNetworkAvailable
import com.initial.gituser.utils.toast
import com.initial.gituser.viewmodel.HomeVM
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_recyclerview.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity(), HomeVM.HomeAuthlistner {

      private lateinit var viewModle : HomeVM
    private lateinit var linear : LinearLayoutManager
    var lastChange: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModle = ViewModelProviders.of(this).get(HomeVM()::class.java)
        onBindViews()
    }

    private fun onBindViews() {
        edit_query.doOnTextChanged { text, start, before, count ->
            if(text.toString().length>1){
                if(isNetworkAvailable(applicationContext)){
                    Handler().postDelayed(Runnable {
                        if (System.currentTimeMillis() - lastChange >= 300) {
                            viewModle.getload(this,text.toString())
                            progress_bar.visibility = View.VISIBLE
                        }
                    }, 300)
                    lastChange = System.currentTimeMillis();

                } else{
                    toast("No Internet Connection")
                }

            }
        }
        linear = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linear
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
            ) {
                outRect.left =  applicationContext!!.resources.getDimension(R.dimen.margin_3dp).toInt()
                outRect.right =  applicationContext!!.resources.getDimension(R.dimen.margin_3dp).toInt()
            }
        })
    }

    override fun onSucess(detailsList: ArrayList<Item>) {

        GlobalScope.launch(Dispatchers.Main){
            recyclerView.adapter = HomeAdapter(detailsList,applicationContext)
            progress_bar.visibility = View.GONE
            retry.visibility = View.GONE
        }
    }

    override fun onError(message: String) {
       toast(message)

    }

    fun onRetry(view: View?) {
        if(isNetworkAvailable(applicationContext)){
            if(edit_query.text.toString().isNotEmpty()){
                viewModle.onRetry(this,edit_query.text.toString())
                progress_bar.visibility = View.VISIBLE
                view?.visibility = View.GONE
            }
        }else{
            progress_bar.visibility = View.GONE
            retry.visibility = View.VISIBLE
            toast("No Internet Connection")
        }
    }

}
