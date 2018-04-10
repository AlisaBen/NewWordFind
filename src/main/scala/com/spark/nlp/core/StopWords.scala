package com.spark.nlp.core

import scala.collection.mutable.ArrayBuffer
import scala.io.Source

/**
  * 2018/4/10
  * by benyafang
  */
object StopWords{
  val sw = Source.fromFile("H:\\documentation\\data\\nlp\\chinese-stopwords-master\\chinese-stopwords-master\\stopwords").getLines()
  val stop_word = ArrayBuffer[String]()
  while(sw.hasNext){
    stop_word += sw.next()
  }
  def findIn(s:String):Boolean={
    if(stop_word.contains(s))return false
    else return true
  }
  def findInType(t:(String,Double,Double,Int)):Boolean={
    if(stop_word.contains(t._1))return false
    else return true
  }

}
