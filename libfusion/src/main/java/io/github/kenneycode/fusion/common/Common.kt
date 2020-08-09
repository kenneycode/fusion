package io.github.kenneycode.fusion.common

import android.opengl.GLES20.glGetError
import java.lang.AssertionError

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * common逻辑
 *
 */

val DEBUG = true

/**
 *
 * 检查GL error，在DEBUG==true的情况下如果检查到GL error会throw AssertionError
 *
 * @param block 要检查的代码块
 *
 */
fun glCheck(block: () -> Unit) {
    block()
    if (DEBUG) {
        val error = glGetError()
        if (error != 0) {
            throw AssertionError("GL checkout error = $error")
        }
    }
}