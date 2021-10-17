package com.hadestructhor.basicjwtauth.config

object Router {
    const val API = "/api"

    const val TEST = "$API/test"
    const val USER = "$TEST/user"
    const val MODS = "$TEST/mod"
    const val ADMINS = "$TEST/admin"

    const val AUTH = "$API/auth"
    const val SIGNUP = "$AUTH/signup"
    const val SIGNIN = "$AUTH/signin"
    const val REFRESH_TOKEN = "$AUTH/refreshtoken"


}