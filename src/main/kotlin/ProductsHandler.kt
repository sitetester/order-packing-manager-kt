class ProductsHandler {

    fun getProductVolume(product: Product): Int {

        return DimensionsHelper().getDimensionsVolume(product.dimensions)
    }

    fun getProductVolumePerOrderedQuantity(product: Product): Int {

        return this.getProductVolume(product) * product.orderedQuantity
    }
}