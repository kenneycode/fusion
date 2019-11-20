package io.github.kenneycode.fusion.context

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * 普通GL Context
 *
 */

class SimpleGLContext : GLContext {

    private val glThread = GLThread()

    init {
        glThread.init()
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

}