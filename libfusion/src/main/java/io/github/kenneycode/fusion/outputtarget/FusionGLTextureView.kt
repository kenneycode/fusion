package io.github.kenneycode.fusion.outputtarget

import android.content.Context
import android.graphics.SurfaceTexture
import android.opengl.GLES20
import android.opengl.GLES20.*
import android.util.AttributeSet
import android.view.Surface
import android.view.TextureView
import android.widget.ImageView
import io.github.kenneycode.fusion.common.Constants
import io.github.kenneycode.fusion.common.Constants.Companion.SIMPLE_FRAGMENT_SHADER

import java.util.LinkedList

import io.github.kenneycode.fusion.common.FusionGLView
import io.github.kenneycode.fusion.context.FusionGLThread
import io.github.kenneycode.fusion.renderer.DisplayRenderer
import io.github.kenneycode.fusion.texture.Texture
import io.github.kenneycode.fusion.util.GLUtil

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * 渲染显示View
 *
 */

class FusionGLTextureView : TextureView, FusionGLView {

    private var glThread: FusionGLThread? = null
    private var displayRenderer = DisplayRenderer(Constants.SIMPLE_VERTEX_SHADER, SIMPLE_FRAGMENT_SHADER)
    private val pendingTasks = LinkedList<() -> Unit>()
    private var surfaceWidth = 0
    private var surfaceHeight = 0

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

    /**
     *
     * 初始化回调
     *
     */
    override fun onInit() {
        displayRenderer.init()
    }

    /**
     *
     * 更新数据回调
     *
     * @param data 传入的数据
     *
     */
    override fun onUpdate(data: MutableMap<String, Any>) {

    }

    override fun onReceiveOutputTexture(texture: Texture) {
        displayRenderer.setDisplaySize(surfaceWidth, surfaceHeight)
        displayRenderer.setInput(texture)
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

    /**
     *
     * detached input window回调，此时销毁资源
     *
     */
    public override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        glThread?.let {
            it.post({
                displayRenderer.release()
                glThread!!.release()
            })
        }
    }

}