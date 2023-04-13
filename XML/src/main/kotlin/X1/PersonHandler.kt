package X1

import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler

class PersonHandler : DefaultHandler() {
    private lateinit var currentPerson: Person
    private var personCount = 0
    private var data = StringBuilder()
    lateinit var merger: Merger

    override fun endDocument() {
        println("Finished parsing, found $personCount count")
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
                    var id = attributes.getValue("id")
                    currentPerson.daughtersIds.add(attributes.getValue("id")!!.trim())
                    currentPerson.childrenIds.add(id)
                }

                "son" -> {
                    var id = attributes.getValue("id")
                    currentPerson.sonsIds.add(attributes.getValue("id")!!.trim())
                    currentPerson.childrenIds.add(id)
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
                merger.addPerson(currentPerson)
            }
        }
        data.clear()
    }
}
