package org.finder.biblioteca;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.finder.Main;
import org.finder.Results.Result;

import javax.swing.*;
import java.awt.font.TextHitInfo;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;

public class biblioteca {
    private static final Logger logger = LogManager.getLogger(Main.class);

    List<Result> array = new ArrayList<>();

    public int size = 0;



    public List<Result> resultado(){
        return array;
    }

    public void add(File file, String textSnippet, long creationDate, String linePosition)
    {
        FileTime fileTime = null;
        try {
            fileTime = Files.getLastModifiedTime(Path.of(file.getAbsolutePath()));
            this.size += 1;
            this.array.add(new Result(file.getName(), file.getAbsolutePath(), (int)file.length(), fileTime, textSnippet, creationDate, linePosition));
        } catch (IOException e) {
            logger.error(e);
        }
    }

    public void delete(File file){
        int i = 0;
        if(file.isDirectory()){
            delete(file);
            return;
        }
        while(i < size) {
            if (file.getAbsolutePath().equals(array.get(i).getFilePath())){
                array.remove(array.get(i));
                size -= 1;
                break;
            }
            else
                i += 1;
            logger.error("Error al eliminar no existe este elemento");
        }
    }






}
