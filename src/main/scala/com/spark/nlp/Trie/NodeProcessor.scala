package com.spark.nlp.Trie

trait NodeProcessor[T,R,E] extends Serializable{
  def process(e:E)(t:T,trie:Trie[T]):R
}
