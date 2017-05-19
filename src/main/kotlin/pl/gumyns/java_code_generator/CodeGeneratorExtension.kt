package pl.gumyns.java_code_generator

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import pl.gumyns.java_code_generator.model.ClassDef
import pl.gumyns.java_code_generator.model.VariableDef
import pl.gumyns.java_code_generator.model.VariableDefDeserializer
import java.io.File

open class CodeGeneratorExtension {

    var packageName : String? = null
    var classes: List<ClassDef>? = null

    fun file(json: String = "config.json") {
        val gson =  GsonBuilder()
                .registerTypeAdapter(VariableDef::class.java, VariableDefDeserializer())
                .create()
        val file = File(json)

        val mapper = ObjectMapper(YAMLFactory())
        val value = mapper.readValue<Any>(file, Any::class.java)
        println(gson.toJson(value))
        classes = gson.fromJson<List<ClassDef>>(gson.toJson(value), object: TypeToken<List<ClassDef>>() {}.type)
    }

    fun packageName(pkg: String) {
        packageName = pkg
    }
}