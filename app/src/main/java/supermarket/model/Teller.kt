package supermarket.model

import java.util.HashMap

class Teller(private val catalog: SupermarketCatalog) {

    private val specialOffers = HashMap<Product, SpecialOffer>()

    fun putSpecialOffer(offerType: SpecialOfferType, product: Product, argument: Double) {
        specialOffers[product] = SpecialOffer(offerType, product, argument)
    }

    fun checkOut(cart: ShoppingCart): Receipt {
        val receipt = Receipt()
        val productQuantities = cart.getItems()
        productQuantities.forEach { pq ->
            val product = pq.product
            val quantity = pq.quantity
            val unitPrice = catalog.getUnitPrice(product)
            val price = quantity * unitPrice
            receipt.addProduct(product, quantity, unitPrice, price)
        }
        applyOffers(receipt, specialOffers, catalog)

        return receipt
    }

    private fun applyOffers(receipt: Receipt, offers: Map<Product, SpecialOffer>, catalog: SupermarketCatalog) {
        receipt.getItems().forEach { receiptItem ->
            val product = receiptItem.product
            val quantity = receiptItem.quantity

            if (offers.containsKey(product)) {
                val offer = offers[product]!!
                val unitPrice = catalog.getUnitPrice(product)
                val quantityAsInt = quantity.toInt()
                var discount: Discount? = null
                var x = 1
                if (offer.offerType === SpecialOfferType.ThreeForTwo) {
                    x = 3
                } else if (offer.offerType === SpecialOfferType.TwoForAmount) {
                    x = 2
                    if (quantityAsInt >= 2) {
                        val total = offer.argument * (quantityAsInt / x) + quantityAsInt % 2 * unitPrice
                        val discountN = unitPrice * quantity - total
                        discount = Discount(product, "2 for " + offer.argument, discountN)
                    }
                }
                if (offer.offerType === SpecialOfferType.FiveForAmount) {
                    x = 5
                }
                val numberOfXs = quantityAsInt / x
                if (offer.offerType === SpecialOfferType.ThreeForTwo && quantityAsInt > 2) {
                    val discountAmount =
                        quantity * unitPrice - (numberOfXs.toDouble() * 2.0 * unitPrice + quantityAsInt % 3 * unitPrice)
                    discount = Discount(product, "3 for 2", discountAmount)
                }
                if (offer.offerType === SpecialOfferType.TenPercentDiscount) {
                    discount =
                        Discount(
                            product,
                            offer.argument.toString() + "% off",
                            quantity * unitPrice * offer.argument / 100.0
                        )
                }
                if (offer.offerType === SpecialOfferType.FiveForAmount && quantityAsInt >= 5) {
                    val discountTotal =
                        unitPrice * quantity - (offer.argument * numberOfXs + quantityAsInt % 5 * unitPrice)
                    discount = Discount(product, x.toString() + " for " + offer.argument, discountTotal)
                }
                if (discount != null) {
                    receipt.addDiscount(discount)
                }
            }
        }
    }
}
