package com.thx.oktv.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.thx.oktv.entity.CommonSubItemInfo
import com.thx.oktv.main.content.CctvTabContent
import com.thx.oktv.main.content.CommonTabContent

/**
 * @Description:
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2024/8/27 下午4:30
 */
@Composable
fun HomeTabController(selectedTabIndex: Int,getTabIndex:(String)->Int,getCommonTabItems:(Int)->ArrayList<CommonSubItemInfo>?) {
    var value = 0
    AnimatedContent(targetState = selectedTabIndex, label = "", modifier = Modifier.fillMaxSize()) {
        value = it

        when (selectedTabIndex) {
            getTabIndex("CCTV") -> {
                CctvTabContent()
            }
            else->{
//                getCommonTabItems(selectedTabIndex)?.let { it1 -> CommonTabContent(it1) }
            }
        }
    }
}
