package X1

@Suppress("unused")
class Merger(
    private val handler: PersonHandler
) {
    val inputPeople = mutableListOf<Person>()
    var result = mutableListOf<Person>()
    private val table = HashMap<String?, Person>()

    init {
        inputPeople.addAll(handler.people)
    }

    fun mergeById() {
        // check everybody for id: children, parents, person, spouse
        for (person in inputPeople) {
            // check for id
            // add person itself to the map
            if (person.id != null) {
                if (table[person.id] == null) {
                    table[person.id] = person
                } else {
                    // add all the info about person from the table
                    var fromTable = table[person.id]
                    if (fromTable != null) {
                        person.gender = fromTable.gender
                    }
                }
            } else {
                continue
            }

            // add person children to the map and set the relation
            for (childId in person.childrenIds) {
                // add if not exist
                if (table[childId] == null) {
                    table[childId] = Person()
                    table[childId]?.id = childId
                }

                // add parent
                person.id?.let { 
                    table[childId]?.parentIds?.add(it) 

                    // if in child it's defined the gender
                    if (table[childId]?.motherId == it) {
                        person.gender = Gender.F
                        table[it]?.gender = Gender.F
                    }
                    if (table[childId]?.fatherId == it) {
                        person.gender = Gender.M
                        table[it]?.gender = Gender.M
                    }

                    // set parent as mother or father
                    if (person.gender == Gender.F) {
                        table[childId]?.motherId = it
                    }
                    if (person.gender == Gender.M) {
                        table[childId]?.fatherId = it
                    }
                }

            }

            // adding siblings and parent info about them
            for (siblingId in person.siblingsIds) {
                // add if not exist
                if (table[siblingId] == null) {
                    table[siblingId] = Person()
                    table[siblingId]?.id = siblingId
                }

                person.id?.let {
                    // add siblings to the person
                    if (table[it]?.siblingsIds?.contains(siblingId) == false) {
                        table[it]?.siblingsIds?.add(siblingId)
                    }

                    // add person to the sibling
                    if (table[siblingId]?.siblingsIds?.contains(it) == false) {
                        table[siblingId]?.siblingsIds?.add(it)
                    }

                    // check for gender
                    // add gender to the sibling
                    if (table[siblingId]?.gender == null) {
                        // from sisters and brothers list add gender to the sibling
                        if (table[it]?.sistersIds?.contains(it)!!) {
                            table[siblingId]?.gender = Gender.F
                        }

                        if (table[it]?.brothersIds?.contains(it)!!) {
                            table[siblingId]?.gender = Gender.M
                        }
                    } else {
                        // sibling has gender
                        if (table[siblingId]?.gender == Gender.F) {
                            // add to lists if not in the list
                            if (!table[it]?.sistersIds?.contains(siblingId)!!) {
                                table[it]?.sistersIds?.add(siblingId)
                                person.sistersIds.add(siblingId)
                            }
                        } else {
                            if (!table[it]?.brothersIds?.contains(siblingId)!! ) {
                                table[it]?.brothersIds?.add(siblingId)
                                person.brothersIds.add(siblingId)
                            }
                        }
                    }

                    // if person has no gender check it in lists of sibling
                    if (table[it]?.gender == null) {
                        if (table[siblingId]?.brothersIds?.contains(it) == true) {
                            table[it]?.gender = Gender.M
                            person.gender = Gender.M
                        }
                        if (table[siblingId]?.sistersIds?.contains(it) == true) {
                            table[it]?.gender = Gender.F
                            person.gender = Gender.F
                        }
                    } else {
                        // if person does have gender -> put the person's id in the list in sibling
                        if (table[siblingId]?.sistersIds?.contains(it) == false) {
                            table[siblingId]?.sistersIds?.add(it)
                        }
                        if (table[siblingId]?.brothersIds?.contains(it) == false) {
                            table[siblingId]?.brothersIds?.add(it)
                        }
                    }

                    // if sibling has parent and this person doesn't have -> add from sibling
                    if (table[siblingId]?.fatherId != null && person.fatherId == null) {
                        table[it]?.fatherId = table[siblingId]?.fatherId
                        person.fatherId = table[siblingId]?.fatherId
                    }
                    if (table[siblingId]?.motherId != null && person.motherId == null) {
                        table[it]?.motherId = table[siblingId]?.motherId
                        person.motherId = table[siblingId]?.motherId
                    }

                    // wise versa
                    if (table[siblingId]?.fatherId == null && person.fatherId != null) {
                        table[siblingId]?.fatherId = person.fatherId
                    }
                    if (table[siblingId]?.motherId == null && person.motherId != null) {
                        table[siblingId]?.motherId = person.motherId
                    }
                }
            }

            // add person parents to the map and set the relation
            // todo need to check for siblings here or not?

            for (parentId in person.parentIds) {
                // add if not exists
                if (table[parentId] == null) {
                    table[parentId] = Person()
                    table[parentId]?.id = parentId
                }

                person.id?.let {
                    // add this id to the parent if not exist
                    if (!table[parentId]?.childrenIds?.contains(it)!!) {
                        table[parentId]?.childrenIds?.add(it)
                    }
                    // if person has motherId -> add gender to the parent
                    if (person.motherId == parentId) {
                        table[parentId]?.gender = Gender.F
                    }
                    if (person.fatherId == parentId) {
                        table[parentId]?.gender = Gender.M
                    }

                    // get the gender through the parent
                    if (table[parentId]?.sonsIds?.contains(it)!!) {
                        person.gender = Gender.M
                        table[it]?.gender = Gender.M
                    }

                    if (table[parentId]?.daughtersIds?.contains(it)!!) {
                        person.gender = Gender.F
                        table[it]?.gender = Gender.F
                    } 
                }
            }

            // adding spouse
            for (spouseId in person.spouceIds) {
                if (table[spouseId] == null) {
                    table[spouseId] = Person()
                    table[spouseId]?.id = spouseId
                }

                person.id?.let {
                    // add spouse to the person if not exist
                    if (!table[it]?.spouceIds?.contains(spouseId)!!) {
                        table[it]?.spouceIds?.add(spouseId)
                    }
                    // add perons to the spouse if not exist
                    if (!table[spouseId]?.spouceIds?.contains(it)!!) {
                        table[spouseId]?.spouceIds?.add(it)
                    }

                    // check the gender of the spouse following by the husbandid or wife id
                    if (table[it]?.wifeId == spouseId) {
                        table[spouseId]?.gender = Gender.F
                    }
                    if (table[it]?.husbandId == spouseId) {
                        table[spouseId]?.gender = Gender.M
                    }

                    // get the gender of person from the spouse
                    if (table[spouseId]?.husbandId == it) {
                        table[it]?.gender = Gender.M
                    }
                    if (table[spouseId]?.wifeId == it) {
                        table[it]?.gender = Gender.F
                    }

                    // adding children to the spouse
                    for (childId in person.childrenIds) {
                        if (!table[spouseId]?.childrenIds?.contains(childId)!!) {
                            table[spouseId]?.childrenIds?.add(childId)
                        }
                    }

                    // adding children to the person
                    for (childId in table[spouseId]?.childrenIds!!) {
                        if (!table[it]?.childrenIds?.contains(childId)!!) {
                            table[it]?.childrenIds?.add(childId)
                        }
                    }
                }
            }
        }

        result.addAll(table.values)

    }

    fun mergeNames() {
        // parse the names and surnames

    }

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


    fun printAll() {
        result.forEach {
            println("Name: ${it.name}")
            println("Id: ${it.id}")
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
        println("The amount of people: ${result.size}")
    }
}
