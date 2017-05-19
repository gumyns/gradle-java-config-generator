package pl.gumyns.java_code_generator.model

import com.google.gson.annotations.SerializedName

data class ClassDef(
        @SerializedName("class") val name: String,
        @SerializedName("vars") val variables: List<VariableDef>
)