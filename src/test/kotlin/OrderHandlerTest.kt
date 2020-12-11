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
}