/**
 * @Description:第三方依赖包
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2024/4/23 3:28 PM
 */
object VersionThirdPart {

    object ARouter {
        private const val version = "1.0.3"
        //  还有其他的fork库
        //  https://github.com/aleyn97/router
        //  https://github.com/jadepeakpoet/ARouter/blob/develop/README_CN.md
        //  https://github.com/wyjsonGo/GoRouter
        //  也可以参照自行修改 https://juejin.cn/post/7222091234100330554 ，https://juejin.cn/post/7280436457135144999
        //core
        const val core = "com.github.jadepeakpoet.ARouter:arouter-api:$version"

        //注解处理
        const val compiler = "com.github.jadepeakpoet.ARouter:arouter-compiler:$version"

        // plugin
        const val plugin = "com.github.jadepeakpoet.ARouter:arouter-register:$version"
    }

    //网络请求 retrofit
    object Retrofit {
        private const val version = "2.9.0"

        //内置了OkHttp 3.14.9 与 okio 3.1.0
        const val core = "com.squareup.retrofit2:retrofit:$version"

        //gson转换器
        const val convertGson = "com.squareup.retrofit2:converter-gson:$version"
        const val moshiConverter = "com.squareup.retrofit2:converter-moshi:$version"

        //指定Gson版本
        const val gson = "com.google.code.gson:gson:2.10.1"
    }

    //图片加载框架 https://muyangmin.github.io/glide-docs-cn/
    object Glide {
        private const val version = "4.11.0"

        const val core = "com.github.bumptech.glide:glide:$version"
        const val annotation = "com.github.bumptech.glide:annotations:$version"
        const val integration = "com.github.bumptech.glide:okhttp3-integration:$version"
        const val compiler = "com.github.bumptech.glide:compiler:$version"
        const val glideTransformations = "jp.wasabeef:glide-transformations:4.3.0"
    }

    //多媒体-相册选择 https://github.com/LuckSiege/PictureSelector
    object ImageSelector {
//        private const val version = "v3.11.2"
        private const val version = "v3.10.5"
        private const val versionUcrop = "v3.10.4"

        const val core = "io.github.lucksiege:pictureselector:$version"
//        const val core = "com.github.tanghongxiang:PhotoSelector:$version"
        const val compress = "io.github.lucksiege:compress:$versionUcrop"
        const val ucrop = "io.github.lucksiege:ucrop:$versionUcrop"
    }

    //第三方 dialog 项目地址：https://github.com/li-xiaojun/XPopup
    object XPopup {
        private const val version = "2.9.4"
        private const val picker_version = "1.0.1"

        //弹窗
        const val core = "com.github.li-xiaojun:XPopup:$version"

        //弹窗+PickerView封装
        const val picker = "com.github.li-xiaojun:XPopupExt:$picker_version"

        //简易RV-Adapter
        const val easyAdapter = "com.github.li-xiaojun:EasyAdapter:1.2.8"
    }

    //第三方刷新布局 https://github.com/scwang90/SmartRefreshLayout
    object SmartRefresh {
        private const val version = "2.1.0"

        //刷新布局
        const val core = "io.github.scwang90:refresh-layout-kernel:$version"

        //经典刷新头布局
        const val classicsHeader = "io.github.scwang90:refresh-header-classics:$version"
    }

    //视频播放器
    object ExoPlayer {
        private const val media3_version = "1.4.0-rc01"
        const val exoplayer = "androidx.media3:media3-exoplayer:$media3_version"
        // 格式支持
        const val dash = "androidx.media3:media3-exoplayer-dash:$media3_version"
        const val hls = "androidx.media3:media3-exoplayer-hls:$media3_version"
        const val rtsp = "androidx.media3:media3-exoplayer-rtsp:$media3_version"
        const val rtmp = "androidx.media3:media3-datasource-rtmp:1.4.1"
        const val smoothStreaming = "androidx.media3:media3-exoplayer-smoothstreaming:$media3_version"
        // UI
        const val ui = "androidx.media3:media3-ui:$media3_version"
        // 用于公开和控制媒体会话
        const val session = "androidx.media3:media3-session:$media3_version"
        // 用于混合媒体文件
        const val muxer = "androidx.media3:media3-muxer:$media3_version"
        // 用于转换媒体文件
        const val transformer = "androidx.media3:media3-transformer:$media3_version"
    }

    // IJK Player
    object IjkPlayer {
        val ijkPlayerJava = "com.github.CarGuo.GSYVideoPlayer:gsyVideoPlayer-java:v8.3.5-release-jitpack"
    }

    // RxJava
    object RxJava {
        val rxJava = "io.reactivex.rxjava3:rxandroid:3.0.2"
        val rxAndroid = "io.reactivex.rxjava3:rxjava:3.1.8"
    }

    // 用到的三方SDK
    object ProjectSDKBundle {
        // 微信SDK
        val wechatSDK = "com.tencent.mm.opensdk:wechat-sdk-android-without-mta:6.8.0"
        // 腾讯云
        val tencentIM = "com.tencent.imsdk:imsdk-plus:7.0.3754"
        // 抖音
        val tikTokOpenSDK = "com.bytedance.ies.ugc.aweme:opensdk-china-external:0.1.9.0"
        val tikTokCommonSDK = "com.bytedance.ies.ugc.aweme:opensdk-common:0.1.9.0"
        // 微博SDK
        val weiboSDK = "io.github.sinaweibosdk:core:13.10.2@aar"
        // 哔哩哔哩SDK
//        val bilibiliSDK = "com.github.tanghongxiang:ResourceAARLibs:1.1.0"
//        val bilibiliSDK = "com.tanghx:bilibili:1.1.0@aar"

