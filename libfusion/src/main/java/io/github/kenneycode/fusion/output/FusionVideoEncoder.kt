package io.github.kenneycode.fusion.output

import android.opengl.EGL14
import io.github.kenneycode.fusion.common.DataKeys
import io.github.kenneycode.fusion.context.GLContext
import io.github.kenneycode.fusion.input.InputReceiver
import io.github.kenneycode.fusion.texture.Texture
import io.github.kenneycode.videostudio.VideoEncoder

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * 视频编码器输入源
 *
 */

class FusionVideoEncoder(private val videoPath: String) : InputReceiver {

    private val videoEncoder = VideoEncoder()
    private var index = 0

    override fun onInit(glContext: GLContext, data: MutableMap<String, Any>) {
        videoEncoder.init(
            videoPath,
            data[DataKeys.KEY_SOURCE_VIDEO_WIDTH] as Int,
            data[DataKeys.KEY_SOURCE_VIDEO_HEIGHT] as Int,
            glContext.getEGLContext()
        )
    }

    override fun onInputReady(input: Texture, data: MutableMap<String, Any>) {
        videoEncoder.encodeFrame(input.texture, (index++) * 40 * 1000000L)
    }

    override fun onRelease() {
        videoEncoder.encodeFrame(0, 0)
        videoEncoder.release()
    }

}