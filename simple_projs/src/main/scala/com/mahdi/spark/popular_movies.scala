package com.mahdi.spark
import org.apache.spark._
import org.apache.spark.SparkContext._

object popular_movies {
  def parser(line:String) = {
    val fields = line.split("\t")
    val user_id = fields(0).toInt
    val movie_id = fields(1).toInt
    val rate = fields(2).toInt
    (movie_id,1)
  }
  def main(args:Array[String]) {
    val sc = new SparkContext("local[*]", "popular_movies")
    val data = sc.textFile("data/u.data")
    val rdd = data.map(parser)
    val sorted_movie = rdd.reduceByKey( (x,y) => x+y).map( x => (x._2, x._1)).sortByKey().map( x => (x._2, x._1))
    sorted_movie.foreach(println)
  }

}
