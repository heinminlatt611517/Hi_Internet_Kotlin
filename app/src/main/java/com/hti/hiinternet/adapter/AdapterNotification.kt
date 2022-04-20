package com.hti.hiinternet.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.hti.hiinternet.Fragments.NotificationFragment
import com.hti.hiinternet.R
import com.hti.hiinternet.data.greedbootevent.EventNotiRead
import com.hti.hiinternet.data.Notification
import com.hti.hiinternet.data.PromotionModel
import com.hti.hiinternet.data.repo.database.DatabaseManger
import kotlinx.android.synthetic.main.item_notificaton.view.*
import org.greenrobot.eventbus.EventBus

class AdapterNotification(var notiFicationList: ArrayList<Notification>, var context: Activity) :
    RecyclerView.Adapter<AdapterNotification.NotiViewHolder>() {

    class NotiViewHolder(v: View) : RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotiViewHolder =
        NotiViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_notificaton, parent, false)
        )

    override fun getItemCount(): Int = notiFicationList.size

    override fun onBindViewHolder(holder: NotiViewHolder, position: Int) {
        holder.itemView.tvNotititle.text = notiFicationList[position].title
        holder.itemView.tvNotiInfo.text = notiFicationList[position].message
        holder.itemView.notiTime.text = notiFicationList[position].createDate
        Log.d("notificatoinId", notiFicationList[position].ID.toString())

        if (notiFicationList[position].isRead) {
            Log.d("notiId", notiFicationList[position].isRead.toString())
            holder.itemView.setBackgroundColor(context.resources.getColor(R.color.noit_read_color))

        } else {
            Log.d("notiId", notiFicationList[position].isRead.toString())

            holder.itemView.setBackgroundColor(context.resources.getColor(android.R.color.white))

        }

        holder.itemView.setOnClickListener {
            ChangeReadStatus(position, holder)

           notiFicationList[position].actionUrl.let {
               val openURL = Intent(Intent.ACTION_VIEW)

               if (!it.equals("")) {
                   openURL.data = Uri.parse(it)
                   context.startActivity(openURL)
               }
               else{}
           }

        }
    }


    private fun ChangeReadStatus(
        position: Int,
        holder: NotiViewHolder
    ) {
        if (!notiFicationList[position].isRead) {
            var data = notiFicationList[position]
            data.isRead = true
            DatabaseManger.getDataBase(context)
                .NotificationDao().update(data)
            holder.itemView.setBackgroundColor(context.resources.getColor(R.color.noit_read_color))

            postNotiFicationReadStatus()
        }
    }

    fun setNewNoti(noti: ArrayList<Notification>) {
        notiFicationList.clear()
        notiFicationList.addAll(noti.reversed())
        notifyDataSetChanged()
    }

    fun postNotiFicationReadStatus() {
        EventBus.getDefault().post(
            EventNotiRead(
                0
            )
        )

    }


}