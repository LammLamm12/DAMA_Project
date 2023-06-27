package pt.ipt.dama.dama_project1

import android.content.AsyncTaskLoader;
import android.content.Context;


class BookLoader(context: Context, private val mUrl: String) : AsyncTaskLoader<List<ReturnBook>>(context) {

    override fun onStartLoading() {
        forceLoad()
    }

    /**
     * This is on a background thread.
     */
    override fun loadInBackground(): List<ReturnBook>? {
        if (mUrl == null) {
            return null
        }
        // Perform the network request, parse the response, and extract a list of books.
        return QueryUtils.fetchBookData(mUrl)
    }
}
