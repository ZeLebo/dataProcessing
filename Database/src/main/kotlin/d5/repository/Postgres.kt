package d5.repository

import d5.dto.ParameterFlight
import d5.dto.Seat
import d5.dto.UnitFlight
import d5.dto.ticket
import d5.entity.Airport
import d5.entity.Flight
import java.sql.Connection
import java.sql.DriverManager
import java.util.*


class Postgres {
    private var connection: Connection = this.connect()
    var sqlRequest: String = ""
    private fun connect(): Connection {
        // check for driver
        Class.forName("org.postgresql.Driver")
        return DriverManager.getConnection(
            "jdbc:postgresql://127.0.0.1:5432/demo",
            "root",
            "root"
        ) ?: throw RuntimeException("Cannot connect to database")
    }

    fun disconnect(): Unit {
        this.connection.close()
    }


    fun addAirport(code: String) {
        sqlRequest = "INSERT INTO bookings.airports (airport_code, airport_name, city, longitude, latitude, timezone) VALUES ('$code', 'name', '$code', 0, 0, 'Europe/Moscow')"
        val statement = connection.createStatement()
        statement.use {
            it.executeUpdate(sqlRequest)
        }
    }

    fun deleteAirport(code: String) {
        sqlRequest = "DELETE FROM bookings.airports WHERE airport_code = '$code'"
        val statement = connection.createStatement()
        statement.use {
            it.executeUpdate(sqlRequest)
        }
    }

    fun getAllAirports(): MutableSet<Airport> {
        sqlRequest = "SELECT airport_code, airport_name, city from bookings.airports"
        val res = mutableSetOf<Airport>()

        val statement = connection.createStatement()
        statement.use {st ->
            val resSet = st.executeQuery(sqlRequest)
            while (resSet.next()) {
                val code = resSet.getString("airport_code")
                val name = resSet.getString("airport_name")
                val city = resSet.getString("city")
                res.add(Airport(code, name, city))
            }
        }

        return res
    }

    fun getAllAirports(city: String): MutableSet<Airport> {
        sqlRequest = "select airport_code, airport_name, city from bookings.airports where city = '$city'"
        val res = mutableSetOf<Airport>()

        val statement = connection.createStatement()
        statement.use {
            val resSet = it.executeQuery(sqlRequest)
            while (resSet.next()) {
                val code = resSet.getString("airport_code")
                val name = resSet.getString("airport_name")
                val city = resSet.getString("city")
                res.add(Airport(code, name, city))
            }
        }

        return res
    }


    fun getAllCities(): MutableSet<String> {
        sqlRequest = """Select city from bookings.airports"""
        val res = mutableSetOf<String>()
        val statement = connection.createStatement()
        statement.use {
            val resSet = it.executeQuery(sqlRequest)
            while (resSet.next()) {
                res.add(resSet.getString("city"))
            }
        }

        return res
    }

    fun getInboundSchedule(airport_code: String): MutableSet<Flight> {
        sqlRequest = "select * from bookings.flights where arrival_airport = '$airport_code' and status != 'Arrived'"
        val res = mutableSetOf<Flight>()
        val statement = connection.createStatement()
        statement.use {
            val resSet = it.executeQuery(sqlRequest)
            while(resSet.next()) {
                val flight_id = resSet.getString("flight_id")
                val flight_no = resSet.getString("flight_no")
                val scheduled_departure = resSet.getString("scheduled_departure")
                val scheduled_arrival = resSet.getString("scheduled_arrival")
                val departure_airport = resSet.getString("departure_airport")
                val arrival_airport = resSet.getString("arrival_airport")
                val status = resSet.getString("status")
                res.add(Flight(
                    flight_id, flight_no,
                    scheduled_departure, scheduled_arrival,
                    departure_airport, arrival_airport,
                    status)
                )
            }
        }
        return res
    }

    fun getOutboundSchedule(airport_code: String): MutableSet<Flight> {
        sqlRequest = "select * from bookings.flights where departure_airport = '$airport_code' and status != 'Arrived'"
        val res = mutableSetOf<Flight>()
        val statement = connection.createStatement()
        statement.use {
            val resSet = it.executeQuery(sqlRequest)
            while(resSet.next()) {
                val flight_id = resSet.getString("flight_id")
                val flight_no = resSet.getString("flight_no")
                val scheduled_departure = resSet.getString("scheduled_departure")
                val scheduled_arrival = resSet.getString("scheduled_arrival")
                val departure_airport = resSet.getString("departure_airport")
                val arrival_airport = resSet.getString("arrival_airport")
                val status = resSet.getString("status")
                res.add(Flight(
                    flight_id, flight_no,
                    scheduled_departure, scheduled_arrival,
                    departure_airport, arrival_airport,
                    status)
                )
            }
        }
        return res
    }

