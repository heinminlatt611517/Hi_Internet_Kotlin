package com.hti.hiinternet.adapter

import android.content.Context
import android.graphics.Color
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hti.hiinternet.R
import com.hti.hiinternet.data.OurAppModel
import com.hti.hiinternet.data.PromotionModel
import com.hti.hiinternet.data.repo.PreFerenceRepo
import com.hti.hiinternet.util.ScreenUtil
import kotlinx.android.synthetic.main.layout_our_app.view.*


class AdapterOurApplication(val dataList: ArrayList<OurAppModel>, val context: Context) :

    RecyclerView.Adapter<AdapterOurApplication.viewHolder>() {
    var onItemClick: ((OurAppModel) -> Unit)? = null
    inner class viewHolder(v: View) : RecyclerView.ViewHolder(v) {
       init {
            itemView.setOnClickListener {
                onItemClick?.invoke(dataList[adapterPosition])

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {

        var view =
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_our_app,
                parent,
                false
            )

        return viewHolder(getRatioView(parent, view));
    }

    private fun getRatioView(parent: ViewGroup, view: View): View {
        val width: Int = parent.getMeasuredWidth() / 3
        val params = view.layoutParams
        val dpWidth = ScreenUtil.convertPixelsToDp(width.toFloat(), context)
        val dpHeight = (dpWidth / 5) * 8
        val pxHeight = ScreenUtil.convertDpToPixel(dpHeight.toFloat(), context)

        params.height = Math.round(pxHeight)
        view.layoutParams = params
        return view
    }


    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(holder: viewHolder, position: Int) {

        holder.itemView.tvappDec.setTextColor(Color.parseColor(dataList[position].description_text_color))
        holder.itemView.tvAppName.setTextColor(Color.parseColor(dataList[position].title_text_color))
        holder.itemView.container.setBackgroundColor(Color.parseColor(dataList[position].background_color))

        if (PreFerenceRepo.lang.equals("0")){
            Glide.with(context)
                .load(dataList[position].image_v1)
                .into(holder.itemView.ivOurApp)
            holder.itemView.tvAppName.text = dataList[position].title
            holder.itemView.tvappDec.text = dataList[position].description
        }
        else if (PreFerenceRepo.lang.equals("1")){
            Glide.with(context)
                .load(dataList[position].image_v1)
                .into(holder.itemView.ivOurApp)
            holder.itemView.tvAppName.text = dataList[position].title_mm
            holder.itemView.tvappDec.text = dataList[position].description_mm
        }


    }

    fun setData(list: ArrayList<OurAppModel>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }


}

