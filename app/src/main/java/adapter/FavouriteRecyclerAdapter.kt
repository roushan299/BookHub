package adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.roushan.bookhub.R
import com.squareup.picasso.Picasso
import database.BookEntity

class FavouriteRecyclerAdapter(private val context: Context, private val bookList: List<BookEntity>) : RecyclerView.Adapter<FavouriteRecyclerAdapter.FavouriteViewHolder>() {
    class FavouriteViewHolder(view: View): RecyclerView.ViewHolder(view){
        val txtFavBookName: TextView = view.findViewById(R.id.txtFavBookName)
        val txtFavBookAuthor: TextView = view.findViewById(R.id.txtFavBookAuthor)
        val txtFavBookPrice: TextView = view.findViewById(R.id.txtFavBookPrice)
        val txtFavBookRating: TextView = view.findViewById(R.id.txtFavBookRating)
        val imgFavBook: ImageView = view.findViewById(R.id.imgFavBookImage)
        val layoutFavBook: LinearLayout = view.findViewById(R.id.layoutFavBook)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_favourite_single_row,parent,false)
        return FavouriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val book = bookList[position]
        holder.txtFavBookName.text = book.bookName
        holder.txtFavBookAuthor.text = book.bookAuthor
        holder.txtFavBookPrice.text = book.bookPrice
        holder.txtFavBookRating.text = book.bookRating
        Picasso.get().load(book.bookImage).error(R.drawable.default_book_cover).into(holder.imgFavBook)
        holder.layoutFavBook.setOnClickListener {
            Toast.makeText(context, "you Selected the  ${book.bookName}", Toast.LENGTH_SHORT).show()
        }

    }

    override fun getItemCount(): Int {
        return bookList.size
    }
}