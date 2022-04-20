package com.hti.hiinternet.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.hti.hiinternet.R
import com.hti.hiinternet.data.PaymentModel
import com.hti.hiinternet.data.repo.PreFerenceRepo
import com.hti.hiinternet.data.response.ResponseMycomplainService
import com.hti.hiinternet.data.response.ResponsePayment
import com.hti.hiinternet.util.Constant

class MycomplainServiceAdapter(mycomplainserviceItemlist: ArrayList<ResponseMycomplainService>) :
    RecyclerView.Adapter<MycomplainServiceAdapter.ViewHolder>() {

    val mycomplainItem: ArrayList<ResponseMycomplainService> = mycomplainserviceItemlist
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_complain_items, parent, false)
        this.context = parent.context
        return MycomplainServiceAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mycomplainItem.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.complain_ticket_id.setText(mycomplainItem[position].ticket_id)
        holder.complain_type.setText(mycomplainItem[position].topic)
        holder.complain_topic.setText(mycomplainItem[position].message)


        if (mycomplainItem[position].status.equals(Constant.SERVICETICKET_STATUS_SOLVED))
        {
            if (PreFerenceRepo.lang.equals("0")){
                holder.complain_status.setText("Resolved")
                holder.complain_solve_date.visibility=View.VISIBLE
                holder.complain_solve_date.setText("Solved Date-${mycomplainItem[position].resloved_time}")
                holder.complain_creation_date.visibility=View.GONE
                holder.complain_status.setBackgroundResource(R.drawable.ticket_solved)
            }
            else{
                holder.complain_status.setText("ဖြေရှင်းပြီး")
                holder.complain_solve_date.visibility=View.VISIBLE
                holder.complain_solve_date.setText("Solved Date-${mycomplainItem[position].resloved_time}")
                holder.complain_creation_date.visibility=View.GONE
                holder.complain_status.setBackgroundResource(R.drawable.ticket_solved)
            }

        }
        else if(mycomplainItem[position].status.equals(Constant.SERVICETICKET_STATUS_CLAIM)){

            if (PreFerenceRepo.lang.equals("0")) {

                holder.complain_status.setText("Claim")
                holder.complain_status.setBackgroundResource(R.drawable.ticket_claim)
                holder.complain_creation_date.visibility = View.VISIBLE
                holder.complain_creation_date.setText("Complain Date-" + mycomplainItem[position].creation_date)
                holder.complain_solve_date.visibility = View.GONE
            }else
            {
                holder.complain_status.setText("စာရင်းသွင်းသည်")
                holder.complain_status.setBackgroundResource(R.drawable.ticket_claim)
                holder.complain_creation_date.visibility = View.VISIBLE
                holder.complain_creation_date.setText("တိုင်ကြားသည့် နေ့စွဲ-" + mycomplainItem[position].creation_date)
                holder.complain_solve_date.visibility = View.GONE
            }
        }
        else if (mycomplainItem[position].status.equals(Constant.SERVICETICKET_STATUS_CLOSED)){
            if (PreFerenceRepo.lang.equals("0")) {

                holder.complain_status.setText("Claim")
                holder.complain_status.setBackgroundResource(R.drawable.ticket_claim)
                holder.complain_creation_date.visibility = View.VISIBLE
                holder.complain_creation_date.setText("Complain Date-" + mycomplainItem[position].creation_date)
                holder.complain_solve_date.visibility = View.GONE
            }else
            {
                holder.complain_status.setText("စာရင်းသွင်းသည်")
                holder.complain_status.setBackgroundResource(R.drawable.ticket_claim)
                holder.complain_creation_date.visibility = View.VISIBLE
                holder.complain_creation_date.setText("တိုင်ကြားသည့် နေ့စွဲ-" + mycomplainItem[position].creation_date)
                holder.complain_solve_date.visibility = View.GONE
            }
        }
        else if (mycomplainItem[position].status.equals(Constant.SERVICETICKET_STATUS_NOC)){
            if (PreFerenceRepo.lang.equals("0")){
                holder.complain_status.setText("Resolved")
                holder.complain_solve_date.visibility=View.VISIBLE
                holder.complain_solve_date.setText("Solved Date-${mycomplainItem[position].resloved_time}")
                holder.complain_creation_date.visibility=View.GONE
                holder.complain_status.setBackgroundResource(R.drawable.ticket_solved)
            }
            else{
                holder.complain_status.setText("ဖြေရှင်းပြီး")
                holder.complain_solve_date.visibility=View.VISIBLE
                holder.complain_solve_date.setText("Solved Date-${mycomplainItem[position].resloved_time}")
                holder.complain_creation_date.visibility=View.GONE
                holder.complain_status.setBackgroundResource(R.drawable.ticket_solved)
            }
        }
        else{

            if (PreFerenceRepo.lang.equals("0")){
                holder.complain_status.setText("Pending")
                holder.complain_status.setBackgroundResource(R.drawable.ticket_pending)
                holder.complain_creation_date.visibility=View.VISIBLE
                holder.complain_creation_date.setText("Complain Date-"+mycomplainItem[position].creation_date)
                holder.complain_solve_date.visibility=View.GONE
            }
            else{
                holder.complain_status.setText("လုပ်ဆောင်ဆဲ")
                holder.complain_status.setBackgroundResource(R.drawable.ticket_pending)
                holder.complain_creation_date.visibility=View.VISIBLE
                holder.complain_creation_date.setText("တိုင်ကြားသည့် နေ့စွဲ-"+mycomplainItem[position].creation_date)
                holder.complain_solve_date.visibility=View.GONE
            }

        }

    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view: View = itemView
        val complain_ticket_id = itemView.findViewById(R.id.txt_ticket_id) as TextView
        val complain_type = itemView.findViewById(R.id.txt_complain_type) as TextView
        val complain_topic = itemView.findViewById(R.id.txt_complain_topic) as TextView
        val complain_message = itemView.findViewById(R.id.txt_complain_message) as TextView
        val complain_creation_date = itemView.findViewById(R.id.txt_complain_date) as TextView
        val complain_solve_date = itemView.findViewById(R.id.txt_solve_date) as TextView
        val complain_status = itemView.findViewById(R.id.complain_status) as TextView
     }

    fun setData(list: ArrayList<ResponseMycomplainService>?) {
        this.mycomplainItem.clear()
        if (list != null) {

            this.mycomplainItem.addAll(list.reversed())
        }
        notifyDataSetChanged()
    }


}