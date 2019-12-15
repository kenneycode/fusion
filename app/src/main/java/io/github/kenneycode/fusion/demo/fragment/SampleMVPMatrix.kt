package io.github.kenneycode.fusion.demo.fragment

import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.github.kenneycode.fusion.common.DataKeys
import io.github.kenneycode.fusion.demo.R
import io.github.kenneycode.fusion.demo.Util
import io.github.kenneycode.fusion.framebuffer.FrameBufferCache

import io.github.kenneycode.fusion.process.RenderChain
import io.github.kenneycode.fusion.renderer.DisplayRenderer
import io.github.kenneycode.fusion.renderer.SimpleRenderer
import io.github.kenneycode.fusion.util.GLUtil
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * MVP matrix demo
 *
 */

class SampleMVPMatrix : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sample_gl_surface_view, container,  false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        view.findViewById<GLSurfaceView>(R.id.glSurfaceView).apply {

            setEGLContextClientVersion(3)
            setEGLConfigChooser(8, 8, 8, 8, 0, 0)
            setRenderer(object : GLSurfaceView.Renderer {

                private var surfaceWidth = 0
                private var surfaceHeight = 0
                private lateinit var renderChain: RenderChain


                override fun onDrawFrame(gl: GL10?) {

                    // 执行渲染
                    renderChain.update(mutableMapOf(
                            DataKeys.KEY_DISPLAY_WIDTH to surfaceWidth,
                            DataKeys.KEY_DISPLAY_HEIGHT to surfaceHeight
                    ))
                    renderChain.render()

                }

                override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
                    surfaceWidth = width
                    surfaceHeight = height
                }

                override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {

                    // 创建一个简单渲染器
                    val simpleRenderer = SimpleRenderer().apply {
                        setFlip(false, true)
                        val mvpMatrix = GLUtil.createMVPMatrix(
                            rotateY = 45f
                        )
                        setMVPMatrix(mvpMatrix)
                    }

                    // 创建一个显示渲染器
                    val displayRenderer = DisplayRenderer()

                    // 创建RenderChain
                    renderChain = RenderChain(simpleRenderer).apply {
                        addNextRenderer(displayRenderer)
                        init()
                    }

                    // 创建图片输入源
                    val bitmap = Util.decodeBitmapFromAssets("test.png")!!
                    val imageTexture = GLUtil.createTexture()
                    GLUtil.bitmap2Texture(imageTexture, bitmap)
                    renderChain.setInput(FrameBufferCache.obtainFrameBuffer(bitmap.width, bitmap.height).apply {
                        texture = imageTexture
                        width = bitmap.width
                        height = bitmap.height
                        retain = true
                    })

                }
            })
        }

    }
}
