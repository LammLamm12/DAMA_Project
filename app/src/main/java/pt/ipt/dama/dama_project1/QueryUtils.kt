package pt.ipt.dama.dama_project1

import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.Charset

object QueryUtils {

    private const val LOG_TAG = "QueryUtils"

    fun fetchBookData(requestUrl: String): List<ReturnBook>? {
        val url = createUrl(requestUrl)

        // Perform HTTP request to the URL and receive a JSON response back
        var jsonResponse: String? = null
        try {
            jsonResponse = makeHttpRequest(url)
        } catch (e: IOException) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e)
        }
        // Extract relevant fields from the JSON response and create a list of {@link Book}s
        val books = extractFeatureFromJson(jsonResponse)

        // Return the list of {@link Book}s
        return books
    }

    private fun createUrl(stringUrl: String): URL? {
        var url: URL? = null
        try {
            url = URL(stringUrl)
        } catch (e: MalformedURLException) {
            Log.e(LOG_TAG, "Problem building the URL ", e)
        }
        return url
    }

    @Throws(IOException::class)
    private fun makeHttpRequest(url: URL?): String {
        var jsonResponse = ""

        if (url == null) {
            return jsonResponse
        }

        var connection: HttpURLConnection? = null
        var inputStream: InputStream? = null
        try {
            connection = url.openConnection() as HttpURLConnection
            connection.readTimeout = 10000 /* milliseconds */
            connection.connectTimeout = 15000 /* milliseconds */
            connection.requestMethod = "GET"
            connection.connect()

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (connection.responseCode == 200) {
                inputStream = connection.inputStream
                jsonResponse = readFromStream(inputStream)
            } else {
                Log.e(LOG_TAG, "Error response code: " + connection.responseCode)
            }
        } catch (e: IOException) {
            Log.e(LOG_TAG, "Problem retrieving the book JSON results.", e)
        } finally {
            connection?.disconnect()
            inputStream?.close()
        }
        return jsonResponse
    }

    @Throws(IOException::class)
    private fun readFromStream(inputStream: InputStream): String {
        val output = StringBuilder()
        val reader = BufferedReader(InputStreamReader(inputStream, Charset.forName("UTF-8")))
        var line = reader.readLine()
        while (line != null) {
            output.append(line)
            line = reader.readLine()
        }
        return output.toString()
    }

    private fun extractFeatureFromJson(bookJSON: String?): List<ReturnBook>? {
        if (bookJSON.isNullOrEmpty()) {
            return null
        }

        val books = mutableListOf<ReturnBook>()
        try {
            val baseJsonResponse = JSONObject(bookJSON)
            val bookArray = baseJsonResponse.getJSONArray("items")

            for (i in 0 until bookArray.length()) {
                var subtitle = ""
                var authors = ""
                var description = ""

                val currentBook = bookArray.getJSONObject(i)
                val properties = currentBook.getJSONObject("volumeInfo")

                val title = properties.getString("title")

                subtitle = properties.optString("subtitle", "")

                authors = properties.optString("authors", "")

                description = properties.optString("description", "")

                val imageObject = properties.getJSONObject("imageLinks")
                val imageUrl = imageObject.getString("smallThumbnail")

                val book = ReturnBook(title, subtitle, authors, description, imageUrl)

                books.add(book)
            }

        } catch (e: JSONException) {
            Log.e("QueryUtils", "Problem parsing the book JSON results", e)
        }

        return books
    }
}
