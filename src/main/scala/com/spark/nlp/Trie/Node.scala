package com.spark.nlp.Trie

import scala.collection.immutable.TreeMap

class Node[T](char:Char) extends Serializable{
  var content:Char = char
  var isEnd:Boolean = false
  var childMap: Map[Char,Node[T]] = TreeMap[Char,Node[T]]()
  var t:T = _ //信息熵，词频
  var depth: Int = 0
  var count: Int = 0

  def nextNode(char:Char):Option[Node[T]]={
    if(childMap.nonEmpty){
      childMap.get(char)
    }else{
      None
    }
  }
}
