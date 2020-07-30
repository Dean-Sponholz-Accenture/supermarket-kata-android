package supermarket.model

import android.util.Log
import org.junit.Before
import org.junit.Test
import supermarket.FakeCatalog

class TellerTest {


    private lateinit var teller: Teller

    val appleProduct = Product("apple", ProductUnit.Each)
    val bananaProduct = Product("banana", ProductUnit.Each)
    val watermelonProduct = Product("watermelon", ProductUnit.Each)

    @Before
    fun setup() {
        val groceryCatalog = FakeCatalog().apply {
            putProduct(appleProduct, 1.00)
            putProduct(bananaProduct, 2.00)
            putProduct(watermelonProduct, 3.00)
        }

        teller = Teller(groceryCatalog)
    }

    @Test
    fun checkOut_noOffers_receiptHasCorrectTotalPrice() {
        val shoppingCart = ShoppingCart().apply {
            addItem(appleProduct)
            addItem(appleProduct)
            addItem(bananaProduct)
            addItem(bananaProduct)
            addItem(watermelonProduct)
        }
        val receipt = teller.checkOut(shoppingCart)

        assert(receipt.totalPrice == 9.00)
    }

    @Test
    fun checkOut_twoForOneOffers_receiptHasCorrectTotalPrice() {
        val shoppingCart = ShoppingCart().apply {
            addItemQuantity(appleProduct, 2.0)
            addItem(appleProduct)
            addItem(appleProduct)
            addItem(bananaProduct)
            addItem(bananaProduct)
            addItem(watermelonProduct)
        }
        teller.putSpecialOffer(SpecialOfferType.XForYDeal(2, 1.0), appleProduct)

        val receipt = teller.checkOut(shoppingCart)
        assert(receipt.totalPrice == 9.00)
    }
}
