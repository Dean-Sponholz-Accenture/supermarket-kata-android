package supermarket

import org.junit.Test
import supermarket.model.Product
import supermarket.model.ProductUnit
import supermarket.model.ShoppingCart
import supermarket.model.SpecialOfferType
import supermarket.model.Teller

class SupermarketTest {

    @Test
    fun testSomething() {
        val catalog = FakeCatalog()
        val toothbrush = Product("toothbrush", ProductUnit.Each)
        catalog.putProduct(toothbrush, 0.99)
        val apples = Product("apples", ProductUnit.Kilo)
        catalog.putProduct(apples, 1.99)

        val cart = ShoppingCart()
        cart.addItemQuantity(apples, 2.5)
        cart.addItemQuantity(toothbrush, 1.0)

        val teller = Teller(catalog)
        teller.putSpecialOffer(SpecialOfferType.TenPercentDiscount, toothbrush, 10.0)

        val receipt = teller.checkOut(cart)

        // TODO: This just prints a receipt to give you an idea how the code works.
        println(ReceiptPrinter().printReceipt(receipt))
    }
}
