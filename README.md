[english README please click here](./README-en.md)

## fusion是什么？

`Android`上的`OpenGL ES`特效渲染库，类似`IOS`上的 `GPUImage`.

- 高度抽象了输入输出及渲染过程，隐藏了复杂繁琐的`OpenGL API`，即使不会`OpenGL`也能轻松上手。
- 统一渲染过程，通过`RenderChain`/`RenderGraph`将渲染器按`chain`/`graph`进行组织管理，并通过`RenderPipline`统一输入输出。
- 支持图片/视频输入，自带视频编解码。
- 支持图片/视频离屏渲染用于保存。
- 支持`texture/frame buffer/program`自动回收复用。
- 封装了`GL`线程及`EGL`环境，可通过`GLThread`及`EGL`帮助快速创建`GL`环境。
- 自带渲染显示`View`，也可以使用系统的`GLSurfaceView`。
- 自带常用渲染效果，可继承`SimpleRenderer/RenderChain/RenderGraph`实现复杂效果，也可自行实现`Renderer`接口。

持续更新中...

## 引入方法

根`gradle`中添加：

```
allprojects {
    repositories {
    	...
    	maven { url 'https://jitpack.io' }
    }
}
```

要引入的`module`中添加：

```
dependencies {
	implementation 'com.github.kenneycode:fusion:1.1.0'
}
```

## 图片渲染基本用法

```kotlin
// 创建RenderChain并添加一些renderer
val renderer = RenderChain()
	.addRenderer(ScaleRenderer().apply { setFlip(false, true); setScale(0.8f) })
	.addRenderer(CropRenderer().apply { setCropRect(0.1f, 0.9f, 0.8f, 0.2f) })
	.addRenderer(LUTRenderer().apply { setLUTImage(Util.decodeBitmapFromAssets("test_lut.png")!!); setLUTStrength(0.8f) })
	.addRenderer(GaussianBlurRenderer().apply { setBlurRadius(10) })

// 创建RenderPipeline，连接输入、渲染器与输出
renderPipeline = RenderPipeline
	.input(FusionImage(Util.decodeBitmapFromAssets("test.png")!!))
	.renderWith(renderer)
	.useContext(fusionView)
	.output(fusionView)

// 开始处理
renderPipeline.start()
```

## 视频渲染基本用法

```kotlin
// 创建RenderChain并添加一些renderer
val renderer = RenderChain()
	.addRenderer(OES2RGBARenderer())
	.addRenderer(LUTRenderer().apply { setLUTImage(Util.decodeBitmapFromAssets("test_lut.png")!!); setLUTStrength(0.8f) })
	.addRenderer(GaussianBlurRenderer().apply { setBlurRadius(10) })

// 创建RenderPipeline，连接输入、渲染器与输出
renderPipeline = RenderPipeline
    .input(FusionVideo("/sdcard/test.mp4"))
    .renderWith(renderer)
    .useContext(fusionView)
    .output(fusionView)

// 开始处理
renderPipeline.start()
```

更多用法请查看demo。

谢谢！