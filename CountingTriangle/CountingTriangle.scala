import org.apache.spark._
import scala.collection.mutable.HashSet
import scala.collection.mutable.ArrayBuffer
import org.apache.log4j.{Level, Logger}
object CountingTriangle
{
// 	Using parse function take those input into the data structure like hashset.
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
		
// 		Some configuration is done like how many cores spark will use, we use 2 cores for this project
		conf.set("spark.master", "local[2]")
		conf.set("spark.app.name", "My App")
		val sc = new SparkContext(conf)
		
		val rootLogger = Logger.getRootLogger()
    		rootLogger.setLevel(Level.ERROR)
		
//     		Read the dataset and split them into 2 partition.
		val dataRDD = sc.textFile("toy.csv", 2).map(parse(_))
		
// 		After parsing we generate edges using flatmap function, which generate an array of tuple named edges.
// 		Collect those edgeArray to compare with actual row.
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
		
// 		Generate triangleRDD using flatmap each node is compared with edges of edgeArray,
// 		if nodes of the edge are exist into the hashset of the dataRDD then it obviously generate a triangle.
// 		After comparison bring all the nodes correspond to the each edge using groupByKey().
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
		
// 		Then just count the number of total triangles.
// 		Though each edge contribute 3 times to one triangle so we divide total number of triangle by 3.
		val numOfTriangles = (triangleRDD.map(x => x._2.size).reduce((x, y) => x + y)) / 3
		triangleRDD.collect().foreach(println(_))
		
// 		And since the graph is undirected so total number of triangle will be divided by 2.
// 		Because if there exist <a,b> then <b,a> also exist.
		println("Number of triangles = " + numOfTriangles / 2)
		sc.stop()
	}
}

