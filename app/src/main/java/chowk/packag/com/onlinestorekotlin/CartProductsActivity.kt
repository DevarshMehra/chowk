package chowk.packag.com.onlinestorekotlin

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_cart_products.*
import java.util.*

class CartProductsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_products)

        var cartProductsUrl = "http://192.168.1.124/OnlineStoreApp/fetch_temporary_order.php?email=${Person.email}"
        var cartProductsList = ArrayList<String>()
        var requestQ = Volley.newRequestQueue(this@CartProductsActivity)
        var jsonAR = JsonArrayRequest(Request.Method.GET, cartProductsUrl, null, Response.Listener{
            response -> // response is an array of JSON Objects

            // using for loop to iterate over the objects of the array
            for (joIndex in 0.until(response.length())) { // id, name, price, email, amount
                // joIndex -> variable

                cartProductsList.add("${response.getJSONObject(joIndex).getInt("id")}" +
                        " \n ${response.getJSONObject(joIndex).getString("name")}" +
                        " \n ${response.getJSONObject(joIndex).getInt("price")}" +
                        " \n ${response.getJSONObject(joIndex).getString("email")}" +
                        " \n ${response.getJSONObject(joIndex).getInt("amount")}")

            }

            // using the default android ArrayAdapter
            var cartProductsAdapter = ArrayAdapter(this@CartProductsActivity, android.R.layout.simple_list_item_1, cartProductsList)
            cartProductsListView.adapter = cartProductsAdapter

        }, Response.ErrorListener { error ->
            
            val dialogBuilder= AlertDialog.Builder(this)
            dialogBuilder.setTitle("Message")
            dialogBuilder.setMessage(error.message)
            dialogBuilder.create().show()
                
        })


        requestQ.add(jsonAR)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // calling the menu here
        // the menu options icons will be on the top toolbar (3 dots), and will be always visible
        menuInflater.inflate(R.menu.cart_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // once a menu option is clicked, this func will perform the respective operation

       if (item?.itemId == R.id.continueShoppingItem) {
           // if user has clicked on 'Continue Shopping' icon,
           // then go from Temp Orders page to Products page (Choose your Brand - page)

           var intent = Intent(this, HomeScreen::class.java)
           startActivity(intent)

       } else if (item?.itemId == R.id.declineOrderItem) {
           // if user has chosen to decline current temp orders
           // then remove these orders from the database
           // and from the users android app (from listview in CartsProductsActivity)

           var deleteUrl = "http://192.168.1.124//OnlineStoreApp/decline_order.php?email=${Person.email}"
           // this URL itself will delete the items from the database

           var requestQ = Volley.newRequestQueue(this@CartProductsActivity)
           var stringRequest = StringRequest(Request.Method.GET, deleteUrl, Response.Listener{
               response ->
               // deleting orders in users app, moving to HomeScreen ( Choose your Brand - page )


               var intent = Intent(this, HomeScreen::class.java)
               startActivity(intent)

           }, Response.ErrorListener {
               error ->
               
               val dialogBuilder= AlertDialog.Builder(this)
               dialogBuilder.setTitle("Message")
               dialogBuilder.setMessage(error.message)
               dialogBuilder.create().show()
               
           })

           requestQ.add(stringRequest)
       } else if (item?.itemId == R.id.verifyOrderItem) {


           var verifyOrderUrl = "http://192.168.1.124/OnlineStoreApp/verify_order.php?email=${Person.email}"
           // this URL is generated when we run the php code
           // the php code does all the changes in the database
           // in the php code, invoice number is generated and details are added in the invoice table (date, time, etc.)
           // and the selections in the temp order table are first transferred to the invoice table and
           // then sequentially deleted row wise from temp orders table

           var requestQ = Volley.newRequestQueue(this@CartProductsActivity)
           var stringRequest = StringRequest(Request.Method.GET, verifyOrderUrl, Response.Listener {
               response -> // response is holding the latest invoice number (which gets auto incremented in our db everytime an invoice is created
               // moving to FinalizeShoppingActivity


               var intent = Intent(this, FinalizeShoppingActivity::class.java)
               Toast.makeText(this, response, Toast.LENGTH_LONG).show() // this toasts the invoice number

               intent.putExtra("LATEST_INVOICE_NUMBER", response) // passing the LATEST_INVOICE_NUMBER to FinalizeShoppingActivity
               // this invoice number is used in making the final bill
               // LATEST_INVOICE_NUMBER -> this is the key (key is conventionally written in caps)
               // response -> value

               startActivity(intent)


           }, Response.ErrorListener { error ->  
               
               val dialogBuilder= AlertDialog.Builder(this)
               dialogBuilder.setTitle("Message")
               dialogBuilder.setMessage(error.message)
               dialogBuilder.create().show()
           
           })



           requestQ.add(stringRequest)
       }

        return super.onOptionsItemSelected(item)
    }

}
