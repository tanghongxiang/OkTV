package com.thx.oktv.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thx.logicroutermodule.LogicRouter
import com.thx.oktv.LogicAlias
import com.thx.oktv.entity.TvProgramListInfo
import com.thx.resourcelib.utils.GsonUtils

/**
 * @Description:
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2024/9/27 下午6:08
 */
class TvProgramViewModel: ViewModel() {

    /** 节目单列表 */
    private var programListObs:MutableLiveData<ArrayList<TvProgramListInfo>> = MutableLiveData()

    /** 错误信息 */
    private var errObs:MutableLiveData<String> = MutableLiveData()


    /**
     * 获取节目单列表
     */
     fun getTvProgramList(){
        LogicRouter.asynExecute(LogicAlias.GET_TV_PROGRAM_LIST){
            GsonUtils.mInstance.toJsonString(it.data)
            if(it.isSuccess){
                programListObs.postValue(it.data as ArrayList<TvProgramListInfo>)
            }else{
                errObs.postValue(it.data?.toString()?:"节目单获取失败！")
            }
        }
    }


    /** 节目单列表 */
    fun programListObs() = programListObs

    /** 错误信息 */
    fun errObs() = errObs
}