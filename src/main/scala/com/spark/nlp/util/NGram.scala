package com.spark.nlp.util

import scala.collection.mutable

/**
  * 2018/3/24
  * by ben yafang
  * learned from @author songyaheng
  */
object NGram extends Serializable {
  def newWordGram(s:String,len:Int,wordGramMap:mutable.HashMap[String,(mutable.HashMap[String,Int],mutable.HashMap[String,Int],Int)]):mutable.HashMap[String,(mutable.HashMap[String,Int],mutable.HashMap[String,Int],Int)]={
//    var wordGramMap = new mutable.HashMap[String,(mutable.HashMap[String,Int],mutable.HashMap[String,Int],Int)]()
    val sen = "$"+s+"$"
    (1 to s.length-len+1).map(i =>{
      val w = sen.substring(i,i+len)
      val lw = sen.substring(i-1,i)
      val rw = sen.substring(i+len).substring(0,1)
      wordGramMap.put(w,(mutable.HashMap(lw->1),mutable.HashMap(rw->1),1))
    })
    wordGramMap
  }
  def nGram(s:String,minlen:Int,maxlen:Int):mutable.HashMap[String,(mutable.HashMap[String,Int],mutable.HashMap[String,Int],Int)]={
    var wordGramMap = new mutable.HashMap[String,(mutable.HashMap[String,Int],mutable.HashMap[String,Int],Int)]()
    val sen = "$"+s+"$"
    (minlen to maxlen).flatMap(i => newWordGram(s,i,wordGramMap))
    wordGramMap
  }
}
