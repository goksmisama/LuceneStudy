package com.marui.demo01;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

/**
 * @author MaRui
 * @date 2021-05-17 23:14
 */
public class SearchIndex {
    /**
     * 根据文件大小的范围查询索引库
     * @throws IOException
     */
    @Test
    public void searchIndex() throws IOException {
        //1. 创建一个Directory对象，指定索引库的位置
        Directory directory = FSDirectory.open(new File("C:\\Users\\98460\\Desktop\\Lucene\\index").toPath());
        //2. 创建一个indexReader对象
        IndexReader indexReader = DirectoryReader.open(directory);
        //3. 创建一个indexSerch对象，构造方法中需要indexReader对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        //4. 创建一个Query对象，通过PointLong静态方法获取
        Query query = LongPoint.newRangeQuery("fileSize",5000,6000);
        //5. 执行查询，得到TopDocs对象
        TopDocs topDocs = indexSearcher.search(query, 10);
        //获取总的记录数
        long totalHits = topDocs.totalHits;
        System.out.println("共有"+totalHits+"个文档");
        //6. 取出查询的总记录
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        //7. 取文档列表
        for (ScoreDoc scoreDoc : scoreDocs) {
            //取文档id
            int docId = scoreDoc.doc;
            //根据id获取文档对象
            Document document = indexSearcher.doc(docId);
            //获取文件名称
            String fileName = document.get("fileName");
            System.out.println(fileName);
            //获取文件路径
            String filePath = document.get("filePath");
            System.out.println(filePath);
            //获取文件内容
            String fileContent = document.get("fileContent");
            System.out.println(fileContent);
            //获取文件大小
            String fileSize = document.get("fileSize");
            System.out.println(fileSize);
        }
        //8. 关闭indexReader对象
        indexReader.close();
    }

    /**
     * 使用QueryParse解析查询表达式查询索引库
     * @throws IOException
     */
    @Test
    public void testQueryParser() throws IOException, ParseException {
        //1. 创建一个Directory对象，指定索引库的位置
        Directory directory = FSDirectory.open(new File("C:\\Users\\98460\\Desktop\\Lucene\\index").toPath());
        //2. 创建一个indexReader对象
        IndexReader indexReader = DirectoryReader.open(directory);
        //3. 创建一个indexSerch对象，构造方法中需要indexReader对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        //4. 创建一个Query对象，通过PointLong静态方法获取
            //创建一个QueryParser对象：参数1 默认的搜索域     参数2 分析器对象
        QueryParser queryParser = new QueryParser("fileContent",new IKAnalyzer());
            //获取Query对象
        Query query = queryParser.parse("习近平出席了会议");
        //5. 执行查询，得到TopDocs对象
        TopDocs topDocs = indexSearcher.search(query, 10);
        //获取总的记录数
        long totalHits = topDocs.totalHits;
        System.out.println("共有"+totalHits+"个文档");
        //6. 取出查询的总记录
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        //7. 取文档列表
        for (ScoreDoc scoreDoc : scoreDocs) {
            //取文档id
            int docId = scoreDoc.doc;
            //根据id获取文档对象
            Document document = indexSearcher.doc(docId);
            //获取文件名称
            String fileName = document.get("fileName");
            System.out.println(fileName);
            //获取文件路径
            String filePath = document.get("filePath");
            System.out.println(filePath);
            //获取文件内容
            String fileContent = document.get("fileContent");
            System.out.println(fileContent);
            //获取文件大小
            String fileSize = document.get("fileSize");
            System.out.println(fileSize);
        }
        //8. 关闭indexReader对象
        indexReader.close();
    }
}
