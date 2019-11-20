package io.github.kenneycode.fusion.context

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * 原地执行的GL Context类
 *
 */

class InPlaceGLContext : GLContext {

    /**
     *
     * 将task在此OpenGL Context中执行
     *
     * @param task 要执行的task
     *
     */
    override fun runOnGLContext(task: () -> Unit) {
        task.invoke()
    }

}