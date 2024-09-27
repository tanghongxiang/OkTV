package com.thx.oktv.entity

/**
 * @Description:
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2024/9/27 下午6:38
 */
data class TvProgramListInfo(
    /** 节目分类 */
    var type: String = "",
    /** 节目分类下列表 */
    var programList: ArrayList<ItemProgramInfo> = arrayListOf()
){
    override fun equals(other: Any?): Boolean {
        return other is TvProgramListInfo && this.type == other.type
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + programList.hashCode()
        return result
    }
}

data class ItemProgramInfo(
    /** 节目名称 */
    var programName: String = "",
    /** 节目宣传图 */
    var programImg: String = "",
    /** 节目播放地址 */
    var playUrls: ArrayList<String> = arrayListOf()
){

    override fun equals(other: Any?): Boolean {
        return other is ItemProgramInfo && this.programName == other.programName
    }

    override fun hashCode(): Int {
        var result = programName.hashCode()
        result = 31 * result + programImg.hashCode()
        result = 31 * result + playUrls.hashCode()
        return result
    }
}