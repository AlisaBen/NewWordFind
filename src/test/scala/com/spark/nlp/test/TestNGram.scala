package com.spark.nlp.test
import com.spark.nlp.util.NGram

object TestNGram {
  def main(args: Array[String]): Unit = {
    val pattern = "[\u4E00-\u9FA5]+".r
    val stopwords = "[你|我|他|她|它]+"
    val str = "六十二载春秋峥嵘岁月，\n六十二载基业传邮万里。\n值此母校六十二华诞之际，\n让我们一起来聆听学子们对我邮爱的表白！"
    NGram.nGram(str,1,4)
        .flatMap(t=>pattern.findAllIn(t._1).toSeq)
        .flatMap(_.split(stopwords))
      .foreach(println)

  }
}
