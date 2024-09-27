package com.thx.resourcelib.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * @Description:
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2022/8/1 11:04 上午
 */
class CommonPageAdapter<T>(fm: FragmentActivity, var fragments: ArrayList<T>) :
    FragmentStateAdapter(fm) where T : Fragment {

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    override fun getItemId(position: Int): Long {
        return fragments[position].hashCode().toLong()
    }

}