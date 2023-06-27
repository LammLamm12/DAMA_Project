package pt.ipt.dama.dama_project1

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.app.LoaderManager
import android.content.Intent;
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Bundle;
import android.view.View;
import android.widget.ListView
import android.widget.TextView
import androidx.loader.content.Loader

class BookActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<List<ReturnBook>> {

    /**
     * URL for book data from Google Books
     */
    private val REQUEST_URL = "https://www.googleapis.com/books/v1/volumes"

    /**
     * Constant value for the book loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private val BOOK_LOADER_ID = 1

    /**
     * Adapter for the list of books
     */
    private lateinit var mAdapter: BookAdapter

    /**
     * TextView that is displayed when the list is empty
     */
    private lateinit var mNoResultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list)

        // Find a reference to the ListView in the layout
        val bookListView: ListView = findViewById(R.id.list)

        mNoResultTextView = findViewById(R.id.empty_view)
        bookListView.emptyView = mNoResultTextView

        // Create a new adapter that takes an empty list of books as input
        mAdapter = BookAdapter(this, ArrayList())

        // Set the adapter on the ListView
        // so the list can be populated in the user interface
        bookListView.adapter = mAdapter

        // Get a reference to the ConnectivityManager to check state of network connectivity
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // Get details on the currently active default data network
        val networkInfo: NetworkInfo? = connMgr.activeNetworkInfo

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            val loaderManager: LoaderManager = supportLoaderManager

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(BOOK_LOADER_ID, null, this)
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            val loadingIndicator: View = findViewById(R.id.progress_bar)
            loadingIndicator.visibility = View.GONE

            // Update empty state with no connection error message
            mNoResultTextView.setText(R.string.connection)
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<ReturnBook>> {
        // Create a new loader for the given URL
        val baseUri: Uri = Uri.parse(REQUEST_URL)
        val uriBuilder: Uri.Builder = baseUri.buildUpon()

        val intent: Intent = getIntent()
        val searchName: String? = intent.getStringExtra(MainActivity.SEARCH_NAME)

        uriBuilder.appendQueryParameter("q", searchName)

        return BookLoader(this, uriBuilder.toString())
    }

    override fun onLoadFinished(loader: Loader<List<ReturnBook>>, books: List<ReturnBook>?) {
        // Hide loading indicator because the data has been loaded
        val loadingIndicator: View = findViewById(R.id.progress_bar)
        loadingIndicator.visibility = View.GONE

        mNoResultTextView.setText(R.string.no_books)

        mAdapter.clear()

        // If there is a valid list of Books, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (books != null && books.isNotEmpty()) {
            mAdapter.addAll(books)
        }
    }

    override fun onLoaderReset(loader: Loader<List<ReturnBook>>) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear()
    }
}
