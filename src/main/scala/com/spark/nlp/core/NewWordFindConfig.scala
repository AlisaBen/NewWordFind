package com.spark.nlp.core

case class NewWordFindConfig(
                            minlen:Int,
                            maxlen:Int,
                            minCount:Int,//词频
                            minInfoEnergy:Double,//信息熵
                            minPmi:Double
                            )
object NewWordFindConfig {
  val minlen: Int = 1
  val maxlen: Int = 4
  val minCount: Int = 2
  val minInfoEnergy: Double = 1.0
  val minPmi: Double = 1.0
}
