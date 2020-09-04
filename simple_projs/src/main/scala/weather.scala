import org.apache.spark._
import org.apache.spark.SparkContext._
import scala.math.min
object weather {
  def parseLine(line: String) = {
    val fields = line.split(',')
    val indicator = fields(2)
    val value = fields(3).toFloat
    val station_id = fields(0)
    (station_id, indicator, value)
  }

  def main(args:Array[String]){
    val sc = new SparkContext("local[*]", "weather")
    val data = sc.textFile("data/1800.csv")
    val rdd = data.map(parseLine)
    val tmin_rdd = rdd.filter(x => x._2 == "TMIN")
    val min_temps = tmin_rdd.map( x => (x._1,x._3))
    val station_min_temp = min_temps.reduceByKey((x,y) => min(x,y))
    val min_temp_value = station_min_temp.collect()
    min_temp_value.foreach(println)
  }
}
