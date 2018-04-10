package com.spark.nlp.Trie

import scala.collection.mutable.ArrayBuffer

class Trie[T] extends Serializable {
  val root:Node[T] = new Node[T](' ')
  def insert(word:String,t:T):Trie[T]={
    insert(word,t,null)
  }

  /**
    * 返回对应词的信息熵和词频
    * @param word
    * @return t
    */
  def value(word:String):Option[T]={//返回对应词的信息熵和词频
    if(word.isEmpty)return None
    var currentNode = root//每次从当前树的根结点遍历
    word.toCharArray.foreach(c=>{
      currentNode.nextNode(c) match {//当前节点下是否有content为c的节点
        case Some(nd) => currentNode = nd
        case None => return None
      }
    })//或者走到词的一段已经走到叶节点，一个孩子节点都没有；或者有孩子节点，但是没有接下来的字符
    if(currentNode.isEnd) Some(currentNode.t)//只有叶节点才有信息熵和词频，或者该词段已经有；或者该词段的一部分有
    else None
  }

  /**
    *
    * @param word
    * @param t
    * @param processor
    * @return
    */
  def insert(word:String,t:T,processor:NodeProcessor[T,Trie[T],String]):Trie[T]={
    this.synchronized{
      if(word.isEmpty)return this//this指代原来的树
      value(word) match {
        case Some(tt) =>{
          if(processor == null)return this
          else{
            return processor.process(word)(t,this)
          }
        }
        case None =>{//没有到达叶节点，并且没有遍历完字段
          var currentNode = this.root
          var deep:Int = 0
          word.trim.foreach(c=>{
            deep+=1
            currentNode.nextNode(c) match {
              case Some(nd) =>currentNode = nd
              case None =>
                currentNode.childMap += (c->new Node[T](c))
                currentNode.count = currentNode.childMap.size
                currentNode.nextNode(c) match {
                  case Some(nd)=>
                    currentNode=nd
                    currentNode.depth = deep
                  case None =>
                    return this
                }
            }
          })
          currentNode.t = t
          currentNode.isEnd = true
          this
        }
      }
    }
  }

  /**
    * 更新词的信息熵和词频
    * @param word
    * @param t
    * @return
    */
  def updateValue(word:String,t:T):Trie[T]={
    if(word.isEmpty)return this
    this.synchronized{
      var currentNode = root
      word.toCharArray.foreach(c=>
                              currentNode.nextNode(c) match{
                                case Some(nd) => currentNode = nd
                                case None => return this
                              })
      if(currentNode.isEnd)
        currentNode.t = t
      this
    }
  }

  def join(processor:NodeProcessor[T,Trie[T],String])(trie:Trie[T]):Trie[T] = {
    this.synchronized{
      val node = trie.root
      if(node.count!=0)
        interacte(node," ",processor)
      this
    }
  }

  private def interacte(node:Node[T],profix:String,processor:NodeProcessor[T,Trie[T],String]): Unit ={
    node.childMap.values.foreach(nd => interacte(nd,profix + nd.content,processor))
    if(node.isEnd){
      this.value(profix) match{
        case Some(t) => processor.process(profix)(node.t,this)
        case None => this.insert(profix,node.t)
      }
    }
  }

  def allWords(profix:String):ArrayBuffer[String] ={
    var wordsBuffer:ArrayBuffer[String] = ArrayBuffer[String]()
    if(profix.isEmpty)return wordsBuffer
    var node:Node[T] = root
    profix.trim.foreach(c=>
                       if(node.count==0)
                       return wordsBuffer
                       else{
                         node.nextNode(c) match{
                           case Some(nd) => node = nd
                           case None => return wordsBuffer
                         }
                       })
    if(node.count != 0){
      fullWords(node,profix,wordsBuffer)
    }
    wordsBuffer
  }
  def fullWords(node: Node[T],profix:String,rs:ArrayBuffer[String]):ArrayBuffer[String]={
    node.childMap.values.foreach(nd=>
                                fullWords(nd,profix+nd.content,rs))
    if(node.isEnd) rs += profix
    rs
  }

  def ergodic(trieErgonicProcessor: TrieErgonicProcessor[String,T]): Trie[T] ={
    this.synchronized{
      val node = this.root
      if(node.count!=0){
//        println("count+"+node.count)
        innerErgodic(node,"",trieErgonicProcessor)
      }
      this
    }
  }

  private def innerErgodic(node:Node[T],profix:String,trieErgonicProcessor: TrieErgonicProcessor[String,T]): Unit ={
//    println(profix)
    if(!node.childMap.isEmpty)
      node.childMap.values.foreach(nd => innerErgodic(nd,profix+nd.content,trieErgonicProcessor))
    if(node.isEnd){
      trieErgonicProcessor.process(profix,node.t)
    }
  }
  def exist(word:String):Boolean={
    var currentNode = root
    word.trim.toCharArray.foreach(c=>{
      if(currentNode.nextNode(c).isEmpty){
        false
      }else{
        currentNode.nextNode(c) match {
          case Some(nd) => currentNode = nd
          case None => return false
        }
      }
    })
    if(currentNode.isEnd) true
    else false
  }


}
