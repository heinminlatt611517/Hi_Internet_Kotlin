package com.hti.hiinternet.adapter

import android.content.ClipData
import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.hti.hiinternet.R
import com.hti.hiinternet.activity_application_guide
import com.hti.hiinternet.data.PromotionModel
import com.hti.hiinternet.data.repo.PreFerenceRepo
import com.hti.hiinternet.data.response.ResponsePayment

import com.smarteist.autoimageslider.SliderViewAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_promotion.view.*

class AdapterTopPromtoin(val list: ArrayList<PromotionModel>, val context: Context) :
    SliderViewAdapter<AdapterTopPromtoin.viewHolder>() {

    var onItemClick: ((String) -> Unit)? = null

    class viewHolder(view: View) : SliderViewAdapter.ViewHolder(view) {

        var ivTopPromotion: ImageView

        init {

            ivTopPromotion = view.findViewById(R.id.ivPromotoinImage)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup?): viewHolder = viewHolder(
        LayoutInflater.from(
            parent!!.context
        ).inflate(R.layout.item_top_promotion, parent, false)


    )


    override fun getCount(): Int = list.size
    override fun onBindViewHolder(viewHolder: viewHolder?, position: Int) {

        if(PreFerenceRepo.lang.equals("1")){
            Glide.with(context)
                .load(list[position].image_mm)

                .into(viewHolder!!.ivTopPromotion)

        }
        else if (PreFerenceRepo.lang.equals("0")){
            Glide.with(context)
                .load(list[position].image)

                .into(viewHolder!!.ivTopPromotion)
        }


        viewHolder?.ivTopPromotion?.setOnClickListener {

           // onItemClick?.invoke(list[position].name)
               if (list[position].name.equals("Slider 3")){
                   context.startActivity(activity_application_guide.newIntent(context))
               }

        }


    }

    fun setData(imageList: ArrayList<PromotionModel>) {

        list.clear()
        list.addAll(imageList)
        notifyDataSetChanged()

    }


}



