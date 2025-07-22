package com.baghdad.viewmodel.people

import com.baghdad.entity.person.People

fun People.toPeopleUi() = PeopleUiState.People(
    id = this.id,
    profilePictureURL = this.profilePicatralUrl,
    name = this.name

)
