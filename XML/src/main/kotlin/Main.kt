import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler
import javax.xml.parsers.SAXParserFactory

fun main() {
    val factory = SAXParserFactory.newInstance()
    val parser = factory.newSAXParser()
    val handler = PersonHandler()
    parser.parse("src/people.xml", handler)
    val res = Merger(handler)
    res.merge()
    res.print()
}

class Merger(
    private val handler: PersonHandler
) {
    private val result = mutableListOf<Person>()
    fun merge() {
        val people = handler.people
        val merged = people.groupBy { it.name }
            .map { (name, people) ->
                Person(
                    name = name,
                )
            }
        result.addAll(merged)
    }

    fun print() {
        result.forEach {
            println("Name: ${it.name}")
            println("Father: ${it.fatherId} ${it.father}")
            println("Mother: ${it.motherId} ${it.mother}")

            println("Husband: ${it.husbandId}")
            println("Wife: ${it.wifeId}")

            println("Spouse: ${it.spouceName} ${it.spouceIds}")

            println("Children: ${it.childrenIds} ${it.childrenNames}")
            println("Brothers: ${it.brothersIds} ${it.brotherNames}")
            println("Sisters: ${it.sistersIds} ${it.sisterNames}")
            println()
        }
    }
}

class PersonHandler : DefaultHandler() {
    val people = mutableListOf<Person>()
    lateinit var currentPerson: Person
    var personCount = 0
    var data = StringBuilder()

    override fun endDocument() {
        println("Finished parsing, found $personCount count and ${people.size} people")
    }

    private fun getElementValue(attributes: Attributes): String? {
        val value = attributes.getValue("val") ?: attributes.getValue("value")
        if ((value == null) || (value == "NONE") || value == "UNKNOWN") {
            return null
        }
        return value
    }

    private fun genderConverter(gender: String?): Gender? {
        return when (gender) {
            "F", "female" -> Gender.F
            "M", "male" -> Gender.M
            null -> null

            else -> {
                throw Exception("No such gender")
            }
        }
    }

    override fun startElement(uri: String, localName: String, qName: String, attributes: Attributes) {
        val elementValue = getElementValue(attributes)?.trim()?.replace("\\s+".toRegex(), " ")

        if (elementValue != null) {
            when (qName) {
                "first", "firstname" -> {
                    currentPerson.name = elementValue
                }

                "family-name", "surname", "family" -> {
                    currentPerson.surname = elementValue
                }

                "id" -> {
                    currentPerson.id = elementValue
                }

                "gender" -> {
                    currentPerson.gender = genderConverter(elementValue)
                }

                "siblings-number" -> {
                    currentPerson.siblingsNumber = elementValue.toInt()
                }

                "children-number" -> {
                    currentPerson.childrenNumber = elementValue.toInt()
                }

                "wife" -> {
                    currentPerson.wifeId = elementValue
                }

                "husband" -> {
                    currentPerson.husbandId = elementValue
                }

                "spouce" -> {
                    currentPerson.spouceName.add(elementValue)
                }

                "parent" -> {
                    currentPerson.parentIds.add(elementValue)
                }

                "siblings" -> {
                    currentPerson.siblingsIds.add(elementValue)
                }

                else -> {
                    println("Unknown element: $qName")
                }
            }
        } else {
            when (qName) {
                "people" -> {
                    personCount = attributes.getValue("count")?.toInt() ?: 0
                }

                "person" -> {
                    currentPerson = Person()
                    currentPerson.id = attributes.getValue("id")
                    currentPerson.setFullName(attributes.getValue("name"))
                }

                "daughter" -> {
                    currentPerson.daughtersIds.add(attributes.getValue("id")!!.trim())
                }

                "son" -> {
                    currentPerson.sonsIds.add(attributes.getValue("id")!!.trim())
                }
            }
        }
    }

    override fun endElement(uri: String, localName: String, qName: String) {
        val d = data.toString().trim().replace("\\s+".toRegex(), " ")

        if (d.isNotBlank()) {
            when (qName) {
                "firstname", "first" -> {
                    currentPerson.name = d
                }

                "family-name", "surname", "family" -> {
                    currentPerson.surname = d
                }

                "gender" -> {
                    currentPerson.gender = genderConverter(d)
                }

                "mother" -> {
                    currentPerson.mother = d
                }

                "father" -> {
                    currentPerson.father = d
                }

                "sister" -> {
                    currentPerson.sisterNames.add(d)
                }

                "brother" -> {
                    currentPerson.brotherNames.add(d)
                }

                "child" -> {
                    currentPerson.childrenNames.add(d)
                }
            }
        }
        when (qName) {
            "person" -> {
                people.add(currentPerson)
            }
        }
        data.clear()
    }
}

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