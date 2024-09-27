import org.gradle.api.artifacts.dsl.DependencyHandler

/**
 * @Description:通过扩展函数的方式导入功能模块的全部依赖，可以自行随意添加或更改
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2024/4/23 3:28 PM
 */
fun DependencyHandler.appcompat() {
    implementation(VersionAndroidX.appcompat)
    implementation(VersionAndroidX.supportV4)
    implementation(VersionAndroidX.coreKtx)
    implementation(VersionAndroidX.activityKtx)
    implementation(VersionAndroidX.fragmentKtx)
    implementation(VersionAndroidX.multidex)
    implementation(VersionAndroidX.documentFile)
    implementation(VersionAndroidX.splashScreen)
}

//生命周期监听
fun DependencyHandler.lifecycle() {
    implementation(VersionAndroidX.Lifecycle.livedata)
    implementation(VersionAndroidX.Lifecycle.liveDataKtx)
    implementation(VersionAndroidX.Lifecycle.runtime)
    implementation(VersionAndroidX.Lifecycle.runtimeKtx)

    implementation(VersionAndroidX.Lifecycle.viewModel)
    implementation(VersionAndroidX.Lifecycle.viewModelKtx)
    implementation(VersionAndroidX.Lifecycle.viewModelSavedState)

    kapt(VersionAndroidX.Lifecycle.compiler)
}

//Kotlin与协程
fun DependencyHandler.kotlin() {
    implementation(VersionKotlin.stdlib)
    implementation(VersionKotlin.reflect)
    implementation(VersionKotlin.stdlibJdk7)
    implementation(VersionKotlin.stdlibJdk8)

    implementation(VersionKotlin.Coroutines.android)
    implementation(VersionKotlin.Coroutines.core)
}

//依赖注入
fun DependencyHandler.hilt() {
//    implementation(VersionAndroidX.Hilt.hiltAndroid)
//    implementation(VersionAndroidX.Hilt.javapoet)
//    implementation(VersionAndroidX.Hilt.javawriter)
//    kapt(VersionAndroidX.Hilt.hiltCompiler)
}

// 网络框架
fun DependencyHandler.netFrame(){
    implementation(VersionThirdPart.netAndRouterFrame)
    implementation(VersionThirdPart.okhttpLoggingInterceptor)
}

//测试Test依赖
fun DependencyHandler.test() {
    testImplementation(VersionTesting.junit)
    androidTestImplementation(VersionTesting.androidJunit)
    androidTestImplementation(VersionTesting.espresso)
}

//常用的布局控件
fun DependencyHandler.widgetLayout() {
    implementation(VersionAndroidX.constraintlayout)
    implementation(VersionAndroidX.cardView)
    implementation(VersionAndroidX.recyclerView)
    implementation(VersionThirdPart.baseRecycleViewHelper)
    implementation(VersionAndroidX.material)
    implementation(VersionAndroidX.ViewPager.viewpager)
    implementation(VersionAndroidX.ViewPager.viewpager2)
}

//路由
fun DependencyHandler.router() {
    implementation(VersionThirdPart.ARouter.core)
    kapt(VersionThirdPart.ARouter.compiler)
}

//Work任务
fun DependencyHandler.work() {
    implementation(VersionAndroidX.Work.runtime)
    implementation(VersionAndroidX.Work.runtime_ktx)
}

//KV存储
fun DependencyHandler.mmkv() {
    implementation(VersionThirdPart.mmkv)
}

//网络请求
fun DependencyHandler.retrofit() {
    implementation(VersionThirdPart.Retrofit.core)
    implementation(VersionThirdPart.Retrofit.convertGson)
    implementation(VersionThirdPart.Retrofit.gson)
//    implementation(VersionThirdPart.gsonFactory)
}

//图片加载
fun DependencyHandler.glide() {
    implementation(VersionThirdPart.Glide.core)
    implementation(VersionThirdPart.Glide.annotation)
    implementation(VersionThirdPart.Glide.integration)
    implementation(VersionThirdPart.Glide.glideTransformations)
    kapt(VersionThirdPart.Glide.compiler)
}

//多媒体相机相册
fun DependencyHandler.imageSelector() {
//    implementation(VersionThirdPart.ImageSelector.core)
    implementation(VersionThirdPart.ImageSelector.compress)
    implementation(VersionThirdPart.ImageSelector.ucrop)
}

//弹窗
fun DependencyHandler.xpopup() {
    implementation(VersionThirdPart.XPopup.core)
    implementation(VersionThirdPart.XPopup.picker)
    implementation(VersionThirdPart.XPopup.easyAdapter)
}

//下拉刷新
fun DependencyHandler.refresh() {
    implementation(VersionThirdPart.SmartRefresh.core)
    implementation(VersionThirdPart.SmartRefresh.classicsHeader)
}

