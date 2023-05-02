package X2

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlElementWrapper
import javax.xml.bind.annotation.XmlID
import javax.xml.bind.annotation.XmlIDREF
import javax.xml.bind.annotation.XmlTransient
import javax.xml.bind.annotation.XmlType

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "person-type", propOrder = [
    "firstname", "surname", "gender",
    "husband", "wife",
    "father", "mother",
    "sons", "daughters",
    "brothers", "sisters",
])
class Person {
    // person info
    @XmlID
    @XmlAttribute
    var id: String? = null
    @XmlElement(name = "firstname", required = true)
    var firstname: String? = null
    @XmlElement(name = "surname",  required = true)
    var surname: String? = null
    @XmlElement(required = true)
    var gender: String? = null

    // wife/husband info
    // ignore the field in xml
    @XmlTransient
    var spouseName: String? = null
    @XmlTransient
    var spouseId: String? = null

    @XmlTransient
    var wifeId: String? = null
    @XmlTransient
    var wifeName: String? = null
    @XmlIDREF
    var wife: Person? = null

    @XmlTransient
    var husbandId: String? = null
    @XmlTransient
    var husbandName: String? = null
    @XmlIDREF
    var husband: Person? = null

    // parents info
    @XmlIDREF
    var mother: Person? = null
    @XmlIDREF
    var father: Person? = null

    @XmlTransient
    var motherId: String? = null
    @XmlTransient
    var motherName: String? = null
    @XmlTransient
    var fatherId: String? = null
    @XmlTransient
    var fatherName: String? = null
    @XmlTransient
    var parentIds: HashSet<String> = HashSet()
    @XmlTransient
    var parentNames: HashSet<String> = HashSet()

    // children info
    @XmlTransient
    var childrenNumber: Int? = null
    @XmlTransient
    var childrenIds: HashSet<String> = HashSet()
    @XmlTransient
    var childrenNames: HashSet<String> = HashSet()

    @XmlTransient
    var sonsIds: HashSet<String> = HashSet()
    @XmlTransient
    var sonsNames: HashSet<String> = HashSet()
    @XmlIDREF
    @XmlElementWrapper
    @XmlElement(name = "son")
    var sons: HashSet<Person> = HashSet()

    @XmlTransient
    var daughtersIds: HashSet<String> = HashSet()
    @XmlTransient
    var daughtersNames: HashSet<String> = HashSet()
    @XmlIDREF
    @XmlElementWrapper
    @XmlElement(name = "daughter")
    var daughters: HashSet<Person> = HashSet()

    // sibling info
    @XmlTransient
    var siblingsNumber: Int? = null
    @XmlTransient
    var siblingsIds: HashSet<String> = HashSet()
    @XmlTransient
    var siblingsNames: HashSet<String> = HashSet()
    @XmlTransient
    var brothersIds: HashSet<String> = HashSet()
    @XmlTransient
    var brotherNames: HashSet<String> = HashSet()
    @XmlIDREF
    @XmlElementWrapper
    @XmlElement(name = "brother")
    var brothers: HashSet<Person> = HashSet()
    @XmlTransient
    var sistersIds: HashSet<String> = HashSet()
    @XmlTransient
    var sisterNames: HashSet<String> = HashSet()
    @XmlIDREF
    @XmlElementWrapper
    @XmlElement(name = "sister")
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
        this.firstname = firstName
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
        firstname = t[0]
        surname = t[1]
    }

    fun getFullName(): String? {
        if (this.firstname != null && this.surname != null) {
            return "$firstname $surname"
        }
        return null
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
                (this.siblingsNumber == null || (this.siblingsNumber == this.brothers.size + this.sisters.size)) &&
                this.id != null && this.firstname != null && this.surname != null && this.gender != null &&
                this.father != null && this.mother != null && (this.husband != null || this.wife != null))
    }

    // merging
    fun mergeFields(person: Person) {
        if (this.id == null && person.id != null) {
            this.id = person.id
        }
        if (this.getFullName() == null && person.getFullName() != null) {
            this.setFullName(person.getFullName())
        }
        if (this.firstname == null && person.firstname != null) {
            this.firstname = person.firstname
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



