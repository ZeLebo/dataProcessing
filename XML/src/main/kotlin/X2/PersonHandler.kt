package X2

import java.io.FileInputStream
import java.util.function.Consumer
import java.util.stream.Stream
import java.util.Objects
import java.util.stream.Collectors
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.events.Attribute
import javax.xml.stream.events.Characters
import javax.xml.stream.events.EndElement
import javax.xml.stream.events.StartElement

class PersonHandler(
    val people: People = People(),
    private var currentPerson: Person = Person(),
    private var needNewOne: Boolean = true,

    private val factory: XMLInputFactory = XMLInputFactory.newInstance(),
    private val tagContext: ArrayDeque<String> = ArrayDeque(),
) {
    fun parse(filename: String):  People {
        try {
            val reader = factory.createXMLEventReader(FileInputStream(filename))
            while (reader.hasNext()) {
                val event = reader.nextEvent()
                if (event.isStartElement) {
                    processStartElement(event.asStartElement())
                } else if (event.isEndElement) {
                    processEndElement(event.asEndElement())
                } else if (event.isCharacters) {
                    processCharacters(event.asCharacters())
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        normalizePeople()
        linkPeople()
        return people
    }

    private fun processAttributes(tag: String, iterator: Iterator<Attribute>, processor: Consumer<String>) {
        this.tagContext.add(tag)
        if (this.needNewOne) {
            this.currentPerson = Person()
        }
        while (iterator.hasNext()) {
            val attribute = iterator.next()
            val localPart = attribute.name.localPart
            val value = attribute.value.trim().replace(" +", "")
            if (localPart.equals("count") || localPart.equals("id") || localPart.equals("name") || localPart.equals("val") || localPart.equals("value")) {
                processData(value, processor)
            }
        }
    }

    private fun processData(value: String, processor: Consumer<String>) {
        if (value.matches("(P\\d+ )+(P\\d+)".toRegex())) {
            for (i in value.split(" ")) {
                processor.accept(i)
            }
        } else {
            processor.accept(value)
        }
    }

    private fun processStartElement(event: StartElement) {
        // switch case for startelement.name.localpart
        when (event.name.localPart) {
            "people" -> processAttributes("people", event.attributes, this::processPeople)

            "person" -> processAttributes("person", event.attributes, this::processPerson)
            "id" -> processAttributes("id", event.attributes, this::processId)
            "fullname" -> processAttributes("name", event.attributes, this::processName)
            "firstname", "first" -> processAttributes("firstname", event.attributes, this::processFirstName)
            "surname", "family", "family-name" -> processAttributes("surname", event.attributes, this::processLastName)
            "gender" -> processAttributes("gender", event.attributes, this::processGender)

            "spouce" -> processAttributes("spouce", event.attributes, this::processSpouse)
            "husband" -> processAttributes("husband", event.attributes, this::processHusband)
            "wife" -> processAttributes("wife", event.attributes, this::processWife)

            "parents", "parent" -> processAttributes("parents", event.attributes, this::processParent)
            "father" -> processAttributes("father", event.attributes, this::processFather)
            "mother" -> processAttributes("mother", event.attributes, this::processMother)

            "children-number" -> processAttributes("children-number", event.attributes, this::processChildrenNumber)
            "children", "child" -> processAttributes("children", event.attributes, this::processChild)
            "son" -> processAttributes("son", event.attributes, this::processSon)
            "daughter" -> processAttributes("daughter", event.attributes, this::processDaughter)

            "siblings-number" -> processAttributes("siblings-number", event.attributes, this::processSiblingsNumber)
            "siblings", "sibling" -> processAttributes("siblings", event.attributes, this::processSiblings)
            "brother" -> processAttributes("brother", event.attributes, this::processBrother)
            "sister" -> processAttributes("sister", event.attributes, this::processSister)
        }
    }

    private fun processEndElement(event: EndElement) {
        if (event.name.localPart.equals("person")) {
            if (currentPerson != null) {
                this.people.addPerson(currentPerson)
            }
            this.needNewOne = true
        }
        tagContext.removeLast()
    }

    private fun processCharacters(characters: Characters) {
        if (!this.tagContext.isEmpty()) {
            // peek in tag context
            when (this.tagContext.first()) {
                "people" -> processData(characters.data.trim().replace(" +", ""), this::processPeople)
                "person" -> processData(characters.data.trim().replace(" +", ""), this::processPerson)
                "id" -> processData(characters.data.trim().replace(" +", ""), this::processId)
                "name" -> processData(characters.data.trim().replace(" +", ""), this::processName)
                "firstname" -> processData(characters.data.trim().replace(" +", ""), this::processFirstName)
                "surname" -> processData(characters.data.trim().replace(" +", ""), this::processLastName)
                "gender" -> processData(characters.data.trim().replace(" +", ""), this::processGender)
                "spouce" -> processData(characters.data.trim().replace(" +", ""), this::processSpouse)
                "husband" -> processData(characters.data.trim().replace(" +", ""), this::processHusband)
                "wife" -> processData(characters.data.trim().replace(" +", ""), this::processWife)
                "parents" -> processData(characters.data.trim().replace(" +", ""), this::processParent)
                "father" -> processData(characters.data.trim().replace(" +", ""), this::processFather)
                "mother" -> processData(characters.data.trim().replace(" +", ""), this::processMother)
                "children-number" -> processData(characters.data.trim().replace(" +", ""), this::processChildrenNumber)
                "children" -> processData(characters.data.trim().replace(" +", ""), this::processChild)
                "son" -> processData(characters.data.trim().replace(" +", ""), this::processSon)
                "daughter" -> processData(characters.data.trim().replace(" +", ""), this::processDaughter)
                "siblings-number" -> processData(characters.data.trim().replace(" +", ""), this::processSiblingsNumber)
                "siblings" -> processData(characters.data.trim().replace(" +", ""), this::processSiblings)
                "brother" -> processData(characters.data.trim().replace(" +", ""), this::processBrother)
                "sister" -> processData(characters.data.trim().replace(" +", ""), this::processSister)
            }
        }
    }

    private fun normalizePeople() {
        for (person in people.getPeople()) {
            normalizeSpouse(person)
            normalizeHusband(person)
            normalizeWife(person)
            normalizeParents(person)
            normalizeFather(person)
            normalizeMother(person)
            normalizeChildren(person)
            normalizeSons(person)
            normalizeDaughters(person)
            normalizeSiblings(person)
            normalizeBrothers(person)
            normalizeSisters(person)
        }
    }
    private fun linkPeople() {
        for (person in people.getPeople()) {
            linkHusband(person)
            linkWife(person)
            linkFather(person)
            linkMother(person)
            linkSons(person)
            linkDaughters(person)
            linkBrothers(person)
            linkSisters(person)
        }
    }

    private fun processPeople(value: String) {
        if (value.matches("\\d+".toRegex())) {
            people.setPeopleCount(value.toLong())
        }
    }

    private fun processChildrenNumber(value: String) {
        if (value.matches("\\d+".toRegex())) {
            currentPerson.childrenNumber = value.toInt()
        }
    }

    private fun processSiblingsNumber(value: String) {
        if (value.matches("\\d+".toRegex())) {
            currentPerson.siblingsNumber = value.toInt()
        }
    }

    private fun processPerson(value: String) {
        if (value.matches("P\\d+".toRegex())) {
            currentPerson.id = value
        } else if (!value.equals("NONE") && !value.equals("UNKNOWN")) {
            currentPerson.setFullName(value)
        }
        needNewOne  = false
    }

    private fun processId(value: String) {
        if (value.matches("P\\d+".toRegex())) {
            currentPerson.id = value
        }
    }

    private fun processName(value: String) {
        if (!value.equals("NONE") && !value.equals("UNKNOWN")) {
            currentPerson.setFullName(value)
        }
    }

    private fun processFirstName(value: String) {
        if (!value.equals("NONE") && !value.equals("UNKNOWN")) {
            currentPerson.name = value
        }
    }

    private fun processLastName(value: String) {
        if (!value.equals("NONE") && !value.equals("UNKNOWN")) {
            currentPerson.surname = value
        }
    }

    private fun processGender(value: String) {
        when (value) {
            "M", "male" -> currentPerson.gender = "Male"
            "F", "female" -> currentPerson.gender = "Female"
        }
    }

    private fun processSpouse(value: String) {
        if (value.matches("P\\d+".toRegex())) {
            currentPerson.spouseId = value
        } else if (!value.equals("NONE") && !value.equals("UNKNOWN")) {
            currentPerson.spouseName = value
        }
    }

    private fun processHusband(value: String) {
        if (value.matches("P\\d+".toRegex())) {
            currentPerson.husbandId = value
        } else if (!value.equals("NONE") && !value.equals("UNKNOWN")) {
            currentPerson.husbandName = value
        }
    }

    private fun processWife(value: String) {
        if (value.matches("P\\d+".toRegex())) {
            currentPerson.wifeId = value
        } else if (!value.equals("NONE") && !value.equals("UNKNOWN")) {
            currentPerson.wifeName = value
        }
    }

    private fun processParent(value: String) {
        if (value.matches("P\\d+".toRegex())) {
            currentPerson.addParentId(value)
        } else if (!value.equals("NONE") && !value.equals("UNKNOWN")) {
            currentPerson.addParentName(value)
        }
    }

    private fun processFather(value: String) {
        if (value.matches("P\\d+".toRegex())) {
            currentPerson.fatherId = value
        } else if (!value.equals("NONE") && !value.equals("UNKNOWN")) {
            currentPerson.fatherName = value
        }
    }

    private fun processMother(value: String) {
        if (value.matches("P\\d+".toRegex())) {
            currentPerson.motherId = value
        } else if (!value.equals("NONE") && !value.equals("UNKNOWN")) {
            currentPerson.motherName = value
        }
    }
    private fun processChild(value: String) {
        if (value.matches("P\\d+".toRegex())) {
            currentPerson.addChildId(value)
        } else if (!value.equals("NONE") && !value.equals("UNKNOWN")) {
            currentPerson.addChildName(value)
        }
    }

    private fun processSon(value: String) {
        if (value.matches("P\\d+".toRegex())) {
            currentPerson.addSonId(value)
        } else if (!value.equals("NONE") && !value.equals("UNKNOWN")) {
            currentPerson.addSonName(value)
        }
    }

    private fun processDaughter(value: String) {
        if (value.matches("P\\d+".toRegex())) {
            currentPerson.addDaughterId(value)
        } else if (!value.equals("NONE") && !value.equals("UNKNOWN")) {
            currentPerson.addDaughterName(value)
        }
    }

    private fun processSiblings(value: String) {
        if (value.matches("P\\d+".toRegex())) {
            currentPerson.addSiblingId(value)
        } else if (!value.equals("NONE") && !value.equals("UNKNOWN")) {
            currentPerson.addSiblingName(value)
        }
    }

    private fun processBrother(value: String) {
        if (value.matches("P\\d+".toRegex())) {
            currentPerson.addBrotherId(value)
        } else if (!value.equals("NONE") && !value.equals("UNKNOWN")) {
            currentPerson.addBrotherName(value)
        }
    }

    private fun processSister(value: String) {
        if (value.matches("P\\d+".toRegex())) {
            currentPerson.addSisterId(value)
        } else if (!value.equals("NONE") && !value.equals("UNKNOWN")) {
            currentPerson.addSisterName(value)
        }
    }

    // linking
    private fun linkHusband(person: Person) {
        val husband = person.husbandId?.let { this.people.getPersonId(it) }
            ?: person.husbandName?.let { this.people.getPersonName(it) }
        if (husband != null) {
            person.husband = husband
            person.husbandId = null
            person.husbandName = null
        }
    }

    private fun linkWife(person: Person) {
        val wife = person.wifeId?.let { this.people.getPersonId(it) }
            ?: person.wifeName?.let { this.people.getPersonName(it) }
        if (wife != null) {
            person.wife = wife
            person.wifeId = null
            person.wifeName = null
        }
    }

    private fun linkFather(person: Person) {
        val father = person.fatherId?.let { this.people.getPersonId(it) }
            ?: person.fatherName?.let { this.people.getPersonName(it) }
        if (father != null) {
            person.father = father
            person.fatherId = null
            person.fatherName = null
        }
    }

    private fun linkMother(person: Person) {
        val mother = person.motherId?.let { this.people.getPersonId(it) }
            ?: person.motherName?.let { this.people.getPersonName(it) }
        if (mother != null) {
            person.mother = mother
            person.motherId = null
            person.motherName = null
        }
    }

    private fun linkSons(person: Person) {
        for (son in Stream.concat(person.sonsIds.stream().map(this.people::getPersonId),  person.sonsNames.stream().map(this.people::getPersonName))
            .filter(Objects::nonNull).collect(Collectors.toSet())) {
            if (son != null) {
                person.addSon(son)
                if (son.id != null) {
                    person.removeSonId(son.id!!)
                }
                if (son.getFullName() != null) {
                    person.removeSonName(son.getFullName()!!)
                }
            }
        }
    }

    private fun linkDaughters(person: Person) {
        for (daughter in Stream.concat(person.daughtersIds.stream().map(this.people::getPersonId),  person.daughtersNames.stream().map(this.people::getPersonName))
            .filter(Objects::nonNull).collect(Collectors.toSet())) {
            if (daughter != null) {
                person.addDaughter(daughter)
                if (daughter.id != null) {
                    person.removeDaughterId(daughter.id!!)
                }
                if (daughter.getFullName() != null) {
                    person.removeDaughterName(daughter.getFullName()!!)
                }
            }
        }
    }

    private fun linkBrothers(person: Person) {
        for (brother in Stream.concat(person.brothersIds.stream().map(this.people::getPersonId),  person.brotherNames.stream().map(this.people::getPersonName))
            .filter(Objects::nonNull).collect(Collectors.toSet())) {
            if (brother != null) {
                person.addBrother(brother)
                if (brother.id != null) {
                    person.removeBrotherId(brother.id!!)
                }
                if (brother.getFullName() != null) {
                    person.removeBrotherName(brother.getFullName()!!)
                }
            }
        }
    }

    private fun linkSisters(person: Person) {
        for (sister in Stream.concat(person.sistersIds.stream().map(this.people::getPersonId),  person.sisterNames.stream().map(this.people::getPersonName))
            .filter(Objects::nonNull).collect(Collectors.toSet())) {
            if (sister != null) {
                person.addSister(sister)
                if (sister.id != null) {
                    person.removeSisterId(sister.id!!)
                }
                if (sister.getFullName() != null) {
                    person.removeSisterName(sister.getFullName()!!)
                }
            }
        }
    }

    // normalization (are we in ML???)
    private fun normalizeHusband(person: Person) {
        val husband = person.husbandId?.let { this.people.getPersonId(it) }
            ?: person.husbandName?.let { this.people.getPersonName(it) }
        if (husband != null) {
            husband.gender = "Male"
            husband.wifeId = person.id
            husband.wifeName = person.getFullName()
            husband.spouseId = null
            husband.spouseName = null

            person.gender = "Female"
            person.husbandId = husband.id
            person.husbandName = husband.getFullName()
            person.spouseId = null
            person.spouseName = null
        }
    }

    private fun normalizeWife(person: Person) {
        val wife = person.wifeId?.let { this.people.getPersonId(it) }
            ?: person.wifeName?.let { this.people.getPersonName(it) }
        if (wife != null) {
            wife.gender = "Female"
            wife.husbandId = person.id
            wife.husbandName = person.getFullName()
            wife.spouseId = null
            wife.spouseName = null

            person.gender = "Male"
            person.wifeId = wife.id
            person.wifeName = wife.getFullName()
            person.spouseId = null
            person.spouseName = null
        }
    }

    private fun normalizeSpouse(person: Person) {
        val spouse = person.spouseId?.let { this.people.getPersonId(it) }
            ?: person.spouseName?.let { this.people.getPersonName(it) }
        if (spouse != null) {
            spouse.spouseId = person.id
            spouse.spouseName = person.getFullName()
            person.spouseId = spouse.id
            person.spouseName = spouse.getFullName()

            if ((person.gender != null && person.gender.equals("Male")) || (spouse.gender != null && spouse.gender.equals("Female"))) {
                spouse.gender = "Female"
                spouse.husbandId = person.id
                spouse.husbandName = person.getFullName()
                spouse.spouseId = null
                spouse.spouseName = null

                person.gender = "Male"
                person.wifeId = spouse.id
                person.wifeName = spouse.getFullName()
                person.spouseId = null
                person.spouseName = null
            } else if ((person.gender != null && person.gender.equals("Female")) || (spouse.gender != null && spouse.gender.equals("Male"))) {
                spouse.gender = "Male"
                spouse.wifeId = person.id
                spouse.wifeName = person.getFullName()
                spouse.spouseId = null
                spouse.spouseName = null

                person.gender = "Female"
                person.husbandId = spouse.id
                person.husbandName = spouse.getFullName()
                person.spouseId = null
                person.spouseName = null
            }
        }
    }

    private fun normalizeFather(person: Person) {
        val father = person.fatherId?.let { this.people.getPersonId(it) }
            ?: person.fatherName?.let { this.people.getPersonName(it) }
        if (father != null) {
            father.gender = "Male"
            person.id?.let { father.addChildId(it) }
            person.getFullName()?.let { father.addChildName(it) }

            person.fatherId = father.id
            person.fatherName = father.getFullName()
            father.id?.let { person.removeParentId(it) }
            father.getFullName()?.let { person.removeParentName(it) }
            if (person.gender != null && person.gender.equals("Male")) {
                person.id?.let { father.addSonId(it) }
                person.getFullName()?.let { father.addSonName(it) }
                person.id?.let { father.removeChildId(it) }
                person.getFullName()?.let { father.removeChildName(it) }
            } else if (person.gender != null && person.gender.equals("Female")) {
                person.id?.let { father.addDaughterId(it) }
                person.getFullName()?.let { father.addDaughterName(it) }
                person.id?.let { father.removeChildId(it) }
                person.getFullName()?.let { father.removeChildName(it) }
            }
        }
    }


    private fun normalizeMother(person: Person) {
        val mother = person.motherId?.let { this.people.getPersonId(it) }
            ?: person.motherName?.let { this.people.getPersonName(it) }
        if (mother != null) {
            mother.gender = "Female"
            person.id?.let { mother.addChildId(it) }
            person.getFullName()?.let { mother.addChildName(it) }

            person.motherId = mother.id
            person.motherName = mother.getFullName()
            mother.id?.let { person.removeParentId(it) }
            mother.getFullName()?.let { person.removeParentName(it) }
            if (person.gender != null && person.gender.equals("Male")) {
                person.id?.let { mother.addSonId(it) }
                person.getFullName()?.let { mother.addSonName(it) }
                person.id?.let { mother.removeChildId(it) }
                person.getFullName()?.let { mother.removeChildName(it) }
            } else if (person.gender != null && person.gender.equals("Female")) {
                person.id?.let { mother.addDaughterId(it) }
                person.getFullName()?.let { mother.addDaughterName(it) }
                person.id?.let { mother.removeChildId(it) }
                person.getFullName()?.let { mother.removeChildName(it) }
            }
        }
    }

    private fun normalizeParents(person: Person) {
        for (parent in Stream.concat(person.parentIds.stream().map(this.people::getPersonId),  person.parentNames.stream().map(this.people::getPersonName)).filter(Objects::nonNull).collect(Collectors.toSet())) {
           if (parent != null) {
               person.id?.let { parent.addChildId(it) }
               person.getFullName()?.let { parent.addChildName(it) }
               parent.id?.let { person.addParentId(it) }
               parent.getFullName()?.let { person.addParentName(it) }

               if (person.gender != null && person.gender.equals("Male")) {
                   person.id?.let { parent.addSonId(it) }
                   person.getFullName()?.let { parent.addSonName(it) }
                   person.id?.let { parent.removeChildId(it) }
                   person.getFullName()?.let { parent.removeChildName(it) }
               } else if (person.gender != null && person.gender.equals("Female")) {
                   person.id?.let { parent.addDaughterId(it) }
                   person.getFullName()?.let { parent.addDaughterName(it) }
                   person.id?.let { parent.removeChildId(it) }
                   person.getFullName()?.let { parent.removeChildName(it) }
               }
               if (parent.gender != null && parent.gender.equals("Male")) {
                   person.fatherId = parent.id
                   person.fatherName = parent.getFullName()
                   parent.id?.let { person.removeParentId(it) }
                   parent.getFullName()?.let { person.removeParentName(it) }
               } else if (parent.gender != null && parent.gender.equals("Female")) {
                   person.motherId = parent.id
                   person.motherName = parent.getFullName()
                   parent.id?.let { person.removeParentId(it) }
                   parent.getFullName()?.let { person.removeParentName(it) }
               }
           }
        }
    }

    private fun normalizeSons(person: Person) {
        for (son in Stream.concat(person.sonsIds.stream().map(this.people::getPersonId), person.sonsNames.stream().map(this.people::getPersonName)).filter(Objects::nonNull).collect(Collectors.toSet())) {
            if (son != null) {
                son.gender = "Male"
                person.id?.let { son.addParentId(it) }
                person.getFullName()?.let { son.addParentName(it) }

                son.id?.let { person.addSonId(it) }
                son.getFullName()?.let { person.addSonName(it) }
                son.id?.let { person.removeChildId(it) }
                son.getFullName()?.let { person.removeChildName(it) }

                if (person.gender != null && person.gender.equals("Male")) {
                    son.fatherId = person.id
                    son.fatherName = person.getFullName()
                    person.getFullName()?.let { son.removeParentName(it) }
                    person.id?.let { son.removeParentId(it) }
                } else if (person.gender != null && person.gender.equals("Female")) {
                    son.motherId = person.id
                    son.motherName = person.getFullName()
                    person.id?.let { son.removeParentId(it) }
                    person.getFullName()?.let { son.removeParentName(it) }
                }
            }
        }
    }

    private fun normalizeDaughters(person: Person) {
        for (daughter in Stream.concat(person.daughtersIds.stream().map(this.people::getPersonId), person.daughtersNames.stream().map(this.people::getPersonName)).filter(Objects::nonNull).collect(Collectors.toSet())) {
            if (daughter != null) {
                daughter.gender = "Female"
                person.id?.let { daughter.addParentId(it) }
                person.getFullName()?.let { daughter.addParentName(it) }

                daughter.id?.let { person.addDaughterId(it) }
                daughter.getFullName()?.let { person.addDaughterName(it) }
                daughter.id?.let { person.removeDaughterId(it) }
                daughter.getFullName()?.let { person.removeDaughterName(it) }
                if (person.gender != null && person.gender.equals("Male")) {
                    daughter.fatherId = person.id
                    daughter.fatherName = person.getFullName()
                    person.id?.let { daughter.removeParentId(it) }
                    person.getFullName()?.let { daughter.removeParentName(it) }
                } else if (person.gender != null && person.gender.equals("Female")) {
                    daughter.motherId = person.id
                    daughter.motherName = person.getFullName()
                    person.id?.let { daughter.removeParentId(it) }
                    person.getFullName()?.let { daughter.removeParentName(it) }
                }
            }
        }
    }

    private fun normalizeChildren(person: Person) {
        for (child in Stream.concat(person.childrenIds.stream().map(this.people::getPersonId), person.childrenNames.stream().map(this.people::getPersonName)).filter(Objects::nonNull).collect(Collectors.toSet())) {
            if (child != null) {
                person.id?.let { child.addParentId(it) }
                person.getFullName()?.let { child.addParentName(it) }
                child.id?.let { person.addChildId(it) }
                child.getFullName()?.let { person.addChildName(it) }
                if (person.gender != null && person.gender.equals("Male")) {
                    child.fatherId = person.id
                    child.fatherName = person.getFullName()
                    person.id?.let { child.removeParentId(it) }
                    person.getFullName()?.let { child.removeParentName(it) }
                } else if (person.gender != null && person.gender.equals("Female")) {
                    person.id?.let {
                        child.motherId = it
                        child.removeParentId(it)
                    }
                    person.getFullName()?.let {
                        child.motherName = it
                        child.removeParentName(it)
                    }
                }
                if (child.gender != null && child.gender.equals("Male")) {
                    child.id?.let {
                       person.addSonId(it)
                       person.removeChildId(it)
                    }
                    child.getFullName()?.let {
                        person.addSonName(it)
                        person.removeChildName(it)
                    }
                } else if (child.gender != null && child.gender.equals("Female")) {
                    child.id?.let {
                        person.addDaughterId(it)
                        person.removeChildId(it)
                    }
                    child.getFullName()?.let {
                        person.addDaughterName(it)
                        person.removeChildName(it)
                    }
                }
            }
        }
    }

    private fun normalizeBrothers(person: Person) {
        for (brother in Stream.concat(person.brothersIds.stream().map(this.people::getPersonId), person.brotherNames.stream().map(this.people::getPersonName)).filter(Objects::nonNull).collect(Collectors.toSet())) {
            if (brother != null) {
                brother.gender = "Male"
                person.id?.let { brother.addSiblingId(it) }
                person.getFullName()?.let { brother.addSiblingName(it) }

                brother.id?.let {
                    person.addBrotherId(it)
                    person.removeSiblingId(it)
                }
                brother.getFullName()?.let {
                    person.addBrotherName(it)
                    person.removeSiblingName(it)
                }

                if (person.gender != null && person.gender.equals("Male")) {
                    person.id?.let { brother.addBrotherId(it) }
                    person.getFullName()?.let { brother.addBrotherName(it) }
                    person.id?.let { brother.removeSiblingId(it) }
                    person.getFullName()?.let { brother.removeSiblingName(it) }
                } else if (person.gender != null && person.gender.equals("Female")) {
                    person.id?.let { brother.addSisterId(it) }
                    person.getFullName()?.let { brother.addSisterName(it) }
                    person.id?.let { brother.removeSiblingId(it) }
                    person.getFullName()?.let { brother.removeSiblingName(it) }
                }
            }
        }
    }

    private fun normalizeSisters(person: Person) {
        for (sister in Stream.concat(person.sistersIds.stream().map(this.people::getPersonId), person.sisterNames.stream().map(this.people::getPersonName)).filter(Objects::nonNull).collect(Collectors.toSet())) {
            if (sister != null) {
                sister.gender = "Female"
                person.id?.let { sister.addSisterId(it) }
                person.getFullName()?.let { sister.addSisterName(it) }

                sister.id?.let {
                    person.addSisterId(it)
                    person.removeSiblingId(it)
                }
                sister.getFullName()?.let {
                    person.addSisterName(it)
                    person.removeSiblingName(it)
                }
                if (person.gender != null && person.gender.equals("Male")) {
                    person.id?.let {
                        sister.addBrotherId(it)
                        sister.removeSiblingId(it)
                    }
                    person.getFullName()?.let {
                        sister.addBrotherName(it)
                        sister.removeSiblingName(it)
                    }
                } else if (person.gender != null && person.gender.equals("Female")) {
                    person.id?.let {
                        sister.addSisterId(it)
                        sister.removeSiblingId(it)
                    }
                    person.getFullName()?.let {
                        sister.addSisterName(it)
                        sister.removeSiblingName(it)
                    }
                }
            }
        }
    }

    private fun normalizeSiblings(person: Person) {
        for (sibling in Stream.concat(person.siblingsIds.stream().map(this.people::getPersonId), person.siblingsNames.stream().map(this.people::getPersonName)).filter(Objects::nonNull).collect(Collectors.toSet())) {
            if (sibling != null) {
                person.id?.let { sibling.addSiblingId(it) }
                person.getFullName()?.let { sibling.addSiblingName(it) }
                sibling.id?.let { person.addSiblingId(it) }
                sibling.getFullName()?.let { person.addSiblingName(it) }

                if (person.gender != null && person.gender.equals("Male")) {
                    person.id?.let {
                        sibling.addBrotherId(it)
                        sibling.removeSiblingId(it)
                    }
                    person.getFullName()?.let {
                        sibling.addBrotherName(it)
                        sibling.removeSiblingName(it)
                    }
                } else if (person.gender != null && person.gender.equals("Female")) {
                    person.id?.let {
                        sibling.addSisterId(it)
                        sibling.removeSiblingId(it)
                    }
                    person.getFullName()?.let {
                        sibling.addSisterName(it)
                        sibling.removeSiblingName(it)
                    }
                }
                if (sibling.gender != null && sibling.gender.equals("Male")) {
                    sibling.id?.let {
                        person.addBrotherId(it)
                        person.removeSiblingId(it)
                    }
                    sibling.getFullName()?.let {
                        person.addBrotherName(it)
                        person.removeSiblingName(it)
                    }
                } else if (sibling.gender != null && sibling.gender.equals("Female")) {
                    sibling.id?.let {
                        person.addSisterId(it)
                        person.removeSiblingId(it)
                    }
                    sibling.getFullName()?.let {
                        person.addSisterName(it)
                        person.removeSiblingName(it)
                    }
                }
            }
        }
    }
}