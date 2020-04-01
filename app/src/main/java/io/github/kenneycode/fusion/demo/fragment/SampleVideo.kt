package io.github.kenneycode.fusion.demo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.github.kenneycode.fusion.demo.R
import io.github.kenneycode.fusion.util.BitmapUtil
import io.github.kenneycode.fusion.framebuffer.FrameBufferPool

import io.github.kenneycode.fusion.input.FusionVideo
import io.github.kenneycode.fusion.process.RenderChain
import io.github.kenneycode.fusion.process.RenderPipeline
import io.github.kenneycode.fusion.program.GLProgramPool
import io.github.kenneycode.fusion.renderer.*
import io.github.kenneycode.fusion.texture.TexturePool
import kotlinx.android.synthetic.main.fragment_sample_fusion_gl_texture_view.*

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * 视频渲染demo
 *
 */

class SampleVideo : Fragment() {

    private lateinit var renderPipeline: RenderPipeline

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sample_fusion_gl_texture_view, container,  false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // 创建RenderChain并添加一些renderer
        val renderer = RenderChain()
                .addRenderer(OES2RGBARenderer())
                .addRenderer(LUTRenderer().apply { setLUTImage(BitmapUtil.decodeBitmapFromAssets("test_lut.png")!!); setLUTStrength(0.8f) })
                .addRenderer(GaussianBlurRenderer().apply { setBlurRadius(10) })

        // 创建RenderPipeline，连接输入、渲染器与输出
        renderPipeline = RenderPipeline
                .input(FusionVideo("/sdcard/test.mp4"))
                .renderWith(renderer)
                .useContext(fusionView)
                .output(fusionView)

        // 开始处理
        renderPipeline.start()

    }

    override fun onDestroy() {
        TexturePool.release()
        FrameBufferPool.release()
        GLProgramPool.release()
        renderPipeline.release()
        super.onDestroy()
    }

}
