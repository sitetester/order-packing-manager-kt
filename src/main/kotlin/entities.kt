data class ContainerSpec(
    val containerType: String,
    val dimensions: Dimensions
)

data class Dimensions(
    val unit: String,
    val length: Int,
    val width: Int,
    val height: Int,
)

data class OrderRequest(val id: String, val products: List<Product>)

data class Product(
    val id: String,
    val name: String,
    val orderedQuantity: Int,
    val unitPrice: Double,
    val dimensions: Dimensions,
)

data class TotalVolume(
    val unit: String,
    val value: Int
)

data class ContainingProduct(
    val id: String,
    val quantity: Int
)

data class Container(
    val containerType: String,
    val containingProducts: List<ContainingProduct>
)

data class ShipmentRecord(
    val orderId: String,
    val totalVolume: TotalVolume,
    val containers: List<Container>
)

