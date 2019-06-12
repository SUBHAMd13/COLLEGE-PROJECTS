import org.apache.spark._
import scala.collection.mutable.HashSet
import scala.collection.mutable.ArrayBuffer
import org.apache.log4j.{Level, Logger}
object CountingTriangle
{
	def parse(line:String): (Int, HashSet[Int]) = {
		val str = line.split(",")
		val friends = str(1).split(":").map(_.toInt)
		val id = str(0).toInt
		val result:HashSet[Int] = HashSet()
		for(i <- 0 until friends.length)
			result += friends(i)
		(id, result)
	}
	def main(args:Array[String])
	{
		val conf = new SparkConf()
		conf.set("spark.master", "local[2]")
		conf.set("spark.app.name", "My App")
		val sc = new SparkContext(conf)
		
		val rootLogger = Logger.getRootLogger()
    		rootLogger.setLevel(Level.ERROR)
    		
		val dataRDD = sc.textFile("/home/argha/toy.csv", 2).map(parse(_))
		val edges = dataRDD.flatMap(x => {
			val edgeArray = new Array[(Int, Int)](x._2.size)
			var i = 0
			for(vertex <- x._2)
			{
				var tempTuple = (x._1, vertex)
				edgeArray(i) = tempTuple
				i = i + 1
			}
			edgeArray
		}).collect()
		val triangleRDD = dataRDD.flatMap(x => {
			val triangleArray = ArrayBuffer[((Int, Int), Int)]()
			for((v1, v2) <- edges)
				if(x._2.contains(v1) && x._2.contains(v2))
				{
					var tempTuple = ((v1, v2), x._1)
					triangleArray += tempTuple
				}
			triangleArray
		}).groupByKey().persist()
		val numOfTriangles = (triangleRDD.map(x => x._2.size).reduce((x, y) => x + y)) / 3
		triangleRDD.collect().foreach(println(_))
		println("Number of triangles = " + numOfTriangles / 2)
		sc.stop()
	}
}

