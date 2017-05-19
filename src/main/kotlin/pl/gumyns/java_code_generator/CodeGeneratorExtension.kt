package pl.gumyns.java_code_generator

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import pl.gumyns.java_code_generator.model.ClassDef
import java.io.File

open class CodeGeneratorExtension {

    var packageName : String? = null
    var classes: List<ClassDef>? = null

    fun file(json: String = "config.json") {
        val gson = Gson()
        val file = File(json)
        classes = gson.fromJson<List<ClassDef>>(file.reader(), object: TypeToken<List<ClassDef>>() {}.type)
    }

    fun packageName(pkg: String) {
        packageName = pkg
    }
}