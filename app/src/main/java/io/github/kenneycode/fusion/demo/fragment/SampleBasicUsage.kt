package io.github.kenneycode.fusion.demo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.github.kenneycode.fusion.demo.R
import io.github.kenneycode.fusion.demo.Util
import io.github.kenneycode.fusion.framebuffer.FrameBufferPool

import io.github.kenneycode.fusion.input.FusionImage
import io.github.kenneycode.fusion.process.RenderChain
import io.github.kenneycode.fusion.process.RenderPipeline
import io.github.kenneycode.fusion.program.GLProgramPool
import io.github.kenneycode.fusion.renderer.CropRenderer
import io.github.kenneycode.fusion.renderer.ScaleRenderer
import io.github.kenneycode.fusion.texture.TexturePool
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

        // 创建图片输入
        val image = FusionImage(Util.decodeBitmapFromAssets("test.png")!!)

        // 创建一个scale renderer
        val scaleRenderer = ScaleRenderer().apply {
            setFlip(false, true)
            scale = 0.8f
        }

        // 创建一个crop renderer
        val cropRenderer = CropRenderer().apply {
            setCropRect(0.1f, 0.9f, 0.8f, 0.2f)
        }

        // 创建RenderChain并添加renderer
        val renderChain = RenderChain.create()
                .addRenderer(scaleRenderer)
                .addRenderer(cropRenderer)

        // 创建RenderPipeline
        val renderPipeline = RenderPipeline
                .input(image)
                .renderWith(renderChain)
                .useContext(fusionGLTextureView)
                .output(fusionGLTextureView)

        // 初始化
        renderPipeline.init()

        // 更新（非必需）
        renderPipeline.update()

        // 渲染
        renderPipeline.render()

    }

    override fun onDestroy() {
        TexturePool.release()
        FrameBufferPool.release()
        GLProgramPool.release()
        super.onDestroy()
    }

}
