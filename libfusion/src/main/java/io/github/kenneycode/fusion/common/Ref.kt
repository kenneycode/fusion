package io.github.kenneycode.fusion.common

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * 引用计数类
 *
 */

open class Ref {

    protected var refCount = 1

    /**
     *
     * 增加count个引用计数
     *
     * @param count 要增加的引用计数
     *
     */
    fun increaseRef(count: Int = 1) {
        refCount += count
    }

    /**
     *
     * 减少1个引用计数
     *
     */
    open fun decreaseRef() {
        --refCount
    }

}