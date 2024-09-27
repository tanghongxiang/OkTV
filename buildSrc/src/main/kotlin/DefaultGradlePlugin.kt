import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Action
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.exclude
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.project
import org.gradle.kotlin.dsl.provideDelegate
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.jetbrains.kotlin.gradle.plugin.KaptExtension
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

/**
 * @Description:默认的配置实现，支持 library 和 application 级别，根据子组件的类型自动判断
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2024/4/23 3:28 PM
 */
open class DefaultGradlePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        setProjectConfig(project)
        setConfigurations(project)
    }

    //项目配置
    private fun setProjectConfig(project: Project) {
        val isApplicationModule = project.plugins.hasPlugin("com.android.application")

        if (isApplicationModule) {
            // 处理 com.android.application 模块逻辑
            println("===> Handle Project Config by [com.android.application] Logic")
            setProjectConfigByApplication(project)
        } else {
            // 处理 com.android.library 模块逻辑
            println("===> Handle Project Config by [com.android.library] Logic")
            setProjectConfigByLibrary(project)
        }
    }

    //指定依赖版本
    private fun setConfigurations(project: Project) {
        //配置ARouter的Kapt配置
        project.configureKapt()

        project.configurations.all {
//            resolutionStrategy.force("androidx.transition:transition:1.4.1")
            resolutionStrategy.force("org.jetbrains.kotlin:kotlin-stdlib:1.8.10")
            resolutionStrategy.force("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.8.10")
            resolutionStrategy.force("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.10")
            resolutionStrategy.force("org.jetbrains.kotlin:kotlin-reflect:1.8.10")
//            resolutionStrategy.force("org.jetbrains:annotations:23.0.0")
            resolutionStrategy.force("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.7.1")
            resolutionStrategy.force("com.google.code.gson:gson:2.10.1")
            resolutionStrategy.force("com.squareup:javapoet:1.13.0")
            resolutionStrategy.force("com.squareup:javawriter:2.5.0")
            exclude("com.intellij","annotations")
        }
    }

    //设置 library 的相关配置
    private fun setProjectConfigByLibrary(project: Project) {
        //添加插件
        project.apply {
            plugin("kotlin-android")
            plugin("kotlin-kapt")
            plugin("org.jetbrains.kotlin.android")
//            plugin("dagger.hilt.android.plugin")
        }

        project.library().apply {

            compileSdk = ProjectConfig.compileSdk

            defaultConfig {
                minSdk = ProjectConfig.minSdk
                testInstrumentationRunner = ProjectConfig.testInstrumentationRunner
                vectorDrawables {
                    useSupportLibrary = true
                }
                ndk {
                    // 常用构建目标 'x86_64','armeabi-v7a','arm64-v8a'
                    // 'armeabi', 'armeabi-v7a', 'arm64-v8a'
//                    abiFilters.addAll(arrayListOf("armeabi-v7a", "arm64-v8a"))
                    abiFilters.addAll(arrayListOf("x86", "armeabi", "armeabi-v7a","arm64-v8a"))
                }
                multiDexEnabled = true
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }

            kotlinOptions {
                jvmTarget = "17"
            }

            buildFeatures {
                buildConfig = true
                viewBinding = true
                dataBinding = true
                compose = true
            }

            packaging {
                resources {
                    excludes += "/META-INF/{AL2.0,LGPL2.1}"
                }
                exclude("META-INF/gradle/incremental.annotation.processors")
                exclude("META-INF/library_release.kotlin_module")
                exclude("META-INF/services/")
            }

            composeOptions {
                kotlinCompilerExtensionVersion = "1.4.3"
//                kotlinCompilerVersion = "1.8.10"
            }

            sourceSets {
                getByName("main") {
                    jniLibs.srcDirs("libs")
                }
            }

        }

        //默认 library 的依赖
        project.dependencies {
            // 网络&路由框架
            netFrame()
            hilt()
            // 路由
            router()
            // 测试库
            test()
            // AndroidX
            appcompat()
            // lifecycle
            lifecycle()
            // kotlin库&协程
            kotlin()
            // 系统布局控件
            widgetLayout()
            // compose库
            compose()
            // 图片加载
            glide()
            // MMKV数据存储
            mmkv()
            // 网络
            retrofit()
            // 图片选择
            imageSelector()
            // 弹框
            xpopup()
            // 下拉刷新
            refresh()
            // room数据库
            room()
            // camera
            camera()
            // 视频播放
            exoplayer()
            // IjkPlayer
            ijkPlayer()
            // Rxjava
            rxJava()
            // 友盟
            umengSDK()
            // 项目用到的三方SDK
            projectSDKBundle()
            // 其他常用组件
            others()

            //依赖 资源文件库
            if(project.name != "resourceLib"){
                implementation(project(":resourceLib"))
            }
        }

    }

    //设置 application 的相关配置
    private fun setProjectConfigByApplication(project: Project) {
        //添加插件
        project.apply {
            plugin("kotlin-android")
            plugin("kotlin-kapt")
            plugin("org.jetbrains.kotlin.android")
            plugin("com.alibaba.arouter")
        }

        project.application().apply {
            compileSdk = ProjectConfig.compileSdk

            defaultConfig {
                minSdk = ProjectConfig.minSdk
                targetSdk = ProjectConfig.targetSdk
                versionCode = ProjectConfig.versionCode
                versionName = ProjectConfig.versionName
                testInstrumentationRunner = ProjectConfig.testInstrumentationRunner
                vectorDrawables {
                    useSupportLibrary = true
                }
                ndk {
                    //常用构建目标 'x86_64','armeabi-v7a','arm64-v8a'
//                    abiFilters.addAll(arrayListOf("armeabi-v7a", "arm64-v8a"))
                    abiFilters.addAll(arrayListOf("x86", "armeabi", "armeabi-v7a","arm64-v8a"))
                }
                multiDexEnabled = true
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }

            // 设置 Kotlin JVM 目标版本
            kotlinOptions {
                jvmTarget = "17"
            }

            buildFeatures {
                buildConfig = true
                viewBinding = true
                dataBinding = true
                compose = true
            }

            packaging {
                resources {
                    excludes += "/META-INF/{AL2.0,LGPL2.1}"
                }
                exclude("META-INF/gradle/incremental.annotation.processors")
                exclude("META-INF/library_release.kotlin_module")
                exclude("META-INF/services/")
            }

            composeOptions {
                kotlinCompilerExtensionVersion = "1.4.3"
//                kotlinCompilerVersion = "1.8.10"
            }

            sourceSets {
                getByName("main") {
                    jniLibs.srcDirs("libs")
                }
            }

            signingConfigs {
                create("release") {
                    keyAlias = SigningConfigs.key_alias
                    keyPassword = SigningConfigs.key_password
                    storeFile = project.rootDir.resolve(SigningConfigs.store_file)
                    storePassword = SigningConfigs.store_password
                    enableV1Signing = true
                    enableV2Signing = true
                    enableV3Signing = true
                    enableV4Signing = true
                }
                getByName("debug") {
                    keyAlias = SigningConfigs.key_alias
                    keyPassword = SigningConfigs.key_password
                    storeFile = project.rootDir.resolve(SigningConfigs.store_file)
                    storePassword = SigningConfigs.store_password
                    enableV1Signing = true
                    enableV2Signing = true
                    enableV3Signing = true
                    enableV4Signing = true
                }
            }

            buildTypes {
                release {
                    isDebuggable = false    //是否可调试
                    isMinifyEnabled = false  //是否启用混淆
                    isShrinkResources = false   //是否移除无用的resource文件
                    isJniDebuggable = false // 是否打开jniDebuggable开关

//                    proguardFiles(
//                        getDefaultProguardFile("proguard-android-optimize.txt"),
//                        "proguard-rules.pro"
//                    )
                    signingConfig = signingConfigs.findByName("release")
                }
                debug {
                    isDebuggable = true
                    isMinifyEnabled = false
                    isShrinkResources = false
                    isJniDebuggable = true
                    signingConfig = signingConfigs.findByName("debug")
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro"
                    )
                }
            }

            applicationVariants.all(
                object : Action<ApplicationVariant> {
                    override fun execute(variant: com.android.build.gradle.api.ApplicationVariant) {
                        println("variant: $variant")
                        variant.outputs.all(
                            object : Action<com.android.build.gradle.api.BaseVariantOutput> {
                                override fun execute(
                                    output: com.android.build.gradle.api.BaseVariantOutput
                                ) {
                                    val outputImpl = output as com.android.build.gradle.internal.api.BaseVariantOutputImpl
                                    val dataStr = SimpleDateFormat("yyyyMMdd_HHmm").format(Date())
                                    val fileName = "Amber_v${variant.versionName}_$dataStr.apk"
                                    outputImpl.outputFileName = fileName
                                }
                            }
                        )
                    }
                }
            )

            // 打release包的时候如果报错了 打开下面的注释可以解决多数情况下的问题 但是不建议这么做 最好根据报错内容去解决问题
//            lintOptions {
//                //true--所有正式版构建执行规则生成崩溃的lint检查，如果有崩溃问题将停止构建
//                checkReleaseBuilds false
//                //lint 错误发生后停止gradle构建
//                abortOnError false
//                // 防止在发布的时候出现因MissingTranslation导致Build Failed!
//                disable 'MissingTranslation'
//                disable 'InvalidPackage'
//                disable "ResourceType"
//            }
            // 麻蛋 极光推送有个权限问题(PROCESS_PUSH_MSG)lint一只报错，临时打开。。
            lintOptions {
                //true--所有正式版构建执行规则生成崩溃的lint检查，如果有崩溃问题将停止构建
                isCheckReleaseBuilds = false
                //lint 错误发生后停止gradle构建
                isAbortOnError = false
            }
        }

        //默认 application 的依赖
        project.dependencies {
            // 网络&路由框架
            netFrame()
            hilt()
            // 路由
            router()
            // 测试库
            test()
            // AndroidX
            appcompat()
            // lifecycle
            lifecycle()
            // kotlin库&协程
            kotlin()
            // 系统布局控件
            widgetLayout()
            // compose库
            compose()
            // 图片加载
            glide()
            // MMKV数据存储
            mmkv()
            // 网络
            retrofit()
            // 图片选择
            imageSelector()
            // 弹框
            xpopup()
            // 下拉刷新
            refresh()
            // room数据库
            room()
            // camera
            camera()
            // 视频播放
            exoplayer()
            // IjkPlayer
            ijkPlayer()
            // Rxjava
            rxJava()
            // 友盟
            umengSDK()
            // 项目用到的三方SDK
            projectSDKBundle()
            // 其他常用组件
            others()

            //依赖 资源文件库
            if(project.name != "resourceLib"){
                implementation(project(":resourceLib"))
            }

        }

    }

    //根据组件模块的类型给出不同的对象去配置
    private fun Project.library(): LibraryExtension {
        return extensions.getByType(LibraryExtension::class.java)
    }

    private fun Project.application(): BaseAppModuleExtension {
        return extensions.getByType(BaseAppModuleExtension::class.java)
    }

    // Application 级别 - 扩展函数来设置 KotlinOptions
    private fun BaseAppModuleExtension.kotlinOptions(action: KotlinJvmOptions.() -> Unit) {
        (this as org.gradle.api.plugins.ExtensionAware).extensions.configure(
            "kotlinOptions",
            action
        )
    }

    // Library 级别 - 扩展函数来设置 KotlinOptions
    private fun LibraryExtension.kotlinOptions(action: KotlinJvmOptions.() -> Unit) {
        (this as org.gradle.api.plugins.ExtensionAware).extensions.configure(
            "kotlinOptions",
            action
        )
    }

    //配置 Project 的 kapt
    private fun Project.configureKapt() {
        this.extensions.findByType(KaptExtension::class.java)?.apply {
            arguments {
                arg("AROUTER_MODULE_NAME", name)
                arg("logicRouterClsName", "${name}Apt")
            }
        }
    }

    //Library模块是否需要依赖底层 Service 服务，一般子 Module 模块或者 Module-api 模块会依赖到
    protected open fun isLibraryNeedService(): Boolean = false

}