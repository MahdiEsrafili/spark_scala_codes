import org.apache.spark._
import org.apache.spark.SparkContext._

object customer_spending {

  def parser(line: String) = {
    val fields = line.split(",")
    val customer_id = fields(0).toInt
    val book_id = fields(1).toInt
    val amount = fields(2).toFloat
    (customer_id, amount)
  }

  def main(args: Array[String]) {
    val sc = new SparkContext("local[*]", "customer_spending")
    val data = sc.textFile("data/customer-orders.csv")
    val rdd = data.map(parser)
    val amount_per_user = rdd.reduceByKey((x,y) => x+y)
    val user_per_amount = amount_per_user.map( x => (x._2, x._1) ).sortByKey()
    val sorted_amount_per_user = user_per_amount.map( x => (x._2, x._1) ).collect()
    sorted_amount_per_user.foreach(println)
  }
}
