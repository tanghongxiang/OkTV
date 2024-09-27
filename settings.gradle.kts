pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://s01.oss.sonatype.org/content/groups/public")
        maven("https://developer.huawei.com/repo/")
        maven("https://artifact.bytedance.com/repository/AwemeOpenSDK")
        maven("https://dl.bintray.com/thelasterstar/maven/")
        maven("https://repo1.maven.org/maven2/")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://s01.oss.sonatype.org/content/groups/public")
        maven("https://developer.huawei.com/repo/")
        maven("https://artifact.bytedance.com/repository/AwemeOpenSDK")
        maven("https://dl.bintray.com/thelasterstar/maven/")
        maven("https://repo1.maven.org/maven2/")
    }
}

rootProject.name = "OkTVAndroid"
include(":app")
include(":resourceLib")