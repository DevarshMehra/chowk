package chowk.packag.com.onlinestorekotlin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.paypal.android.sdk.payments.PayPalConfiguration
import com.paypal.android.sdk.payments.PayPalPayment
import com.paypal.android.sdk.payments.PayPalService
import com.paypal.android.sdk.payments.PaymentActivity
import kotlinx.android.synthetic.main.activity_finalize_shopping.*
import java.math.BigDecimal

class FinalizeShoppingActivity : AppCompatActivity() {
    // this activity is only called when we verify our temp orders in CartsProductsActivity


    var ttPrice: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finalize_shopping)

        var calculateTotalPriceUrl = "http://192.168.1.124//OnlineStoreApp/calculate_total_price.php?invoice_num=${intent.getStringExtra("LATEST_INVOICE_NUMBER")}"
        // we used the invoice number that we send from CartsProductsActivity

        var requestQ = Volley.newRequestQueue(this@FinalizeShoppingActivity)
        var stringRequest = StringRequest(Request.Method.GET, calculateTotalPriceUrl, Response.Listener { response ->
            // 'response' is the total price that is echoed in php code

            btnPaymentProcessing.text = "Pay $$response via Paypal Now!"
            ttPrice = response.toLong() // we are saving total price in ttpirce as this ttprice we can use later in the paypal service below

        }, Response.ErrorListener { error ->
            
            val dialogBuilder= AlertDialog.Builder(this)
            dialogBuilder.setTitle("Message")
            dialogBuilder.setMessage(error.message)
            dialogBuilder.create().show()            

        })


        requestQ.add(stringRequest)


        var paypalConfig: PayPalConfiguration = PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(MyPayPal.clientID)
        // we pass the clientID that we created in MyPayPal.kt to the sandbox environment
        // here we are chaining the method calls by chaining environment() and clientID() to PayPalConfiguration()

        //  Creating the PayPal service and starting it

        var ppService = Intent(this@FinalizeShoppingActivity, PayPalService::class.java)
        // we call services in android using Intent
        // pp -> PayPal

        ppService.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig)
        // sending paypalConfig to ppservice
        // (key, value) => value -> paypalconfig
        startService(ppService)

        btnPaymentProcessing.setOnClickListener {


            var ppProcessing = PayPalPayment(BigDecimal.valueOf(ttPrice),
                    "USD", "Online Store Kotlin!",
                    PayPalPayment.PAYMENT_INTENT_SALE)
            // converting long to BigDecimal (ttprice)
            // currency -> USD
            // Online Store Kotlin! -> this text will be showed in the PayPal page

            var paypalPaymentIntent = Intent(this, PaymentActivity::class.java)
            // we get PaymentActivity from the PayPal SDK that we implemented in our gradle

            paypalPaymentIntent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig) // (key, value)
            paypalPaymentIntent.putExtra(PaymentActivity.EXTRA_PAYMENT, ppProcessing) // (key, value)
            startActivityForResult(paypalPaymentIntent, 1000) // requestCode must be a unique value you use for this particular intent .. see below



        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == 1000) {

            if (resultCode == Activity.RESULT_OK) { // resultCode specifies in the PayPal payment went smoothly or not

                // if payment is successful, then go to Thank You page

                var intent = Intent(this, ThankYouActivity::class.java)
                startActivity(intent)

            } else { // if payment wasn't successful

                Toast.makeText(this, "Sorry! Something went wrong. Try Again", Toast.LENGTH_SHORT).show()

            }

        }
    }


    override fun onDestroy() {
        // once we start a servie (here ppService), we must make sure to end it otherwise we get memory leakes, app crashers, etc.

        // onDestroy() -> one of the Android Life Cycle methods
        // this method will be called when the activity is no longer in stack (no longer in use)

        super.onDestroy()

        stopService(Intent(this, PayPalService::class.java))
        // we want to stop PayPal Service
    }

    }



