package io.github.kenneycode.fusion.demo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.github.kenneycode.fusion.demo.R
import io.github.kenneycode.fusion.util.BitmapUtil
import io.github.kenneycode.fusion.framebuffer.FrameBufferPool
import io.github.kenneycode.fusion.input.FusionImage
import io.github.kenneycode.fusion.output.FusionBitmap
import io.github.kenneycode.fusion.process.RenderChain
import io.github.kenneycode.fusion.process.RenderPipeline
import io.github.kenneycode.fusion.program.GLProgramPool
import io.github.kenneycode.fusion.renderer.CropRenderer
import io.github.kenneycode.fusion.renderer.GaussianBlurRenderer
import io.github.kenneycode.fusion.renderer.LUTRenderer
import io.github.kenneycode.fusion.renderer.ScaleRenderer
import io.github.kenneycode.fusion.texture.TexturePool
import kotlinx.android.synthetic.main.fragment_sample_offscreen.*

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * 离屏渲染
 *
 */

class SampleImageOffscreenRender : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sample_offscreen, container,  false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        button.setOnClickListener { startSample() }

    }

    private fun startSample() {

        val sourceImagePath = "test.png"

        // 创建RenderChain并添加一些renderer
        val renderer = RenderChain()
                .addRenderer(ScaleRenderer().apply { setFlip(false, true); setScale(0.8f) })
                .addRenderer(CropRenderer().apply { setCropRect(0.1f, 0.9f, 0.8f, 0.2f) })
                .addRenderer(LUTRenderer().apply { setLUTImage(BitmapUtil.decodeBitmapFromAssets("test_lut.png")!!); setLUTStrength(0.8f) })
                .addRenderer(GaussianBlurRenderer().apply { setBlurRadius(10) })

        // 输出bitmap
        val output = FusionBitmap()

        // 创建RenderPipeline，连接输入、渲染器与输出
        val renderPipeline = RenderPipeline
                .input(FusionImage(BitmapUtil.decodeBitmapFromAssets(sourceImagePath)!!))
                .renderWith(renderer)
                .output(output)

        // 开始处理
        renderPipeline.start()

        // 等待RenderPipeline执行完成
        renderPipeline.flush()

        activity?.runOnUiThread { tips.text = "bitmap已生成!" }

    }

    override fun onDestroy() {
        TexturePool.release()
        FrameBufferPool.release()
        GLProgramPool.release()
        super.onDestroy()
    }

}
