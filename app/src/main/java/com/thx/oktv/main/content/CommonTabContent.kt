package com.thx.oktv.main.content

import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.foundation.lazy.grid.TvGridCells
import androidx.tv.foundation.lazy.grid.TvLazyVerticalGrid
import androidx.tv.foundation.lazy.grid.itemsIndexed
import androidx.tv.material3.Button
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.CardLayoutDefaults
import androidx.tv.material3.ClassicCard
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.StandardCardLayout
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.thx.oktv.entity.CommonSubItemInfo
import com.thx.oktv.entity.ItemProgramInfo
import com.thx.oktv.ext.ifElse
import com.thx.resourcelib.ARouterPath
import com.thx.resourcelib.ext.openNewActivity

/**
 * @Description:
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2024/8/27 下午5:54
 */
@OptIn(
    ExperimentalTvMaterial3Api::class, ExperimentalComposeUiApi::class,
    ExperimentalLayoutApi::class
)
@Composable
fun CommonTabContent(items: ArrayList<ItemProgramInfo>) {
    val firstChildFr = remember { FocusRequester() }
    Column(
        Modifier
            .fillMaxSize()
            .padding(start = 12.dp, top = 0.dp, end = 12.dp, bottom = 12.dp)
    ) {
        TvLazyVerticalGrid(
            columns = TvGridCells.Fixed(4),
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 12.dp)
                .focusRestorer { firstChildFr },
            contentPadding = PaddingValues(vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(items) { index, item ->
                ComponentsGridCard(
                    item,
                    Modifier.ifElse(index == 0, Modifier.focusRequester(firstChildFr))
                ) {
//                    item.programImg
//                    firstChildFr.requestFocus()
                    val bundle = Bundle()
                    bundle.putString("playUrl", item.playUrls[0])// TODO 设置动态URL
                    openNewActivity(ARouterPath.TvPlayerActivity, bundle)
                }
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ComponentsGridCard(
    itemInfo: ItemProgramInfo,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
//    StandardCardLayout(
//        modifier = modifier,//.size(150.dp, 40.dp),
//        imageCard = {interactionSource->
//            CardLayoutDefaults.ImageCard(
//                onClick = onClick,
//                interactionSource = interactionSource,
//            ) {
//                AsyncImage(
//                    model = itemInfo.image,
//                    contentDescription = null,
//                    modifier = Modifier.size(150.dp, 40.dp),
//                    contentScale = ContentScale.Crop
//                )
//            }
//        },
//        title = {
//            Text(
//                text = itemInfo.title,
//                fontSize = 8.sp,
//                color = Color.LightGray,
//                fontFamily = FontFamily.Monospace,
//            )
//        }
//    )
    ClassicCard(
        modifier = modifier
            .height(56.dp)
            .fillMaxWidth(),
        title = {
            Box(
                modifier = Modifier.fillMaxSize().background(Color.DarkGray),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = itemInfo.programName,
                    fontSize = 8.sp,
                    color = Color.White,
                    fontFamily = FontFamily.Monospace
                )
            }
        },
        image = {
            AsyncImage(
                model = itemInfo.programImg,
                contentDescription = null,
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
        },
        onClick = onClick
    )
}