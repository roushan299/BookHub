package fragment

import adapter.FavouriteRecyclerAdapter
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.roushan.bookhub.R
import database.BookDatabase
import database.BookEntity

class FavouritesFragment : Fragment() {
    private lateinit var recyclerViewFavourite: RecyclerView
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var favouriteRecyclerAdapter: FavouriteRecyclerAdapter
    var dbBookList = listOf<BookEntity>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favourites, container, false)

        recyclerViewFavourite = view.findViewById(R.id.recyclerViewFavourite)
        layoutManager = GridLayoutManager(activity as Context, 2)

        dbBookList = RetrieveFavourite(activity as Context).execute().get()


        if (activity != null){
            favouriteRecyclerAdapter = FavouriteRecyclerAdapter(activity as Context, dbBookList)
            recyclerViewFavourite.adapter = favouriteRecyclerAdapter
            recyclerViewFavourite.layoutManager = layoutManager
        }

        return view
    }

    class RetrieveFavourite(val context: Context): AsyncTask<Void, Void, List<BookEntity>>(){
        override fun doInBackground(vararg p0: Void?): List<BookEntity> {
            val db = Room.databaseBuilder(context, BookDatabase::class.java,"book-db").build()
            return db.bookDao().getAllBooks()

        }

    }


}