package com.hti.hiinternet.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.hti.hiinternet.R
import com.hti.hiinternet.data.PromotionModel
import com.hti.hiinternet.data.repo.PreFerenceRepo
import com.hti.hiinternet.util.Constant


class PromotionAdapter(promotionItemList: ArrayList<PromotionModel>) :
    RecyclerView.Adapter<PromotionAdapter.ViewHolder>() {
    val promotioItem: ArrayList<PromotionModel> = promotionItemList
    lateinit var context: Context

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view: View = itemView
        val ivPromotion = itemView.findViewById(R.id.ivPromotoinImage) as ImageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_promotion, parent, false)
        this.context = parent.context
        return ViewHolder(view)
    }

    fun setData(promotionList: ArrayList<PromotionModel>) {
        this.promotioItem.clear()
        this.promotioItem.addAll(promotionList)
        Log.d("homeFragment", "bind data ${promotionList.size}")
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = promotioItem.size
    override fun onBindViewHolder(holder: PromotionAdapter.ViewHolder, position: Int) {

        if (PreFerenceRepo.lang.equals("0")){
            Glide.with(context)
                .load(promotioItem[position].image)
                .into(holder.ivPromotion)
        }
        else if (PreFerenceRepo.lang.equals("1")){
            Glide.with(context)
                .load(promotioItem[position].image_mm)
                .into(holder.ivPromotion)
        }
    }

}

