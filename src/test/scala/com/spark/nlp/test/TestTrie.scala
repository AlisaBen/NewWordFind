package com.spark.nlp.test

import com.spark.nlp.Trie.{NodeProcessor, Trie}

object TestTrie {
  def main(args: Array[String]): Unit = {
    val trie:Trie[(Double,Int)] = new Trie[(Double, Int)]()
    trie.insert("我是",(1.1,1))
    trie.insert("北京邮电",(2.3,2))
    println(trie.value("我是"))
    println(trie.value("北京邮电"))
    trie.updateValue("我是",(3.0,4))
    println(trie.value("我是"))

    trie.insert("北京邮电",(3.2,4),new NodeProcessor[(Double,Int),Trie[(Double,Int)],String] {
      override def process(e: String)(t: (Double, Int), trie: Trie[(Double, Int)]): Trie[(Double, Int)] = {
        trie.value(e) match {
          case Some(tt)=>trie.updateValue(e,(math.min(tt._1,t._1),tt._2+t._2))
          case None => trie.insert(e,t)
        }
      }
    })

    trie.insert("你好",(3.1,5),new NodeProcessor[(Double,Int),Trie[(Double,Int)],String] {
      override def process(e: String)(t: (Double, Int), trie: Trie[(Double, Int)]): Trie[(Double, Int)] = {
        trie.value(e) match {
          case Some(tt)=>trie.updateValue(e,(math.min(tt._1,t._1),tt._2+t._2))
          case None => trie.insert(e,t)
        }
      }
    })
    println(trie.value("北京邮电"))
    println(trie.value("你好"))

    print("join")
    val trie2 = new Trie[(Double,Int)]()
    trie2.insert("北京邮电大学",(10.2,100))
    trie2.insert("我是小冰",(23,12))
    trie.join(new NodeProcessor[(Double,Int),Trie[(Double,Int)],String] {
      override def process(e: String)(t: (Double, Int), trie: Trie[(Double, Int)]): Trie[(Double, Int)] = {
        trie.value(e) match{
          case Some(tt) => trie.updateValue(e,(Math.min(tt._1,t._1),tt._2+t._2))
          case None =>trie.insert(e,t)
        }
      }
    })(trie2)





  }
}
