package chowk.packag.com.onlinestorekotlin

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_fetch_eproducts.*
import java.util.*

class FetchProductsActivity : AppCompatActivity() {
// this activity displays all the products of that specific brand

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetch_products) // links to the xml file

        val selectedBrand:String = intent.getStringExtra("BRAND")
        // passing key -> BRAND and getting the value
        // selectedBrand is the tapped brand

        txtBrandName.text = "Products of $selectedBrand"
        // displaying the Products of 'that brand' in the top in our textView (id - txtBrandName )

        var productsList = ArrayList<Product>()
        // this will hold products of type EProduct class

        val productsUrl = "http://192.168.1.124/OnlineStoreApp/fetch_eproducts.php?brand=$selectedBrand"
        // getting the result from URL of selected brand
        // URL we've generated from PHP Code

        val requestQ = Volley.newRequestQueue(this@FetchProductsActivity)

        // using JsonArrayRequest as the URL gives us an array of JSON Objects
        val jsonAR = JsonArrayRequest(Request.Method.GET, productsUrl, null, Response.Listener {
            response ->


            for (productJOIndex in 0.until(response.length())) {
                // productJOIndex will hold index of the JSON Objects that come in our response


                productsList.add(Product(response.getJSONObject(productJOIndex).getInt("id"), response.getJSONObject(productJOIndex).getString("name"), response.getJSONObject(productJOIndex).getInt("price"), response.getJSONObject(productJOIndex).getString("picture")))
                // in getInt() or getString(), we are giving the key, and in return we get the value


            }

            val pAdapter = ProductAdapter(this@FetchProductsActivity, productsList)
            // the constructor of EProductAdapter class will take these input parameters
            // and return an object
            // typical Adapter job being done -> populate the view with entries from db

            // pAdapter ->product adapter

            productsRV.layoutManager  = LinearLayoutManager(this@FetchProductsActivity)
            // we want to show products linearly

            productsRV.adapter = pAdapter

        }, Response.ErrorListener { error ->

            val dialogBuilder= AlertDialog.Builder(this)
            dialogBuilder.setTitle("Message")
            dialogBuilder.setMessage(error.message)
            dialogBuilder.create().show()

        })

        requestQ.add(jsonAR)

    }
}
