package components

import csstype.Position
import csstype.integer
import csstype.number
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.FC
import react.Props
import react.ReactNode
import react.dom.aria.AriaHasPopup
import react.dom.aria.ariaHasPopup
import react.dom.aria.ariaLabel
import react.dom.html.ReactHTML
import react.useState


val Header = FC<Props> {

    Box{
        var drawerIsOpen by useState(false)
        fun toggleDrawer(){
            console.log("hello from appbar")
            drawerIsOpen = !drawerIsOpen
        }
        AppBar{
            sx{
                zIndex = integer(0)
            }
            position = AppBarPosition.fixed
            Toolbar {
                //   NavLink{
                //  to = "/"
                //NavLink{to = "/"
                Typography {

                    sx { flexGrow = number(1.0) }
                    variant = TypographyVariant.h6
                    noWrap = true
                    component = ReactHTML.div

                    +"Movie Rating App"
                }
                // }
                Button{
                    color = ButtonColor.inherit
                    href = "/"
                    Typography{
                        variant = TypographyVariant.h6
                        +"Home"
                    }

                }
                Tooltip{
                    title = ReactNode("test")
                    IconButton{
                        ariaLabel = "account of current user"
                        ariaHasPopup = AriaHasPopup.`false`
                        size = Size.large
                        color = IconButtonColor.inherit
                        Avatar{
                            alt = "account icon"
                        }
                        onClick = {_->toggleDrawer()}
                    }
                }
            }
        }
        Box{
            sx{
                position = Position.relative
                zIndex = integer(1)
            }
            AccountDrawer{
                isOpen = drawerIsOpen
                drawerToggle = ::toggleDrawer
            }
        }

    }

}