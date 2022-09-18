package com.demo.strongbox.adapter0914

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.strongbox.R
import com.demo.strongbox.app.serverIcon
import com.demo.strongbox.connect.Connect0914InfoObject
import com.demo.strongbox.fire.Local0914Conf
import com.demo.strongbox.fire.Server0914En
import kotlinx.android.synthetic.main.layout_server_item0914.view.*

class ServerListAdapter(private val context:Context,private val clickServer:(server0914En:Server0914En)->Unit):RecyclerView.Adapter<ServerListAdapter.ServerView>() {
    private val list= arrayListOf<Server0914En>()
    init {
        list.clear()
        list.add(Connect0914InfoObject.newFastSever0914En())
        list.addAll(Local0914Conf.server0914List)
    }

    inner class ServerView(view:View):RecyclerView.ViewHolder(view){
        init {
            view.setOnClickListener {
                clickServer.invoke(list[layoutPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServerView {
        return ServerView(LayoutInflater.from(context).inflate(R.layout.layout_server_item0914,parent,false))
    }

    override fun onBindViewHolder(holder: ServerView, position: Int) {
        with(holder.itemView){
            val server0914En = list[position]
            tv_item_server_name.text=server0914En.country_0914_strong
            iv_item_server_icon.setImageResource(serverIcon(server0914En.country_0914_strong))
        }
    }

    override fun getItemCount(): Int = list.size

}