package supermarket.model

sealed class SpecialOfferType(open val requiredProductThreshold: Int) {

    data class XForYDeal(override val requiredProductThreshold: Int, val atPriceOfNumberOfProducts: Double) :
        SpecialOfferType(requiredProductThreshold)

    data class PercentOffDeal(val percentageOff: Double) : SpecialOfferType(1)
}
