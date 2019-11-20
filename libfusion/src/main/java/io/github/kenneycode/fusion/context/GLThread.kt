package io.github.kenneycode.fusion.context

import android.opengl.EGL14
import android.opengl.EGLContext
import android.os.Handler
import android.os.HandlerThread
import android.view.Surface
import java.util.concurrent.Semaphore

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * 包含GL Context和线程
 *
 */

class GLThread {

    private val handlerThread : HandlerThread = HandlerThread("GLThread")
    private lateinit var handler : Handler
    lateinit var egl : EGL

    /**
     *
     * 初始化
     *
     * @param surface 用于创建window surface的surface，如果传null则创建pbuffer surface
     * @param shareContext 共享的GL Context
     *
     */
    fun init(surface: Surface? = null, shareContext: EGLContext = EGL14.EGL_NO_CONTEXT) {
        handlerThread.start()
        handler = Handler(handlerThread.looper)
        egl = EGL()
        egl.init(surface, shareContext)
        handler.post {
            egl.bind()
        }
    }

    /**
     *
     * 异步执行一个任务
     *
     * @param task 要执行的任务
     * @param render 是否需要渲染
     *
     */
    fun post(task : () -> Unit, render: Boolean = false) {
        handler.post {
            task.invoke()
            if (render) {
                egl.swapBuffers()
            }
        }
    }

    /**
     *
     * 同步执行一个任务
     *
     * @param task 要执行的任务
     * @param render 是否需要渲染
     *
     */
    fun execute(task : () -> Unit, render: Boolean = false) {
        val semaphore = Semaphore(0)
        handler.post {
            task.invoke()
            if (render) {
                egl.swapBuffers()
            }
            semaphore.release()
        }
        semaphore.acquire()
    }

    /**
     *
     * 交换显示buffer
     *
     */
    fun swapBuffers() {
        egl.swapBuffers()
    }

    /**
     *
     * 停止线程和释放资源
     *
     */
    fun release() {
        handler.post {
            egl.release()
            handlerThread.looper.quit()
        }
    }

}