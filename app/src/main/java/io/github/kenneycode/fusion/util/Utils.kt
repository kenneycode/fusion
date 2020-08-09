package io.github.kenneycode.fusion.util

import android.content.Context
import java.io.File
import java.io.FileOutputStream

class Utils {

    companion object {

        fun copyAssetsFiles(context: Context, source: String, dst: String): Boolean {
            try {
                val fileNames = context.assets.list(source)
                if (fileNames!!.isNotEmpty()) {
                    val file = File(dst)
                    file.mkdirs()
                    for (fileName in fileNames) {
                        copyAssetsFiles(context, "$source/$fileName", "$dst/$fileName")
                    }
                } else {
                    val ins = context.assets.open(source)
                    val fos = FileOutputStream(File(dst))
                    val buffer = ByteArray(1024)
                    var byteCount = 0
                    while (true) {
                        byteCount = ins.read(buffer)
                        if (byteCount == -1) {
                            break
                        }
                        fos.write(buffer, 0, byteCount)
                    }
                    fos.flush()
                    ins.close()
                    fos.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return true
        }
    }

}