    /*
    * @name - name of user (first + surname)
    * @identification - passport number
    * @flight_id - flight id
    * @fare_conditions - fare conditions (comfort, economy, business)
    * */
    fun bookTheFlight(name: String, identification: String, flight_id: Int, fare_conditions: String): ticket {
        // need to do in transaction
        // first need to check that we have enough seats for this person to this flight
        // count all the places with given flight_id and fare_conditions from table ticket_flights
        // count all the possible places for the plane with given flight_id
        // from flight_id get aircraft_code and then count all the places from table seats for this aircraft_code
        // if we have enough places then we can book the flight -> add new row to table bookings.bookings
        // then add new row to table bookings.tickets using new booking_ref and data about user
        // then add new row to table bookings.ticket_flights using new ticket_no and flight_id

        // check that flight exists
        sqlRequest = "select count(*) from bookings.flights where flight_id = $flight_id"
        var count = 0
        var statement = connection.createStatement()
        statement.use {
            val resSet = it.executeQuery(sqlRequest)
            while(resSet.next()) {
                count = resSet.getInt(1)
            }
        }
        if (count == 0) {
            throw Exception("Flight with id $flight_id doesn't exist")
        }

        // check that flight not in status Arrived
        sqlRequest = "select status from bookings.flights where flight_id = $flight_id"
        var status = ""
        statement = connection.createStatement()
        statement.use {
            val resSet = it.executeQuery(sqlRequest)
            while(resSet.next()) {
                status = resSet.getString("status")
            }
        }
        if (status == "Arrived") {
            throw RuntimeException("Flight already arrived")
            return ticket("", 0.0)
        }

        var left_seats = 0
        // the following code is transaction
        connection.autoCommit = false

        // getting amount of free seats
        sqlRequest = "select count(*) -" +
                "       (select count(*) from bookings.ticket_flights where fare_conditions = '${fare_conditions}' and flight_id = $flight_id) left_places " +
                "from bookings.seats " +
                "where aircraft_code = (select aircraft_code from bookings.flights where flight_id = $flight_id and fare_conditions = '${fare_conditions}');"
        statement = connection.createStatement()
        statement.use {
            val resSet = it.executeQuery(sqlRequest)
            while(resSet.next()) {
                left_seats = resSet.getInt("left_places")
            }
        }

        println("left_seats = $left_seats")

        if (left_seats == 0) {
            // we don't have enough seats
            println("we don't have enough seats")
            connection.rollback()
            connection.autoCommit = true
            throw RuntimeException("we don't have enough seats")
        }

        // get the price for the place
        var price = 0.0
        sqlRequest = "select amount from bookings.ticket_flights where flight_id = $flight_id and fare_conditions = '$fare_conditions' order by amount limit 1;"
        statement = connection.createStatement()
        statement.use {
            val resSet = it.executeQuery(sqlRequest)
            while(resSet.next()) {
                price = resSet.getDouble("amount")
            }
        }

        // if we have enough seats then we can book the flight
        // booking_ref is in 0085C7 this format
        val booking_ref = UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase()
        // check the uniqueness of booking_ref
        sqlRequest = "select count(*) from bookings.bookings where book_ref = '$booking_ref';"
        statement = connection.createStatement()
        statement.use {
            val resSet = it.executeQuery(sqlRequest)
            while(resSet.next()) {
                if (resSet.getInt("count") != 0) {
                    // we have the same booking_ref
                    println("we have the same booking_ref")
                    connection.rollback()
                    connection.autoCommit = true
                    throw RuntimeException("we have the same booking_ref")
                }
            }
        }
        // ticket_no is in 0005432000988 this format
        val ticket_no: String = UUID.randomUUID().toString().replace("-", "").substring(0, 13)
        sqlRequest = "insert into bookings.bookings (book_ref, book_date, total_amount) values ('$booking_ref', now(), $price) returning book_ref;"
        statement = connection.createStatement()
        statement.use {
            val resSet = it.executeQuery(sqlRequest)
            while(resSet.next()) {
                println(resSet.getString("book_ref"))
            }
        }

        // adding new ticket to table bookings.tickets with booking_ref
        sqlRequest = """insert into bookings.tickets (ticket_no, book_ref, passenger_id, passenger_name, contact_data) values ('$ticket_no', '$booking_ref', '$identification', '$name', '{"email": "example.com", "phone": "+77777777777"}') returning *;"""
        statement = connection.createStatement()
        statement.use {
            it.executeQuery(sqlRequest)
        }
        connection.commit()
        // adding to ticket_flight
        sqlRequest = "insert into bookings.ticket_flights (ticket_no, flight_id, fare_conditions, amount) VALUES ('$ticket_no', $flight_id, '$fare_conditions', $price) returning *;"
        statement = connection.createStatement()
        statement.use {
            it.executeQuery(sqlRequest)
        }


        connection.commit()
        connection.autoCommit = true
        return ticket(ticket_no, price)
    }

