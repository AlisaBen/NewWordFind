package com.spark.nlp.test

import com.spark.nlp.core.{NewWordFindConfig, NewWordFindWithSource, StopWords}

import scala.collection.mutable.ArrayBuffer
import scala.io.Source

object TestNewWordFind {
  def main(args: Array[String]): Unit = {
    val pattern = "[\u4E00-\u9FA5]+".r
    val stopwords = "[你|我|他|她|它]+"
    val minLen = 2
    val maxLen = 6
    val minCount = 10
    val minInfoEnergy = 2.0
    val minPim = 20.0

    val newWordFindConfig = new NewWordFindConfig(minLen,maxLen,minCount,minInfoEnergy,minPim)

    val file = Source.fromFile("H:\\documentation\\data\\nlp\\32214\\12931_1.txt")
    val lines = file.getLines()
      .flatMap(pattern.findAllIn(_).toSeq)
      .flatMap(_.split(stopwords))
    val words = NewWordFindWithSource.newWord(lines,newWordFindConfig)
        .toList
        .filter(StopWords.findInType(_))
        .map(l=>(l._2 * l._3 * l._4,l._1))
        .sortWith(_._1>_._1)
        .foreach(println)

    file.close()
  }


}
