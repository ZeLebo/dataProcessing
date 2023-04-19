package X1

fun main() {
    val parser = PersonHandler()
    parser.parse("src/people.xml")
    println(parser.people.getPeopleCount())
    println(parser.people.getPeople().size)
    val writer = XmlWriter()
    writer.write(parser.people.getPeople())
}


