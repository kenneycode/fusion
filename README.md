[english README please click here](./README-en.md)

## fusion是什么？

`Android`上的`OpenGL ES`渲染库，类似`IOS`上的 `GPUImage`.

- 高度抽象了输入输出及渲染过程，隐藏了复杂繁琐的`OpenGL API`，即使不会`OpenGL`也能轻松上手。
- 统一渲染过程，通过`RenderChain`/`RenderGraph`将渲染器按`chain`/`graph`进行组织管理，并通过`RenderPipline`统一输入输出。
- 支持`texture/frame buffer/program`自动回收复用。
- 封装了`GL`线程及`EGL`环境，可通过`GLThread`及`EGL`帮助快速创建`GL`环境。
- 自带渲染显示`View`，也可以使用系统的`GLSurfaceView`。
- 自带常用渲染效果，可继承`SimpleRenderer`实现复杂效果，也可自行实现`Renderer`接口。

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
	implementation 'com.github.kenneycode:fusion:1.0.4'
}
```

## 基本用法

```java
// 创建图片输入
val image = FusionImage(Util.decodeBitmapFromAssets("test.png")!!)

// 创建一个scale renderer
val scaleRenderer = ScaleRenderer().apply {
    setFlip(false, true)
    scale = 0.8f
}

// 创建一个renderer用于演示
val cropRenderer = CropRenderer().apply {
    setCropRect(0.1f, 0.9f, 0.8f, 0.2f)
}

// 创建一个renderer用于演示
val renderChain = RenderChain.create()
    .addRenderer(scaleRenderer)
    .addRenderer(cropRenderer)

// 创建RenderPipeline将RenderChain与输入/输出连接
val renderPipeline = RenderPipeline
    .input(image)
    .renderWith(renderChain)
    .useContext(fusionGLTextureView)
    .output(fusionGLTextureView)

// 初始化
renderPipeline.init()

// 更新（非必需，调用时可传递数据）
renderPipeline.update()

// 渲染
renderPipeline.render()
```

更多用法请查看demo。

谢谢！