// compose
fun DependencyHandler.compose() {
    implementation(VersionAndroidX.Compose.composeActivity)
    implementation(VersionAndroidX.Compose.composeMaterial)
    implementation(VersionAndroidX.Compose.composeUi)
    implementation(VersionAndroidX.Compose.composeUiGraphics)
    implementation(VersionAndroidX.Compose.composeUiTooling)
    implementation(VersionAndroidX.Compose.composeUiToolingPreview)
    implementation(VersionAndroidX.Compose.composeRuntime)
    implementation(VersionAndroidX.Compose.composePager)
    implementation(VersionAndroidX.Compose.composeSwiperefresh)
}

// Room
fun DependencyHandler.room() {
    implementation(VersionAndroidX.Room.runtime)
    kapt(VersionAndroidX.Room.compiler)
    implementation(VersionAndroidX.Room.rxJava3)
    implementation(VersionAndroidX.Room.ktx)
}

// Camera
fun DependencyHandler.camera() {
    implementation(VersionAndroidX.Camera.camera2)
    implementation(VersionAndroidX.Camera.core)
    implementation(VersionAndroidX.Camera.lifecycle)
    implementation(VersionAndroidX.Camera.view)
    implementation(VersionAndroidX.Camera.video)
    implementation(VersionAndroidX.Camera.extensions)
}

// ExoPlayer
fun DependencyHandler.exoplayer(){
    implementation(VersionThirdPart.ExoPlayer.exoplayer)
    implementation(VersionThirdPart.ExoPlayer.dash)
    implementation(VersionThirdPart.ExoPlayer.hls)
    implementation(VersionThirdPart.ExoPlayer.rtsp)
    implementation(VersionThirdPart.ExoPlayer.rtmp)
    implementation(VersionThirdPart.ExoPlayer.smoothStreaming)
    implementation(VersionThirdPart.ExoPlayer.ui)
    implementation(VersionThirdPart.ExoPlayer.session)
    implementation(VersionThirdPart.ExoPlayer.muxer)
    implementation(VersionThirdPart.ExoPlayer.transformer)
}

// IjkPlayer
fun DependencyHandler.ijkPlayer(){
    implementation(VersionThirdPart.IjkPlayer.ijkPlayerJava)
}

// RxJava
fun DependencyHandler.rxJava(){
    implementation(VersionThirdPart.RxJava.rxJava)
    implementation(VersionThirdPart.RxJava.rxAndroid)
}

// 友盟统计
fun DependencyHandler.umengSDK(){
    implementation(VersionThirdPart.UmengSDK.common)
    implementation(VersionThirdPart.UmengSDK.asms)
    implementation(VersionThirdPart.UmengSDK.apm)
}

// 用到的三方SDK
fun DependencyHandler.projectSDKBundle(){
    implementation(VersionThirdPart.ProjectSDKBundle.wechatSDK)
 }

// 其他三方常用库
fun DependencyHandler.others(){
    // 气泡吐司
    implementation(VersionThirdPart.toaster)
    // 权限申请
    implementation(VersionThirdPart.xxPermission)
    // 轮播图
    implementation(VersionThirdPart.banner)
    // RecyclerView适配器
    implementation(VersionThirdPart.baseRecycleViewHelper)
    // 消息线程
    implementation(VersionThirdPart.liveEventBus)
    // lottie动画加载
    implementation(VersionThirdPart.lottie)
    // 内存泄漏检测
//    debugImplementation(VersionThirdPart.leakcanary)
    // 图片加载
    implementation(VersionThirdPart.CoilImageLoad.imageCoil)
    implementation(VersionThirdPart.CoilImageLoad.composeImageCoil)
    // 屏幕适配
    implementation(VersionThirdPart.autoSize)
    // 圆形图片
    implementation(VersionThirdPart.circleImageView)
    // 大图片展示View
    implementation(VersionThirdPart.bigImgShowView)
    // PhotoView
    implementation(VersionThirdPart.photoView)
    // EventBus
    implementation(VersionThirdPart.eventBus)
    // jsBridge
    implementation(VersionThirdPart.jsBridge)
    // ZXing
    implementation(VersionThirdPart.zxing)
    // 饺子视频播放
    implementation(VersionThirdPart.jzvdVideo)
    // FastJson
    implementation(VersionThirdPart.fastJson)
    // tablayout
    implementation(VersionThirdPart.tabLayout)
    // Indicator
    implementation(VersionThirdPart.magicIndicator)
    // 仿iOS开关按钮（兼容阿拉伯UI布局）
    implementation(VersionThirdPart.arSwitchButton)
    // 鲁班压缩
//    implementation(VersionThirdPart.luBanZip)
    // 折叠布局
    implementation(VersionThirdPart.expandLayout)



}