        // 极光推送版本
        private const val jPushVersion = "5.0.0"
        // 极光SDK
        val jpush = "cn.jiguang.sdk:jpush:$jPushVersion"
        // 极光一键登录
        val jverification = "cn.jiguang.sdk:jverification:3.1.7"
        // 接入小米厂商
        val xiaomi = "cn.jiguang.sdk.plugin:xiaomi:$jPushVersion"
        // 接入华为厂商
        val huaweiHms = "com.huawei.hms:push:6.5.0.300"
        val huawei = "cn.jiguang.sdk.plugin:huawei:$jPushVersion"
        // 接入 OPPO 厂商
//        val oppoCodec = "commons-codec:commons-codec:1.15"
        val oppo = "cn.jiguang.sdk.plugin:oppo:$jPushVersion"
        // 接入 VIVO 厂商
        val vivo = "cn.jiguang.sdk.plugin:vivo:$jPushVersion"
        // 接入魅族厂商
        val meizu = "cn.jiguang.sdk.plugin:meizu:$jPushVersion"
        // 接入荣耀厂商
        val honor = "cn.jiguang.sdk.plugin:honor:$jPushVersion"
    }

    //气泡吐司  https://github.com/getActivity/Toaster
    const val toaster = "com.github.getActivity:Toaster:12.6"

    //权限申请  https://github.com/getActivity/XXPermissions
    const val xxPermission = "com.github.getActivity:XXPermissions:18.63"

    //Gson 解析容错  https://github.com/getActivity/GsonFactory
    const val gsonFactory = "com.github.getActivity:GsonFactory:9.5"

    //第三方轮播图  https://github.com/youth5201314/banner
    const val banner = "io.github.youth5201314:banner:2.2.3"

    //RecycleView适配器工具  https://github.com/CymChad/BaseRecyclerViewAdapterHelper
    const val baseRecycleViewHelper = "com.github.CymChad:BaseRecyclerViewAdapterHelper:3.0.7"

    //LiveBus - 消息总线  https://github.com/JeremyLiao/LiveEventBus
//    const val liveEventBus = "io.github.jeremyliao:live-event-bus-x:1.8.0"
    const val liveEventBus = "com.github.neo-turak:LiveEventBus:1.8.1"

    // 数据存储 MMKV https://github.com/Tencent/MMKV
    const val mmkv = "com.tencent:mmkv:1.3.5"

    //lottie 动画 https://github.com/airbnb/lottie-android
    // 你可以使用转换工具将mp4转换成json【https://isotropic.co/video-to-lottie/】
    // 去这里下载素材【https://lottiefiles.com/】
    const val lottie = "com.airbnb.android:lottie:6.3.0"

    //内存泄露检测工具
    const val leakcanary = "com.squareup.leakcanary:leakcanary-android:2.14"

    // Coil Android 图片加载库，通过 Kotlin 协程的方式加载图片（支持Compose） https://github.com/coil-kt/coil/blob/main/README-zh.md
    object CoilImageLoad {
        private const val version = "2.6.0"

        const val imageCoil = "io.coil-kt:coil:2.6.0"
        const val composeImageCoil = "io.coil-kt:coil-compose:2.6.0"
    }

    // 网络&组件路由框架
    const val netAndRouterFrame = "com.github.tanghongxiang:RouterLibs:1.0.15"

    // 网络请求日志拦截器
    const val okhttpLoggingInterceptor = "com.squareup.okhttp3:logging-interceptor:4.12.0"

    // 屏幕适配
    const val autoSize = "com.github.JessYanCoding:AndroidAutoSize:v1.2.1"

    // 圆形图片View
    const val circleImageView = "de.hdodenhof:circleimageview:3.0.1"

    // 大图片展示View
    const val bigImgShowView = "com.davemorrissey.labs:subsampling-scale-image-view-androidx:3.10.0"

    // PhotoView
    const val photoView = "com.github.chrisbanes:PhotoView:2.3.0"

    // EventBus
    const val eventBus = "org.greenrobot:eventbus:3.1.1"

    // JSBridge
    const val jsBridge = "com.github.wendux:DSBridge-Android:3.0.0"

    // ZXing二维码
    const val zxing = "com.github.bingoogolapple.BGAQRCode-Android:zxing:1.3.8"

    // 饺子视频播放---老项目移植
    const val jzvdVideo = "cn.jzvd:jiaozivideoplayer:7.7.0"

    // FastJson
    const val fastJson = "com.alibaba:fastjson:1.2.61"

    // TabLayout
    const val tabLayout = "com.github.fanmingyi:FlycoTabLayout2:v1.1.2"

    // Indicator
    const val magicIndicator = "com.github.hackware1993:MagicIndicator:1.7.0"

    // 仿iOS开关按钮（兼容阿拉伯UI布局）
    const val arSwitchButton = "com.github.tanghongxiang:ArSwitchButton:v1.0.0"

    // 鲁班压缩
//    const val luBanZip = "com.github.Curzibn:Luban:1.1.8"

    // 折叠布局
    const val expandLayout = "com.github.angcyo:DslAdapter:6.0.3"

    // 友盟
    object UmengSDK {
        const val common = "com.umeng.umsdk:common:9.4.7"
        const val asms = "com.umeng.umsdk:asms:1.4.0"
        const val apm = "com.umeng.umsdk:apm:1.9.8"
    }
}