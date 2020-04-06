## What is fusion?

`fusion` is an `OpenGL ES` effects rendering library on `Android` written by `kotlin`, which is similar to `GPUImage` on `IOS`.

- `fusion` highly abstracts the ` input / output` and rendering process, hides the complex and trivial `OpenGL API`. Developers without `OpenGL` knowledge can use `fusion` easily
- organize and manage the renderers by `chain/graph` through `RenderChain / RenderGraph`, and unify input and output through `RrenderPipline`.
- support image/video input, and video decode/encode.
- support image/video offscreen rendering to save the effects.
- support auto reuse `texture`/`frame buffer`/`program` to reduce the usage of graphic memory.
- support `GL thread` and `EGL`  to easily create `GL` enviroment.
- support display the rendered result by the built-in view, and also support `GLSurfaceView` .
- supports common rendering effects, and developers can inherit `SimpleRenderer/RenderChain/RenderGraph` to achieve complex effects, or implement `Renderer` interface.

continuously updating...

## Integration：

add the following code to the root `gradle` of your project:

```
allprojects {
    repositories {
    	...
    	maven { url 'https://jitpack.io' }
    }
}
```

then add the following code to the `gradle` of your module:

```
dependencies {
	implementation 'com.github.kenneycode:fusion:1.1.0'
}
```

## Basic usage of image rendering

```java
// create RenderChain and add some renderers
val renderer = RenderChain()
	.addRenderer(ScaleRenderer().apply { setFlip(false, true); setScale(0.8f) })
	.addRenderer(CropRenderer().apply { setCropRect(0.1f, 0.9f, 0.8f, 0.2f) })
	.addRenderer(LUTRenderer().apply { setLUTImage(Util.decodeBitmapFromAssets("test_lut.png")!!); setLUTStrength(0.8f) })
	.addRenderer(GaussianBlurRenderer().apply { setBlurRadius(10) })

// create RenderPipeline，connecting input, renderer and ouput
renderPipeline = RenderPipeline
	.input(FusionImage(Util.decodeBitmapFromAssets("test.png")!!))
	.renderWith(renderer)
	.useContext(fusionView)
	.output(fusionView)

// start processing
renderPipeline.start()
```

## Basic usage of video rendering

```java
// create RenderChain and add some renderers
val renderer = RenderChain()
	.addRenderer(OES2RGBARenderer())
	.addRenderer(LUTRenderer().apply { setLUTImage(Util.decodeBitmapFromAssets("test_lut.png")!!); setLUTStrength(0.8f) })
	.addRenderer(GaussianBlurRenderer().apply { setBlurRadius(10) })

// create RenderPipeline，connecting input, renderer and ouput
renderPipeline = RenderPipeline
    .input(FusionVideo("/sdcard/test.mp4"))
    .renderWith(renderer)
    .useContext(fusionView)
    .output(fusionView)

// start processing
renderPipeline.start()
```

for more usages please see the demo.

thank you!