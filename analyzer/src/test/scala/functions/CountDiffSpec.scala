package functions

import org.joda.time.DateTime
import org.scalatest.{FlatSpec, Matchers}

class CountDiffSpec extends FlatSpec with Matchers {

  it should "return time difference on the same hour" in {
    val res = CountDiff("15:22", "-67",
      DateTime.now().withTime(15, 25, 12, 0).getMillis)
    res should be (125)
  }

  it should "return time difference on next hour" in {
    val res = CountDiff("15:57", "-184",
      DateTime.now().withTime(16, 8, 37, 0).getMillis)
    res should be (513)
  }

  it should "return time difference on next day" in {
    val res = CountDiff("23:57", "-20",
      DateTime.now().withTime(0, 1, 13, 0).getMillis)
    res should be (233)
  }

  it should "return time difference if bus come earlier" in {
    val res = CountDiff("8:19", "-40",
      DateTime.now().withTime(8, 19, 13, 0).getMillis)
    res should be (-27)
  }

  it should "return time difference if bus come exactly in time" in {
    val res = CountDiff("10:27", "-4",
      DateTime.now().withTime(10, 27, 4, 0).getMillis)
    res should be (0)
  }
}
