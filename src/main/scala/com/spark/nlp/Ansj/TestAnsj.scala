//package com.spark.nlp.Ansj
//
//import java.util.Dictionary
//
//import org.ansj.app.keyword.KeyWordComputer
//import org.ansj.domain.Term
//import org.ansj.library.DicLibrary
//import org.ansj.splitWord.analysis.ToAnalysis
//import org.ansj.util.Counter.Result
//
//import scala.collection.immutable.HashSet
//object TestAnsj {
//  def main(args: Array[String]): Unit = {
//    val expectedNature:Set[String] = new HashSet[String]()+"n"+"v"
//    val str:String = "有俄罗斯国会议员，9号在社交网站推特表示，美国中情局前雇员斯诺登，已经接受委内瑞拉的庇护，不过推文" +
//      "在发布几分钟后随即删除。俄罗斯当局拒绝发表评论，而一直协助斯诺登的维基解密否认他将投靠委内瑞拉。" +
//    val result = ToAnalysis.parse(str)
////    println(result.getTerms)
//    val terms = result.getTerms
//    for(i <- 0 to terms.size()){
//      val word = terms.get(i).getName
//      val nature = terms.get(i).getNatureStr
//      if(expectedNature.contains(nature)){
//        println(word+":"+nature)
//      }
//    }
//    DicLibrary.insert(DicLibrary.DEFAULT,"抠图")
//    println(DicLibrary.get())
//
//  }
//}
