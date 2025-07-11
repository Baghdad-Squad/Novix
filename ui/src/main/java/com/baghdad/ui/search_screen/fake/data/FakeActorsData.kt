package com.baghdad.ui.search_screen.fake.data

import androidx.compose.runtime.Composable
import com.baghdad.component.Actor

@Composable
fun getFakeActors(): List<Actor> {
    return listOf(
        Actor(
            name = "Brad Pitt",
            image = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/65/Brad_Pitt_2019.jpg/440px-Brad_Pitt_2019.jpg"
        ),
        Actor(
            name = "Damson Idris",
            image = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/31/Damson_Idris_2019.jpg/440px-Damson_Idris_2019.jpg"
        ),
        Actor(
            name = "Kerry Condon",
            image = "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2e/Kerry_Condon_2022.jpg/440px-Kerry_Condon_2022.jpg"
        ),
        Actor(
            name = "Angelina Jolie",
            image = "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4f/Angelina_Jolie_at_Davos_2020.jpg/440px-Angelina_Jolie_at_Davos_2020.jpg"
        ),
        Actor(
            name = "Emma Watson",
            image = "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7f/Emma_Watson_2013.jpg/440px-Emma_Watson_2013.jpg"
        )
    )
}

