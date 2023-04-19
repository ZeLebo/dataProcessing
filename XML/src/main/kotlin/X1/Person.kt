package X1

class Person {
    // person info
    var id: String? = null
    var name: String? = null
    var surname: String? = null
    var gender: String? = null

    // wife/husband info
    var spouseName: String? = null
    var spouseId: String? = null

    var wifeId: String? = null
    var wifeName: String? = null
    var wife: Person? = null

    var husbandId: String? = null
    var husbandName: String? = null
    var husband: Person? = null

    // parents info
    var mother: Person? = null
    var father: Person? = null

    var motherId: String? = null
    var motherName: String? = null
    var fatherId: String? = null
    var fatherName: String? = null
    var parentIds: HashSet<String> = HashSet()
    var parentNames: HashSet<String> = HashSet()

    // children info
    var childrenNumber: Int? = null
    var childrenIds: HashSet<String> = HashSet()
    var childrenNames: HashSet<String> = HashSet()

    var sonsIds: HashSet<String> = HashSet()
    var sonsNames: HashSet<String> = HashSet()
    var sons: HashSet<Person> = HashSet()

    var daughtersIds: HashSet<String> = HashSet()
    var daughtersNames: HashSet<String> = HashSet()
    var daughters: HashSet<Person> = HashSet()

    // sibling info
    var siblingsNumber: Int? = null
    var siblingsIds: HashSet<String> = HashSet()
    var siblingsNames: HashSet<String> = HashSet()
    var brothersIds: HashSet<String> = HashSet()
    var brotherNames: HashSet<String> = HashSet()
    var brothers: HashSet<Person> = HashSet()
    var sistersIds: HashSet<String> = HashSet()
    var sisterNames: HashSet<String> = HashSet()
    var sisters: HashSet<Person> = HashSet()

    constructor() {
        this.parentIds = HashSet()
        this.parentNames = HashSet()

        this.childrenIds = HashSet()
        this.childrenNames = HashSet()

        this.sonsIds = HashSet()
        this.sonsNames = HashSet()
        this.sons = HashSet()

        this.daughtersIds = HashSet()
        this.daughtersNames = HashSet()
        this.daughters = HashSet()

        this.siblingsIds = HashSet()
        this.siblingsNames = HashSet()

        this.brothersIds = HashSet()
        this.brotherNames = HashSet()
        this.brothers = HashSet()

        this.sistersIds = HashSet()
        this.sisterNames = HashSet()
        this.sisters = HashSet()
    }

    constructor(
        id: String,
        firstName: String,
        lastName: String,
        gender: String,
        spouseId: String,
        spouseName: String,
        husbandId: String,
        husbandName: String,
        wifeId: String,
        wifeName: String,
        fatherId: String,
        fatherName: String,
        motherId: String,
        motherName: String,
        childrenNumber: Long,
        siblingsNumber: Long
    ) {
        this.id = id
        this.name = firstName
        this.surname = lastName
        this.gender = gender
        this.spouseId = spouseId
        this.spouseName = spouseName
        this.husbandId = husbandId
        this.husbandName = husbandName
        this.wifeId = wifeId
        this.wifeName = wifeName
        this.fatherId = fatherId
        this.fatherName = fatherName
        this.motherId = motherId
        this.motherName = motherName
        this.childrenNumber = childrenNumber.toInt()
        this.siblingsNumber = siblingsNumber.toInt()
    }

    fun getPersonId(): String? {
        return id
    }

    fun setFullName(newName: String?) {
        if (newName == null) return
        val t = newName.trim().replace("\\s+".toRegex(), " ").split(" ")
        if (t[0].isEmpty() or t[1].isEmpty()) return
        name = t[0]
        surname = t[1]
    }

    fun getFullName(): String? {
        if (name == null || surname == null) return null
        return "$name $surname"
    }

    fun hasParentId(id: String): Boolean {
        return parentIds.contains(id)
    }

    fun hasParentName(name: String): Boolean {
        return parentNames.contains(name)
    }

    fun addParentId(id: String) {
        parentIds.add(id)
    }

    fun addParentName(name: String) {
        parentNames.add(name)
    }

    fun removeParentId(id: String) {
        parentIds.remove(id)
    }

    fun removeParentName(name: String) {
        parentNames.remove(name)
    }

    fun hasChildId(id: String): Boolean {
        return childrenIds.contains(id)
    }

    fun hasChildName(name: String): Boolean {
        return childrenNames.contains(name)
    }

    fun addChildId(id: String) {
        childrenIds.add(id)
    }

    fun addChildName(name: String) {
        childrenNames.add(name)
    }

    fun removeChildId(id: String) {
        childrenIds.remove(id)
    }

    fun removeChildName(name: String) {
        childrenNames.remove(name)
    }

    fun hasSonId(id: String): Boolean {
        return sonsIds.contains(id)
    }

    fun hasSonName(name: String): Boolean {
        return sonsNames.contains(name)
    }

    fun addSonId(id: String) {
        sonsIds.add(id)
    }

    fun addSonName(name: String) {
        sonsNames.add(name)
    }

    fun removeSonId(id: String) {
        sonsIds.remove(id)
    }

    fun removeSonName(name: String) {
        sonsNames.remove(name)
    }

    fun addSon(son: Person) {
        sons.add(son)
    }

    fun removeSon(son: Person) {
        sons.remove(son)
    }

    fun hasDaughterId(id: String): Boolean {
        return daughtersIds.contains(id)
    }

    fun hasDaughterName(name: String): Boolean {
        return daughtersNames.contains(name)
    }

    fun addDaughterId(id: String) {
        daughtersIds.add(id)
    }

    fun addDaughterName(name: String) {
        daughtersNames.add(name)
    }

