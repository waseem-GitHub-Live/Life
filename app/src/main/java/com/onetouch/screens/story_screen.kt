//package com.onetouch.screens
//
//import android.app.Fragment
//import android.content.ContentResolver
//import android.content.ContentValues
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.graphics.Matrix
//import android.net.Uri
//import android.os.Bundle
//import android.provider.MediaStore
//import androidx.activity.result.ActivityResultLauncher
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.material.MaterialTheme
//import androidx.compose.material.OutlinedButton
//import androidx.compose.material.Surface
//import androidx.compose.material.Text
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.ImageBitmap
//import androidx.compose.ui.graphics.asImageBitmap
//import androidx.compose.ui.platform.LocalContext
//import java.text.SimpleDateFormat
//import java.util.*
//import kotlin.math.min
//class MyFragment : Fragment() {
//
//    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        requestPermissionLauncher =
//            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
//                cameraPermissionStatusState.value =
//                    if (isGranted) CameraPermissionStatus.PermissionGranted else CameraPermissionStatus.PermissionDenied
//            }
//    }
//    @Composable
//    fun MyApp() {
//        val context = LocalContext.current
//        val cameraPermissionStatusState = remember { mutableStateOf(CameraPermissionStatus.NoPermission) }
//        val photoUriState = remember { mutableStateOf<Uri?>(null) }
//        val hasPhotoState = remember { mutableStateOf(false) }
//        val resolver = context.contentResolver
//
//        val requestPermissionLauncher =
//            rememberLauncher {
//                cameraPermissionStatusState.value = if (it) CameraPermissionStatus.PermissionGranted else CameraPermissionStatus.PermissionDenied
//            }
//
//        val takePhotoLauncher =
//            rememberLauncher { isSaved ->
//                hasPhotoState.value = isSaved
//            }
//
//        val takePhoto: () -> Unit = {
//            hasPhotoState.value = false
//
//            val values = ContentValues().apply {
//                val title = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
//                put(MediaStore.Images.Media.TITLE, "Compose Camera Example Image - $title")
//                put(MediaStore.Images.Media.DISPLAY_NAME, title)
//                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
//                put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
//            }
//
//            val uri = resolver.insert(
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                values
//            )
//            uri?.let {
//                takePhotoLauncher.launch(it.toString())
//                photoUriState.value = it
//            }
//        }
//
//        val getThumbnail: (Uri?) -> ImageBitmap? = { uri ->
//            val targetSize = 256f
//            uri?.let {
//                resolver.openInputStream(it)?.use { inputStream ->
//                    BitmapFactory.decodeStream(inputStream)
//                }?.let { bitmap ->
//                    val height = bitmap.height.toFloat()
//                    val width = bitmap.width.toFloat()
//                    val scaleFactor = min(targetSize / height, targetSize / width)
//                    Bitmap.createScaledBitmap(bitmap, (scaleFactor * width).toInt(), (scaleFactor * height).toInt(), true)
//                }?.let { scaledBitmap ->
//                    val rotation = getImageRotation(resolver, uri)
//                    Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.width, scaledBitmap.height, Matrix().apply { postRotate(rotation.toFloat()) }, true)
//                }?.asImageBitmap()
//            }
//        }
//
//        val getFullImage: (Uri?) -> ImageBitmap? = { uri ->
//            uri?.let {
//                resolver.openInputStream(it)?.use { inputStream ->
//                    BitmapFactory.decodeStream(inputStream)
//                }?.let { bitmap ->
//                    val rotation = getImageRotation(resolver, uri)
//                    Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, Matrix().apply { postRotate(rotation.toFloat()) }, true)
//                }?.asImageBitmap()
//            }
//        }
//
//        Surface(
//            modifier = Modifier.fillMaxSize(),
//            color = MaterialTheme.colors.background
//        ) {
//            Column(
//                modifier = Modifier.fillMaxSize(),
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                TakePhotoButton(
//                    cameraPermissionStatus = cameraPermissionStatusState.value,
//                    onClick = {
//                        when (cameraPermissionStatusState.value) {
//                            CameraPermissionStatus.NoPermission ->
//                                requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
//
//                            CameraPermissionStatus.PermissionGranted ->
//                                takePhoto()
//
//                            CameraPermissionStatus.PermissionDenied -> {}
//                        }
//                    }
//                )
//                if (hasPhotoState.value) {
//                    val bitmap = getThumbnail(photoUriState.value)
//                    bitmap?.let {
//                        Image(
//                            bitmap = it,
//                            contentDescription = "Thumbnail of Saved Photo",
//                            modifier = Modifier.clickable {
//                                // Handle showing full image
//                            }
//                        )
//                    }
//                }
//            }
//        }
//    }
//
//    @Composable
//    fun TakePhotoButton(
//        cameraPermissionStatus: CameraPermissionStatus,
//        onClick: () -> Unit
//    ) {
//        OutlinedButton(
//            onClick = onClick
//        ) {
//            when (cameraPermissionStatus) {
//                CameraPermissionStatus.NoPermission ->
//                    Text(text = "Request Camera Permissions")
//
//                CameraPermissionStatus.PermissionDenied ->
//                    Text(text = "Camera Permissions Have Been Denied")
//
//                CameraPermissionStatus.PermissionGranted ->
//                    Text(text = "Take Photo")
//            }
//        }
//    }
//
//    private fun getImageRotation(resolver: ContentResolver, uri: Uri): Int {
//        val cursor = resolver.query(uri, arrayOf(MediaStore.Images.Media.ORIENTATION), null, null, null)
//        var result = 0
//
//        cursor?.apply {
//            moveToFirst()
//            val index = getColumnIndex(MediaStore.Images.Media.ORIENTATION)
//            result = getInt(index)
//            close()
//        }
//        return result
//    }
//
//    enum class CameraPermissionStatus { NoPermission, PermissionGranted, PermissionDenied }
//
//    private inline fun rememberLauncher(crossinline callback: (Boolean) -> Unit): ActivityResultLauncher<String> {
//        return registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
//            callback(isGranted)
//        }
//    }
//
//}
//
//
