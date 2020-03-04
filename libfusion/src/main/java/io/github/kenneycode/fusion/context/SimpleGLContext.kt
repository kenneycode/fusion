package io.github.kenneycode.fusion.context

import android.opengl.EGL14
import android.opengl.EGLContext
import android.view.Surface

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * 普通GL Context
 *
 */

class SimpleGLContext(surface: Surface? = null) : GLContext {

    private val glThread = FusionGLThread()

    init {
        glThread.init(surface)
    }

    override fun getEGLContext(): EGLContext {
        return EGL14.eglGetCurrentContext()
    }

    /**
     *
     * 将task在此OpenGL Context中执行
     *
     * @param task 要执行的task
     *
     */
    override fun runOnGLContext(task: () -> Unit) {
        glThread.post(task)
    }

    override fun release() {
        glThread.release()
    }

}