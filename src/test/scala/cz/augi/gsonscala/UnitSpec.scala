package cz.augi.gsonscala

import java.util.Optional
import java.util.concurrent.TimeUnit

import com.google.gson.{Gson, GsonBuilder}
import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner

import scala.concurrent.duration.Duration
import org.scalatest.Matchers._

class AsMillis extends UnitSpec {
  val gson = new GsonBuilder()
              .registerBasicConverters()
              .registerMillisDurationConverters()
              .registerUnixMillisInstantConverter()
              .create()
}

class AsSeconds extends UnitSpec {
  val gson = new GsonBuilder()
              .registerBasicConverters()
              .registerSecondsDurationConverters()
              .registerUnixSecondsInstantConverter()
              .create()
}

class AsString extends UnitSpec {
  val gson = new GsonBuilder()
              .registerBasicConverters()
              .registerStringDurationConverters()
              .registerStringInstantConverter()
              .create()
}

@RunWith(classOf[JUnitRunner])
abstract class UnitSpec extends FlatSpec {
  def gson: Gson

  private case class ExampleClass(
                       instant: java.time.Instant,
                       duration: java.time.Duration,
                       scalaDuration: Duration,
                       optional: Optional[String],
                       option: Option[String],
                       seq: Seq[Int])

  "Converters" must "serialize and deserialize" in {
    val input = ExampleClass(
                  java.time.Instant.ofEpochMilli(123456000),
                  java.time.Duration.ofMillis(123000),
                  Duration(456, TimeUnit.SECONDS),
                  Optional.of("this is optional string"),
                  Some("this is option string"),
                  Seq(1, 2, 3))
    val json = gson.toJson(input)
    val deserialized = gson.fromJson(json, input.getClass)
    deserialized shouldBe input
  }

  it must "handle missing values" in {
    val input = ExampleClass(
      java.time.Instant.ofEpochMilli(123456000),
      java.time.Duration.ofMillis(123000),
      Duration(456000, TimeUnit.SECONDS),
      Optional.empty(),
      None,
      Seq.empty)
    val json = gson.toJson(input)
    val deserialized = gson.fromJson(json, input.getClass)
    deserialized shouldBe input
  }
}
