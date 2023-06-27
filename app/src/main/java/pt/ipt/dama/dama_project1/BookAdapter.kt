package pt.ipt.dama.dama_project1

import android.content.Context;
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.View;
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView;
import java.io.InputStream
import java.net.URL

class BookAdapter(context: Context, books: List<ReturnBook>) :
    ArrayAdapter<ReturnBook>(context, 0, books) {

    /**
     * Returns a list item view that displays information about the book
     * in the list of books.
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItemView = convertView
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(
                R.layout.book, parent, false
            )
        }

        val currentBook = getItem(position)

        val titleView: TextView = listItemView!!.findViewById(R.id.title)
        val subtitleView: TextView = listItemView.findViewById(R.id.subtitle)
        val authorsView: TextView = listItemView.findViewById(R.id.author)
        val descriptionView: TextView = listItemView.findViewById(R.id.description)
        val imageView: ImageView = listItemView.findViewById(R.id.imageView)

        titleView.text = currentBook?.title
        subtitleView.text = currentBook?.subtitle
        authorsView.text = currentBook?.authors
        descriptionView.text = currentBook?.description

        if (currentBook?.imageUrl == "") {
            imageView.setImageResource(R.drawable.ic_launcher_background)
        } else {
            DownloadImageTask(imageView).execute(currentBook?.imageUrl)
        }

        return listItemView
    }

    private inner class DownloadImageTask(private val bmImage: ImageView) :
        AsyncTask<String, Void, Bitmap>() {

        override fun doInBackground(vararg urls: String): Bitmap? {
            val urldisplay = urls[0]
            var mIcon11: Bitmap? = null
            try {
                val `in`: InputStream = URL(urldisplay).openStream()
                mIcon11 = BitmapFactory.decodeStream(`in`)
            } catch (e: Exception) {
                Log.e("Error", e.message)
                e.printStackTrace()
            }
            return mIcon11
        }

        override fun onPostExecute(result: Bitmap?) {
            bmImage.setImageBitmap(result)
        }
    }
}