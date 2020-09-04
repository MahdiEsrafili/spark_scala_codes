import org.apache.spark._
import org.apache.spark.SparkContext._

object book_words {

  def main(args: Array[String]) {
    val sc = new SparkContext("local[*]", "book_words")
    val data = sc.textFile("data/book.txt")
    // val words = data.flatMap(x => x.split(' ')).map(x => (x,1)).reduceByKey((x,y)=> x+y)
    // val to_print = words.sorted
    // to_print.foreach(println)
    // val words = data.flatMap(x => x.split("\\W+"))
    // val lower_words = words.map(x => x.toLowerCase())
    // val word_count = lower_words.map(x => (x, 1)).reduceByKey( (x,y) => x + y )
    // val sorted_word_count = word_count.map(x => (x._2, x._1)).sortByKey()
    // for (word <- sorted_word_count) {
    //   val count = word._1
    //   val w = word._2
    //   println(s"$w: $count")
    // }
    val words = data.flatMap(x => x.split("\\W+"))

    // Normalize everything to lowercase
    val lowercaseWords = words.map(x => x.toLowerCase())

    // Count of the occurrences of each word
    val wordCounts = lowercaseWords.map(x => (x, 1)).reduceByKey( (x,y) => x + y )

    // Flip (word, count) tuples to (count, word) and then sort by key (the counts)
    val wordCountsSorted = wordCounts.map( x => (x._2, x._1) ).sortByKey()

    // Print the results, flipping the (count, word) results to word: count as we go.
    for (result <- wordCountsSorted) {
      val count = result._1
      val word = result._2
      println(s"$word: $count")
    }
  }
}
