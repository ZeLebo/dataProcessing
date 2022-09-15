import java.util.ArrayList
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.TimeUnit

internal class Company(departmentsCount: Int) {
    private val departments: List<Department>

    init {
        departments = ArrayList(departmentsCount)
        for (i in 0 until departmentsCount) {
            departments.add(i, Department(i))
        }
    }

    /**
     * Вывод результата по всем отделам.
     * P.S. Актуально после того, как все отделы выполнят свою работу.
     */
    fun showCollaborativeResult() {
        System.out.println("All departments have completed their work.")
        val result: Int = departments.stream()
            .map { obj: Department -> obj.calculationResult }
            .reduce(Integer::sum)
            .orElse(-1)
        System.out.println("The sum of all calculations is: $result")
    }

    /**
     * @return Количество доступных отделов для симуляции выполнения
     * работы.
     */

    fun getDepartmentsCount() {
        return size(departments)
    }
    /**
     * @param index Индекс для текущего свободного отдела.
     * @return Свободный отдел для симуляции выполнения работы.
     */
    fun getFreeDepartment(index: Int): Department {
        return departments[index]
    }
}

internal class Department(
    /**
     * @return Уникальный идентификатор для отдела,
     * по которому мы можем отличить его от других.
     */
    val identifier: Int
    ) {
    private val workingSeconds: Int = ThreadLocalRandom.current().nextInt(1, 6)

    /**
     * ВАЖНО!
     * Далеко не самый правильный способ вычисления и получения данных,
     * но для демонстрации работы барьера пойдёт.
     *
     * @return Результат вычислений.
     */
    var calculationResult = 0
        private set

    /**
     * Симуляция работы длительностью в workingSeconds секунд.
     * В данном случае просто вычисляем сумму.
     */
    fun performCalculations() {
        for (i in 0 until workingSeconds) {
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(1))
            } catch (ignored: InterruptedException) {
                // Ignored case.
            }
            calculationResult += i
        }
    }
}

internal class Founder(company: Company) {
    private val workers: List<Runnable>

    init {
        workers = ArrayList(company.departmentsCount)
    }

    fun start() {
        for (worker in workers) {
            Thread(worker).start()
        }
    }
}