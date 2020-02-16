package io.github.kenneycode.fusion.input

import io.github.kenneycode.fusion.texture.Texture

interface InputReceiver {

    fun onInputReady(input: Texture)

}