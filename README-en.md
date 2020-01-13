## What is fusion?

`fusion` is an `OpenGL ES` rendering library on `Android` written by `kotlin`, which is similar to `GPUImage` on `IOS`.

- `fusion` highly abstracts the ` input / output` and rendering process, hides the complex and trivial `OpenGL API`. Developers without `OpenGL` knowledge can use `fusion` easily

- organize and manage the renderers by `chain/graph` through `RenderChain / RenderGraph`, and unify input and output through `RrenderPipline`.

- support auto reuse `texture`/`frame buffer`/`program` to reduce the usage of graphic memory.

- support `GL thread` and `EGL`  to easily create `GL` enviroment.

- support display the rendered result by the built-in view, and also support `GLSurfaceView` .

- supports common rendering effects, and developers can inherit `SimpleRenderer` to achieve complex effects, or implement `Renderer` interface.

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
	implementation 'com.github.kenneycode:fusion:1.0.4'
}
```

## Basic usage

```java
// create the input image
val image = FusionImage(Util.decodeBitmapFromAssets("test.png")!!)

// create a renderer for test
val scaleRenderer = ScaleRenderer().apply {
    setFlip(false, true)
    scale = 0.8f
}

// create a renderer for test
val cropRenderer = CropRenderer().apply {
    setCropRect(0.1f, 0.9f, 0.8f, 0.2f)
}

// create RenderChain and add renderers
val renderChain = RenderChain.create()
    .addRenderer(scaleRenderer)
    .addRenderer(cropRenderer)

// create RenderPipeline to connect the input/output to RenderChain
val renderPipeline = RenderPipeline
    .input(image)
    .renderWith(renderChain)
    .useContext(fusionGLTextureView)
    .output(fusionGLTextureView)

// init
renderPipeline.init()

// update（you can pass some data if needed）
renderPipeline.update()

// render
renderPipeline.render()
```

for more usages please see the demo.

thank you!