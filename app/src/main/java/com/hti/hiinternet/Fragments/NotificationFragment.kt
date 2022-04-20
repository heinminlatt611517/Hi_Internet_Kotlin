package com.hti.hiinternet.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.hti.hiinternet.R
import com.hti.hiinternet.adapter.AdapterNotification
import com.hti.hiinternet.data.greedbootevent.EventNotiCount
import com.hti.hiinternet.data.Notification
import com.hti.hiinternet.data.greedbootevent.EventRecieveNewNoti
import com.hti.hiinternet.data.repo.PreFerenceRepo
import com.hti.hiinternet.data.repo.database.DatabaseManger
import kotlinx.android.synthetic.main.fragment_notification.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class NotificationFragment : Fragment() {
    lateinit var notiAdapter: AdapterNotification

    companion object {
        fun newIntance() = NotificationFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notiAdapter = AdapterNotification(arrayListOf(), this!!.activity!!)
        recNoti.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = notiAdapter
        }


        loadAllNotiFication()
        loadUnreadNotiCount()
    }


    private fun loadUnreadNotiCount() {
        DatabaseManger.getDataBase(this!!.context!!)
            .NotificationDao().getNotiCount().observe(this, Observer {
                postNotiEvent(EventNotiCount(it))
            })
    }

    private fun loadAllNotiFication() {
        DatabaseManger.getDataBase(this!!.context!!)
            .NotificationDao().getAllNoti().observe(this, Observer {
                it.let {

                    if (it.size== 0){
                        showNotiText()
                    }
                    else{
                        hideNotiText()
                    }
                    var data = arrayListOf<Notification>()
                    it.forEach {
                        data.add(it)
                        notiAdapter.setNewNoti(data)
                    }
                }


            })
    }

    //reload noti
    @Subscribe
    fun onReieveNewNoti(event: EventRecieveNewNoti) {
        loadAllNotiFication()
    }

    fun postNotiEvent(event: EventNotiCount) {
        EventBus.getDefault().post(event)
    }

    fun showNotiText(){
        txt_alertNotification.visibility=View.VISIBLE
    }

    fun hideNotiText(){
        txt_alertNotification.visibility=View.GONE
    }

}
