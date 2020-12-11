import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class OrderHandlerTest {

    private val containerSpecs: List<ContainerSpec> = listOf(
        ContainerSpec(
            "Cardboard A",
            Dimensions("centimeter", 30, 30, 30)
        ),
        ContainerSpec(
            "Cardboard B",
            Dimensions("centimeter", 10, 20, 20)
        )
    )

    private val orderHandler = OrderHandler(containerSpecs)

    @Test
    fun `Given a small order, pack it into a single container`() {

        val orderRequest = OrderRequest(
            id = "ORDER-001",
            listOf(
                Product(
                    "PRODUCT-001",
                    "GOOD FORTUNE COOKIES",
                    9,
                    13.4,
                    Dimensions(
                        "centimeter",
                        10,
                        10,
                        30,
                    )
                )
            )
        )

        val expectedShipmentRecord = ShipmentRecord(
            "ORDER-001",
            TotalVolume("cubic centimeter", 27000),
            listOf(
                Container("Cardboard A", listOf(ContainingProduct("PRODUCT-001", 9)))
            )
        )

        assertEquals(orderHandler.packOrder(orderRequest), expectedShipmentRecord)
    }

    @Test
    fun `Given a large order, pack it using multiple containers, without exceeding maximum capacity of any containers`() {
        val orderRequest = OrderRequest(
            "ORDER-002",
            listOf(
                Product(
                    "PRODUCT-002",
                    "BAD FORTUNE COOKIES",
                    10,
                    13.4,
                    Dimensions(
                        "centimeter",
                        10,
                        10,
                        30
                    )
                )
            )
        )

        ShipmentRecord(
            "ORDER-002",
            TotalVolume(
                "cubic centimeter",
                1
            ),
            listOf(
                Container(
                    "Cardboard A", listOf(
                        ContainingProduct("PRODUCT-002", 2)
                    )
                )
            )
        )

        val shipmentRecord = orderHandler.packOrder(orderRequest)
        // TODO
    }

    @Test
    fun `Given an order that cannot fit into any containers, throw an error`() {
        val orderRequest: OrderRequest = OrderRequest(
            "ORDER-003",
            listOf(
                Product(
                    "PRODUCT-001",
                    "GOOD FORTUNE COOKIES",
                    1,
                    13.4,
                    Dimensions(
                        "centimeter",
                        10,
                        10,
                        30
                    )
                ),
                Product(
                    "PRODUCT-003",
                    "GIANT FORTUNE COOKIES",
                    1,
                    99.95,
                    Dimensions(
                        "centimeter",
                        30,
                        30,
                        5
                    )
                )
            )
        )

        orderHandler.packOrder(orderRequest)
        // TODO
    }

    @Test
    fun `Given an order that packs into multiple containers, calculate the total volume of all containers used`() {

        val orderRequest = OrderRequest(
            "ORDER-004",
            listOf(
                Product(
                    "PRODUCT-004",
                    "ALMOST-GREAT FORTUNE COOKIES",
                    2,
                    13.4,
                    Dimensions(
                        "centimeter",
                        30,
                        30,
                        25
                    )
                )
            )
        )

        val expectedShipmentRecord = ShipmentRecord(
            "ORDER-004",
            TotalVolume(
                "cubic centimeter",
                54000,
            ),
            listOf(
                Container(
                    "Cardboard A",
                    listOf(
                        ContainingProduct(
                            "PRODUCT-004",
                            1
                        )
                    )
                ),
                Container(
                    "Cardboard A",
                    listOf(
                        ContainingProduct(
                            "PRODUCT-004",
                            1
                        )
                    )
                )
            )
        )

        assertEquals(orderHandler.packOrder(orderRequest), expectedShipmentRecord)
    }
}