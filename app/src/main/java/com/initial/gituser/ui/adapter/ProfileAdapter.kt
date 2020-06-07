package com.initial.gituser.ui.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.initial.gituser.R
import com.initial.gituser.data.UserRepoItem
import com.initial.gituser.ui.RepoActivity
import com.initial.gituser.utils.isNetworkAvailable
import com.initial.gituser.utils.toast

class ProfileAdapter (var sources: ArrayList<UserRepoItem?>, val mContext: Context) :
    RecyclerView.Adapter<ProfileAdapter.MyViewHolder>(),Filterable {

    private var userFiltered : ArrayList<UserRepoItem?>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val rootView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_repo, parent, false)
        return MyViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val datas = userFiltered[position]

        holder.titleText.text = datas?.name
        holder.forks.text = String.format(mContext.resources.getString(R.string.m_Forks),datas?.forks_count)
        holder.starts.text = String.format(mContext.resources.getString(R.string.m_Stars),datas?.stargazers_count)

        holder.itemView.setOnClickListener {
            if(isNetworkAvailable(mContext)){
                mContext.run(fun Context.() {
                    startActivity(
                        Intent(mContext, RepoActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("name", datas?.name).putExtra("url", datas?.html_url))
                })
            }else{
                toast("No Internet Connection",mContext)
            }
        }
    }


    override fun getItemCount(): Int {
        return userFiltered.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class MyViewHolder(itemView: View?) :
        RecyclerView.ViewHolder(itemView!!) {

        val titleText : TextView
            get() = itemView.findViewById(R.id.name)
        val forks : TextView
            get() = itemView.findViewById(R.id.forks)
        val starts : TextView
            get() = itemView.findViewById(R.id.starts)
    }

    init {
        this.sources = sources
        this.userFiltered = sources

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                val filteredList: ArrayList<UserRepoItem?> = ArrayList()
                if (charString.isEmpty()) {
                    userFiltered = sources
                } else {
                    for (row in sources) {
                        if(row?.name.equals(charSequence.toString(),ignoreCase = true) || row?.name?.startsWith(charSequence.toString(),ignoreCase = true)!!){
                            filteredList.add(row)
                        }

                    }
                    userFiltered = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = userFiltered
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence,
                filterResults: FilterResults) {
                userFiltered = (filterResults.values as? ArrayList<UserRepoItem?>)!!
                notifyDataSetChanged()
            }
        }
    }
}