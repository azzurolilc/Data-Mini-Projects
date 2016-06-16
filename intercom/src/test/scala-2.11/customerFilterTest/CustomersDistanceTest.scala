package customerFilterTest
import customerFilter.CustomersDistance._
import java.lang.Math._
import org.scalatest._
/**
  * Test cases for Customer Filter by Distance
  * Created by azzurolilc on 6/16/16.
  */
class CustomersDistanceTest extends FlatSpec with Matchers {

  /*
   * Method Tests
   */

  "A convertToRadius" should "convert degree to radius" in {
    convertToRadius(0) should be (0)
    convertToRadius(90) should be (PI*.5)
    convertToRadius(180) should be (PI)
    convertToRadius(-90) should be (-PI*.5)
    convertToRadius(-180) should be (-PI)
  }

  "A distance" should "calculate the distance between default coordinate and given coordinate" in {
    distance(53.3381985, -6.2592576) should be (0)
    distance(-53.3381985, 180-6.2592576) should be (6371.0*PI)
  }

  "A distanceIsWithin" should "compare the distances" in {
    distanceIsWithin(99.9,100) should be (true)
    distanceIsWithin(100.00,100) should be (false)
    distanceIsWithin(100.001,100) should be (false)
  }

  /*
   * Main Tests
   */

  "Error CustomerDistance main" should "throw error on invalid input" in {
    an [Exception] should be thrownBy main(Array(""))
    an [Exception] should be thrownBy main(Array("","",""))
    an [Exception] should be thrownBy main(Array("./output.txt"))
  }

  "Ok CustomerDistance main" should "run with valid file path inputs" in {
    noException should be thrownBy main(Array("./customers.txt","./output.txt"))
    noException should be thrownBy main(Array("./customers.txt","./output.txt","lalalalala"))
  }
}