    fun checkoutFlight(ticket_no: String): Seat {
        // get the flight_id and fare_conditions from ticket_no from table bookings.ticket_flights
        // get aircraft_code from flight_id from table bookings.flights
        // get the seat_no from seats table based on aircraft_code and fare_conditions order by extra_space limit 1
        // add new row to table bookings.boarding_passes with ticket_no, flight_id, boarding_no, seat_no
        // where boarding_no is cnt of boarding_passes with given flight_id

        // check the ticket is not checked
        var sqlRequest = "select count(*) from bookings.boarding_passes where ticket_no = '$ticket_no';"
        var statement = connection.createStatement()
        statement.use {
            val resSet = it.executeQuery(sqlRequest)
            while(resSet.next()) {
                if (resSet.getInt("count") != 0) {
                    // we have the same booking_ref
                    println("the ticket is already checked")
                    throw RuntimeException("the ticket is already checked")
                    return Seat("", 0)
                }
            }
        }

        connection.autoCommit = false

        sqlRequest = "select flight_id, fare_conditions from bookings.ticket_flights where ticket_no = '$ticket_no';"
        var flight_id = 0
        var fare_conditions = ""
        statement = connection.createStatement()
        statement.use {
            val resSet = it.executeQuery(sqlRequest)
            while(resSet.next()) {
                flight_id = resSet.getInt("flight_id")
                fare_conditions = resSet.getString("fare_conditions")
            }
        }

        sqlRequest = "select aircraft_code from bookings.flights where flight_id = $flight_id;"
        var aircraft_code = ""
        statement = connection.createStatement()
        statement.use {
            val resSet = it.executeQuery(sqlRequest)
            while(resSet.next()) {
                aircraft_code = resSet.getString("aircraft_code")
            }
        }

        // get the seat_no from seats table based on aircraft_code and fare_conditions order by extra_space limit 1 and not in boarding_passes
        sqlRequest = "select seat_no from bookings.seats where aircraft_code = '$aircraft_code' and fare_conditions = '$fare_conditions' and seat_no not in (select seat_no from bookings.boarding_passes where flight_id = $flight_id) order by extra_space limit 1;"
        var seat_no = ""
        statement = connection.createStatement()
        statement.use {
            val resSet = it.executeQuery(sqlRequest)
            while(resSet.next()) {
                seat_no = resSet.getString("seat_no")
            }
        }

        sqlRequest = "select count(*) from bookings.boarding_passes where flight_id = $flight_id;"
        var boarding_no = 0
        statement = connection.createStatement()
        statement.use {
            val resSet = it.executeQuery(sqlRequest)
            while(resSet.next()) {
                boarding_no = resSet.getInt("count")
            }
        }
        boarding_no++

        connection.commit()

        // check for place in boarding_passes table, if such seat already exists then rollback
        sqlRequest = "select count(*) from bookings.boarding_passes where flight_id = $flight_id and seat_no = '$seat_no';"
        statement = connection.createStatement()
        statement.use {
            val resSet = it.executeQuery(sqlRequest)
            while(resSet.next()) {
                if (resSet.getInt("count") > 0) {
                    println("this seat is already taken")
                    connection.rollback()
                    connection.autoCommit = true
                    return Seat("", 0)
                }
            }
        }

        // insert into bookings.boarding_passes (ticket_no, flight_id, boarding_no, seat_no) VALUES ('d37bb5e8c2014', 12, 0, '3D') returning *;
        sqlRequest = "insert into bookings.boarding_passes (ticket_no, flight_id, boarding_no, seat_no) VALUES ('$ticket_no', $flight_id, $boarding_no, '$seat_no') returning *;"
        statement = connection.createStatement()
        statement.use {
            it.executeQuery(sqlRequest)
        }

        connection.commit()
        connection.autoCommit = true
        return Seat(seat_no, boarding_no)
    }

