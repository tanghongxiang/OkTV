package com.thx.oktv.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Tab
import androidx.tv.material3.TabRow
import androidx.tv.material3.Text
import com.thx.oktv.entity.CommonSubItemInfo
import com.thx.oktv.ui.theme.Pink40

/**
 * @Description:
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2024/8/27 下午4:02
 */

private val tabs = listOf("安徽", "湖南", "浙江", "四川", "山东")//"CCTV",
private val subItems = mapOf(
    "安徽" to arrayListOf(
        CommonSubItemInfo(
            title = "安徽卫视",
            image = "https://i2.hdslb.com/bfs/archive/98878051dc3c6dc204fdbd765e6c524a521debde.jpg",
            playUrl = "http://27.10.76.34:8002/udp/225.0.4.133:7980"
        ),
        CommonSubItemInfo(
            title = "经济生活",
            image = "https://i2.hdslb.com/bfs/archive/98878051dc3c6dc204fdbd765e6c524a521debde.jpg",
            playUrl = "http://112.30.194.221:20080/live/eac499adad7b49ff9cfa79ba84693959/hls.m3u8"
        ),
        CommonSubItemInfo(
            title = "综艺体育",
            image = "https://i2.hdslb.com/bfs/archive/98878051dc3c6dc204fdbd765e6c524a521debde.jpg",
            playUrl = "http://112.30.194.221:20080/live/c41f112b83f644ddb082669501c8ecd3/hls.m3u8"
        ),
        CommonSubItemInfo(
            title = "影视频道",
            image = "https://i2.hdslb.com/bfs/archive/98878051dc3c6dc204fdbd765e6c524a521debde.jpg",
            playUrl = "http://112.30.194.221:20080/live/d18ff95cb1fb4bbcb56215e189fc12be/hls.m3u8"
        ),
        CommonSubItemInfo(
            title = "安徽公共",
            image = "https://i2.hdslb.com/bfs/archive/98878051dc3c6dc204fdbd765e6c524a521debde.jpg",
            playUrl = ""
        ),
        CommonSubItemInfo(
            title = "农业·科教",
            image = "https://i2.hdslb.com/bfs/archive/98878051dc3c6dc204fdbd765e6c524a521debde.jpg",
            playUrl = ""
        ),
        CommonSubItemInfo(
            title = "安徽国际",
            image = "https://i2.hdslb.com/bfs/archive/98878051dc3c6dc204fdbd765e6c524a521debde.jpg",
            playUrl = ""
        ),
        CommonSubItemInfo(
            title = "移动电视",
            image = "https://i2.hdslb.com/bfs/archive/98878051dc3c6dc204fdbd765e6c524a521debde.jpg",
            playUrl = ""
        ),
    ),
    "湖南" to arrayListOf(
        CommonSubItemInfo(title = "湖南卫视", image = ""),
        CommonSubItemInfo(title = "金鹰卡通频道", image = ""),
        CommonSubItemInfo(title = "经视综合频道", image = ""),
        CommonSubItemInfo(title = "经视都市频道", image = ""),
        CommonSubItemInfo(title = "经视生活频道", image = ""),
        CommonSubItemInfo(title = "娱乐频道", image = ""),
        CommonSubItemInfo(title = "影视频道", image = ""),
        CommonSubItemInfo(title = "公共频道", image = ""),
        CommonSubItemInfo(title = "时尚频道", image = ""),
        CommonSubItemInfo(title = "海外频道", image = ""),
        CommonSubItemInfo(title = "潇湘影片频道", image = ""),
        CommonSubItemInfo(title = "教育频道", image = ""),
    ),
    "浙江" to arrayListOf(
        CommonSubItemInfo(title = "浙江卫视", image = ""),
        CommonSubItemInfo(title = "钱江都市频道", image = ""),
        CommonSubItemInfo(title = "经济生活频道", image = ""),
        CommonSubItemInfo(title = "教育科技频道", image = ""),
        CommonSubItemInfo(title = "影视娱乐频道", image = ""),
        CommonSubItemInfo(title = "民生休闲频道", image = ""),
        CommonSubItemInfo(title = "公共新闻频道", image = ""),
        CommonSubItemInfo(title = "少儿频道", image = ""),
        CommonSubItemInfo(title = "好易购频道", image = ""),
        CommonSubItemInfo(title = "国际频道", image = ""),
        CommonSubItemInfo(title = "留学世界频道", image = ""),
        CommonSubItemInfo(title = "数码时代频道", image = ""),
    ),
    "四川" to arrayListOf(
        CommonSubItemInfo(title = "四川卫视", image = ""),
        CommonSubItemInfo(title = "经济频道", image = ""),
        CommonSubItemInfo(title = "文化旅游频道", image = ""),
        CommonSubItemInfo(title = "新闻频道", image = ""),
        CommonSubItemInfo(title = "影视文艺频道", image = ""),
        CommonSubItemInfo(title = "星空购物频道", image = ""),
        CommonSubItemInfo(title = "妇女儿童频道", image = ""),
        CommonSubItemInfo(title = "科教频道", image = ""),
        CommonSubItemInfo(title = "公共·乡村频道", image = ""),
        CommonSubItemInfo(title = "峨眉电影频道", image = ""),
        CommonSubItemInfo(title = "康巴卫视", image = ""),
        CommonSubItemInfo(title = "国际频道", image = ""),
        CommonSubItemInfo(title = "星空移动电视", image = ""),
        CommonSubItemInfo(title = "星空城市电视", image = ""),
    ),
    "山东" to arrayListOf(
        CommonSubItemInfo(title = "山东卫视", image = ""),
        CommonSubItemInfo(title = "齐鲁频道", image = ""),
        CommonSubItemInfo(title = "文旅频道", image = ""),
        CommonSubItemInfo(title = "综艺频道", image = ""),
        CommonSubItemInfo(title = "生活频道", image = ""),
        CommonSubItemInfo(title = "农科频道", image = ""),
        CommonSubItemInfo(title = "少儿频道", image = ""),
        CommonSubItemInfo(title = "居家频道", image = ""),
        CommonSubItemInfo(title = "付费频道", image = ""),
        CommonSubItemInfo(title = "国际在线", image = ""),
    )
)

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun PillIndicatorTabRow() {

    fun getTabIndex(text: String): Int {
        return tabs.indexOf(text)
    }

    fun getCommonTabItems(index: Int): ArrayList<CommonSubItemInfo>? {
        return subItems[tabs[index]]
    }

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    Column(
        modifier = Modifier
            .background(Pink40)
            .fillMaxSize()
            .padding(vertical = 18.dp, horizontal = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TabRow(selectedTabIndex = selectedTabIndex, modifier = Modifier.height(28.dp)) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = index == selectedTabIndex,
                    onFocus = { selectedTabIndex = index },
                    modifier = Modifier.height(28.dp)
                ) {
                    Column(
                        modifier = Modifier.height(28.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = tab,
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
        HomeTabController(selectedTabIndex = selectedTabIndex, ::getTabIndex, ::getCommonTabItems)
    }
}

