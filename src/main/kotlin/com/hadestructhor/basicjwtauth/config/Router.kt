package com.hadestructhor.basicjwtauth.config

object Router {
    const val API = "/api"

    const val TEST = "$API/test"
    const val USER = "$TEST/user"
    const val MODS = "$TEST/mods"
    const val ADMINS = "$TEST/admins"

    const val AUTH = "$API/auth"
    const val SIGNUP = "$AUTH/signup"
    const val SIGNIN = "$AUTH/signin"


}