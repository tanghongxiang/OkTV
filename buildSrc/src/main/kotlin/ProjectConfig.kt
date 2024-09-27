/**
 * @Description:项目编译配置与AppId配置
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2024/4/23 3:28 PM
 */
object ProjectConfig {
    const val minSdk = 26
    const val compileSdk = 34
    const val targetSdk = 34

    const val versionCode = 1
    const val versionName = "1.0.0"

    const val applicationId = "com.thx.oktv"
    const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

}

//签名文件信息配置
object SigningConfigs {
    //密钥文件路径
    const val store_file = "key/oktv.jks"

    //密钥密码
    const val store_password = "123456"

    //密钥别名
    const val key_alias = "oktv"

    //别名密码
    const val key_password = "123456"
}