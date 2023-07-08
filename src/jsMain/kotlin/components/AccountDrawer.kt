package components

import context.useUser
import csstype.px
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.FC
import react.Props
import react.useState

external interface AccountDrawerProps: Props {
    var isOpen : Boolean
    var drawerToggle : () -> Unit
}

val AccountDrawer = FC<AccountDrawerProps>{props ->
    var registrationIsActive by useState(false)
    val userContext = useUser()
    fun toggleDrawer(){
        console.log("hello from drawer")
        props.drawerToggle()
    }
    fun toggleRegistration(){
        registrationIsActive = !registrationIsActive
    }

    Drawer{
        sx{
            width = 400.px
        }
        anchor = DrawerAnchor.right
        open = props.isOpen
        onClose = {_,_ -> toggleDrawer()}
        Container{
            Button{
                onClick = {_ -> toggleDrawer()}
                +"Close"
            }
            Box{
                if (userContext != null) {
                    if(userContext.user != null){
                        Box {
                            Typography {
                                variant = TypographyVariant.h4
                                +"Welcome ${userContext.user!!.username}"
                            }
                            Button {
                                variant = ButtonVariant.contained
                                onClick = {}
                                +"Check your reviews"
                            }
                            Button {
                                onClick = { _ -> userContext.logout() }
                                variant = ButtonVariant.contained
                                +"Logout"
                            }
                        }
                    }else if(registrationIsActive){
                        RegisterForm{
                            this.registrationIsActive = ::toggleRegistration
                        }
                        Button{
                            variant = ButtonVariant.text
                            onClick = {_ -> registrationIsActive = !registrationIsActive}
                            +"Already have an account? Login here"
                        }
                    }else{
                        LoginForm{}
                        Button{
                            variant = ButtonVariant.text
                            onClick = {_ -> registrationIsActive = !registrationIsActive}
                            +"Dont have an account? Register here"
                        }
                    }
                }
            }
        }

    }
}