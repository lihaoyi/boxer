package demo

class Meter(val value: Double) extends AnyVal {
  def +(other: Meter) = new Meter(value + other.value)
}
