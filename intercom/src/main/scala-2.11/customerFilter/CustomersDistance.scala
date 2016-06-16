package customerFilter

import java.io.FileWriter
import java.lang.Math._

import scala.collection.immutable.TreeMap
import scala.io.Source._

/**
  * This program read the full list of customers
  * output the names and user ids of matching customers (within 100 km)
  * sorted by user id (ascending)
  *
  * All distances are in kilometer
  *
  * Run with two Arguments separated by space:
  * <Input File Path> <Output File Path>
  *
  * Created by azzurolilc on 6/16/16.
  */

object CustomersDistance {

  val companyCoordinate = Tuple2(53.3381985, -6.2592576)
  val EarthRadius = 6371.0
  val comparedDistance = 100

  /*
  * "convertToRadius" converts degree to radius
  *
  * "distanceIsWithin" method returns boolean check if distance is less than standard distance
  *
  * "distance" method takes in a coordinate, compare to default company coordinate
  *  and returns the distance between two coordinates
  *
  */

  def convertToRadius(degree: Double): Double = PI * degree / 180.0

  def distanceIsWithin(distance: Double, standardDistance: Double): Boolean = distance < standardDistance

  def distance(latitude: Double, longitude: Double): Double = {

    // Convert to radians:
    // company latitude, company longitude, input latitude, input longitude
    val compLat = convertToRadius(companyCoordinate._1)
    val compLon = convertToRadius(companyCoordinate._2)
    val inLat = convertToRadius(latitude)
    val inLon = convertToRadius(longitude)


    // Calculate delta x,y,z coordinates difference
    val x = cos(inLat) * cos(inLon) - cos(compLat) * cos(compLon)
    val y = cos(inLat) * sin(inLon) - cos(compLat) * sin(compLon)
    val z = sin(inLat) - sin(compLat)

    val chordLength = sqrt(pow(x, 2) + pow(y, 2) + pow(z, 2))
    val centerAngle = 2 * asin(chordLength / 2)

    // return great circle distance
    EarthRadius * centerAngle

  }

  /*
  * main run with two Arguments separated by space:
  * <Input File Path> <Output File Path>
  *
  * */

  def main(args: Array[String]): Unit = {

    // SortedMap to hold output
    var listOfCustomers = TreeMap.empty[Int, String]

    // regex to group wanted info from json string ? alternative: use Json Parsing libraries
    val pattern =
      """^\{\"latitude\": \"(-?\d+.?\d*)\", \"user_id\": (\d+), \"name\": \"(\w+\s\w+)\", \"longitude\": \"(-?\d+.?\d*)\"\}$""".r

    try {
      val inputPath = args(0)
      val outputPath = args(1)
      val fw = new FileWriter(outputPath, false)

      // Read File
      fromFile(inputPath).getLines.foreach(line => {

        // pattern match each line and group
        pattern.findAllMatchIn(line.toString).foreach(

          // matching string m: latitude -> group(1); user_id -> group(2); name -> group(3); longitude -> group(4)
          m => {
            val latitude = m.group(1).toDouble
            val longitude = m.group(4).toDouble

            val dist = distance(latitude, longitude)

            // add customer id and name to SortedMap if location is within 100km
            if (distanceIsWithin(dist, comparedDistance))
              listOfCustomers += (m.group(2).toInt -> m.group(3).toString)

          }
        )
      })

      // Write file
      listOfCustomers.foreach(resultLine => {
        printf("%s\t%s\n",resultLine._1,resultLine._2)
        fw.write(resultLine._1+","+resultLine._2+ "\n")
      })
      fw.close()

    } catch {
      // exception if args does not have at least two arguments, trailing arguments ignored:(
      case ex: ArrayIndexOutOfBoundsException => {
        println("Invalid input, please pass in valid <Input File Path> <Output File Path> as arguments!")
        throw ex
      }

      // exception if args(0) path file is not found
      case ex: Exception => {
        println(ex)
        throw ex
      }
    }
  }
}
