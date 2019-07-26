package com.oecoo.gf.service;


import org.springframework.web.multipart.MultipartFile;

/**
 * Created by gf on 2018/5/1.
 */
public interface IFileService {

    public String upload(MultipartFile file, String path);

}
