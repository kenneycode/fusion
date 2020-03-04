package io.github.kenneycode.fusion.input

import io.github.kenneycode.fusion.context.GLContext
import io.github.kenneycode.fusion.texture.Texture

interface InputReceiver {

    fun onInit(glContext: GLContext, data: MutableMap<String, Any>) { }
    fun onInputReady(input: Texture, data: MutableMap<String, Any> = mutableMapOf())
    fun onRelease() { }

}