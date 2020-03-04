package io.github.kenneycode.fusion.demo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.github.kenneycode.fusion.demo.R
import io.github.kenneycode.fusion.demo.Util
import io.github.kenneycode.fusion.framebuffer.FrameBufferPool
import io.github.kenneycode.fusion.input.FusionVideoDecoder
import io.github.kenneycode.fusion.output.FusionVideoEncoder
import io.github.kenneycode.fusion.process.RenderChain
import io.github.kenneycode.fusion.process.RenderPipeline
import io.github.kenneycode.fusion.program.GLProgramPool
import io.github.kenneycode.fusion.renderer.*
import io.github.kenneycode.fusion.texture.TexturePool
import kotlinx.android.synthetic.main.fragment_sample_offscreen.*

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * 视频离屏渲染
 *
 */

class SampleVideoOffscreenRender : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sample_offscreen, container,  false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        button.setOnClickListener { startSample() }

    }

    private fun startSample() {

        val sourceVideoPath = "/sdcard/test.mp4"
        val outputVideoPath = "/sdcard/test_encoder.mp4"

        // 创建RenderChain并添加一些renderer
        val renderer = RenderChain.create()
                .addRenderer(OES2RGBARenderer())
                .addRenderer(LUTRenderer().apply { setLUTImage(Util.decodeBitmapFromAssets("test_lut.png")!!); setLUTStrength(0.8f) })
                .addRenderer(GaussianBlurRenderer().apply { setBlurRadius(10) })

        // 视频解码器
        val fusionVideoDecoder = FusionVideoDecoder(sourceVideoPath)

        // 创建RenderPipeline，连接输入、渲染器与输出
        val renderPipeline = RenderPipeline
                .input(fusionVideoDecoder)
                .renderWith(renderer)
                .output(FusionVideoEncoder(outputVideoPath))

        fusionVideoDecoder.setCallback(object : FusionVideoDecoder.Callback {

            override fun onStart() {
            }

            override fun onEnd() {
                renderPipeline.release()
                activity?.runOnUiThread { tips.text = "视频已生成! path = $outputVideoPath" }
            }

        })

        // 开始处理
        renderPipeline.start()

    }

    override fun onDestroy() {
        TexturePool.release()
        FrameBufferPool.release()
        GLProgramPool.release()
        super.onDestroy()
    }

}
