package adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.roushan.bookhub.Book
import activity.DescriptionActivity
import com.roushan.bookhub.R
import com.squareup.picasso.Picasso

class DashboardRecyclerAdapter(private val context: Context, private val itemList: ArrayList<Book>) :RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>(){
    class DashboardViewHolder(view: View): RecyclerView.ViewHolder(view){
        val textBookName: TextView = view.findViewById(R.id.txtBookName)
        val textBookAuthor: TextView = view.findViewById(R.id.txtBooKAuthor)
        val textBooKPrice: TextView = view.findViewById(R.id.txtBookPrice)
        val textBookRating: TextView = view.findViewById(R.id.txtBookRating)
        val imgBookImage: ImageView = view.findViewById(R.id.imgBookImage)
        val iContent: LinearLayout = view.findViewById(R.id.iContent)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
       val view = LayoutInflater
           .from(parent.context)
           .inflate(R.layout.recycler_dashboard_single_row,
               parent,
               false)

        return DashboardViewHolder(view)
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val book = itemList[position]
        holder.textBookName.text = book.bookName
        holder.textBooKPrice.text = book.bookPrice
        holder.textBookAuthor.text = book.bookAuthor
        holder.textBookRating.text = book.bookRating
       // holder.imgBookImage.setImageResource(book.bookImage)
        Picasso.get().load(book.bookImage).error(R.drawable.default_book_cover).into(holder.imgBookImage)
        holder.iContent.setOnClickListener {
            Toast.makeText(context, "clicked on ${holder.textBookName.text}", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, DescriptionActivity::class.java)
            intent.putExtra("book_id", book.bookId)
            context.startActivity(intent)
        }


    }

    override fun getItemCount(): Int {
        return  itemList.size
    }
}