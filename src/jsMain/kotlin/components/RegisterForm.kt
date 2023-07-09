package components

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
import services.register
import utils.xs

private val scope = MainScope()

external interface RegisterProps : Props {
    var registrationIsActive: () -> Unit
}

val RegisterForm = FC<RegisterProps>{props ->
    var username by useState("")
    var email by useState("")
    var password by useState("")
    var confirmPassword by useState("")
    var passwordAreNotTheSame by useState(false)
    var thereWasError by useState(false)
    var registrationSuccessful by useState(false)

    fun handleLogin(event: MouseEvent<HTMLButtonElement, *>){
        event.preventDefault()
        if (password != confirmPassword){
            passwordAreNotTheSame = true
            return
        }

        scope.launch{
            try{
                registrationSuccessful = register(username, email, password)
            }finally{
                thereWasError = !registrationSuccessful
            }
        }
    }

    Box{
        if(registrationSuccessful){
            Typography{
                variant = TypographyVariant.h4
                +"Registration successful"
            }
            Button{
                variant = ButtonVariant.contained
                color = ButtonColor.primary
                onClick = {
                    props.registrationIsActive()
                }
                +"Login"
            }
        }else{
            Grid {
                container = true
                spacing = responsive(2)
                direction = responsive(GridDirection.column)
                Grid {
                    item = true
                    xs = 12
                    Typography {
                        variant = TypographyVariant.h4
                        +"Register"
                    }
                }
                Grid{
                    item = true
                    xs = 12
                    TextField {
                        required = true
                        label = ReactNode("username")
                        type = InputType.text
                        variant = FormControlVariant.outlined
                        fullWidth = true
                        value = username
                        onChange = { event ->
                            val target = event.target as? HTMLInputElement
                            val value = target?.value ?: ""
                            username = value
                        }
                    }
                }
                Grid {
                    item = true
                    xs = 12
                    TextField {
                        required = true
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
                        required = true
                        error = passwordAreNotTheSame
                        helperText = if (passwordAreNotTheSame) ReactNode("Passwords are not the same") else ReactNode("")
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
                    TextField {
                        required = true
                        error = passwordAreNotTheSame
                        helperText = if (passwordAreNotTheSame) ReactNode("Passwords are not the same") else ReactNode("")
                        label = ReactNode("Confirm Password")
                        type = InputType.password
                        variant = FormControlVariant.outlined
                        fullWidth = true
                        value = confirmPassword
                        onChange = { event ->
                            val target = event.target as? HTMLInputElement
                            val value = target?.value ?: ""
                            confirmPassword = value
                        }
                    }
                }
                Grid {
                    item = true
                    xs = 12
                    Button {
                        variant = ButtonVariant.contained
                        color = ButtonColor.primary
                        onClick = {event ->
                            handleLogin(event)
                        }
                        Typography{
                            +"Register"
                        }
                    }
                }
            }
        }
    }
}
