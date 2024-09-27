package com.thx.oktv.entity

import androidx.compose.runtime.Composable

/**
 * @Description:
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2024/8/27 下午4:30
 */
data class HomeTabAction(
    val title: String,
    val content: @Composable () -> Unit
)