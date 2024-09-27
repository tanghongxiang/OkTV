package com.thx.oktv.logic

import android.text.TextUtils
import android.util.Log
import com.thx.commonlibrary.network.BaseNetworkLogic
import com.thx.logicroutermodule.ILogicHandler
import com.thx.logicroutermodule.annotation.Logic
import com.thx.oktv.LogicAlias
import com.thx.oktv.RequestUrls
import com.thx.oktv.entity.ItemProgramInfo
import com.thx.oktv.entity.TvProgramListInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @Description:
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2024/9/27 下午5:20
 */
@Logic(LogicAlias.GET_TV_PROGRAM_LIST)
class GetProgramListLogic : BaseNetworkLogic() {

    override fun requestType() = GET

    override fun host() = RequestUrls.BASE_URL_FOR_TV_PROGRAM

    override fun path() = RequestUrls.GET_TV_PROGRAM_LIST

    override fun onSuccess(p0: String?) {
        val list = p0?.split("\n")
        if (list.isNullOrEmpty()) {
            markResult(ILogicHandler.CODE_FAILURE, "获取节目列表失败！")
        } else {
            GlobalScope.launch(Dispatchers.IO) {
//                val program = hashMapOf<String, HashMap<String, ArrayList<String>>>()
                val program = arrayListOf<TvProgramListInfo>()
                var typeTitle = ""
                var tempItemProgramArr: List<String>
                var tempProgramIndex = -1
                var tempSubProgramIndex = -1
                for (item in list) {
                    if (TextUtils.isEmpty(item) || !item.contains(",")) continue
                    if (item.endsWith(",#genre#")) {
                        typeTitle = item.replace(",#genre#", "")
                        program.add(TvProgramListInfo(typeTitle))
                        continue
                    }
                    tempItemProgramArr = item.split(",")
                    if (tempItemProgramArr.size != 2) continue
                    if (TextUtils.isEmpty(tempItemProgramArr[0]) || TextUtils.isEmpty(
                            tempItemProgramArr[1]
                        )
                    ) continue
                    tempProgramIndex = program.indexOf(TvProgramListInfo(typeTitle))
                    if (tempProgramIndex < 0) {
                        program.add(
                            TvProgramListInfo(
                                typeTitle,
                                arrayListOf(
                                    ItemProgramInfo(
                                        tempItemProgramArr[0],
                                        "",
                                        arrayListOf(tempItemProgramArr[1])
                                    )
                                )
                            )
                        )
                    }else{
                        tempSubProgramIndex = program[tempProgramIndex].programList.indexOf(ItemProgramInfo(tempItemProgramArr[0]))
                        if(tempSubProgramIndex<0){
                            program[tempProgramIndex].programList.add(
                                ItemProgramInfo(
                                    tempItemProgramArr[0],
                                    "",
                                    arrayListOf(tempItemProgramArr[1])
                                )
                            )
                        }else{
                            program[tempProgramIndex].programList[tempSubProgramIndex].playUrls.add(tempItemProgramArr[1])
                        }
                    }
//                    if (null == program[typeTitle]) {
//                        program[typeTitle] =
//                            hashMapOf(tempItemProgramArr[0] to arrayListOf(tempItemProgramArr[1]))
//                    } else {
//                        if (true == program[typeTitle].contains(tempItemProgramArr[0])) {
//                            program[typeTitle].get(tempItemProgramArr[0])
//                                ?.add(tempItemProgramArr[1])
//                        } else {
//                            program[typeTitle].put(
//                                tempItemProgramArr[0],
//                                arrayListOf(tempItemProgramArr[1])
//                            )
//                        }
//                    }
                }
                withContext(Dispatchers.Main) {
                    markResult(ILogicHandler.CODE_SUCCESS, program)
                }
            }
        }
        Log.e("xiang", "===节目单列表：$p0")
    }

}