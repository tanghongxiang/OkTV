package com.thx.oktv.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Tab
import androidx.tv.material3.TabRow
import androidx.tv.material3.Text
import com.alibaba.android.arouter.facade.annotation.Route
import com.hjq.toast.Toaster
import com.thx.oktv.entity.CommonSubItemInfo
import com.thx.oktv.main.content.CctvTabContent
import com.thx.oktv.main.content.CommonTabContent
import com.thx.oktv.ui.theme.Pink40
import com.thx.resourcelib.ARouterPath

/**
 * @Description:
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2024/8/26 下午5:35
 */
@Route(path = ARouterPath.HomePageActivity)
class HomePageActivity : ComponentActivity() {

    /** viewModel */
    private var viewModel: TvProgramViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContent { ImmersiveListScreen() }
        viewModel = ViewModelProvider(this)[TvProgramViewModel::class.java]
        viewModel?.errObs()?.observe(this) {
            Toaster.show(it)
        }
        viewModel?.programListObs()?.observe(this) {
            setContent {
                PillIndicatorTabRowLayout()
            }
        }
        viewModel?.getTvProgramList()
    }

    @SuppressLint("UnusedContentLambdaTargetStateParameter")
    @OptIn(ExperimentalTvMaterial3Api::class)
    @Composable
    fun PillIndicatorTabRowLayout() {
        var selectedTabIndex by remember { mutableIntStateOf(0) }
        Column(
            modifier = Modifier
                .background(Pink40)
                .fillMaxSize()
                .padding(vertical = 18.dp, horizontal = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TabRow(selectedTabIndex = selectedTabIndex, modifier = Modifier.height(28.dp)) {
                viewModel?.programListObs()?.value?.forEachIndexed { index, tab ->
                    Tab(
                        selected = index == selectedTabIndex,
                        onFocus = {selectedTabIndex = index},
                        modifier = Modifier.height(28.dp)
                    ) {
                        Column(
                            modifier = Modifier.height(28.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = tab.type,
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
            }
            viewModel?.programListObs()?.value?.let {programInfo->
                AnimatedContent(targetState = selectedTabIndex, label = "", modifier = Modifier.fillMaxSize()) {
                    CommonTabContent(programInfo[selectedTabIndex].programList)
                }
            }
        }
    }


}