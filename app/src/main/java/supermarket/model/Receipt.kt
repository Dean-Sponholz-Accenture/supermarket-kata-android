package supermarket.model

import java.util.*

class Receipt {

    private val items = ArrayList<ReceiptItem>()
    private val discounts = ArrayList<Discount>()

    val totalPrice: Double?
        get() {
            var total = 0.0
            items.forEach { item ->
                total += item.totalPrice
            }
            discounts.forEach { discount ->
                total -= discount.discountAmount
            }
            return total
        }

    fun addProduct(product: Product, quantity: Double, price: Double, totalPrice: Double) {
        items.add(ReceiptItem(product, quantity, price, totalPrice))
    }

    fun getItems(): List<ReceiptItem> {
        return ArrayList(items)
    }

    fun addDiscount(discount: Discount) {
        discounts.add(discount)
    }

    fun getDiscounts(): List<Discount> {
        return discounts
    }
}
