package chowk.packag.com.onlinestorekotlin

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_home_screen.*

// this activity displays the brands (seller) user wants to buy from
class HomeScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)

        // connecting to brands database
        // instead of localhost we put our own pc's ipv4 address
        var brandsUrl = "http://192.168.1.124/OnlineStoreApp/fetch_brands.php"

        // will hold the brands in an array
        var brandsList = ArrayList<String>()

        var requestQ = Volley.newRequestQueue(this@HomeScreen)



        var jsonAR = JsonArrayRequest(Request.Method.GET, brandsUrl, null, Response.Listener {
                response ->  // this response will be an array of json objects


            for (jsonObject in 0.until(response.length())) {
                // The int variable (which is an index to our JSON Object) jsonObject will run through each response and
                // add the brand to brandList
                brandsList.add(response.getJSONObject(jsonObject).getString("brand"))

            }

            // Implemented the MVC design pattern here
            // Model -> Data from Server ... View -> ListView ... Controller -> brandsListAdapter
            // this adapter(controller) is used to populate our listview by transferring data from model to view
            var brandsListAdapter = ArrayAdapter(this@HomeScreen, R.layout.brand_item_text_view, brandsList)
            // brand_item_text_view is our text view that we created and we're treating it as custom row in
            // our listview -- hence this helps in changing font, color, size etc.
            brandsListView.adapter = brandsListAdapter


        }, Response.ErrorListener { error -> // if some error comes and we dont get response from URL
            // errors can come as we're dealing with internet ( eg- network connection, etc.)

            val dialogBuilder= AlertDialog.Builder(this)
            dialogBuilder.setTitle("Message")
            dialogBuilder.setMessage(error.message)
            dialogBuilder.create().show()
        })


        requestQ.add(jsonAR)



        brandsListView.setOnItemClickListener { adapterView, view, i, l -> // here we start the intent of that specific brand's products

            // Parent -> adapterView -> the Adapter View where the click happened
            // view -> the view within the adapterview which was clicked (this will be a view provided
               // by the adapter)
            // i -> index (int)-> position of the view in the adapter .. 0th index .. 1st index, etc.
            // l - (long) - the row id of the item that was clicked

            val tappedBrand = brandsList.get(i) // getting the brand that was tapped in brandsList
            // brandsList.get(i) => brandsList[i]

            // this intent will transfer us from HomeScreen activity (brands page)
            // to the page containing all products of that particular brand
            val intent = Intent(this@HomeScreen, FetchProductsActivity::class.java)
            //context -> HomeScreen => we actually dont need to specify HomeScreen (as we've already
            //specified 'this' , but we're still writing cuz its a good practice to keep it clear

            intent.putExtra("BRAND", tappedBrand)
            // (key, value) --> its a good practice to put key name in caps
            // here we're passing our tappedBrand to the intent

            startActivity(intent)
            // now starting the intent variable


        }


    }
}