    fun getFlightsByAirports(airport_code_dest: String, airport_code_arrival: String, n: Int = 1): MutableList<ParameterFlight> {
        val res: MutableList<ParameterFlight> = mutableListOf()
        if (n == 1) {
            sqlRequest = "select distinct flight_no, departure_airport, arrival_airport from bookings.flights where departure_airport = '$airport_code_dest' and arrival_airport = '$airport_code_arrival';"
            val statement = connection.createStatement()
            statement.use {
                val resSet = it.executeQuery(sqlRequest)
                var tmp: UnitFlight
                while(resSet.next()) {
                    tmp = UnitFlight(resSet.getString("flight_no"), resSet.getString("departure_airport"),
                        resSet.getString("arrival_airport"))
                    res.add(ParameterFlight(listOf(tmp)))
                }
            }
        } else if (n == 2) {
            sqlRequest = "select distinct g.flight_no, g.arrival_airport, g.departure_airport, r.flight_no rfli, r.arrival_airport rarri, r.departure_airport rdep" +
                    " from bookings.routes g " +
                    "         left join bookings.routes r" +
                    "                   on g.arrival_airport = r.departure_airport and r.arrival_airport != g.departure_airport" +
                    " where g.departure_airport = '$airport_code_dest'" +
                    "  and r.arrival_airport = '$airport_code_arrival';"
            val statement = connection.createStatement()
            statement.use {
                val resSet = it.executeQuery(sqlRequest)
                // println the result of query

                var tmp: UnitFlight
                var tmp2: UnitFlight
                while(resSet.next()) {
                    tmp = UnitFlight(resSet.getString("flight_no"), resSet.getString("departure_airport"),
                        resSet.getString("arrival_airport"))
                    tmp2 = UnitFlight(resSet.getString("rfli"), resSet.getString("rdep"),
                        resSet.getString("rarri"))
                    res.add(ParameterFlight(listOf(tmp, tmp2)))
                }
            }
        } else if (n == 3) {
            sqlRequest = "select distinct g.flight_no, g.arrival_airport, g.departure_airport, r.flight_no rfli, r.arrival_airport rarri, r.departure_airport rdep," +
                    " s.flight_no sfli, s.arrival_airport sarri, s.departure_airport sdep" +
                    " from bookings.routes g" +
                    "         left join bookings.routes r" +
                    "                   on g.arrival_airport = r.departure_airport and r.arrival_airport != g.departure_airport" +
                    "         left join bookings.routes s" +
                    "                   on r.arrival_airport = s.departure_airport and r.arrival_airport != g.departure_airport and" +
                    "                           s.arrival_airport != r.departure_airport " +
                    " where g.departure_airport = '$airport_code_dest'" +
                    "  and s.arrival_airport = '$airport_code_arrival';"
            val statement = connection.createStatement()
            statement.use {
                val resSet = it.executeQuery(sqlRequest)
                var tmp: UnitFlight
                var tmp2: UnitFlight
                var tmp3: UnitFlight
                while(resSet.next()) {
                    tmp = UnitFlight(resSet.getString("flight_no"), resSet.getString("departure_airport"),
                        resSet.getString("arrival_airport"))
                    tmp2 = UnitFlight(resSet.getString("rfli"), resSet.getString("rdep"),
                        resSet.getString("rarri"))
                    tmp3 = UnitFlight(resSet.getString("sfli"), resSet.getString("sdep"),
                        resSet.getString("sarri"))
                    res.add(ParameterFlight(listOf(tmp, tmp2, tmp3)))
                }
            }
        } else if (n == 4) {
            sqlRequest = "select distinct g.flight_no, g.arrival_airport, g.departure_airport, r.flight_no rfli, r.arrival_airport rarri, r.departure_airport rdep," +
                    " s.flight_no sfli, s.arrival_airport sarri, s.departure_airport sdep, t.flight_no tfli, t.arrival_airport tarri, t.departure_airport tdep" +
                    " from bookings.routes g" +
                    "         left join bookings.routes r" +
                    "                   on g.arrival_airport = r.departure_airport and r.arrival_airport != g.departure_airport" +
                    "         left join bookings.routes s" +
                    "                   on r.arrival_airport = s.departure_airport and r.arrival_airport != g.departure_airport and" +
                    "                           s.arrival_airport != r.departure_airport " +
                    "         left join bookings.routes t" +
                    "                   on s.arrival_airport = t.departure_airport and r.arrival_airport != g.departure_airport and" +
                    "                      s.arrival_airport != r.departure_airport and t.arrival_airport != s.departure_airport" +
                    " where g.departure_airport = '$airport_code_dest'" +
                    "  and t.arrival_airport = '$airport_code_arrival';"
            val statement = connection.createStatement()
            statement.use {
                val resSet = it.executeQuery(sqlRequest)
                var tmp: UnitFlight
                var tmp2: UnitFlight
                var tmp3: UnitFlight
                var tmp4: UnitFlight
                while(resSet.next()) {
                    tmp = UnitFlight(resSet.getString("flight_no"), resSet.getString("departure_airport"),
                        resSet.getString("arrival_airport"))
                    tmp2 = UnitFlight(resSet.getString("rfli"), resSet.getString("rdep"),
                        resSet.getString("rarri"))
                    tmp3 = UnitFlight(resSet.getString("sfli"), resSet.getString("sdep"),
                        resSet.getString("sarri"))
                    tmp4 = UnitFlight(resSet.getString("tfli"), resSet.getString("tdep"),
                        resSet.getString("tarri"))
                    res.add(ParameterFlight(listOf(tmp, tmp2, tmp3, tmp4)))
                }
            }
        } else if (n == 5) {
            sqlRequest = "select distinct g.flight_no, g.arrival_airport, g.departure_airport, r.flight_no rfli, r.arrival_airport rarri, r.departure_airport rdep," +
                    " s.flight_no sfli, s.arrival_airport sarri, s.departure_airport sdep, t.flight_no tfli, t.arrival_airport tarri, t.departure_airport tdep," +
                    " u.flight_no ufli, u.arrival_airport uarri, u.departure_airport udep" +
                    " from bookings.routes g" +
                    "         left join bookings.routes r" +
                    "                   on g.arrival_airport = r.departure_airport and r.arrival_airport != g.departure_airport" +
                    "         left join bookings.routes s" +
                    "                   on r.arrival_airport = s.departure_airport and r.arrival_airport != g.departure_airport and" +
                    "                           s.arrival_airport != r.departure_airport " +
                    "         left join bookings.routes t" +
                    "                   on s.arrival_airport = t.departure_airport and r.arrival_airport != g.departure_airport and" +
                    "                      s.arrival_airport != r.departure_airport and t.arrival_airport != s.departure_airport" +
                    "         left join bookings.routes u" +
                    "                   on u.arrival_airport = t.departure_airport and r.arrival_airport != g.departure_airport and" +
                    "                      s.arrival_airport != r.departure_airport and t.arrival_airport != s.departure_airport and" +
                    "                    u.arrival_airport != t.departure_airport" +
                    " where g.departure_airport = '$airport_code_dest'" +
                    "  and u.arrival_airport = '$airport_code_arrival';"
            val statement = connection.createStatement()
            statement.use {
                val resSet = it.executeQuery(sqlRequest)
                var tmp: UnitFlight
                var tmp2: UnitFlight
                var tmp3: UnitFlight
                var tmp4: UnitFlight
                var tmp5: UnitFlight
                while(resSet.next()) {
                    tmp = UnitFlight(resSet.getString("flight_no"), resSet.getString("departure_airport"),
                        resSet.getString("arrival_airport"))
                    tmp2 = UnitFlight(resSet.getString("rfli"), resSet.getString("rdep"),
                        resSet.getString("rarri"))
                    tmp3 = UnitFlight(resSet.getString("sfli"), resSet.getString("sdep"),
                        resSet.getString("sarri"))
                    tmp4 = UnitFlight(resSet.getString("tfli"), resSet.getString("tdep"),
                        resSet.getString("tarri"))
                    tmp5 = UnitFlight(resSet.getString("ufli"), resSet.getString("udep"),
                        resSet.getString("uarri"))
                    res.add(ParameterFlight(listOf(tmp, tmp2, tmp3, tmp4, tmp5)))
                }
            }
        }
//        println(res)
        return res
    }
}

fun main() {
    val db = Postgres()
//    val ticket_no = db.bookTheFlight("Ivan Ivanov", "123456789", 12, "Economy")
//    println(ticket_no)
//    println(db.checkoutFlight(ticket_no.ticket_no))
    println(db.getFlightsByAirports("UIK", "DME", 3).size)
    db.disconnect()
}