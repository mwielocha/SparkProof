import com.tuplejump.calliope.Types._
import java.util.{UUID => JUUID}
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import com.tuplejump.calliope.utils.RichByteBuffer._
import com.tuplejump.calliope.Implicits._
import com.tuplejump.calliope.CasBuilder

object SimpleApp {
  def main(args: Array[String]) {

    implicit def ThriftRowKeyToJUUID(rowKey: ThriftRowKey): JUUID = {
      JUUID.fromString(rowKey.asCharBuffer().toString)
    }

    implicit def ThriftRowMapToJUUID(rowMap: ThriftRowMap): Map[JUUID, String] = {
      rowMap.map {
        case (name, value) => {
          JUUID.fromString(name.asCharBuffer().toString) -> value.asCharBuffer().toString
        }
      }.toMap
    }

    val conf = new SparkConf()
      .setMaster("spark://10.53.2.21:7077")
      .setAppName("Crunchero.Analytics.analyze")
      .set("spark.executor.memory", "1g")
      .set("spark.driver.host", "10.53.152.13")

    val sc = new SparkContext(conf)

    val rdd = sc.thriftCassandra[JUUID, Map[JUUID, String]](
      "cassandra.stgwaw.opigram", "9160", "DefaultOpigramLibrary", "ValuesByMemberAndVariable")

    val result = rdd.filter({
      case (memberRef, values) => {
        println("filter...")
        values.contains(JUUID.fromString("14f2eac0-f162-11e3-b1af-1ed043699d3c"))
      }
    }).flatMap({
      case (memberRef, values) => {
        println("flatMap")
        values.keys.map(_.toString -> 1)
      }
    }).reduceByKey(_ + _)
      .foreach(println)


  }
}