plugins {
    id("com.android.application")
}

apply<DefaultGradlePlugin>()

android {
    namespace = ProjectConfig.applicationId

    defaultConfig {
        applicationId = ProjectConfig.applicationId

    }
    resourcePrefix = "app_"
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.aar"))))
    implementation(project(":resourceLib"))

    implementation("androidx.tv:tv-foundation:1.0.0-alpha10")
    implementation("androidx.tv:tv-material:1.0.0-alpha10")

    val leanback_version = "1.2.0-alpha04"
    implementation("androidx.leanback:leanback:$leanback_version")
    // leanback-preference is an add-on that provides a settings UI for TV apps.
    implementation("androidx.leanback:leanback-preference:$leanback_version")
    // leanback-paging is an add-on that simplifies adding paging support to a RecyclerView Adapter.
    implementation("androidx.leanback:leanback-paging:1.1.0-alpha11")
    // leanback-tab is an add-on that provides customized TabLayout to be used as the top navigation bar.
    implementation("androidx.leanback:leanback-tab:1.1.0-beta01")

    val nav_version = "2.7.7"
    implementation("androidx.navigation:navigation-compose:$nav_version")
}