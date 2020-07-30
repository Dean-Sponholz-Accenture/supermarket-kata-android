package supermarket

import supermarket.model.Product
import supermarket.model.SupermarketCatalog

class FakeCatalog : SupermarketCatalog {

    private val products = HashMap<String, Product>()
    private val prices = HashMap<String, Double>()

    override fun putProduct(product: Product, price: Double) {
        products[product.name] = product
        prices[product.name] = price
    }

    override fun getUnitPrice(product: Product): Double {
        return prices[product.name]!!
    }
}
