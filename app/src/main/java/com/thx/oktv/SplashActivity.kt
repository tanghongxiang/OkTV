package com.thx.oktv

import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import com.thx.resourcelib.ARouterPath
import com.thx.resourcelib.base.BaseApplication
import com.thx.resourcelib.ext.openNewActivity
import kotlinx.coroutines.delay
import org.intellij.lang.annotations.Language

/**
 * @Description:
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2024/8/26 下午5:07
 */
class SplashActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            setContent {
                TextRenderEffect()
            }
        } else {
            BaseApplication.getInstance().initThirdPart()
            finish()
            openNewActivity(ARouterPath.HomePageActivity)
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    @Composable
    private fun TextRenderEffect() {
        val animateTextList =
            listOf(
                "欢迎来到OkTV",
                "让广告去死！"
            )

        var index by remember {
            mutableIntStateOf(0)
        }

        var textToDisplay by remember {
            mutableStateOf("")
        }

        val blur = remember { Animatable(0f) }

        LaunchedEffect(textToDisplay) {
            blur.animateTo(30f, tween(easing = LinearEasing))
            blur.animateTo(0f, tween(easing = LinearEasing))
        }

        LaunchedEffect(key1 = animateTextList) {
            animateTextList.forEach { itemText ->
                textToDisplay = itemText
                delay(2000)
            }
            BaseApplication.getInstance().initThirdPart()
            finish()
            openNewActivity(ARouterPath.HomePageActivity)
        }
        ShaderContainer(
            modifier = Modifier.fillMaxSize()
        ) {
            BlurContainer(
                modifier = Modifier.fillMaxSize(),
                blur = blur.value,
                component = {
                    AnimatedContent(
                        targetState = textToDisplay,
                        modifier = Modifier
                            .fillMaxWidth(),
                        transitionSpec = {
                            (scaleIn()).togetherWith(
                                scaleOut()
                            )
                        }, label = ""
                    ) { text ->
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = text,
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            ) {}
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Composable
    fun ShaderContainer(
        modifier: Modifier = Modifier,
        content: @Composable BoxScope.() -> Unit,
    ) {
        val runtimeShader = remember {
            RuntimeShader(Source)
        }
        Box(
            modifier
                .graphicsLayer {
                    runtimeShader.setFloatUniform("visibility", 0.2f)
                    renderEffect = RenderEffect
                        .createRuntimeShaderEffect(
                            runtimeShader, "composable"
                        )
                        .asComposeRenderEffect()
                },
            content = content
        )
    }

    @Language("AGSL")
    val Source = """
        uniform shader composable;
        
        uniform float visibility;
        
        half4 main(float2 cord) {
            half4 color = composable.eval(cord);
            color.a = step(visibility, color.a);
            return color;
        }
    """

    @RequiresApi(Build.VERSION_CODES.S)
    @Composable
    fun BlurContainer(
        modifier: Modifier = Modifier,
        blur: Float = 60f,
        component: @Composable BoxScope.() -> Unit,
        content: @Composable BoxScope.() -> Unit = {},
    ) {
        Box(modifier, contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .customBlur(blur),
                content = component,
            )
            Box(
                contentAlignment = Alignment.Center
            ) {
                content()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun Modifier.customBlur(blur: Float) = this.then(
        graphicsLayer {
            if (blur > 0f)
                renderEffect = RenderEffect
                    .createBlurEffect(
                        blur,
                        blur,
                        Shader.TileMode.DECAL,
                    )
                    .asComposeRenderEffect()
        }
    )

}