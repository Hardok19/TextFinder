package org.finder;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class DocxFileReader {
    private static final Logger logger = LogManager.getLogger(DocxFileReader.class);
    private AVLTree avlTree;

    public DocxFileReader(AVLTree avlTree) {
        this.avlTree = avlTree;
    }

    public void readFileAndInsertWords(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath);
             XWPFDocument document = new XWPFDocument(fis)) {
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            int wordCount = 0;

            for (XWPFParagraph paragraph : paragraphs) {
                String[] words = paragraph.getText().split("\\s+");
                for (String word : words) {
                    if (!word.isEmpty()) {
                        avlTree.insert(word.trim(), filePath, wordCount + 1);
                        wordCount++;
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Error al leer el archivo: " + e.getMessage(), e);
        }
    }
}
