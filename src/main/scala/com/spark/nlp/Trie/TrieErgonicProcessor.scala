package com.spark.nlp.Trie

trait TrieErgonicProcessor[T,E] extends Serializable {
  def process(t:T,e:E)
}
