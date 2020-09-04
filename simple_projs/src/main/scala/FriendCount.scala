import org.apache.spark.SparkContext._
import org.apache.spark._

object FriendCount {
  def parseLine(line:String) ={
    val fields = line.split(",")
    val age = fields(2).toInt
    val friend = fields(3).toInt
    (age,friend)
  }

  def main(args : Array[String]) {
    val sc = new SparkContext("local[*]", "FriendCount")
    val data = sc.textFile("data/fakefriends.csv")
    val split_data = data.map(parseLine)
    val total_friends = split_data.mapValues(x => (x,1)).reduceByKey((x,y) => (x._1 + y._1, x._2 + y._2))
    val average_friend = total_friends.mapValues(x => x._1/x._2)
    val res = average_friend.collect()
    res.sorted.foreach(println)

  }
}
