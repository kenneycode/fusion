package io.github.kenneycode.fusion.context

import android.opengl.EGL14
import android.opengl.EGLConfig
import android.opengl.EGLContext
import android.opengl.EGLExt
import android.view.Surface

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * EGL封装类
 *
 */

class FusionEGL {

    var eglContext = EGL14.EGL_NO_CONTEXT

    private var eglDisplay = EGL14.EGL_NO_DISPLAY
    private var eglSurface = EGL14.EGL_NO_SURFACE
    private var previousDisplay = EGL14.EGL_NO_DISPLAY
    private var previousDrawSurface = EGL14.EGL_NO_SURFACE
    private var previousReadSurface = EGL14.EGL_NO_SURFACE
    private var previousContext = EGL14.EGL_NO_CONTEXT

    /**
     *
     * 初始化
     *
     * @param surface 用于创建window surface的surface，如果传null则创建pbuffer surface
     * @param shareContext 共享的GL Context
     *
     */
    fun init(surface: Surface?, shareContext: EGLContext = EGL14.EGL_NO_CONTEXT) {
        eglDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY)
        val version = IntArray(2)
        EGL14.eglInitialize(eglDisplay, version, 0, version, 1)
        val attribList = intArrayOf(EGL14.EGL_RED_SIZE, 8, EGL14.EGL_GREEN_SIZE, 8, EGL14.EGL_BLUE_SIZE, 8, EGL14.EGL_ALPHA_SIZE, 8, EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT or EGLExt.EGL_OPENGL_ES3_BIT_KHR, EGL14.EGL_NONE, 0, EGL14.EGL_NONE)
        val eglConfig = arrayOfNulls<EGLConfig>(1)
        val numConfigs = IntArray(1)
        EGL14.eglChooseConfig(
            eglDisplay,
            attribList, 0,
            eglConfig, 0,
            eglConfig.size,
            numConfigs, 0
        )
        eglContext = EGL14.eglCreateContext(
            eglDisplay, eglConfig[0], shareContext,
            intArrayOf(EGL14.EGL_CONTEXT_CLIENT_VERSION, 2, EGL14.EGL_NONE), 0
        )
        val surfaceAttribs = intArrayOf(EGL14.EGL_NONE)
        eglSurface = if (surface == null) {
            EGL14.eglCreatePbufferSurface(eglDisplay, eglConfig[0], surfaceAttribs, 0)
        } else {
            EGL14.eglCreateWindowSurface(eglDisplay, eglConfig[0], surface, surfaceAttribs, 0)
        }
    }

    /**
     *
     * 将EGL环境绑定到调用线程
     *
     */
    fun bind() {
        previousDisplay = EGL14.eglGetCurrentDisplay()
        previousDrawSurface = EGL14.eglGetCurrentSurface(EGL14.EGL_DRAW)
        previousReadSurface = EGL14.eglGetCurrentSurface(EGL14.EGL_READ)
        previousContext = EGL14.eglGetCurrentContext()
        EGL14.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)
    }

    /**
     *
     * 将之前的EGL环境恢复
     *
     */
    fun unbind() {
        EGL14.eglMakeCurrent(previousDisplay, previousDrawSurface, previousReadSurface, previousContext)
    }

    /**
     *
     * 交换显示buffer
     *
     */
    fun swapBuffers(): Boolean {
        return EGL14.eglSwapBuffers(eglDisplay, eglSurface)
    }

    /**
     *
     * 释放资源
     *
     */
    fun release() {
        if (eglDisplay !== EGL14.EGL_NO_DISPLAY) {
            EGL14.eglMakeCurrent(eglDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT)
            EGL14.eglDestroySurface(eglDisplay, eglSurface)
            EGL14.eglDestroyContext(eglDisplay, eglContext)
            EGL14.eglReleaseThread()
            EGL14.eglTerminate(eglDisplay)
        }
        eglDisplay = EGL14.EGL_NO_DISPLAY
        eglContext = EGL14.EGL_NO_CONTEXT
        eglSurface = EGL14.EGL_NO_SURFACE
    }

}