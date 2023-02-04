package ru.khozyainov.splashun.data.network.models

object PhotoRemoteContract {

    object Search {
        const val RESULTS = "results"
    }

    object AbbreviatedPhoto {
        const val PHOTO = "photo"
        const val USER = "user"
    }

    object Photo {
        const val ID = "id"
        const val IMAGE_WIDTH = "width"
        const val IMAGE_HEIGHT = "height"
        const val BLUR = "blur_hash"
        const val DOWNLOADS = "downloads"
        const val LIKES = "likes"
        const val LIKE_BY_USER = "liked_by_user"
        const val EXIF = "exif"
        const val LOCATION = "location"
        const val TAGS = "tags"
        const val IMAGE_URLS = "urls"
    }

    object Exif {
        const val EXIF_MADE = "make"
        const val EXIF_MODEL = "model"
        const val EXIF_EXPOSURE = "exposure_time"
        const val EXIF_APERTURE = "aperture"
        const val EXIF_FOCAL = "focal_length"
        const val EXIF_ISO = "iso"
    }

    object Location {
        const val LOCATION_CITY = "city"
        const val LOCATION_COUNTRY = "country"
        const val LOCATION_POSITION = "position"
    }

    object Position {
        const val LOCATION_POSITION_LATITUDE = "latitude"
        const val LOCATION_POSITION_LONGITUDE = "longitude"
    }

    object Tag {
        const val TAGS_TITLE = "title"
    }

    object Image {
        const val IMAGE_RAW = "raw"
    }

    object Author {
        const val AUTHOR_NAME = "username"
        const val AUTHOR_FULL_NAME = "name"
        const val AUTHOR_IMAGES = "profile_image"
        const val AUTHOR_ABOUT = "bio"
    }

    object ImageAuthor {
        const val AUTHOR_IMAGE_SMALL = "small"
    }

    object Profile {
        const val NAME = "name"
        const val FIRST_NAME = "first_name"
        const val LAST_NAME = "last_name"
        const val TOTAL_LIKES = "total_likes"
    }

}