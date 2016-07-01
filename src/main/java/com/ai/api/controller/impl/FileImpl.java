package com.ai.api.controller.impl;

import com.ai.api.bean.FileDetailBean;
import com.ai.api.controller.File;
import com.ai.api.exception.AIException;
import com.ai.api.service.FileService;
import com.ai.commons.annotation.TokenSecured;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/***************************************************************************
 * <PRE>
 * Project Name    : api
 * <p>
 * Package Name    : com.ai.api.controller.impl
 * <p>
 * Creation Date   : 2016/6/30 12:20
 * <p>
 * Author          : Jianxiong Cai
 * <p>
 * Purpose         : TODO
 * <p>
 * <p>
 * History         : TODO
 * <p>
 * </PRE>
 ***************************************************************************/

@RestController
public class FileImpl implements File {

    private static final Logger logger = LoggerFactory.getLogger(FileImpl.class);

    @Autowired
    private FileService fileService;

    @Override
    @TokenSecured
    @RequestMapping(value = "/user/{userId}/file/{fileId}/detail", method = RequestMethod.GET)
    public ResponseEntity<FileDetailBean> getFileDetailInfo(@PathVariable("userId") String userId,
                                                            @PathVariable("fileId") String fileId)
            throws IOException, AIException {
        FileDetailBean fileDetailBean = null;
        try{
            fileDetailBean = fileService.getFileDetailInfo(userId,fileId);
        }catch (Exception e){
            logger.error("ERROR! from[getFileDetailInfo]:",e);
        }

        if (null== fileDetailBean) {
            logger.info("Null result!from[getFileDetailInfo]");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(fileDetailBean, HttpStatus.OK);
    }

    @Override
//    @ResponseBody
    @RequestMapping(value = "/user/{userId}/file/{fileId}", method = RequestMethod.GET)
    public void downloadFile(@PathVariable("userId") String userId, @PathVariable("fileId") String fileId,
                             HttpServletResponse httpResponse) {
        try {
            InputStream instream = fileService.downloadFile(userId,fileId);
//            httpResponse.getOutputStream().write(s);
//            httpResponse.setStatus(HttpServletResponse.SC_OK);

//            String url = "http://192.168.0.31:8092/file-service/getFile?id=4ebef3ba-4398-4321-8caa-02c9a192a2c5";
//            HttpClient httpclient = new DefaultHttpClient();
//            HttpGet httpget = new HttpGet(url);
//            HttpResponse response = httpclient.execute(httpget);
//            HttpEntity entity = response.getEntity();
//            InputStream instream = entity.getContent();

            httpResponse.setHeader("Pragma", "No-cache");
            httpResponse.setHeader("Cache-Control", "no-cache");
            httpResponse.setDateHeader("Expires", 0);
            httpResponse.setContentType("image/jpeg");
            BufferedImage image = ImageIO.read(instream);
            ImageIO.write(image, "jpg", httpResponse.getOutputStream());
            logger.info("success!");
        }catch (Exception e){
            logger.error("ERROR! from[downloadFile]:",e);
        }
    }
}
