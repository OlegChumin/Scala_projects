import scala.collection.mutable.Seq
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration._

case class Employee(name: String, surName: String, age: Int, salary: Double, department: String)

object ThreadExample {
  def main(args: Array[String]): Unit = {
    val resourceLimit = 4 // Максимальное количество потоков с доступом к ресурсу

    // Создаем список для хранения Employee
    var employees: Seq[Employee] = Seq()

    // Заполняем список
    for (i <- 1 to 20) {
      employees :+= Employee(s"Name$i", s"Surname$i", 25 + i, 1000 + i * 100, s"Department${i % 5}")
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
