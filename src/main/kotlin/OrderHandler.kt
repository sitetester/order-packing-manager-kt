class OrderHandler {

    private var containersHandler: ContainersHandler
    private var productsHandler: ProductsHandler

    // https://stackoverflow.com/questions/57249356/kotlin-array-property-in-data-class-error
    constructor(containerSpecs: List<ContainerSpec>) {

        this.containersHandler = ContainersHandler(containerSpecs)
        this.productsHandler = ProductsHandler()
    }

    fun packOrder(orderRequest: OrderRequest): ShipmentRecord {

        checkOrderExecutable(orderRequest)
        val containers: MutableList<Container> = getContainers(orderRequest)

        return ShipmentRecord(
            orderRequest.id,
            TotalVolume("cubic centimeter", getTotalVolume(containers)),
            containers
        )
    }

    // TODO: Apply logic
    private fun checkOrderExecutable(orderRequest: OrderRequest): Boolean {
        return true;
    }

    private fun getContainers(orderRequest: OrderRequest): MutableList<Container> {

        // https://stackoverflow.com/questions/33278869/how-do-i-initialize-kotlins-mutablelist-to-empty-mutablelist
        var containers: MutableList<Container> = mutableListOf()

        val availableContainers = this.containersHandler.getContainers()
        for (product in orderRequest.products) {
            var quantityAdded = 0

            for (containerSpec in availableContainers) {
                var containingProducts: MutableList<ContainingProduct> = mutableListOf()

                if (canStoreProduct(containerSpec, product)) {

                    if (canStoreProductPerOrderedQuantity(containerSpec, product)) {
                        containers.add(
                            Container(
                                containerSpec.containerType,
                                addToContainingProducts(containingProducts, product.id, product.orderedQuantity)
                            )
                        )

                        quantityAdded += product.orderedQuantity

                        // https://stackoverflow.com/questions/32540947/break-and-continue-in-foreach-in-kotlin
                        break // no need to check in next container

                    } else {
                        val howManyCanBeStored = this.howManyCanBeStored(containerSpec, product)
                        containers.add(
                            Container(
                                containerSpec.containerType,
                                addToContainingProducts(containingProducts, product.id, howManyCanBeStored)
                            )
                        )

                        quantityAdded += howManyCanBeStored
                    }

                }
            }

            val diff = product.orderedQuantity - quantityAdded
            if (diff > 0) {
                // same container could be used multiple times
                containers = this.reuseSameContainer(diff, containers)
            }
        }

        return containers
    }

    private fun canStoreProduct(containerSpec: ContainerSpec, product: Product): Boolean {

        return this.containersHandler.getContainerVolume(containerSpec) > this.productsHandler.getProductVolume(product)
    }

    private fun canStoreProductPerOrderedQuantity(containerSpec: ContainerSpec, product: Product): Boolean {

        return this.containersHandler.getContainerVolume(containerSpec) >= this.productsHandler.getProductVolumePerOrderedQuantity(
            product
        )
    }

    private fun howManyCanBeStored(containerSpec: ContainerSpec, product: Product): Int {

        val containerVolume = this.containersHandler.getContainerVolume(containerSpec)
        val productVolume = this.productsHandler.getProductVolume(product)
        var adjustableVolume = productVolume

        var quantity = 0
        while (adjustableVolume <= containerVolume) {
            adjustableVolume += productVolume
            quantity += 1
        }

        return quantity
    }

    private fun addToContainingProducts(
        containingProducts: MutableList<ContainingProduct>,
        id: String,
        quantity: Int
    ): List<ContainingProduct> {

        containingProducts.add(ContainingProduct(id, quantity))

        return containingProducts
    }


    private fun reuseSameContainer(howManyTimes: Int, containers: MutableList<Container>): MutableList<Container> {

        val container = containers[0]

        for (i in 1..howManyTimes) {
            containers.add(container)
        }

        return containers
    }

    private fun getTotalVolume(containers: MutableList<Container>): Int {

        var totalVolume = 0
        for (container in containers) {
            totalVolume += this.containersHandler.getContainerTypeVolume(container.containerType)
        }

        return totalVolume
    }
}

