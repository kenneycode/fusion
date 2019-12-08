package io.github.kenneycode.fusion.demo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.github.kenneycode.fusion.demo.R
import io.github.kenneycode.fusion.demo.Util

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
 * 基本用法
 *
 */

class SampleBasicUsage : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sample_fusion_gl_texture_view, container,  false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // 创建图片输入源
        val image = FusionImageSource(Util.decodeBitmapFromAssets("test.png")!!)

        // 创建一个简单渲染器
        val simpleRenderer = SimpleRenderer().apply {
            setFlip(false, true)
        }

        // 创建RenderGraph
        val renderGraph = RenderGraph(simpleRenderer)

        // 设置RenderGraph的输出目标
        renderGraph.addOutputTarget(simpleRenderer, view.findViewById<FusionGLTextureView>(R.id.fusionGLTextureView))

        // 给输入源设置渲染器
        image.addRenderer(renderGraph)

        // 开始处理
        image.process()

    }

}
