package PhotoLoader

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.widget.ImageView

class Selectionphoto (private val imageView: ImageView){
    private var imageUri: Uri? = null

    fun pickImage(activity: Activity, requestCode: Int) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(intent, requestCode)
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?, expectedRequestCode: Int) {
        if (resultCode == Activity.RESULT_OK && requestCode == expectedRequestCode) {
            imageUri = data?.data
            imageUri?.let {
                imageView.setImageURI(it)
            }
        }
    }

    fun getImageUri(): Uri? {
        return imageUri
    }
}

