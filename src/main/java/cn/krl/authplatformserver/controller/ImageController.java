package cn.krl.authplatformserver.controller;

import cn.krl.authplatformserver.common.response.ResponseWrapper;
import cn.krl.authplatformserver.common.utils.ImageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/api/image")
@CrossOrigin
@Api(tags = "图片接口")
public class ImageController {

    @Autowired private ImageUtil imageUtil;

    @PostMapping("/upload")
    @ApiOperation(value = "上传图片")
    public ResponseWrapper imageUpload(@RequestPart("file") MultipartFile upload) {
        ResponseWrapper responseWrapper;
        try {
            String fileName = upload.getOriginalFilename();
            fileName = UUID.randomUUID().toString().replaceAll("-", "") + fileName;
            String url = imageUtil.uploadImage(upload.getInputStream(), fileName);
            responseWrapper = ResponseWrapper.markSuccess();
            responseWrapper.setExtra("iconUrl", url);
            log.info("图片上传成功");
        } catch (Exception e) {
            log.error("图片上传失败");
            e.printStackTrace();
            return ResponseWrapper.markUploadImageError();
        }
        return responseWrapper;
    }
}