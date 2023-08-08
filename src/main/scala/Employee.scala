import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration._

/**
 * В данном примере мы создаем список сотрудников (employees), разделяем его на части для каждого потока, создаем список
 * будущих результатов (futures), и каждый поток обрабатывает свою часть сотрудников с ограниченным доступом к ресурсу
 * (используя synchronized для синхронизации доступа). В итоге все потоки дожидаются завершения друг друга, и результаты
 * выводятся на экран.
 * */

case class Employee(name: String, surName: String, age: Int, salary: Double, department: String)

object ThreadExample {
  def main(args: Array[String]): Unit = {
    val resourceLimit = 4 // Максимальное количество потоков с доступом к ресурсу

    // Создаем список для хранения Employee
    val employees: ListBuffer[Employee] = ListBuffer()

    // Заполняем список
    for (i <- 1 to 20) {
      employees += Employee(s"Name$i", s"Surname$i", 25 + i, 1000 + i * 100, s"Department${i % 5}")
    }

    // Разделяем список на части для каждого потока
    val partitionedEmployees = employees.grouped(employees.size / resourceLimit).toList

    // Создаем список будущих результатов
    val futures = partitionedEmployees.map { partition =>
      Future {
        processEmployees(partition)
      }
    }

    // Дожидаемся завершения всех потоков
    Await.result(Future.sequence(futures), Duration.Inf)

    // Выводим результат
    employees.foreach(println)
  }

  def processEmployees(employees: Seq[Employee]): Unit = {
    employees.foreach { employee =>
      synchronized { // Ограничиваем доступ к ресурсу с помощью synchronized
        println(s"Processing employee: ${employee.name}")
        // Здесь может быть логика обработки сотрудника
      }
    }
  }
}
