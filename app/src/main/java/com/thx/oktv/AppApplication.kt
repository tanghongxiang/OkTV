package com.thx.oktv

import com.thx.logicroutermodule.LogicRouter
import com.thx.perfect.logic.appApt
import com.thx.resourcelib.base.BaseApplication

/**
 * @Description:
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2024/8/26 下午6:00
 */
class AppApplication: BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        LogicRouter.registerLogic(appApt.map())
    }

}