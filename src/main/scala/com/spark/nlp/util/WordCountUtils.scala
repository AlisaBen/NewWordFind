package com.spark.nlp.util

import com.spark.nlp.Trie.{NodeProcessor, Trie}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

object WordCountUtils extends Serializable {
  def energyCount(v:(String,(mutable.HashMap[String,Int],mutable.HashMap[String,Int],Int))):(String,(Double,Int))={
    val lcount = v._2._1.values.sum
    val rcount = v._2._2.values.sum
    val lenergy = v._2._1.values.map(c=>{
      val p = c * 1.0 / lcount
      -1 * p * Math.log(p) / Math.log(2)
    }).sum
    val renergy = v._2._2.values.map(c=>{
      val p = c * 1.0 / rcount
      -1 * p * Math.log(p) / Math.log(2)
    }).sum
    val energy = Math.min(lenergy,renergy)
    (v._1,(energy,v._2._3))
  }
  def trieRDD(words:Iterator[(String,(Double,Int))]):Iterator[Trie[(Double,Int)]]={
    val trie_iter = new ArrayBuffer[Trie[(Double,Int)]]()
    while(words.hasNext){
      val w = words.next()
      var trie = new Trie[(Double,Int)]()
      val trie_1 = trie.insert(w._1,w._2,new NodeProcessor[(Double,Int),Trie[(Double,Int)],String] {
        override def process(e: String)(t: (Double, Int), trie: Trie[(Double, Int)]): Trie[(Double, Int)] = {
          trie.value(e) match {
            case Some(tt) => trie.updateValue(e,(Math.min(tt._1,t._1),t._2+tt._2))
            case None => trie.insert(e,t)
          }
        }
      })
      trie_iter+=trie_1
    }
    trie_iter.toIterator
  }

}
