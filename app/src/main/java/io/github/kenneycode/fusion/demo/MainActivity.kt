package io.github.kenneycode.fusion.demo

import androidx.appcompat.app.AppCompatActivity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View

import java.io.IOException

import io.github.kenneycode.fusion.inputsource.FusionImageSource
import io.github.kenneycode.fusion.outputtarget.FusionGLTextureView
import io.github.kenneycode.fusion.process.RenderGraph
import io.github.kenneycode.fusion.renderer.SimpleRenderer

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * fusion demo
 *
 */

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 创建图片输入源
        val image = FusionImageSource(decodeBitmapFromAssets("test.png")!!)

        // 创建一个简单渲染器
        val simpleRenderer = SimpleRenderer()

        // 创建RenderGraph
        val renderGraph = RenderGraph(simpleRenderer)
        // 设置RenderGraph的输出目标
        renderGraph.addOutputTarget(simpleRenderer, findViewById<FusionGLTextureView>(R.id.fusionGLTextureView))

        // 给输入源设置渲染器
        image.addRenderer(renderGraph)

        // 开始处理
        image.process()

    }

    private fun decodeBitmapFromAssets(filename: String): Bitmap? {
        val options = BitmapFactory.Options()
        options.inSampleSize = 1
        try {
            return BitmapFactory.decodeStream(assets.open(filename))
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

}
