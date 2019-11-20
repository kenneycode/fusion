package io.github.kenneycode.fusion.common

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * Size类
 *
 */

class Size(var width: Int, var height: Int) {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (javaClass != other?.javaClass) {
            return false
        }
        other as Size
        if (width != other.width) {
            return false
        }
        if (height != other.height) {
            return false
        }
        return true
    }

    override fun hashCode(): Int {
        var result = width
        result = 31 * result + height
        return result
    }

}
