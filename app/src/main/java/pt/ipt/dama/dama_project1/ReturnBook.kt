package pt.ipt.dama.dama_project1

class ReturnBook(
    private val mTitle: String,
    private val mSubtitle: String,
    private val mAuthors: String,
    private val mDescription: String,
    private val mImageUrl: String
) {
    /**
     * Returns the image, title, subtitle, description, and author of the book.
     */
    fun getTitle(): String {
        return mTitle
    }

    fun getSubtitle(): String {
        return mSubtitle
    }

    fun getAuthors(): String {
        return mAuthors
    }

    fun getDescription(): String {
        return mDescription
    }

    fun getImageUrl(): String {
        return mImageUrl
    }
}
