package com.example.tea1

import java.io.Serializable

class ListeToDo : Serializable {
    var titreListeToDo: String = ""
    var lesItems: ArrayList<ItemToDo> = ArrayList()

    constructor()

    constructor(titreListeToDo: String) {
        this.titreListeToDo = titreListeToDo
    }

    fun rechercherItem(description: String): ItemToDo? {
        return lesItems.find { it.description == description }
    }

    override fun toString(): String {
        return "ListeToDo(titreListeToDo='$titreListeToDo', lesItems=$lesItems)"
    }
} 