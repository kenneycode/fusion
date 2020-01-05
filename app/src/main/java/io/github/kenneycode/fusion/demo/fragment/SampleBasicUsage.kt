package io.github.kenneycode.fusion.demo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.github.kenneycode.fusion.demo.R
import io.github.kenneycode.fusion.demo.Util

import io.github.kenneycode.fusion.inputsource.FusionImageSource
import io.github.kenneycode.fusion.process.RenderChain
import io.github.kenneycode.fusion.process.RenderPipeline
import io.github.kenneycode.fusion.renderer.CropRenderer
import io.github.kenneycode.fusion.renderer.ScaleRenderer
import kotlinx.android.synthetic.main.fragment_sample_fusion_gl_texture_view.*

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


        val image = FusionImageSource(Util.decodeBitmapFromAssets("test.png")!!)

        // 创建一个scale renderer
        val scaleRenderer = ScaleRenderer().apply {
            setFlip(false, true)
            scale = 0.8f
        }

        // 创建一个crop renderer
        val cropRenderer = CropRenderer().apply {
            setCropRect(0.1f, 0.9f, 0.8f, 0.2f)
        }

        // 创建RenderChain
        val renderChain = RenderChain(scaleRenderer).apply {
            addNextRenderer(cropRenderer)
        }

        RenderPipeline
                .input(image)
                .renderWith(renderChain, fusionGLTextureView)
                .output(fusionGLTextureView).apply {
                    init()
                    update()
                    render()
                }

    }

}
