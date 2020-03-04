package io.github.kenneycode.fusion.input

import android.graphics.SurfaceTexture
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.opengl.GLES11Ext
import android.os.Handler
import android.os.HandlerThread
import android.view.Surface
import io.github.kenneycode.fusion.common.DataKeys
import io.github.kenneycode.fusion.common.Size
import io.github.kenneycode.fusion.context.GLContext
import io.github.kenneycode.fusion.process.RenderPipeline
import io.github.kenneycode.fusion.texture.Texture
import io.github.kenneycode.fusion.util.GLUtil
import io.github.kenneycode.videostudio.VideoDecoder
import javax.microedition.khronos.opengles.GL11Ext

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * 视频解码器
 *
 */

class FusionVideoDecoder(private val videoPath: String) : RenderPipeline.Input {

    private lateinit var decodeHandler: Handler
    private val videoDecoder = VideoDecoder()
    private var decodeOutputTexture = 0
    private val transformMatrix = FloatArray(16)
    private lateinit var surfaceTexture: SurfaceTexture
    private lateinit var videoSize: Size
    private lateinit var inputReceiver: InputReceiver
    private var callback: Callback? = null

    override fun onInit(glContext: GLContext, data: MutableMap<String, Any>) {
        decodeHandler = Handler(HandlerThread("DecodeHT").apply { start() }.looper)
        decodeOutputTexture = GLUtil.createOESTexture()
        surfaceTexture = SurfaceTexture(decodeOutputTexture).apply {
            setOnFrameAvailableListener {
                it.updateTexImage()
                it.getTransformMatrix(transformMatrix)
                inputReceiver.onInputReady(
                    Texture(videoSize.width, videoSize.height, decodeOutputTexture, GLES11Ext.GL_TEXTURE_EXTERNAL_OES).apply { retain = true },
                    mutableMapOf<String, Any>().apply { put(DataKeys.ST_MATRIX, transformMatrix) }
                )
            }
        }
        videoDecoder.init(videoPath, surfaceTexture)
        videoSize = Size(videoDecoder.getVideoWidth(), videoDecoder.getVideoHeight())
        MediaMetadataRetriever().let {
            it.setDataSource(videoPath)
            data[DataKeys.KEY_SOURCE_VIDEO_WIDTH] = it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH).toInt()
            data[DataKeys.KEY_SOURCE_VIDEO_HEIGHT] = it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT).toInt()
        }
    }

    override fun onUpdate(data: MutableMap<String, Any>) {
    }

    override fun setInputReceiver(inputReceiver: InputReceiver) {
        this.inputReceiver = inputReceiver
    }

    override fun start(data: MutableMap<String, Any>) {
        decodeHandler.post {
            callback?.onStart()
            while(videoDecoder.decode()) { }
            callback?.onEnd()
        }
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    override fun onRelease() {
        decodeHandler.looper.quit()
        surfaceTexture.release()
        videoDecoder.release()
        inputReceiver.onRelease()
    }

    interface Callback {

        fun onStart()
        fun onEnd()

    }

}