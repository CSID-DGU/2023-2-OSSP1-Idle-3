package com.almostThere.global.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class FileUtil {

    public HashMap<String, String> fileCreate(String hostname, MultipartFile image) throws IOException {
        HashMap<String, String> result = new HashMap<>();

        //서버에 파일 저장
        UUID uuid = UUID.randomUUID();
        String name = uuid.toString() + image.getOriginalFilename();
        String path = "";
        File file = null;

        if (hostname.substring(0, 7).equals("DESKTOP")) {
            path = "C:/almostthere/";
            file = new File(path + name);
        } else {
            path = "/var/lib/almostthere/";
            file = new File(path + name);
        }

        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();

        image.transferTo(file);
        result.put("filePath",path);
        result.put("fileName",name);
        return result;
    }
    public void fileDelete(String hostname, String fileName){
        //서버에 파일 저장
        String path = "";
        File file = null;

        if (hostname.substring(0, 7).equals("DESKTOP")) {
            path = "C:/almostthere/";
            file = new File(path + fileName);
        } else {
            path = "/var/lib/almostthere/";
            file = new File(path + fileName);
        }

        if(file.exists()){
            if(file.delete()){
                System.out.println("파일삭제 성공");
            }else{
                System.out.println("파일삭제 실패");
            }
        }else{
            System.out.println("파일이 존재하지 않습니다.");
        }
    }
}
