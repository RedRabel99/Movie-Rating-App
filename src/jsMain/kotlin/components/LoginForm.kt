package components

import context.useUser
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.responsive
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLInputElement
import react.FC
import react.Props
import react.ReactNode
import react.dom.events.MouseEvent
import react.dom.html.InputType
import react.dom.onChange
import react.useState
import utils.xs

private val scope = MainScope()

val LoginForm = FC<Props> {
    var email by useState("")
    var password by useState("")
    var thereWasError by useState(false)
    val userContext = useUser()

    suspend fun handleLogin() {
        var wasLoginSuccessful: Boolean? = null
        if (userContext != null) {
            wasLoginSuccessful = userContext.login(email, password)
        }
        if (!wasLoginSuccessful!!){
            thereWasError = true
        }
    }

    fun onLoginClick(event: MouseEvent<HTMLButtonElement, *>) {
        event.preventDefault()
        scope.launch {
            handleLogin()
        }
    }

    Box {
        Grid {
            container = true
            spacing = responsive(2)
            direction = responsive(GridDirection.column)
            Grid {
                item = true
                xs = 12
                Typography {
                    variant = TypographyVariant.h4
                    +"Login"
                }
            }
            Grid {
                item = true
                xs = 12
                TextField {
                    error = thereWasError
                    helperText = if (thereWasError) ReactNode("Invalid credentials") else ReactNode("")
                    label = ReactNode("email")
                    type = InputType.email
                    variant = FormControlVariant.outlined
                    fullWidth = true
                    value = email
                    onChange = { event ->
                        val target = event.target as? HTMLInputElement
                        val value = target?.value ?: ""
                        email = value
                    }
                }
            }
            Grid {
                item = true
                xs = 12
                TextField {
                    error = thereWasError
                    helperText = if (thereWasError) ReactNode("Invalid credentials") else ReactNode("")
                    label = ReactNode("Password")
                    type = InputType.password
                    variant = FormControlVariant.outlined
                    fullWidth = true
                    value = password
                    onChange = { event ->
                        val target = event.target as? HTMLInputElement
                        val value = target?.value ?: ""
                        password = value
                    }
                }
            }
            Grid {
                item = true
                xs = 12
                Button {
                    variant = ButtonVariant.contained
                    color = ButtonColor.primary
                    onClick =  ::onLoginClick

                    Typography {
                        +"Login"
                    }
                }
            }
        }
    }
}
