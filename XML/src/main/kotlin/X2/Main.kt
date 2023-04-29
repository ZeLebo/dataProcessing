package X2

fun main() {
    val parser = PersonHandler()
    println("start parsing")
    parser.parse("src/people.xml")
    println(parser.people.getPeopleCount())
    println("size of people set:")
    println(parser.people.getPeople().size)
    val writer = XmlWriter()
    println("start writing")
    writer.write(parser.people)
}


