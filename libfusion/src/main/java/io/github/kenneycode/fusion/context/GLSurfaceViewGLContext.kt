package io.github.kenneycode.fusion.context

import android.opengl.GLSurfaceView

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * GLSurfaceView OpenGL上下文包装类
 *
 */

class GLSurfaceViewGLContext(private val glSurfaceView: GLSurfaceView) : GLContext {

    /**
     *
     * 将task在此GLSurfaceView的OpenGL Context中执行
     *
     * @param task 要执行的task
     *
     */
    override fun runOnGLContext(task: () -> Unit) {
        glSurfaceView.queueEvent(task)
    }

}
