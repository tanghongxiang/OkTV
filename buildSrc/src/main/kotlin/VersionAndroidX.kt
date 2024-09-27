/**
 * @Description:AndroidX Support Google 官方的包
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2024/4/23 3:28 PM
 */
object VersionAndroidX {

    //appcompat中默认引入了很多库，比如activity库、fragment库、core库、annotation库、drawerLayout库、appcompat-resources等
    const val appcompat = "androidx.appcompat:appcompat:1.6.1"

    //support兼容库
    const val supportV4 = "androidx.legacy:legacy-support-v4:1.0.0"

    //core包+ktx扩展函数
    const val coreKtx = "androidx.core:core-ktx:1.9.0"

    //activity+ktx扩展函数
    const val activityKtx = "androidx.activity:activity-ktx:1.8.0"

    //fragment+ktx扩展函数
    const val fragmentKtx = "androidx.fragment:fragment-ktx:1.5.1"

    //约束布局
    const val constraintlayout = "androidx.constraintlayout:constraintlayout:2.1.4"

    //卡片控件
    const val cardView = "androidx.cardview:cardview:1.0.0"

    //recyclerView
    const val recyclerView = "androidx.recyclerview:recyclerview:1.2.1"

    //材料设计
    const val material = "com.google.android.material:material:1.11.0"

    //分包
    const val multidex = "androidx.multidex:multidex:2.0.1"

    //文档管理
    const val documentFile = "androidx.documentfile:documentfile:1.0.1"

    // Splash Screen
    const val splashScreen = "androidx.core:core-splashscreen:1.0.0"


    object ViewPager {
        //viewpager
        const val viewpager = "androidx.viewpager:viewpager:1.0.0"

        //viewpager2
        const val viewpager2 = "androidx.viewpager2:viewpager2:1.1.0-beta01"
    }

    object Hilt {
        private const val version = "2.45"

        const val hiltAndroid = "com.google.dagger:hilt-android:$version"
        const val hiltCompiler = "com.google.dagger:hilt-android-compiler:$version"
        const val hiltPlugin = "com.google.dagger:hilt-android-gradle-plugin:$version"

        const val javapoet = "com.squareup:javapoet:1.13.0"
        const val javawriter = "com.squareup:javawriter:2.5.0"

        // compose - Hilt
//        const val navigation_compose = "androidx.hilt:hilt-navigation-compose:1.1.0-alpha01"
    }

    object Lifecycle {
        private const val version = "2.7.0"

        const val livedata = "androidx.lifecycle:lifecycle-livedata:$version"
        const val liveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:$version"

        const val runtime = "androidx.lifecycle:lifecycle-runtime:$version"
        const val runtimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:$version"

        //ViewModel处理
        const val viewModel = "androidx.lifecycle:lifecycle-viewmodel:$version"
        const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
        const val viewModelSavedState = "androidx.lifecycle:lifecycle-viewmodel-savedstate:$version"

        //编译处理器
        const val compiler = "androidx.lifecycle:lifecycle-compiler:$version"
    }


    object Navigation {
        private const val version = "2.6.0-beta01"

        const val fragmentKtx = "androidx.navigation:navigation-fragment-ktx:$version"
        const val uiKtx = "androidx.navigation:navigation-ui-ktx:$version"

        const val safeArgs = "androidx.navigation:navigation-safe-args-generator:$version"

        const val safeArgsGradlePlugin =
            "androidx.navigation:navigation-safe-args-gradle-plugin:$version"

        const val dynamic = "androidx.navigation:navigation-dynamic-features-fragment:$version"
        const val dynamicRuntime =
            "androidx.navigation:navigation-dynamic-features-runtime:$version"


        const val testing = "androidx.navigation:navigation-testing:$version"

    }

    object Work {
        private const val version = "2.8.1"
        const val runtime = "androidx.work:work-runtime:$version"
        const val runtime_ktx = "androidx.work:work-runtime-ktx:$version"
    }

    object Compose {
        private const val composeVersion = "1.4.3"
        private const val composeMaterial3Version = "1.1.1"

        const val composeActivity ="androidx.activity:activity-compose:1.7.0"
        const val composeMaterial = "androidx.compose.material3:material3:$composeMaterial3Version"
        const val composeUi = "androidx.compose.ui:ui:$composeVersion"
        const val composeUiGraphics = "androidx.compose.ui:ui-graphics:$composeVersion"
        const val composeUiTooling = "androidx.compose.ui:ui-tooling:$composeVersion"
        const val composeUiToolingPreview = "androidx.compose.ui:ui-tooling-preview:$composeVersion"
        const val composeRuntime = "androidx.compose.runtime:runtime:$composeVersion"
        const val composePager = "com.google.accompanist:accompanist-pager:0.23.1"
        const val composeSwiperefresh = "com.google.accompanist:accompanist-swiperefresh:0.25.1"

    }

    object Room {
        private const val version = "2.4.2"
        private const val versionKtx = "2.3.0"

        const val runtime = "androidx.room:room-runtime:$version"

        const val compiler = "androidx.room:room-compiler:$version"

        const val rxJava3 = "androidx.room:room-rxjava3:$version"

        const val ktx = "androidx.room:room-ktx:$versionKtx"

    }

    object Camera {
        private const val version = "1.3.0-beta01"

        const val camera2 = "androidx.camera:camera-camera2:$version"

        const val core = "androidx.camera:camera-core:$version"

        const val lifecycle = "androidx.camera:camera-lifecycle:$version"

        const val view = "androidx.camera:camera-view:$version"

        const val video = "androidx.camera:camera-video:$version"

        const val extensions = "androidx.camera:camera-extensions:$version"
    }

}