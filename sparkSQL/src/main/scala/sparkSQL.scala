import org.apache.spark._
import org.apache.spark.sql._
import org.apache.spark.SparkContext._

object sparkSQL {
  case class Person(ID:Int, name:String, age:Int, numFriends:Int)

  def mapper(line:String): Person = {
    val fields = line.split(',')

    val person:Person = Person(fields(0).toInt, fields(1), fields(2).toInt, fields(3).toInt)
    return person
  }

  def main(args: Array[String]) {

    val spark = SparkSession.builder.appName("sparkSQL")
        .master("local[*]")
        .getOrCreate()

    val data = spark.sparkContext.textFile("data/fakefriends.csv")
    val people = data.map(mapper)

    import spark.implicits._
    val schemaPeople = people.toDS()
    schemaPeople.printSchema()
    schemaPeople.createOrReplaceTempView("people")

    val teenagers = spark.sql("SELECT * FROM people WHERE age>14 AND age<20")
    val results = teenagers.collect()
    results.foreach(println)
    spark.stop()
  }
}
