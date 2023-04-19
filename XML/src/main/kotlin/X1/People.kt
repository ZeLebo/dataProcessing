package X1

import java.util.stream.Stream
import java.util.stream.Collectors

class People(
    private val people: MutableSet<Person> = HashSet(),
    private var peopleCount : Int = 0,
    private var idToPerson: MutableMap<String, Person> = HashMap(),
    private var nameToPerson: MutableMap<String, Person> = HashMap(),
) {
    fun getPeopleCount() = peopleCount
    fun setPeopleCount(cnt: Long) {
        peopleCount = cnt.toInt()
    }
    fun getPersonId(id: String) = idToPerson[id]
    fun getPersonName(name: String) = nameToPerson[name]

    fun addPerson(person: Person) {
        var byId: Person? = null
        var byName: Person? = null
        if (person.id != null && idToPerson[person.id] != null) {
            byId = this.idToPerson[person.id]!!
        }
        if (person.getFullName() != null && nameToPerson[person.getFullName()] != null) {
            byName = this.nameToPerson[person.getFullName()]!!
        }
        if (byName != null) {
            person.merge(byName)
        }
        if (byId != null) {
            person.merge(byId)
        }

        if (person.id != null) {
            idToPerson[person.id!!] = person
        }
        if (person.getFullName() != null) {
            nameToPerson[person.name!!] = person
        }
    }

    fun removePerson(person: Person) {
        if (person.id != null) {
            idToPerson.remove(person.id)
        }
        if (person.getFullName() != null) {
            nameToPerson.remove(person.name)
        }
    }

    fun getPeople(): MutableSet<Person> {
       this.people.addAll(Stream.concat(idToPerson.values.stream(), nameToPerson.values.stream()).collect(Collectors.toSet()))
        return this.people
    }

    fun validatePeople(): Boolean {
        var people = this.getPeople()
//        return people.size == peopleCount && people.all { it.validate() }
        return people.size == peopleCount && people.stream().allMatch(Person::validate)
    }
}
