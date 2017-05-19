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
        with(project) {
            project.extensions?.create("configGenerator", CodeGeneratorExtension::class.java)

            afterEvaluate({
                val codeGenerator = extensions.getByType(CodeGeneratorExtension::class.java)
                codeGenerator.classes?.let {
                    for (classDef in it) {
                        generateClass(classDef, codeGenerator.packageName!!).let {
                            it?.writeTo(File("$buildDir/generated/config"))
                        }
                    }
                }
            })
        }
    }

    fun generateClass(classDef: ClassDef, packageName: String): JavaFile? {
        val classBuilder = TypeSpec.classBuilder(classDef.name)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
        classDef.variables.forEach({
            classBuilder.addField(generateField(it))
        })
        return JavaFile.builder(packageName, classBuilder.build()).build()
    }

    fun generateField(variableDef: VariableDef): FieldSpec? {
        return FieldSpec.builder(
                when (variableDef.type) {
                    "String" -> ClassName.get(String::class.java)
                    "boolean" -> TypeName.BOOLEAN
                    "char" -> TypeName.CHAR
                    "byte" -> TypeName.BYTE
                    "short" -> TypeName.SHORT
                    "int" -> TypeName.INT
                    "float" -> TypeName.FLOAT
                    "long" -> TypeName.LONG
                    "double" -> TypeName.DOUBLE
                    else -> ClassName.get(
                            variableDef.type.substring(0..variableDef.type.lastIndexOf('.') - 1),
                            variableDef.type.substring(variableDef.type.lastIndexOf('.') + 1)
                    )
                }, variableDef.name)
                .addModifiers(Modifier.STATIC, Modifier.PUBLIC, Modifier.FINAL)
                .initializer(when (variableDef.type) {
                    "String" -> "\$S"
                    else -> "\$L"
                }, variableDef.value)
                .build()
    }
}