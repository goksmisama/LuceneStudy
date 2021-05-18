package com.marui.demo01;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

/**
 * @author MaRui
 * @date 2021-05-17 22:02
 */
public class IndexManager {
    /**
     * 增加索引库
     * @throws IOException
     */
    @Test
    public void  addIndex() throws IOException {
        Directory directory =FSDirectory.open(new File("C:\\Users\\98460\\Desktop\\Lucene\\index").toPath());
        IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig(new IKAnalyzer()));
        Document document = new Document();
        document.add(new TextField("fileName","新添加的文件的文件名", Field.Store.YES));
        document.add(new TextField("fileContent","新添加的文件的内容", Field.Store.NO));
        document.add(new StoredField("filePath","C:/a.txt"));
        indexWriter.addDocument(document);
        indexWriter.close();
    }

    /**
     * 删除索引库
     * @throws IOException
     */
    @Test
    public void  deleteIndex() throws IOException {
        Directory directory =FSDirectory.open(new File("C:\\Users\\98460\\Desktop\\Lucene\\index").toPath());
        IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig(new IKAnalyzer()));
        indexWriter.deleteAll();
        indexWriter.close();
    }

    /**
     * 根据查询删除索引库
     * @throws IOException
     */
    @Test
    public void  deleteIndexByQuery() throws IOException {
        Directory directory =FSDirectory.open(new File("C:\\Users\\98460\\Desktop\\Lucene\\index").toPath());
        IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig(new IKAnalyzer()));
        indexWriter.deleteDocuments(new Term("fileName", "文件"));
        indexWriter.close();
    }

    /**
     * 跟新索引库
     * @throws IOException
     */
    @Test
    public void  updateIndex() throws IOException {
        Directory directory =FSDirectory.open(new File("C:\\Users\\98460\\Desktop\\Lucene\\index").toPath());
        IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig(new IKAnalyzer()));
        Document document = new Document();
        document.add(new TextField("fileName","修改的文件名", Field.Store.YES));
        indexWriter.updateDocument(new Term("fileName", "文件"),document);
        indexWriter.close();
    }



}
