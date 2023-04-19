package X1

import java.io.File
import javax.xml.stream.XMLOutputFactory
import javax.xml.stream.XMLStreamWriter
import javax.xml.validation.SchemaFactory

class XmlWriter {
    private val outputFile = File("src/output-people.xml")
    private val writer: XMLStreamWriter = XMLOutputFactory.newFactory().createXMLStreamWriter(outputFile.outputStream())

    fun write(people: MutableSet<Person>) {
        writer.writeStartDocument()
        writer.writeStartElement("people")

        for (person in people) {
            if (person.id  == null) { continue }
            writer.writeStartElement("person")
            writer.writeAttribute("id", person.id)
            person.name?.let {
                writer.writeAttribute("name", it)
            }
            person.surname?.let {
                writer.writeAttribute("surname", it)
            }
            person.gender?.let {
                writer.writeAttribute("gender", it)
            }

            person.wife?.id?.let {
                writer.writeAttribute("wife-id", it)
            }
            person.husband?.id?.let {
                writer.writeAttribute("husband-id", it)
            }

            person.father?.id?.let {
                writer.writeAttribute("father-id", it)
            }
            person.mother?.id?.let {
                writer.writeAttribute("mother-id", it)
            }

            // if sons set is not empty - write sons
            if (person.sons.isNotEmpty()) {
                writer.writeStartElement("sons")
                for (son in person.sons) {
                    writer.writeAttribute("id", son.id)
                }
                writer.writeEndElement() // end sons element
                writer.writeCharacters("\n ")
            }
            // if daughters set is not empty - write daughters
            if (person.daughters.isNotEmpty()) {
                writer.writeStartElement("daughters")
                for (daughter in person.daughters) {
                    writer.writeAttribute("id", daughter.id)
                }
                writer.writeEndElement() // end daughters element
                writer.writeCharacters("\n ")
            }

            // if brothers set is not empty - write brothers
            if (person.brothers.isNotEmpty()) {
                writer.writeStartElement("brothers")
                for (brother in person.brothers) {
                    writer.writeAttribute("id", brother.id)
                }
                writer.writeEndElement() // end brothers element
                writer.writeCharacters("\n ")
            }

            // if sisters set is not empty - write sisters
            if (person.sisters.isNotEmpty()) {
                writer.writeStartElement("sisters")
                for (sister in person.sisters) {
                    writer.writeAttribute("id", sister.id)
                }
                writer.writeEndElement() // end sisters element
                writer.writeCharacters("\n ")
            }

            writer.writeEndElement() // end person element
            writer.writeCharacters("\n ")
        }

        writer.writeEndDocument()
        writer.flush()
        writer.close()

        println("XML file written to ${outputFile.absolutePath}")
    }
}