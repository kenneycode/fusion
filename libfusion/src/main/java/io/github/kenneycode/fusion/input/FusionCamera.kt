package io.github.kenneycode.fusion.input

import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.opengl.GLES11Ext
import android.view.Surface
import io.github.kenneycode.fusion.common.DataKeys
import io.github.kenneycode.fusion.common.Size
import io.github.kenneycode.fusion.context.GLContext
import io.github.kenneycode.fusion.process.RenderPipeline
import io.github.kenneycode.fusion.texture.Texture
import io.github.kenneycode.fusion.util.GLUtil

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * 相机输入源
 *
 */

class FusionCamera(private val config: Config) : RenderPipeline.Input {

    class Config {

        var cameraFacing = Camera.CameraInfo.CAMERA_FACING_FRONT
        var windowRotation = 0
        var desiredPreviewSize = Size(0, 0)

    }

    private lateinit var cameraTexture: Texture
    private lateinit var camera: Camera
    private lateinit var surfaceTexture: SurfaceTexture
    private lateinit var inputReceiver: InputReceiver
    private lateinit var finalPreviewSize: Size

    override fun start(data: MutableMap<String, Any>) {
        camera.startPreview()
    }

    override fun setInputReceiver(inputReceiver: InputReceiver) {
        this.inputReceiver = inputReceiver
    }

    override fun onInit(glContext: GLContext, data: MutableMap<String, Any>) {
        val cameraId = getCameraId(config.cameraFacing)
        camera = Camera.open(cameraId)
        setPreviewSize(camera.parameters)
        val info = Camera.CameraInfo()
        Camera.getCameraInfo(cameraId, info)
        setCameraDisplayOrientation(cameraId)
        cameraTexture = Texture(finalPreviewSize.width, finalPreviewSize.height, GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLUtil.createOESTexture()).apply { retain = true }
        surfaceTexture = SurfaceTexture(cameraTexture.texture).apply {
            setOnFrameAvailableListener {
                it.updateTexImage()
                val transformMatrix = FloatArray(16)
                it.getTransformMatrix(transformMatrix)
                inputReceiver.onInputReady(
                        cameraTexture,
                        mutableMapOf<String, Any>().apply { put(DataKeys.ST_MATRIX, transformMatrix) }
                )
            }
        }
        camera.setPreviewTexture(surfaceTexture)
    }

    override fun onUpdate(data: MutableMap<String, Any>) {
    }

    private fun getCameraId(facing : Int) : Int {
        val numberOfCameras = Camera.getNumberOfCameras()
        for (i in 0 until numberOfCameras) {
            val info = Camera.CameraInfo()
            Camera.getCameraInfo(i, info)
            if (info.facing == facing) {
                return i
            }
        }
        return -1
    }

    private fun setPreviewSize(parameters: Camera.Parameters) {
        parameters.supportedPreviewSizes.forEach {
            // desiredPreviewSize是竖屏时的宽高
            if (it.height == config.desiredPreviewSize.width && it.width == config.desiredPreviewSize.height) {
                finalPreviewSize = Size(it.height, it.width)
                parameters.setPreviewSize(it.width, it.height)
                return
            }
        }
        finalPreviewSize = Size(parameters.supportedPreviewSizes.first().height, parameters.supportedPreviewSizes.first().width)
        parameters.setPreviewSize(parameters.supportedPreviewSizes.first().width, parameters.supportedPreviewSizes.first().height)
    }

    private fun setCameraDisplayOrientation(cameraId: Int) {
        val info = Camera.CameraInfo()
        Camera.getCameraInfo(cameraId, info)
        var degrees = 0
        when (config.windowRotation) {
            Surface.ROTATION_0 -> degrees = 0
            Surface.ROTATION_90 -> degrees = 90
            Surface.ROTATION_180 -> degrees = 180
            Surface.ROTATION_270 -> degrees = 270
        }

        var rotation = 0
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            rotation = (info.orientation + degrees) % 360;
            rotation = (360 - rotation) % 360
        } else {
            rotation = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(rotation)
    }

    override fun onRelease() {
        camera.stopPreview()
        camera.release()
        cameraTexture.release()
    }

}