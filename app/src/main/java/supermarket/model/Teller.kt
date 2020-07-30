package supermarket.model

import java.util.HashMap

class Teller(private val catalog: SupermarketCatalog) {

    private val specialOffers = HashMap<Product, SpecialOffer>()

    fun putSpecialOffer(offerType: SpecialOfferType, product: Product) {
        specialOffers[product] = SpecialOffer(offerType, product)
    }

    fun checkOut(cart: ShoppingCart): Receipt {
        val receipt = Receipt()
        val productQuantities = cart.productQuantities()
        productQuantities.forEach { productQuantity ->
            val product = productQuantity.key
            val quantity = productQuantity.value
            val unitPrice = catalog.getUnitPrice(product)
            val price = quantity * unitPrice
            receipt.addProduct(product, quantity, unitPrice, price)
        }
        val receiptWithSpecialDiscounts = applyOffers(receipt, specialOffers, catalog)

        return receiptWithSpecialDiscounts
    }

    private fun applyOffers(
        receipt: Receipt,
        offers: Map<Product, SpecialOffer>,
        catalog: SupermarketCatalog
    ): Receipt {
        receipt.getItems().forEach { receiptItem ->
            val product = receiptItem.product
            val quantity = receiptItem.quantity

            if (offers.containsKey(product)) {
                val offer = offers[product]!!
                val unitPrice = catalog.getUnitPrice(product)
                val quantityAsInt = quantity.toInt()

                //item count
                val requiredDiscountThreshold = offer.offerType.requiredProductThreshold
                val numberOfSpecialOffersApplied = quantityAsInt / requiredDiscountThreshold

                //discount amount
                val discount = when (val offerType = offer.offerType) {
                    is SpecialOfferType.XForYDeal -> {
                        getDiscountAmount(
                            quantity,
                            requiredDiscountThreshold,
                            unitPrice,
                            offerType.atPriceOfNumberOfProducts,
                            numberOfSpecialOffersApplied,
                            product
                        )
                    }
                    is SpecialOfferType.PercentOffDeal -> {
                        Discount(
                            product,
                            offerType.percentageOff.toString() + "% off",
                            quantity * unitPrice * offerType.percentageOff / 100.0
                        )
                    }
                }

                if (discount != null) {
                    receipt.addDiscount(discount)
                }
            }
        }
        return receipt
    }

    private fun getDiscountAmount(
        quantity: Double,
        requiredDiscountThreshold: Int,
        unitPrice: Double,
        atPriceOfNumberOfProducts: Double,
        numberOfSpecialOffersApplied: Int,
        product: Product
    ): Discount? {
        val quantityAsInt = quantity.toInt()
        val discount = if (quantityAsInt >= requiredDiscountThreshold) {
            val nonDiscountedPrice = unitPrice * quantity
            val remainingUnitsAfterDiscount = (quantityAsInt % requiredDiscountThreshold)
            val discountedPrice =
                (atPriceOfNumberOfProducts * numberOfSpecialOffersApplied + remainingUnitsAfterDiscount * unitPrice)
            val discountTotal = nonDiscountedPrice - discountedPrice
            Discount(
                product,
                requiredDiscountThreshold.toString() + " for " + atPriceOfNumberOfProducts.toString(),
                discountTotal
            )
        } else {
            null
        }
        return discount
    }
}

//val offer = offers[product]!!
//val unitPrice = catalog.getUnitPrice(product)
//val quantityAsInt = quantity.toInt()