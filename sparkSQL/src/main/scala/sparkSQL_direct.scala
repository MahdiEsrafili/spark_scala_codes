import org.apache.spark._
import org.apache.spark.sql._
import org.apache.spark.SparkContext._

object sparkSQL_direct {
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
    import spark.implicits._
    val data = spark.sparkContext.textFile("data/fakefriends.csv")
    val people = data.map(mapper).toDS().cache()



    people.printSchema()
    println("============names:")
    people.select("name").show()
    println("============age>20:")
    people.filter(people("age") > 20).show()
    println("============age count:")
    people.groupBy("age").count().show()
    people.select(people("name"), people("age") + 10).show()

    spark.stop()
  }
}
