package org.android.tiffinseva

class AppConstants {

    companion object {
        const val HTTP_OK = 200
        const val HTTP_CREATED = 201
        const val HTTP_UNAUTHORISED = 401
        const val HTTP_FORBIDDEN = 403
        const val HTTP_NOT_FOUND = 404
        const val HTTP_EXCEPTION = 100
        const val TIFFIN_SEVA_API_FAIL = 999
        const val BUTTON_CLICK_DEBOUNCE_INTERVAL: Long = 2000
        const val INTENT_TRIGGER_OTP_TO = "INTENT_TRIGGER_OTP_TO"
        const val KEY_TOKEN = "Authorization"
        const val FIREBASE_TOKEN = "firebase_token"
        const val COUNTDOWN_TIME = 120000L

        const val STATE_LIST_FILE = "state_list.json"
    }

    enum class FRAGMENT_ID {
        LOGIN_FRAGMENT, OTP_FRAGMENT, SIGNUP_FRAGMENT, TIFFIN_PROVIDER_FRAGMENT,CHANGE_ADDRESS_FRAG,
        TIFFIN_SEEKER_FRAGMENT, TIFFIN_HOME_FRAGMENT, TIFFIN_FRAGMENT_REQUEST, TIFFIN_FRAGMENT_SERVER
    }
}

class PreferenceConstant {

}

class ApiConstant {
    companion object {
        const val BASE_URL = "https://tiffinseva.org/tiffinseva/"
        //const val BASE_URL = "http://tiffinseva.org:8080/tiffinseva/"
        const val GEN_OTP_END_POINT = "v1/user/login"
        const val UPDATE_FIREBASE_TOKEN = "/v1/user/firebasetoken"
        const val VERIFY_OTP_END_POINT = "v1/user/verifyotp"
        const val CREATE_USER_END_POINT = "v1/user/register"
        const val PIN_CODE_CITY_API = "v1/addressmasterdata/states/{state}"
        const val CITY_INFO_API = "v1/addressmasterdata/pincode/{pincode}"
        const val SAVE_ADDRESS_API = "v1/address"
        const val CHANGE_CITY_AND_STATE_API = "v1/user/cityandstate"
        const val ALL_ADDRESS_BY_USER_ID_API = "v1/address/getbyuserid"
        const val TIFFIN_HOME_END_POINT = "v1/home"
        const val TIFFIN_PERSONA_LIST_END_POINT = "v1/tiffin/search"
        const val TIFFIN_SAVE = "v1/tiffin"
        const val USER_META_INFO = "v1/user/tiffindata/{userType}"
    }
}

class NetworkConstant

class BundleConstants {
    companion object {
        const val TIFFIN_SEARCH_USER_TYPE_REQUESTER = "REQUESTER"
        const val TIFFIN_SEARCH_USER_TYPE_PROVIDER = "SERVER"
        const val TIFFIN_LIST_DECIDER = "TIFFIN_LIST_DECIDER"
        const val TIFFIN_USER_TYPE = "TIFFIN_USER_TYPE"
    }

    enum class TIFFIN_LIST_SWITCHER {
        TIFFIN_SEEKER, TIFFIN_PROVIDER
    }
}

enum class UserType {
    REQUESTER, SERVER
}