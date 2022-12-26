import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler
import javax.xml.parsers.SAXParserFactory

fun main() {
    val factory = SAXParserFactory.newInstance()
    val parser = factory.newSAXParser()
    val handler = PersonHandler()
    parser.parse("src/people.xml", handler)

    println("The data is")
    handler.people.forEach {
        println(it.id)
        println(it.name)
        println(it.surname)
    }
}

class PersonHandler : DefaultHandler() {
    val people = mutableListOf<Person>()
    lateinit var currentPerson: Person

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

                "spouse" -> {
                    currentPerson.spouseNames.add(elementValue)
                }
            }
        }

    }
}

class Person(
    var id: String? = null,
    var name: String? = null,
    var surname: String? = null,
    var gender: Gender? = null,

    var spouseNames: MutableList<String> = mutableListOf(),
    var spouseIds: MutableList<String> = mutableListOf(),

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