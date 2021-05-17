package com.marui.demo01;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

/**
 * @author MaRui
 * @date 2021-05-17 11:16
 */
public class LuceneFirst {
    @Test
    public void createIndex() throws IOException {
        //1. 创建一个Direct对象，指定索引库的保存位置
        //把索引保存在内存中:RAMDirectory directory = new RAMDirectory();
        //把索引保存在硬盘上
        Directory directory = FSDirectory.open(new File("C:\\Users\\98460\\Desktop\\Lucene\\index").toPath());
        //2. 基于Directory对象创建一个indexWriter对象
        IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig());
        //3. 读取硬盘上的文件，对应每一个文件创建一个文档对象
        File dir = new File("D:\\develop\\luke-7.4.0\\search");
        File[] files = dir.listFiles();
        for (File file : files) {
            //读取文件名
            String fileName = file.getName();
            //读取文件路径
            String filePath = file.getPath();
            //读取文件的内容
            String fileContent = FileUtils.readFileToString(file, "utf-8");
            //读取文件大小
            long fileSize = FileUtils.sizeOf(file);
            //创建field：参数1 域的名称     参数2 域的内容     参数3 是否存储
            Field fieldName = new TextField("fileName",fileName, Field.Store.YES);
            Field fieldPath = new TextField("filePath",filePath,Field.Store.YES);
            Field fieldContent = new TextField("fileContent",fileContent,Field.Store.YES);
            Field fieldSize = new TextField("fileSize",fileSize+"",Field.Store.YES);
            //创建文档对象
            Document document = new Document();
            //4.向文档对象中添加域
            document.add(fieldName);
            document.add(fieldPath);
            document.add(fieldContent);
            document.add(fieldSize);
            //5.把文档对象写入索引库
            indexWriter.addDocument(document);
        }
        //6.关闭indexWriter对象
        indexWriter.close();
    }

    @Test
    public void searchIndex() throws IOException {
        //1. 创建一个Directory对象，指定索引库的位置
        Directory directory = FSDirectory.open(new File("C:\\Users\\98460\\Desktop\\Lucene\\index").toPath());
        //2. 创建一个indexReader对象
        IndexReader indexReader = DirectoryReader.open(directory);
        //3. 创建一个indexSerch对象，构造方法中需要indexReader对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        //4. 创建一个Query对象，通过TermQuery实现类创建
        Query query = new TermQuery(new Term("fileContent","were"));
        //5. 执行查询，得到TopDocs对象
        TopDocs topDocs = indexSearcher.search(query, 10);
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
            String filePath = document.get("fileContent");
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

    @Test
    public void testTokenStream() throws IOException {
        //1. 使用Analyzer的StandardAnalyzer实现了创建对象
        Analyzer analyzer = new StandardAnalyzer();
        //2. 使用分析器对象的tokenStream()方法获得TokenStream对象
        TokenStream tokenStream = analyzer.tokenStream(null, "我爱中国");
        //3. 想TokenStream对象设置一个引用作为指针
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        //4. 调用TokenStream对象的reset方法，如果不调用会抛错
        tokenStream.reset();
        //5. 使用while循环遍历TokenStream对象
        while (tokenStream.incrementToken()) {
            System.out.println(charTermAttribute);
        }
        //6. 释放资源，关闭TokenStream对象
        tokenStream.close();
    }

    @Test
    public void testIKAnalyzer() throws IOException {
        //1. 使用Analyzer的StandardAnalyzer实现了创建对象
        Analyzer analyzer = new IKAnalyzer();
        //2. 使用分析器对象的tokenStream()方法获得TokenStream对象
        TokenStream tokenStream = analyzer.tokenStream(null, "耗子尾汁");
        //3. 想TokenStream对象设置一个引用作为指针
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        //4. 调用TokenStream对象的reset方法，如果不调用会抛错
        tokenStream.reset();
        //5. 使用while循环遍历TokenStream对象
        while (tokenStream.incrementToken()) {
            System.out.println(charTermAttribute);
        }
        //6. 释放资源，关闭TokenStream对象
        tokenStream.close();
    }
}
