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
import com.hti.hiinternet.data.response.ResponsePayment
import com.hti.hiinternet.util.Constant
import org.w3c.dom.Text

class PaymentAdapter(paymentItemList: ArrayList<ResponsePayment>) :
    RecyclerView.Adapter<PaymentAdapter.ViewHolder>() {


    var onItemClick: ((ResponsePayment) -> Unit)? = null
    val paymentItem: ArrayList<ResponsePayment> = paymentItemList
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.payment_items, parent, false)
        this.context = parent.context
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return paymentItem.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

//        holder.payment_amount.setText(paymentItem[position].amount)
//        holder.payment_modified_date.setText(paymentItem[position].creation_date)
//        holder.payment_pay.setText(paymentItem[position].paid_status)
//        holder.payment_invoice_no.setText(paymentItem[position].invoice_id)
//
//        if (paymentItem[position].paid_status.equals(Constant.INVOICE_STATUS_PAID)) {
//            holder.payment_pay.setBackgroundResource(R.drawable.custom_text_green)
//            holder.payment_due_date.setText(paymentItem[position].paid_date)
//        } else {
//            holder.payment_pay.setBackgroundResource(R.drawable.custom_text)
//            holder.payment_due_date.setText(paymentItem[position].due_date)
//        }

        holder.payment_amount.setText(paymentItem[position].amount)
        holder.payment_invoice_no.setText(paymentItem[position].invoice_id)
        holder.payment_modified_date.setText(paymentItem[position].startDate+" - "+paymentItem[position].endDate)

        if (paymentItem[position].paid_status.equals(Constant.INVOICE_STATUS_PAID)) {
            holder.payment_pay.setBackgroundResource(R.drawable.custom_text_green)
            holder.tv_pay.setBackgroundResource(R.color.blue_light)

           if (PreFerenceRepo.lang.equals("0")){
               holder.payment_pay.setText(Constant.INVOICE_STATUS_PAID)
               holder.tv_pay.setText("PAID")
               holder.payment_date_title.setText(Constant.PAID_DATE)
           }
           else{
               holder.payment_pay.setText("ပေးချေပြီး")
               holder.tv_pay.setText("ပေးချေပြီး")
               holder.payment_date_title.setText("ပေးခဲ့ပြီး နေ့စွဲ")
           }

            if (paymentItem[position].paid_date != null) {

                holder.payment_day.setText(paymentItem[position].paid_date!!.day + "")
                holder.payment_month_year.setText(paymentItem[position].paid_date!!.monthYear)

            }

//            paymentItem[position].paid_date?.day?.let { holder.payment_day.setText(it) }
//            paymentItem[position].paid_date?.monthYear?.let { holder.payment_month_year.setText(it) }
        } else {

            holder.payment_pay.setBackgroundResource(R.drawable.custom_text)

            holder.tv_pay.setBackgroundResource(R.color.toolbarBackground)


            if (PreFerenceRepo.lang.equals("0")){
                holder.tv_pay.setText("PAY NOW")
                holder.payment_date_title.setText(Constant.DUE_DATE)
                holder.payment_pay.setText(Constant.INVOICE_STATUS_UNPAID)
            }
            else{
                holder.tv_pay.setText("ပေးချေရန်")
                holder.payment_date_title.setText("ပေးချေရန် နေ့စွဲ")
                holder.payment_pay.setText("ပေးချေရန်")
            }


         if(paymentItem[position].due_date!=null){

             holder.payment_day.setText(paymentItem[position].due_date!!.day + "")
             holder.payment_month_year.setText(paymentItem[position].due_date!!.monthYear)
         }
//            paymentItem[position].due_date?.day?.let { holder.payment_day.setText(it) }
//            paymentItem[position].due_date?.monthYear?.let { holder.payment_month_year.setText(it) }

//            paymentItem[position].due_date.let {
//                it.day.let {
//                    holder.payment_day.setText(it)
//                }
//            }
//
//            paymentItem[position].due_date.let {
//                it.monthYear.let {
//                    holder.payment_month_year.setText(it)
//                }
//            }

        }

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view: View = itemView

        val payment_date_title = itemView.findViewById(R.id.tv_payment_date_title) as TextView
        val payment_day = itemView.findViewById(R.id.tv_payment_day) as TextView
        val payment_month_year: TextView = itemView.findViewById(R.id.tv_payment_month_year)
        val payment_modified_date: TextView = itemView.findViewById(R.id.tv_payment_modified_date)
        val payment_amount: TextView = itemView.findViewById(R.id.tv_payment_amount)
        val payment_pay: TextView = itemView.findViewById(R.id.tv_payment_pay)
        val payment_invoice_no: TextView = itemView.findViewById(R.id.tv_invoice_no)
        val tv_pay : TextView=itemView.findViewById(R.id.tv_pay)
        //
//       val tv_card_cornor_bg=itemView.findViewById(R.id.tv_card_cornor_bg) as TextView
//       val tv_payment_date=itemView.findViewById(R.id.tv_payment_date) as TextView
//       val payment_date=itemView.findViewById(R.id.tv_date) as TextView
////       val payment_amount=itemView.findViewById(R.id.tv_payment_amount) as TextView
//       val payment_invoice_id=itemView.findViewById(R.id.tv_invoice_id) as TextView
//
//
        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(paymentItem[adapterPosition])
            }
        }

    }

    fun setData(paymentlist: ArrayList<ResponsePayment>?) {
        this.paymentItem.clear()
        if (paymentlist != null) {
            this.paymentItem.addAll(paymentlist)
//            notifyItemRangeChanged(0, paymentlist?.size!!)
            notifyDataSetChanged()
        }

    }


}