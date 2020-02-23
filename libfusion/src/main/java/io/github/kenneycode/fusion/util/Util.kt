package io.github.kenneycode.fusion.util

import java.lang.RuntimeException

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * util类
 *
 */

class Util {

    companion object {

        fun assert(v: Boolean) {
            if (!v) {
                throw RuntimeException("assertion failed")
            }
        }

        fun genId(obj: Any): String {
            return "${obj.javaClass.simpleName}@${Integer.toHexString(System.identityHashCode(obj))}"
        }

    }

}
