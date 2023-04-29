package X2

import java.util.stream.Stream
import java.util.stream.Collectors
import javax.xml.bind.annotation.*

public var noName: Int = 0
public var noSurname: Int = 0
public var noGender: Int = 0


@XmlRootElement(name = "people")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "people-type")
class People {
    @XmlElement(name = "person")
    private val people: MutableSet<Person> = HashSet()
    @XmlTransient
    private var peopleCount : Int = 0
    @XmlTransient
    private var idToPerson: MutableMap<String, Person> = HashMap()
    @XmlTransient
    private var nameToPerson: MutableMap<String, Person> = HashMap()

    fun getPeopleCount() = peopleCount
    fun setPeopleCount(cnt: Long) {
        peopleCount = cnt.toInt()
    }
    fun getPersonId(id: String) = idToPerson[id]
    fun getPersonName(name: String) = nameToPerson[name]

//    fun addPerson(person: Person) {
//        var byId: Person? = null
//        var byName: Person? = null
//        if (person.id != null && idToPerson[person.id] != null) {
//            byId = this.idToPerson[person.id]!!
//        }
//        if (person.getFullName() != null && nameToPerson[person.getFullName()] != null) {
//            byName = this.nameToPerson[person.getFullName()]!!
//        }
//        if (byName != null) {
//            person.merge(byName)
//        }
//        if (byId != null) {
//            person.merge(byId)
//        }
//
//        if (person.id != null) {
//            idToPerson[person.id!!] = person
//        }
//        if (person.getFullName() != null) {
//            nameToPerson[person.getFullName()!!] = person
//        }
//    }

    fun addPerson(person: Person) {
        val byId = if (person.id != null) idToPerson[person.id] else null
        val byName = if (person.getFullName() != null) nameToPerson[person.getFullName()] else null

        byId?.let { person.merge(it) }
        byName?.let { person.merge(it) }

        person.id?.let { idToPerson[it] = person }
        person.getFullName()?.let { nameToPerson[it] = person }
    }

    fun removePerson(person: Person) {
        if (person.id != null) {
            idToPerson.remove(person.id)
        }
        if (person.getFullName() != null) {
            nameToPerson.remove(person.firstname)
        }
    }

    fun getPeople(): MutableSet<Person> {
       this.people.addAll(Stream.concat(idToPerson.values.stream(), nameToPerson.values.stream()).collect(Collectors.toSet()))
        return this.people
    }

    fun filterPeopleNonNull() {
        this.people.map { person ->
            if (person.firstname == null) {
                person.firstname = "noNAME"
                noName++
            }
            if (person.surname == null) {
                person.surname = "noSURNAME"
                noSurname++
            }
            if (person.gender == null) {
                person.gender = "noGENDER"
                noGender++
            }
        }
    }
}
