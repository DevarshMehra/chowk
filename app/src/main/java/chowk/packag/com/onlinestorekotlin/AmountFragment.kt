package chowk.packag.com.onlinestorekotlin


import android.app.DialogFragment
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

// this file relates to fragment_amount.xml

// this is the dialog fragment that we use when we click on + sign
// DialogFragment is just like a pop up on the screen that pauses the current activity and pops itself up
class AmountFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // : View? => (?) - it is optional to return a value of type View

        // Inflate the layout for this fragment

        var fragmentView =  inflater.inflate(R.layout.fragment_amount, container, false)


        // creating references to the ids of the EditText and Button in fragment_amount.xml
        // as we can't directly use the their ids
        var edtEnterAmount = fragmentView.findViewById<EditText>(R.id.edtEnterAmount)
        var btnAddToCart = fragmentView.findViewById<ImageButton>(R.id.btnAddToCart)

        btnAddToCart.setOnClickListener {

            // ptoUrl (place temp order URL) -> links to our temp order table
            var ptoUrl = "http://192.168.1.124/OnlineStoreApp/insert_temporary_order.php?email=${Person.email}&product_id=${Person.addToCartProductID}&amount=${edtEnterAmount.text.toString()}"
            // we are using the Person class for details of email,..
            // instead of $ we can also use normal + to add in URL
            // & separates the inputs in the URL

            var requestQ = Volley.newRequestQueue(activity)
            var stringRequest = StringRequest(Request.Method.GET, ptoUrl, Response.Listener{ response ->

                var intent = Intent(activity, CartProductsActivity::class.java)
                // intent -> transition from current activity ('activity') to CartProductsActivity

                startActivity(intent)


            }, Response.ErrorListener { error ->
                
                val dialogBuilder= AlertDialog.Builder(this)
                dialogBuilder.setTitle("Message")
                dialogBuilder.setMessage(error.message)
                dialogBuilder.create().show()
                
            })

            requestQ.add(stringRequest)

        }

        return fragmentView

    }


}
