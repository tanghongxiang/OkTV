/**
 * @Description:相关依赖以及 Kotlin 的协程相关
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2024/4/23 3:28 PM
 */
object VersionKotlin {
    private var version = "1.8.10"

    var stdlib = "org.jetbrains.kotlin:kotlin-stdlib:$version"
    var reflect = "org.jetbrains.kotlin:kotlin-reflect:$version"
    val stdlibJdk7 = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$version"
    val stdlibJdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"

    //协程
    object Coroutines {
        private const val version = "1.7.1"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
    }

}