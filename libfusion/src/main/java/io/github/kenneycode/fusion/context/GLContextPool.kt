package io.github.kenneycode.fusion.context

import io.github.kenneycode.fusion.inputsource.InputSource

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * GL Context池
 *
 */

object GLContextPool {

    private val glContextMap = mutableMapOf<Int, GLContext>()

    /**
     *
     * 获取GL Context
     *
     * @param inputSource 输入源
     *
     * @return GL Context
     *
     */
    fun obtainGLContext(inputSource: InputSource): GLContext? {
        val id = inputSource.hashCode()
        return glContextMap[id] ?: run {
            val glContext = createGLContext(inputSource)
            glContextMap[id] = glContext
            glContext
        }
    }

    /**
     *
     * 创建GL Context
     *
     * @param inputSource 输入源
     *
     * @return GL Context
     *
     */
    private fun createGLContext(inputSource: InputSource): GLContext {
        return if (inputSource.runGLCommandInPlace()!!) {
            InPlaceGLContext()
        } else {
            SimpleGLContext()
        }
    }

    /**
     *
     * 为输入源设置GL Context
     *
     * @param inputSource 输入源
     * @param glContext GL Context
     *
     */
    fun setGLContextForInputSource(inputSource: InputSource, glContext: GLContext) {
        val id = inputSource.hashCode()
        if (!glContextMap.containsKey(id)) {
            glContextMap[id] = glContext
        }
    }

}