    fun removeDaughterId(id: String) {
        daughtersIds.remove(id)
    }

    fun removeDaughterName(name: String) {
        daughtersNames.remove(name)
    }

    fun addDaughter(daughter: Person) {
        daughters.add(daughter)
    }

    fun removeDaughter(daughter: Person) {
        daughters.remove(daughter)
    }

    fun hasSiblingId(id: String): Boolean {
        return siblingsIds.contains(id)
    }

    fun hasSiblingName(name: String): Boolean {
        return siblingsNames.contains(name)
    }

    fun addSiblingId(id: String) {
        siblingsIds.add(id)
    }

    fun addSiblingName(name: String) {
        siblingsNames.add(name)
    }

    fun removeSiblingId(id: String) {
        siblingsIds.remove(id)
    }

    fun removeSiblingName(name: String) {
        siblingsNames.remove(name)
    }

    fun hasBrotherId(id: String): Boolean {
        return brothersIds.contains(id)
    }

    fun hasBrotherName(name: String): Boolean {
        return brotherNames.contains(name)
    }

    fun hasBrother(brother: Person): Boolean {
        return brothers.contains(brother)
    }

    fun addBrotherId(id: String) {
        brothersIds.add(id)
    }

    fun addBrotherName(name: String) {
        brotherNames.add(name)
    }

    fun removeBrotherId(id: String) {
        brothersIds.remove(id)
    }

    fun removeBrotherName(name: String) {
        brotherNames.remove(name)
    }

    fun addBrother(brother: Person) {
        brothers.add(brother)
    }

    fun removeBrother(brother: Person) {
        brothers.remove(brother)
    }

    fun hasSisterId(id: String): Boolean {
        return sistersIds.contains(id)
    }

    fun hasSisterName(name: String): Boolean {
        return sisterNames.contains(name)
    }

    fun hasSister(sister: Person): Boolean {
        return sisters.contains(sister)
    }

    fun addSisterId(id: String) {
        sistersIds.add(id)
    }

    fun addSisterName(name: String) {
        sisterNames.add(name)
    }

    fun removeSisterId(id: String) {
        sistersIds.remove(id)
    }

    fun removeSisterName(name: String) {
        sisterNames.remove(name)
    }

    fun addSister(sister: Person) {
        sisters.add(sister)
    }

    fun removeSister(sister: Person) {
        sisters.remove(sister)
    }

    fun validate(): Boolean {
        return (this.childrenNumber == null || (this.childrenNumber == this.sons.size + this.daughters.size) &&
                (this.siblingsNumber == null || (this.siblingsNumber == this.brothers.size + this.sisters.size)))
    }

    // merging
    fun mergeFields(person: Person) {
        if (this.id == null && person.id != null) {
            this.id = person.id
        }
        if (this.getFullName() == null && person.getFullName() != null) {
            this.setFullName(person.getFullName())
        }
        if (this.name == null && person.name != null) {
            this.name = person.name
        }
        if (this.surname == null && person.surname != null) {
            this.surname = person.surname
        }
        if (this.gender == null && person.gender != null) {
            this.gender = person.gender
        }
        if (this.spouseId == null && person.spouseId != null) {
            this.spouseId = person.spouseId
        }
        if (this.spouseName == null && person.spouseName != null) {
            this.spouseName = person.spouseName
        }
        if (this.husbandId == null && person.husbandId != null) {
            this.husbandId = person.husbandId
        }
        if (this.husbandName == null && person.husbandName != null) {
            this.husbandName = person.husbandName
        }
        if (this.husband == null  && person.husband != null) {
            this.husband = person.husband
        }
        if (this.wifeId == null && person.wifeId != null) {
            this.wifeId = person.wifeId
        }
        if (this.wifeName == null && person.wifeName != null) {
            this.wifeName = person.wifeName
        }
        if (this.wife == null && person.wife != null) {
            this.wife = person.wife
        }
        if (this.fatherId == null && person.fatherId != null) {
            this.fatherId = person.fatherId
        }
        if (this.fatherName == null && person.fatherName != null) {
            this.fatherName = person.fatherName
        }
        if (this.father == null && person.father != null) {
            this.father = person.father
        }
        if (this.motherId == null && person.motherId != null) {
            this.motherId = person.motherId
        }
        if (this.motherName == null && person.motherName != null) {
            this.motherName = person.motherName
        }
        if (this.mother == null && person.mother != null) {
            this.mother = person.mother
        }
        if (this.childrenNumber == null && person.childrenNumber != null) {
            this.childrenNumber = person.childrenNumber
        }
        if (this.siblingsNumber == null && person.siblingsNumber != null) {
            this.siblingsNumber = person.siblingsNumber
        }
    }

    fun mergeSets(person: Person) {
        this.parentIds.addAll(person.parentIds)
        this.parentNames.addAll(person.parentNames)
        this.childrenIds.addAll(person.childrenIds)
        this.childrenNames.addAll(person.childrenNames)
        this.sonsIds.addAll(person.sonsIds)
        this.sonsNames.addAll(person.sonsNames)
        this.sons.addAll(person.sons)
        this.daughtersIds.addAll(person.daughtersIds)
        this.daughtersNames.addAll(person.daughtersNames)
        this.daughters.addAll(person.daughters)
        this.siblingsIds.addAll(person.siblingsIds)
        this.siblingsNames.addAll(person.siblingsNames)
        this.brothersIds.addAll(person.brothersIds)
        this.brotherNames.addAll(person.brotherNames)
        this.brothers.addAll(person.brothers)
        this.sistersIds.addAll(person.sistersIds)
        this.sisterNames.addAll(person.sisterNames)
        this.sisters.addAll(person.sisters)
    }

    fun merge(person: Person) {
        this.mergeFields(person)
        this.mergeSets(person)
    }
}



