# fusion

`Android`上的`OpenGL`渲染库

- 高度抽象了输入输出及渲染过程，隐藏了复杂繁琐的`OpenGL API`，即使不会`OpenGL`也能轻松上手。
- 统一渲染过程，通过`RenderGraph`将渲染器按`graph`进行组织管理。
- 支持`frame buffer`及`GL program`自动回收复用。
- 封装了`GL`线程及`EGL`环境，可通过`GLThread`及`EGL`帮助快速创建`GL`环境。
- 自带渲染显示`View`，也可以使用系统的`GLSurfaceView`。
- 可继承`SimpleRenderer`实现复杂效果，也可自行实现`Renderer`接口。

持续更新中...

引入方法：

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
	implementation 'com.github.kenneycode:fusion:Tag'
}
```

基本用法：

```java
// 创建图片输入源
val image = FusionImageSource(decodeBitmapFromAssets("test.png")!!)

// 创建一个简单渲染器
val simpleRenderer = SimpleRenderer()

// 创建RenderGraph
val renderGraph = RenderGraph(simpleRenderer)
  
// 设置RenderGraph的输出目标
renderGraph.addOutputTarget(simpleRenderer, findViewById<FusionGLTextureView>(R.id.fusionGLTextureView))

// 给输入源设置渲染器
image.addRenderer(renderGraph)

// 开始处理
image.process()
```




