package X1

class Person(
    var id: String? = null,
    var name: String? = null,
    var surname: String? = null,
    var gender: Gender? = null,

    var spouceName: MutableList<String> = mutableListOf(),
    var spouceIds: MutableList<String> = mutableListOf(),

    var siblingsNumber: Int? = null,
    var siblingsIds: MutableList<String> = mutableListOf(),
    var brothersIds: MutableList<String> = mutableListOf(),
    var brotherNames: MutableList<String> = mutableListOf(),

    var sistersIds: MutableList<String> = mutableListOf(),
    var sisterNames: MutableList<String> = mutableListOf(),

    var childrenNumber: Int? = null,
    var childrenIds: MutableList<String> = mutableListOf(),
    var childrenNames: MutableList<String> = mutableListOf(),
    var sonsIds: MutableList<String> = mutableListOf(),
    var daughtersIds: MutableList<String> = mutableListOf(),

    var wifeId: String? = null,
    var husbandId: String? = null,

    var mother: String? = null,
    var father: String? = null,
    var motherId: String? = null,
    var fatherId: String? = null,
    var parentIds: MutableList<String> = mutableListOf(),
) {
    fun setFullName(newName: String?) {
        if (newName == null) {
            return
        }
        val t = newName.trim().replace("\\s+".toRegex(), " ").split(" ")
        name = t[0]
        surname = t[1]
    }
}

enum class Gender {
    F, M
}
