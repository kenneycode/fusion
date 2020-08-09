package io.github.kenneycode.fusion.input

import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.opengl.GLES11Ext
import android.view.Surface
import io.github.kenneycode.fusion.common.DataKeys
import io.github.kenneycode.fusion.common.Size
import io.github.kenneycode.fusion.context.GLContext
import io.github.kenneycode.fusion.process.RenderPipeline
import io.github.kenneycode.fusion.texture.Texture
import io.github.kenneycode.fusion.util.GLUtil
import javax.microedition.khronos.opengles.GL11Ext

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * 视频输入源
 *
 */

class FusionVideo(private val videoPath: String) : RenderPipeline.Input {

    private val videoPlayer = MediaPlayer()
    private var decodeOutputTexture = 0
    private val transformMatrix = FloatArray(16)
    private lateinit var surfaceTexture: SurfaceTexture
    private lateinit var surface: Surface
    private lateinit var videoSize: Size
    private lateinit var inputReceiver: InputReceiver

    override fun onInit(glContext: GLContext, data: MutableMap<String, Any>) {
        decodeOutputTexture = GLUtil.createOESTexture()
        surfaceTexture = SurfaceTexture(decodeOutputTexture).apply {
            setOnFrameAvailableListener {
                it.updateTexImage()
                it.getTransformMatrix(transformMatrix)
                inputReceiver.onInputReady(
                    Texture(videoSize.width, videoSize.height, GLES11Ext.GL_TEXTURE_EXTERNAL_OES, decodeOutputTexture).apply { retain = true },
                    mutableMapOf<String, Any>().apply { put(DataKeys.ST_MATRIX, transformMatrix) }
                )
            }
        }
        surface = Surface(surfaceTexture)
        videoPlayer.setDataSource(videoPath)
        videoPlayer.setSurface(surface)
        videoPlayer.isLooping = true
        videoPlayer.prepare()
        videoSize = Size(videoPlayer.videoWidth, videoPlayer.videoHeight)
    }

    override fun onUpdate(data: MutableMap<String, Any>) {
    }

    override fun setInputReceiver(inputReceiver: InputReceiver) {
        this.inputReceiver = inputReceiver
    }

    override fun start(data: MutableMap<String, Any>) {
        videoPlayer.start()
    }

    override fun onRelease() {
        surfaceTexture.release()
        surface.release()
        videoPlayer.release()
    }

}