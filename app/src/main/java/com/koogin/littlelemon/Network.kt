package com.koogin.littlelemon
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MenuNetworkData (
    val menu : List<MenuItemNetwork>
)


@Serializable
data class MenuItemNetwork (
    val id : Int,
    val title : String,
    val description: String,
    val price : String,
    val image: String,
    val category: String

) {
    fun toRoomMenuItem() = MenuItemRoom(
        id = id,
        title = title,
        description = description,
        price = price,
        category = category,
        image = image,
    )
}



