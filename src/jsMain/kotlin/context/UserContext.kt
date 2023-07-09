package context

import kotlinx.coroutines.MainScope
import models.User
import react.*
import services.getCurrentUser
import services.login
import services.logout
import kotlin.reflect.KSuspendFunction2

private val scope = MainScope()

external interface UserProviderProps: Props{
    var user: User?
    val login : KSuspendFunction2<String, String, Boolean>
    val logout: () -> Unit

}


val UserContext = createContext<UserProviderProps?>(null)

fun useUser(): UserProviderProps? = useContext(UserContext)

val UserProvider = FC<PropsWithChildren> {props ->
    fun currentUser(): User? {
        return getCurrentUser()
    }

    var user by useState(currentUser())

    suspend fun loginUser(email: String, password: String): Boolean {
        var loginWasSuccessful = false
//        scope.launch {
//
//            try{
//                loginWasSuccessful = login(email, password)
//
//            }finally{
//                if (loginWasSuccessful){
//                    user = getCurrentUser()
//                }
//            }
//        }
        loginWasSuccessful = login(email, password)
        if (loginWasSuccessful){
                    user = getCurrentUser()
                }
        return loginWasSuccessful
    }


    fun logoutUser(){
        logout()
        user = null
    }


    UserContext.Provider{
        value = object : UserProviderProps{
            override var user: User? = user
            override var login = ::loginUser
            override var logout = ::logoutUser
        }
        +props.children
    }

}