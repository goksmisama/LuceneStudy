package com.marui.demo01;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

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
            Field fieldName = new TextField("fieldName",fileName, Field.Store.YES);
            Field fieldPath = new TextField("fieldPath",filePath,Field.Store.YES);
            Field fieldContent = new TextField("fieldContent",fileContent,Field.Store.YES);
            Field fieldSize = new TextField("fieldSize",fileSize+"",Field.Store.YES);
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
}
