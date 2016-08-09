package com.ai.api.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * Created by henry yue on 2016/8/8.
 */
public class BASE64DecodedMultipartFile implements MultipartFile
{
    private final byte[] imgContent;
    private final String fileName;
    private final String fileOriginalName;

    public BASE64DecodedMultipartFile(String fileName, String fileOriginalName, byte[] imgContent)
    {
        this.imgContent = imgContent;
        this.fileName = fileName;
        this.fileOriginalName = fileOriginalName;
    }

    @Override
    public String getName()
    {
        // TODO - implementation depends on your requirements
        return this.fileName;
        //return null;
    }

    @Override
    public String getOriginalFilename()
    {
        // TODO - implementation depends on your requirements
        return this.fileOriginalName;
        //return null;
    }

    @Override
    public String getContentType()
    {
        // TODO - implementation depends on your requirements
        return null;
    }

    @Override
    public boolean isEmpty()
    {
        return imgContent == null || imgContent.length == 0;
    }

    @Override
    public long getSize()
    {
        return imgContent.length;
    }

    @Override
    public byte[] getBytes() throws IOException
    {
        return imgContent;
    }

    @Override
    public InputStream getInputStream() throws IOException
    {
        return new ByteArrayInputStream(imgContent);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException
    {
        new FileOutputStream(dest).write(imgContent);
    }
}
