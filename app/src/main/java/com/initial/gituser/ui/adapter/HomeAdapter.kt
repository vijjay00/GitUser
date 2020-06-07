package com.initial.gituser.ui.adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.initial.gituser.R
import com.initial.gituser.data.Item
import com.initial.gituser.ui.ProfileDetailsActivity
import com.initial.gituser.utils.isNetworkAvailable
import com.initial.gituser.utils.toast


class HomeAdapter (var sources: ArrayList<Item>, val mContext: Context) :
    RecyclerView.Adapter<HomeAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val rootView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_display, parent, false)
        return MyViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val datas = sources.get(position)

        holder.titleText.text = datas.login
        holder.repo.text = String.format(mContext.resources.getString(R.string.m_Repo),datas.score.toString())
        onImageRetry(datas.avatar_url,holder.progress_bar,holder.retry,holder.image,holder.noImage)

        holder.image.setOnClickListener {
            if(isNetworkAvailable(mContext)){
            holder.progress_bar.visibility = View.VISIBLE
            holder.retry.visibility = View.GONE
            onImageRetry(datas.avatar_url, holder.progress_bar, holder.retry, holder.image, holder.noImage)
            }else{
                toast("No Internet Connection",mContext )
            }
        }
        holder.itemView.setOnClickListener {
            if(isNetworkAvailable(mContext)){
                mContext.run(fun Context.() {
                    startActivity(
                        Intent(mContext, ProfileDetailsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("itemData", datas))
                })
            }else{
                toast("No Internet Connection",mContext)
            }
        }
    }

    private fun onImageRetry(image: String, progressBar: ProgressBar, retry: TextView, image1: AppCompatImageView, noImage: TextView
    ) {
        if(image.length>4){
            Glide.with(mContext).load(image).listener(object :
                RequestListener<Drawable?> {
                override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable?>, isFirstResource: Boolean): Boolean {
                    progressBar.visibility = View.GONE
                    retry.visibility =View.VISIBLE
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any, target: Target<Drawable?>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                    progressBar.visibility =View.GONE
                    return false
                }
            }).into(image1)
        } else{
            progressBar.visibility = View.GONE
            noImage.visibility = View.VISIBLE
        }
    }


    override fun getItemCount(): Int {
        return sources.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class MyViewHolder(itemView: View?) :
        RecyclerView.ViewHolder(itemView!!) {

        val repo : TextView
            get() = itemView.findViewById(R.id.repo)
        val titleText : TextView
            get() = itemView.findViewById(R.id.name)
        val retry : TextView
            get() = itemView.findViewById(R.id.retry)
        val image : AppCompatImageView
            get() = itemView.findViewById(R.id.image)
        val progress_bar : ProgressBar
            get() = itemView.findViewById(R.id.progress_bar)
        val noImage : TextView
            get() = itemView.findViewById(R.id.noImage)
    }

    init {
        this.sources = sources
    }
}