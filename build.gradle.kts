buildscript {

    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://s01.oss.sonatype.org/content/groups/public")
        maven("https://developer.huawei.com/repo/")
        maven("https://dl.bintray.com/thelasterstar/maven/")
    }

    dependencies {
        classpath("com.android.tools.build:gradle:8.1.3")
        //其他插件依赖
        classpath(VersionThirdPart.ARouter.plugin)
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.10")
        classpath("com.huawei.agconnect:agcp:1.9.1.301")
    }

}