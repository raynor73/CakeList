package ilapin.cakelist.dataSources

import com.google.gson.annotations.SerializedName
import ilapin.cakelist.common.safeLet
import ilapin.cakelist.domain.Cake

class CakeDto(
    @SerializedName("title") val title: String?,
    @SerializedName("desc") val description: String?,
    @SerializedName("image") val imageUrl: String?
) {
    fun toCake(): Cake? {
        return safeLet(title, description, imageUrl) { title, description, imageUrl ->
            Cake(title, description, imageUrl)
        }
    }
}