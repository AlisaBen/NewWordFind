package com.spark.nlp.core


import com.spark.nlp.util.{NGram, WordCountUtils}
import com.spark.nlp.Trie.{NodeProcessor, Trie, TrieErgonicProcessor}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

object NewWordFindWithSource extends Serializable {
  /**
    * 2018/4/9
    * @param iterator
    * @param newWordFindConfig
    * @return
    */
  def newWord(iterator: Iterator[String],newWordFindConfig: NewWordFindConfig):Iterator[(String,Double,Double,Int)]={
    var word_buffer = new ArrayBuffer[String]()
    while(iterator.hasNext){
      word_buffer+=iterator.next()
    }
    val lineCount = word_buffer.size
    val triemap = trieMap(word_buffer,newWordFindConfig)
    val it = trieMap(word_buffer,newWordFindConfig)
    val trie_1 = triemap.reduce(_.join(new NodeProcessor[(Double,Int),Trie[(Double,Int)],String] {
      override def process(e: String)(t: (Double, Int), trie: Trie[(Double, Int)]): Trie[(Double, Int)] = {
        trie.value(e) match {
          case Some(tt) => trie.updateValue(e,(Math.min(tt._1,t._1),tt._2+t._2))
          case None => trie.insert(e,t)
        }
      }
    })(_))
    rddPmi(it,trie_1,newWordFindConfig,lineCount)
  }
  def rddPmi(iterator:Iterator[Trie[(Double,Int)]],trieCount:Trie[(Double,Int)],newWordFindConfig: NewWordFindConfig,lineCount:Double):Iterator[(String,Double,Double,Int)] ={
    val array = new ArrayBuffer[(String,Double,Double,Int)]()
    while(iterator.hasNext){
      var trie = iterator.next()
      val q = trie.ergodic(new TrieErgonicProcessor[String,(Double,Int)] {
        override def process(t: String, e: (Double, Int)): Unit = {
          if(t.length >= newWordFindConfig.minlen){
            val pmi = (0 to t.length -2).map(i => {
              val a = t.substring(0,i+1)
              val b = t.substring(i+1)
              val av = trieCount.value(a) match{
                case Some(v) => v._2.toDouble
                case _ => newWordFindConfig.minCount.toDouble
              }
              val bv = trieCount.value(b) match{
                case Some(v)=>v._2.toDouble
                case _ => newWordFindConfig.minCount.toDouble
              }
              e._2*lineCount/(av*bv)
            }).min
            if(pmi >= newWordFindConfig.minPmi && e._1 >= newWordFindConfig.minInfoEnergy){//filter with pmi and infoenergy
              array+=((t,e._1,pmi,e._2))
            }
          }
        }
      })
    }
    array.foreach(println)
    array.toIterator
  }

  /**
    *
    * @param word_buffer
    * @param newWordFindConfig
    * @return
    */
  def trieMap(word_buffer:ArrayBuffer[String],newWordFindConfig: NewWordFindConfig):Iterator[Trie[(Double,Int)]]={
    println("buffer")
    var a = new mutable.HashMap[String,(mutable.HashMap[String,Int],mutable.HashMap[String,Int],Int)]()
    for(i <- word_buffer){
      val b = NGram.nGram(i,1,newWordFindConfig.maxlen)
      b.foreach(l=>{
        var initial_key = l._1
        var initial_value = l._2
        a.get(initial_key) match{
          case Some(value) =>{
            var contain_Left = value._1
            initial_value._1.foreach(kv=>{
              if(contain_Left.contains(kv._1)){
                val left_Value = contain_Left(kv._1) + kv._2
                contain_Left += (kv._1->left_Value)
              }else{
                contain_Left += kv
              }
            })
            var contain_Right = value._2
            initial_value._2.foreach(kv=>{
              if(contain_Right.contains(kv._1)){
                val v = contain_Right(kv._1) + kv._2
                contain_Right += (kv._1->v)
              }else{
                contain_Right += kv
              }
            })
            val word_Frequency = value._3 + initial_value._3
            initial_value = (contain_Left,contain_Right,word_Frequency)
            a.put(initial_key,initial_value)
          }
          case None => a += l
        }
      })
    }
  WordCountUtils.trieRDD(a.filter(_._2._3>=newWordFindConfig.minCount)
                          .map(WordCountUtils.energyCount)
                          .toIterator)
  }

}
