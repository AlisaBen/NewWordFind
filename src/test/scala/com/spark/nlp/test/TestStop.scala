package com.spark.nlp.test
import com.spark.nlp.core.StopWords

/**
  * 2018/4/10
  * by benyafang
  */
object TestStop {
  def main(args: Array[String]): Unit = {
//    if(StopWords.findIn("陡然"))println("存在")
//    else println("不存在")
    val words = List(("论说",12.1,1.2,23),("行者",4.1,2.2,3),("抠图",2.1,54.4,2))
    val word = List("论说","行者","抠图")
    word.filter(StopWords.findIn(_)).foreach(println)
    words.filter(StopWords.findInType(_)).foreach(println)
  }
}
