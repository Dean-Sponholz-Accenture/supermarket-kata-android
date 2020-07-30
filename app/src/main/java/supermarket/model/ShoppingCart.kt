package supermarket.model

import java.util.HashMap

class ShoppingCart {

    private var productQuantities: MutableMap<Product, Double> = HashMap()

    internal fun addItem(product: Product) {
        addItemQuantity(product, 1.0)
    }

    internal fun productQuantities(): Map<Product, Double> {
        return productQuantities
    }

    fun addItemQuantity(product: Product, quantity: Double) {
        if (productQuantities.containsKey(product)) {
            productQuantities[product] = productQuantities[product]!! + quantity
        } else {
            productQuantities[product] = quantity
        }
    }
}
