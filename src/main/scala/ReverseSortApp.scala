import scala.io.StdIn

object ReverseSortApp {
  def main(args: Array[String]): Unit = {
    // Считываем количество чисел
    println("Введите количество чисел:")
    val count = StdIn.readInt()

    // Инициализируем массив для хранения чисел
    val numbers = new Array[Int](count)

    // Считываем числа и сохраняем их в массив
    println(s"Введите $count чисел:")
    for (i <- 0 until count) {
      numbers(i) = StdIn.readInt()
    }

    // Используем стримы для сортировки чисел в обратном порядке
    val sortedNumbers = numbers.sorted(Ordering[Int].reverse)

    // Выводим отсортированный массив
    println("Отсортированный массив в обратном порядке:")
    sortedNumbers.foreach(println)
  }
}
