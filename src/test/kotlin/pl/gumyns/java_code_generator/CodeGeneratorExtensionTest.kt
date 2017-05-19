package pl.gumyns.java_code_generator

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.junit.Assert
import org.junit.Test
import pl.gumyns.java_code_generator.model.ClassDef
import pl.gumyns.java_code_generator.model.VariableDef
import pl.gumyns.java_code_generator.model.VariableDefDeserializer

class CodeGeneratorExtensionTest {

    val yaml =
        """
- class: Mail
  vars:
  - boolean buu true
  - String mail some@email.com
- class: Whoa
  vars:
  - String texts [ok;cancel]
"""

    val gson = GsonBuilder()
            .registerTypeAdapter(VariableDef::class.java, VariableDefDeserializer())
            .create()

    @Test fun `test parsing`() {
        val mapper = ObjectMapper(YAMLFactory())
        val value = mapper.readValue<Any>(yaml, Any::class.java)
        println(gson.toJson(value))
        val classes = gson.fromJson<List<ClassDef>>(gson.toJson(value), object: TypeToken<List<ClassDef>>() {}.type)
        Assert.assertEquals(2, classes.size)
        Assert.assertEquals(1, classes[1].variables.size)
        Assert.assertEquals(2, classes[1].variables[0].values?.size)
        println(gson.toJson(classes))
    }
}