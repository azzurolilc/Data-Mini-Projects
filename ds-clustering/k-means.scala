import org.apache.spark.mllib.clustering.{KMeans, KMeansModel}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.rdd.RDD
import java.io._
    
// Import trig functions
import Math.{PI,cos,sin}

def conv(latitudeDegrees:Double, longitudeDegrees:Double) = {
    val EARTH_RADIUS_MILES = 3959.0

    // Convert to radians
    val lat = PI * latitudeDegrees  / 180.0
    val lon = PI * longitudeDegrees / 180.0
    // Calculate x,y,z coordinates
    val R = EARTH_RADIUS_MILES 
    val x = R * cos(lat) * cos(lon)
    val y = R * cos(lat) * sin(lon)
    val z = R * sin(lat)
   Array(x,y,z)
}
    
def loadMcdonaldsData() = {
  // Load and parse the data
  val path = "mcdonalds.csv"
  val data = sc.textFile(path)
  val parsedData = data.map{s => (s.split(",")(0).toDouble,s.split(",")(1).toDouble)
       }.map(tur=> Vectors.dense(conv(tur._1,tur._2))).cache
   parsedData
}

def createModel(parsedData:RDD[Vector],
    numClusters:Int,numIterations:Int) = {
  // Cluster the data into two classes using KMeans
  val model = KMeans.train(parsedData, numClusters, numIterations)
  model
}

def getModelError(model:KMeansModel,parsedData:RDD[Vector]) = {
  // Evaluate clustering by computing Within Set Sum of Squared Errors
  val WSSSE = model.computeCost(parsedData)
  WSSSE
}

def saveModel(model:KMeansModel,path:String) = {
  // Save model
  model.save(sc, path)
}

def loadModel(path:String) = {
  // Load model
  KMeansModel.load(sc, path)
}

def clustering(numCluster:Int){
    val numIterations = 30
    val parsedData = loadMcdonaldsData()
    val model = createModel(parsedData, numClusters, numIterations)
    val WSSSE = getModelError(model,parsedData)
    println(numClusters+","+ WSSSE)
}
val file = new File("input.txt")
val bw = new BufferedWriter(new FileWriter(file))
    
def run(num:Int)= {
    val numIterations = 20
    val parsedData = loadMcdonaldsData()
    
    for (numClusters <- 10 to 400){
        for (i<-0 to num){
            val model = createModel(parsedData, numClusters, numIterations)
            val WSSSE = getModelError(model,parsedData)
            bw.write(numClusters+","+ WSSSE)
            println(numClusters+","+ WSSSE)
        }
    }
}

// FileWriter

run(5)
bw.close()



