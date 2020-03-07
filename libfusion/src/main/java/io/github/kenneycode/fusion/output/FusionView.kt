package io.github.kenneycode.fusion.output

import android.content.Context
import android.graphics.SurfaceTexture
import android.opengl.EGL14
import android.opengl.EGLContext
import android.util.AttributeSet
import android.view.Surface
import android.view.TextureView
import io.github.kenneycode.fusion.common.Constants
import io.github.kenneycode.fusion.common.Constants.Companion.SIMPLE_FRAGMENT_SHADER
import io.github.kenneycode.fusion.context.FusionGLThread
import io.github.kenneycode.fusion.context.GLContext
import io.github.kenneycode.fusion.input.InputReceiver
import io.github.kenneycode.fusion.renderer.DisplayRenderer
import io.github.kenneycode.fusion.texture.Texture
import io.github.kenneycode.fusion.util.GLUtil
import java.util.*

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * 渲染显示View
 *
 */

class FusionView : TextureView, InputReceiver, GLContext {

    private var glThread: FusionGLThread? = null
    private var displayRenderer = DisplayRenderer(Constants.SIMPLE_VERTEX_SHADER, SIMPLE_FRAGMENT_SHADER)
    private val pendingTasks = LinkedList<() -> Unit>()
    private var surfaceWidth = 0
    private var surfaceHeight = 0
    private var initialized = false

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        surfaceTextureListener = object : SurfaceTextureListener {

            private var surface: Surface? = null

            override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
                surface?.release()
                return true
            }

            override fun onSurfaceTextureAvailable(surfaceTexture: SurfaceTexture?, width: Int, height: Int) {
                surfaceWidth = width
                surfaceHeight = height
                glThread = FusionGLThread().apply {
                    surface = Surface(surfaceTexture)
                    init(surface)
                    pendingTasks.forEach { task ->
                        post(task)
                    }
                }
            }

        }
    }

    override fun onInputReady(input: Texture, data: MutableMap<String, Any>) {
        if (!initialized) {
            displayRenderer.init()
        }
        displayRenderer.setDisplaySize(surfaceWidth, surfaceHeight)
        displayRenderer.setInput(input)
        displayRenderer.render()
        glThread?.swapBuffers()
        GLUtil.checkGLError()
    }

    /**
     *
     * 通知渲染输出目标输入已经准备好了
     *
     * @param textures 输入textures
     *
     */

    /**
     *
     * 在该view对应的GL Context中执行一个任务
     *
     * @param task 要执行的任务
     *
     */
    override fun runOnGLContext(task: () -> Unit) {
        post {
            glThread.let {
                if (it != null) {
                    it.post(task)
                } else {
                    pendingTasks.addLast(task)
                }
            }
        }
    }

    override fun getEGLContext(): EGLContext {
        return glThread?.getEGLContext() ?: EGL14.EGL_NO_CONTEXT
    }

    /**
     *
     * detached from window回调，此时销毁资源
     *
     */
    public override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        glThread?.let {
            it.post({
                displayRenderer.release()
                it.release()
            })
        }
    }

    override fun release() {
    }

}