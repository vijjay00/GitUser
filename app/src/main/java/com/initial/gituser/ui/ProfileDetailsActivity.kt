@file:Suppress("DEPRECATION")

package com.initial.gituser.ui

import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.initial.gituser.R
import com.initial.gituser.data.Item
import com.initial.gituser.data.UserDetails
import com.initial.gituser.data.UserRepo
import com.initial.gituser.data.UserRepoItem
import com.initial.gituser.ui.adapter.ProfileAdapter
import com.initial.gituser.utils.getDate
import com.initial.gituser.utils.toast
import com.initial.gituser.viewmodel.ProfileVM
import kotlinx.android.synthetic.main.activity_profile_details.*
import kotlinx.android.synthetic.main.item_recyclerview.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileDetailsActivity : AppCompatActivity(),ProfileVM.ProfileAuthlistner {

    private lateinit var item: Item
    private lateinit var viewModle : ProfileVM
    private lateinit var linear : LinearLayoutManager
    private  lateinit var adaptering : ProfileAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_details)

        viewModle = ViewModelProviders.of(this).get(ProfileVM()::class.java)

        item = intent.getParcelableExtra<Item>("itemData")
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = item.login
        }
        viewModle.getload(this,item.login)
        onBindViews()
    }

    private fun onBindViews() {
        progress_bar.visibility = View.VISIBLE
        if(item.avatar_url.length>4){
            Glide.with(applicationContext).load(item.avatar_url).listener(object :
                RequestListener<Drawable?> {
                override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable?>, isFirstResource: Boolean): Boolean {
                    progress_bar1.setVisibility(View.GONE)
                    return false
                }
                override fun onResourceReady(resource: Drawable?, model: Any, target: Target<Drawable?>, dataSource: DataSource, isFirstResource: Boolean
                ): Boolean {
                    progress_bar1.setVisibility(View.GONE)
                    return false
                }
            }).into(image)
        } else{
            progress_bar1.visibility = View.GONE
            noImage.visibility = View.VISIBLE
        }
        edit_query2.addTextChangedListener(textWatcher)

        linear = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linear
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
            ) { outRect.left =  applicationContext!!.resources.getDimension(R.dimen.margin_5dp).toInt()
                outRect.right =  applicationContext!!.resources.getDimension(R.dimen.margin_5dp).toInt()
            }
        })
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            adaptering.filter.filter(s.toString())
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adaptering.filter.filter(s.toString())
        }
    }
    override fun onSucess1(userDetails: UserDetails) {
        lifecycleScope.launch(Dispatchers.Main) {
            name.text = userDetails.login
            if(userDetails.email !=null) { email.text = userDetails.email.toString() }
            if(userDetails.location  !=null) { location.text = userDetails.location.toString() }
            if(userDetails.created_at.isEmpty()) { joinDate.text = userDetails.created_at}
            followers.text = String.format(applicationContext.resources.getString(R.string.m_Followers),userDetails.followers)
            following.text = String.format(applicationContext.resources.getString(R.string.m_Following),userDetails.following)
            if(userDetails.bio!=null) { blog.text = userDetails.bio.toString() }
            joinDate.text = getDate(userDetails.created_at)
        }

    }

    override fun onError1(message: String) {

    }

    override fun onSucess2(userRepo: ArrayList<UserRepoItem?>) {
        lifecycleScope.launch(Dispatchers.Main){
            recyclerView.adapter = ProfileAdapter(userRepo,applicationContext)
            adaptering = recyclerView.adapter as ProfileAdapter
            progress_bar.visibility = View.GONE
        }
    }

    override fun onError2(message: String) {
        toast(message)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
