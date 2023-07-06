package components

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


val Header = FC<Props> {
    AppBar{
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
                }
            }
        }
    }
}