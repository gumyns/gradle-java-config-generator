package pl.gumyns.java_code_generator

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import pl.gumyns.java_code_generator.model.ClassDef
import pl.gumyns.java_code_generator.model.VariableDef
import pl.gumyns.java_code_generator.model.VariableDefDeserializer
import java.io.File
import java.util.*

open class CodeGeneratorExtension(
        val gson: Gson = GsonBuilder()
                .registerTypeAdapter(VariableDef::class.java, VariableDefDeserializer())
                .create()
) {
    var packageName: String? = null
    var classes = LinkedList<ClassDef>()

    fun file(vararg yaml: String) {
        ObjectMapper(YAMLFactory()).apply {
            yaml.forEach {
                classes.addAll(gson.fromJson<List<ClassDef>>(gson.toJson(readValue<Any>(File(it), Any::class.java)),
                        object : TypeToken<List<ClassDef>>() {}.type))
            }
        }
    }

    fun packageName(pkg: String) {
        packageName = pkg
    }
}