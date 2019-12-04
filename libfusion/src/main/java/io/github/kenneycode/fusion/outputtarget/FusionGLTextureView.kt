package io.github.kenneycode.fusion.outputtarget

import android.content.Context
import android.graphics.SurfaceTexture
import android.util.AttributeSet
import android.view.Surface
import android.view.TextureView

import java.util.LinkedList

import io.github.kenneycode.fusion.common.FusionGLView
import io.github.kenneycode.fusion.context.FusionGLThread
import io.github.kenneycode.fusion.framebuffer.FrameBuffer
import io.github.kenneycode.fusion.renderer.DisplayRenderer

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
    private var displayRenderer = DisplayRenderer()
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
    override fun onUpdate(data: Map<String, Any>) {

    }

    /**
     *
     * 通知渲染输出目标输入已经准备好了
     *
     * @param frameBuffers 输入FrameBuffer
     *
     */
    override fun onInputReady(frameBuffers: List<FrameBuffer>) {
        displayRenderer.setDisplaySize(surfaceWidth, surfaceHeight)
        displayRenderer.setInput(frameBuffers)
        displayRenderer.render()
        glThread?.swapBuffers()
    }

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
     * detached from window回调，此时销毁资源
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