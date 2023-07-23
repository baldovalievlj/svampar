//import com.example.models.dto.OrderDTO
import com.example.models.dto.OrderItemDTO
import com.example.models.dto.UserDTO
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.serializer

//object OrderDTOSerializer : KSerializer<OrderDTO> {
//    private val json = Json { encodeDefaults = true }
//
//    override val descriptor = json.serializersModule.serializer(OrderDTO::class).descriptor
//
//    override fun serialize(encoder: Encoder, value: OrderDTO) {
//        val jsonObject = buildJsonObject {
//            put("id", value.id)
//            put("user", json.encodeToJsonElement(UserDTO.serializer(), value.user))
//            put("dateCreated", value.dateCreated.toString())
//            put("details", value.details)
//            put("items", JsonArray(value.items.map { json.encodeToJsonElement(OrderItemDTO.serializer(), it) }))
//            put("totalPrice", value.totalPrice)
//            put("totalAmount", value.totalAmount)
//        }
//
//        encoder.encodeJsonElement(jsonObject)
//    }
//    override fun deserialize(decoder: Decoder): OrderDTO {
//        val jsonObject = decoder.decodeJsonElement() as JsonObject
//
//        return OrderDTO(
//            id = jsonObject.getValue("id").jsonPrimitive.int,
//            user = json.decodeFromJsonElement(UserDTO.serializer(), jsonObject.getValue("user")),
//            dateCreated = LocalDateTime.parse(jsonObject.getValue("dateCreated").jsonPrimitive.content),
//            details = jsonObject.getValue("details").jsonPrimitive.content,
//            items = json.decodeFromJsonElement(ListSerializer(OrderItemDTO.serializer()), jsonObject.getValue("items")),
//            totalPrice = jsonObject.getValue("totalPrice").jsonPrimitive.double,
//            totalAmount = jsonObject.getValue("totalAmount").jsonPrimitive.int
//        )
//    }
//}
