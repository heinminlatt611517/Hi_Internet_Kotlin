package com.hti.hiinternet.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.hti.hiinternet.Fragments.ImageFragment
import com.hti.hiinternet.Fragments.VideoFragment
import com.hti.hiinternet.data.PromotionModel
import com.hti.hiinternet.data.repo.PreFerenceRepo


class MultiMediaAdapter(fm: FragmentManager, private val list : ArrayList<PromotionModel>) : FragmentStatePagerAdapter(fm,list.count()) {

    override fun getItem(position: Int): Fragment {

        return when(position){
            0 -> {
                VideoFragment.newInstance(list[position].videoUrl)
            }
            else -> {

                when {
                    PreFerenceRepo.lang.equals("1") -> {
                        ImageFragment.newInstance(list[position].image_mm)
                    }
                    PreFerenceRepo.lang.equals("0") -> {
                        ImageFragment.newInstance(list[position].image)
                    }
                    else -> ImageFragment.newInstance(list[position].image)
                }

            }
        }

    }

    override fun getCount(): Int {
        return list.size
    }



}