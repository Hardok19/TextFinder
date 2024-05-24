package org.finder.biblioteca;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.finder.Main;
import org.finder.Results.Result;

import java.awt.font.TextHitInfo;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;


public class resultconnector {

    private File file;
    private String textSnippet;
    private long creationDate;
    private String linePosition;


    public resultconnector(File file, String textSnippet, long creationDate, String linePosition) {
        this.file = file;
        this.textSnippet = textSnippet;
        this.creationDate = creationDate;
        this.linePosition = linePosition;
    }

}
