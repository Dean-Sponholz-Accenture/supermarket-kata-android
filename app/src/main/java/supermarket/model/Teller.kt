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

                //item count
                var requiredDiscountThreshold = when (offer.offerType) {
                    SpecialOfferType.ThreeForTwo -> 3
                    SpecialOfferType.TwoForAmount -> 2
                    SpecialOfferType.FiveForAmount -> 5
                    SpecialOfferType.TenPercentDiscount -> 1


                }
                val numberOfSpecialOffersApplied = quantityAsInt / requiredDiscountThreshold

                //discount amount
                discount = when (offer.offerType) {
                    SpecialOfferType.ThreeForTwo -> {
                        if (quantityAsInt >= 3){
                            val discountAmount =
                                quantity * unitPrice - (numberOfSpecialOffersApplied.toDouble() * 2.0 * unitPrice + quantityAsInt % 3 * unitPrice)
                          Discount(product, "3 for 2", discountAmount)
                        }
                        else{
                            null
                        }
                    }
                    SpecialOfferType.TenPercentDiscount -> {
                            Discount(
                                product,
                                offer.argument.toString() + "% off",
                                quantity * unitPrice * offer.argument / 100.0
                            )
                    }
                    SpecialOfferType.FiveForAmount -> {
                        if (quantityAsInt >= 5){
                            val nonDiscountedPrice = unitPrice * quantity
                            val remainingUnitsAfterDiscount = (quantityAsInt % 5)
                            val discountedPrice = (offer.argument * numberOfSpecialOffersApplied + remainingUnitsAfterDiscount * unitPrice)
                            val discountTotal = nonDiscountedPrice - discountedPrice
                            Discount(product, requiredDiscountThreshold.toString() + " for " + offer.argument, discountTotal)
                        }
                        else{
                            null
                        }
                    }
                    SpecialOfferType.TwoForAmount -> {
                        if (quantityAsInt >= 2){
                            val total = offer.argument * (quantityAsInt / requiredDiscountThreshold) + quantityAsInt % 2 * unitPrice
                            val discountN = unitPrice * quantity - total
                            Discount(product, "2 for " + offer.argument, discountN)
                        }
                        else{
                            null
                        }
                    }
                }

                if (discount != null) {
                    receipt.addDiscount(discount)
                }
            }
        }
    }
}
