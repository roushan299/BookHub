package activity

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.roushan.bookhub.R
import com.squareup.picasso.Picasso
import database.BookDatabase
import database.BookEntity
import org.json.JSONException
import org.json.JSONObject
import util.connectionManager
import kotlin.collections.HashMap

class DescriptionActivity : AppCompatActivity() {
    //Declaring all the variables that are used in the xml file
    private lateinit var toolbar: Toolbar
    private lateinit var iContent: LinearLayout
    private lateinit var imgBookImage: ImageView
    private lateinit var txtBookName: TextView
    private lateinit var txtBookAuthor:TextView
    private lateinit var txtBookPrice: TextView
    private lateinit var txtBookRating: TextView
    private lateinit var txtAboutTheBookStatic: TextView
    private lateinit var txtBookDesc: TextView
    private lateinit var btnAddFav: Button
    private lateinit var progressLayout: RelativeLayout
    private lateinit var progressBar: ProgressBar

   private var bookId: String? = "100"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        // linking with the the ids
        toolbar = findViewById(R.id.toolbar)
        iContent = findViewById(R.id.IContent)
        imgBookImage = findViewById(R.id.imgBookImage)
        txtBookName = findViewById(R.id.txtBookName)
        txtBookAuthor = findViewById(R.id.txtBooKAuthor)
        txtBookPrice = findViewById(R.id.txtBookPrice)
        txtBookRating = findViewById(R.id.txtBookRating)
        txtAboutTheBookStatic = findViewById(R.id.txtAboutTheBookStatic)
        txtBookDesc = findViewById(R.id.txtBookDesc)
        btnAddFav = findViewById(R.id.btnAddToFav)

        setSupportActionBar(toolbar)
        supportActionBar?.title="Book Details"

       //making visible progress bar
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        //making visible progress layout
        progressLayout = findViewById(R.id.progressLayout)
        progressLayout.visibility = View.VISIBLE

        if(intent != null){
            bookId = intent.getStringExtra("book_id")
        } else{
            Toast.makeText(this@DescriptionActivity, "Some unexpected error occurred", Toast.LENGTH_SHORT).show()
        }
        if (bookId == "100"){
            Toast.makeText(this@DescriptionActivity, "Some unexpected error occurred", Toast.LENGTH_SHORT).show()
            finish()
        }

        val queue = Volley.newRequestQueue(this@DescriptionActivity)
        val url = "http://13.235.250.119/v1/book/get_book/"
        val jsonParams = JSONObject()
            jsonParams.put("book_id",bookId)

        if (connectionManager().checkConnectivity(this@DescriptionActivity)){
            val jsonObject =
                object: JsonObjectRequest(Method.POST,url,jsonParams,Response.Listener {

                try {

                    val success = it.getBoolean("success")
                    if(success){
                        val bookJsonObject = it.getJSONObject("book_data")
                        progressLayout.visibility = View.GONE


                        val bookImageUrl = bookJsonObject.getString("image")
                        Picasso.get().load(bookJsonObject.getString("image"))
                            .error(R.drawable.default_book_cover).into(imgBookImage)
                        txtBookName.text = bookJsonObject.getString("name")
                        txtBookAuthor.text = bookJsonObject.getString("author")
                        txtBookPrice.text = bookJsonObject.getString("price")
                        txtBookRating.text = bookJsonObject.getString("rating")
                        txtBookDesc.text = bookJsonObject.getString("description")


                        val bookEntity=  BookEntity(
                        bookId?.toInt() as Int,
                        txtBookName.text.toString(),
                        txtBookAuthor.text.toString(),
                        txtBookPrice.text.toString(),
                            txtBookRating.text.toString(),
                            txtBookDesc.text.toString(),
                            bookImageUrl
                        )

                        val checkFav = DBAsyncTask(applicationContext,bookEntity,1).execute()
                        val isFav = checkFav.get()


                        if (isFav){
                            btnAddFav.text = "Remove from Favourite"
                            val favColor = ContextCompat.getColor(applicationContext,R.color.blue)
                            btnAddFav.setBackgroundColor(favColor)
                        }
                        else{
                            btnAddFav.text = "Add to Favourite"
                            val noFavColor = ContextCompat.getColor(applicationContext,R.color.purple_500)
                            btnAddFav.setBackgroundColor(noFavColor)
                        }


                        btnAddFav.setOnClickListener {
                            if (!DBAsyncTask(applicationContext,bookEntity,1).execute().get()){
                                val async = DBAsyncTask(applicationContext,bookEntity,2).execute()
                                val result = async.get()
                                if(result){
                                    Toast.makeText(this@DescriptionActivity, "Book added to favourites", Toast.LENGTH_SHORT).show()
                                         btnAddFav.text = "Remove from Favourites"
                                    val favColor = ContextCompat.getColor(applicationContext,R.color.blue)
                                    btnAddFav.setBackgroundColor(favColor)
                                }
                                else{
                                    Toast.makeText(this@DescriptionActivity, "Some error occurred", Toast.LENGTH_SHORT).show()
                                }

                            }else{
                                val async = DBAsyncTask(applicationContext,bookEntity,3).execute()
                                val result = async.get()
                                if(result){
                                    Toast.makeText(this@DescriptionActivity, "Book removed from Favourites", Toast.LENGTH_SHORT).show()
                                    btnAddFav.text= "Add to Favourite"
                                    val noFavColor = ContextCompat.getColor(applicationContext,R.color.purple_500)
                                    btnAddFav.setBackgroundColor(noFavColor)
                                }else{
                                    Toast.makeText(this@DescriptionActivity, "Some error occurred", Toast.LENGTH_SHORT).show()
                                }

                            }
                        }

                    }
                    else{
                        Toast.makeText(this@DescriptionActivity, "Some error occurred!!!", Toast.LENGTH_SHORT).show()
                    }

                }   catch (e: JSONException){
                    Toast.makeText(this@DescriptionActivity, "Some unexpected error occurred!!!", Toast.LENGTH_SHORT).show()
                }
                }, Response.ErrorListener{
                    Toast.makeText(this@DescriptionActivity, "Volley Error $it", Toast.LENGTH_SHORT).show()
                }){
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Context-type"] = "android/json"
                        headers["token"] = "5fd00fe5ce03f3"
                        return headers
                    }
                }
            queue.add(jsonObject)
        }
        else{
            val alertDialog = AlertDialog.Builder(this@DescriptionActivity)
               alertDialog.setTitle("Error")
                alertDialog.setMessage("Internet connection is not found")
                alertDialog.setPositiveButton("Open Settings"){text, listener->
                    val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingIntent)
                    finish()
                }
            alertDialog.setNegativeButton("Exit"){text, listener->
                ActivityCompat.finishAffinity(this@DescriptionActivity)
            }
            alertDialog.create()
            alertDialog.show()
        }



            //Database storage work
    }

    class DBAsyncTask(val context: Context, val bookEntity: BookEntity, val mode: Int) : AsyncTask<Void, Void, Boolean>(){

         val db = Room.databaseBuilder(context,BookDatabase::class.java,"book-db").build()
        override fun doInBackground(vararg p0: Void?): Boolean {

            when(mode){
                1 ->{
                    val book: BookEntity? = db.bookDao().getBookById(bookEntity.book_id.toString())
                    db.close()
                    return true
                }

                2 ->{
                    db.bookDao().insertBook(bookEntity)
                    db.close()
                    return true
                }

                3 ->{

                    db.bookDao().deleteBook(bookEntity)
                    db.close()
                    return true
                }
            }
            return false
        }

    }


}