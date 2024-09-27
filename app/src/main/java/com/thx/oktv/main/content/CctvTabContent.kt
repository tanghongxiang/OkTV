package com.thx.oktv.main.content

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.itemsIndexed
import androidx.tv.material3.Border
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.CompactCard
import androidx.tv.material3.ExperimentalTvMaterial3Api
import com.thx.oktv.R
import com.thx.oktv.entity.CCTVSubItemInfo
import com.thx.oktv.ext.ifElse

/**
 * @Description:
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2024/8/27 下午4:47
 */


@OptIn(ExperimentalTvMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun CctvTabContent(){
    // 选中的卡片，默认选中第一个
    var selectedCard by remember { mutableStateOf(immersiveListItems.first()) }
    Box(modifier = Modifier.fillMaxSize()) {
        // background image
        Image(
            painter = painterResource(id = selectedCard.image),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        // gradient and text
        Box(
            modifier = Modifier
                .fillMaxSize()
                .immersiveListGradient(),
            contentAlignment = Alignment.TopStart
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(bottom = 25.dp)
//                        .width(480.dp)
                    .scale(0.5f)
                    .wrapContentHeight()
            ) {
                Text(
                    text = selectedCard.subtitle,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.6f)
                )

                Text(
                    text = selectedCard.title,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = selectedCard.description,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.8f)
                )
            }
        }
        val firstChildFr = remember { FocusRequester() }
        TvLazyRow(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = 20.dp)
                .focusRestorer { firstChildFr },
            contentPadding = PaddingValues(start = 25.dp,end = 25.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(immersiveListItems) { index, card ->
                CompactCard(
                    modifier = Modifier
                        .width(40.dp)
                        .aspectRatio(16f / 9)
                        .ifElse(index == 0, Modifier.focusRequester(firstChildFr))
                        .onFocusChanged {
                            if (it.isFocused) {
                                selectedCard = card
                            }
                        },
                    onClick = { firstChildFr.requestFocus() },
                    border = CardDefaults.border(
                        focusedBorder = Border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = androidx.tv.material3.MaterialTheme.colorScheme.border
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                    ),
                    image = {
                        Image(
                            painter = painterResource(id = card.image),
                            contentDescription = "Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop  //ContentScale.FillBounds
                        )
                    },
                    title = {},
                    colors = CardDefaults.colors(containerColor = Color.Transparent)
                )
            }
        }
    }
}

/**
 * 卡片描述
 */
private val description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
        "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
        "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi" +
        " ut aliquip ex ea commodo consequat. "

/**
 * 卡片列表
 */
private val immersiveListItems = listOf(
    CCTVSubItemInfo(
        title = "Shadow Hunter",
        subtitle = "Secondary · text",
        description = description,
        image = R.mipmap.fc_1,
    ),
    CCTVSubItemInfo(
        title = "Super Puppy",
        subtitle = "Secondary · text",
        description = description,
        image = R.mipmap.fc_2,
    ),
    CCTVSubItemInfo(
        title = "Man with a cape",
        subtitle = "Secondary · text",
        description = description,
        image = R.mipmap.fc_3,
    ),
    CCTVSubItemInfo(
        title = "Power Sisters",
        subtitle = "Secondary · text",
        description = description,
        image = R.mipmap.fc_4,
    ),
)

@OptIn(ExperimentalTvMaterial3Api::class)
fun Modifier.immersiveListGradient(): Modifier = composed {
//    val color = androidx.tv.material3.MaterialTheme.colorScheme.surface
    val color = androidx.tv.material3.MaterialTheme.colorScheme.tertiary

    val colorAlphaList = listOf(1.0f, 0.2f, 0.0f)
    val colorStopList = listOf(0.2f, 0.8f, 0.9f)

    val colorAlphaList2 = listOf(1.0f, 0.1f, 0.0f)
    val colorStopList2 = listOf(0.1f, 0.4f, 0.9f)
    this
        .then(
            background(
                brush = Brush.linearGradient(
                    colorStopList[0] to color.copy(alpha = colorAlphaList[0]),
                    colorStopList[1] to color.copy(alpha = colorAlphaList[1]),
                    colorStopList[2] to color.copy(alpha = colorAlphaList[2]),
                    start = Offset(0.0f, 0.0f),
                    end = Offset(Float.POSITIVE_INFINITY, 0.0f)
                )
            )
        )
        .then(
            background(
                brush = Brush.linearGradient(
                    colorStopList2[0] to color.copy(alpha = colorAlphaList2[0]),
                    colorStopList2[1] to color.copy(alpha = colorAlphaList2[1]),
                    colorStopList2[2] to color.copy(alpha = colorAlphaList2[2]),
                    start = Offset(0f, Float.POSITIVE_INFINITY),
                    end = Offset(0f, 0f)
                )
            )
        )
}
