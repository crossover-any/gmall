package com.gmall.manage.util;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Author Tengxq
 * @Date 2020/2/25 14:11
 * @Describe
 * @Version 1.0
 **/

@Component
public class FastDFSUtil {

    private  String tracker;

    private  TrackerClient trackerClient;

    private  TrackerServer trackerServer;

    private  StorageClient storageClient;

    @Value("${fast.address}")
    private  String FASTDFS_ADDRESS;


    public FastDFSUtil(){
        tracker = FastDFSUtil.class.getResource("/tracker.conf").getPath();
        try {
            ClientGlobal.init(tracker);
            trackerClient = new TrackerClient();
            trackerServer = trackerClient.getTrackerServer();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
           storageClient = new StorageClient(trackerServer);
    }

    public  String uploadImg(MultipartFile multipartFile){
        String imgUrl = "";
        if (multipartFile == null){
            return imgUrl;
        }
        try {
            byte[] bytes = multipartFile.getBytes();
            String extName = getFileExtName(multipartFile);
            String[] strings = storageClient.upload_appender_file(bytes, extName, null);
            for (String str: strings) {
                imgUrl+="/"+str;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e){
            e.printStackTrace();
        }
        return FASTDFS_ADDRESS+imgUrl;
    }

    public  String getFileExtName(MultipartFile multipartFile){
        if (multipartFile == null){
            return null;
        }
        String fileName = multipartFile.getOriginalFilename();
        return fileName.substring(fileName.lastIndexOf(".")+1);
    }

    public String getTracker() {
        return tracker;
    }

    public void setTracker(String tracker) {
        this.tracker = tracker;
    }

    public TrackerClient getTrackerClient() {
        return trackerClient;
    }

    public void setTrackerClient(TrackerClient trackerClient) {
        this.trackerClient = trackerClient;
    }

    public TrackerServer getTrackerServer() {
        return trackerServer;
    }

    public void setTrackerServer(TrackerServer trackerServer) {
        this.trackerServer = trackerServer;
    }

    public StorageClient getStorageClient() {
        return storageClient;
    }

    public void setStorageClient(StorageClient storageClient) {
        this.storageClient = storageClient;
    }

    public String getFASTDFS_ADDRESS() {
        return FASTDFS_ADDRESS;
    }


    public void setFASTDFS_ADDRESS(String FASTDFS_ADDRESS) {
        this.FASTDFS_ADDRESS = FASTDFS_ADDRESS;
    }
}
