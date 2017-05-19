package pl.gumyns.java_code_generator.model

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class VariableDefDeserializer : JsonDeserializer<VariableDef> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): VariableDef? {
        json?.asString?.split(delimiters = " ", limit = 3)?.let {
            if (!it[2].trim().startsWith(prefix = "["))
                return VariableDef(it[0], it[1], it[2], null)
            it[2].substring(1..it[2].length - 2).split(delimiters = ";").let { array ->
                return VariableDef(it[0], it[1], null, array)
            }
        }
        return null
    }
}