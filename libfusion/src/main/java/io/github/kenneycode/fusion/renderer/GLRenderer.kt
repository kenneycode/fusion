package io.github.kenneycode.fusion.renderer

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * GL渲染器接口
 *
 */

interface GLRenderer : Renderer {

    /**
     *
     * 初始化参数
     *
     */
    fun initParameter()

    /**
     *
     * 设置attribute float数组
     *
     * @param key attribute参数名
     * @param value float数组
     * @param componentCount 每个顶点的成份数（一维，二维..）
     *
     */
    fun setAttributeFloats(key: String, value: FloatArray, componentCount: Int = 2)

    /**
     *
     * 设置顶点坐标，默认是二维坐标
     *
     * @param positions 顶点坐标数组
     *
     */
    fun setPositions(positions: FloatArray)

    /**
     *
     * 设置纹理坐标
     *
     * @param textureCoordinates 纹理坐标数组
     *
     */
    fun setTextureCoordinates(textureCoordinates: FloatArray)

    /**
     *
     * 设置纹理参数
     *
     * @param key 纹理参数名
     * @param value 纹理id
     *
     */
    fun setUniformTexture2D(key: String, value: Int)

    /**
     *
     * 绑定输入
     *
     */
    fun bindInput()

    /**
     *
     * 绑定输出
     *
     */
    fun bindOutput()

    /**
     *
     * 解绑输入
     *
     */
    fun unBindInput()

}
