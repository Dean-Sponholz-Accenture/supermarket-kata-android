package supermarket.model

interface SupermarketCatalog {

    fun putProduct(product: Product, price: Double)

    fun getUnitPrice(product: Product): Double
}
