package pl.gumyns.java_code_generator

import com.squareup.javapoet.*
import org.gradle.api.Plugin
import org.gradle.api.Project
import pl.gumyns.java_code_generator.model.ClassDef
import pl.gumyns.java_code_generator.model.VariableDef
import javax.lang.model.element.Modifier
import com.squareup.javapoet.FieldSpec
import java.io.File

class CodeGeneratorPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.run {
            extensions?.create("configGenerator", CodeGeneratorExtension::class.java)
            afterEvaluate {
                extensions.getByType(CodeGeneratorExtension::class.java).apply {
                    classes.forEach { generateClass(it, packageName!!)?.writeTo(File("$buildDir/generated/config")) }
                }
            }
        }
    }

    fun generateClass(classDef: ClassDef, packageName: String): JavaFile? {
        TypeSpec.classBuilder(classDef.name).addModifiers(Modifier.PUBLIC, Modifier.FINAL).run {
            classDef.variables.forEach { addField(generateField(it)) }
            return JavaFile.builder(packageName, build()).build()
        }
    }

    fun generateField(variableDef: VariableDef): FieldSpec? {
        if (variableDef.value != null) {
            return FieldSpec.builder(generateType(variableDef.type), variableDef.name)
                    .addModifiers(Modifier.STATIC, Modifier.PUBLIC, Modifier.FINAL)
                    .initializer(when (variableDef.type) {
                        "String" -> "\$S"
                        else -> "\$L"
                    }, variableDef.value)
                    .build()
        } else if (variableDef.values != null) {
            return FieldSpec.builder(ArrayTypeName.of(generateType(variableDef.type)), variableDef.name)
                    .addModifiers(Modifier.STATIC, Modifier.PUBLIC, Modifier.FINAL)
                    .initializer("\$L", generateArrayConstructor(variableDef))
                    .build()
        } else {
            return FieldSpec.builder(TypeName.BOOLEAN, variableDef.name)
                    .initializer("\$L", "false")
                    .build()
        }
    }

    private fun generateArrayConstructor(variableDef: VariableDef): String {
        when (variableDef.type) {
            "String" -> variableDef.values?.joinToString(transform = { "\"" + it + "\"" })
            else -> variableDef.values?.joinToString()
        }.let {
            return "new %s[]{ %s }".format(variableDef.type.apply { substring(lastIndexOf('.') + 1) }, it)
        }
    }

    private fun generateType(type: String): TypeName {
        return when (type) {
            "String" -> ClassName.get(String::class.java)
            "boolean" -> TypeName.BOOLEAN
            "char" -> TypeName.CHAR
            "byte" -> TypeName.BYTE
            "short" -> TypeName.SHORT
            "int" -> TypeName.INT
            "float" -> TypeName.FLOAT
            "long" -> TypeName.LONG
            "double" -> TypeName.DOUBLE
            else -> type.run { ClassName.get(substring(0..lastIndexOf('.') - 1), substring(lastIndexOf('.') + 1)) }
        }
    }
}