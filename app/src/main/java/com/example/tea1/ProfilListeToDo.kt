package com.example.tea1

import java.io.Serializable

class ProfilListeToDo : Serializable {
    var login: String = ""
    var mesListesToDo: ArrayList<ListeToDo> = ArrayList()

    constructor()

    constructor(login: String) {
        this.login = login
    }

    constructor(login: String, mesListesToDo: ArrayList<ListeToDo>) {
        this.login = login
        this.mesListesToDo = mesListesToDo
    }

    fun ajouteListe(listeToDo: ListeToDo) {
        mesListesToDo.add(listeToDo)
    }

    override fun toString(): String {
        return "ProfilListeToDo(login='$login', mesListesToDo=$mesListesToDo)"
    }
} 