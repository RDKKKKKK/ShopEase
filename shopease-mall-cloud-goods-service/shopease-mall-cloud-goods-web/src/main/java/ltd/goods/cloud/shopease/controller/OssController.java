package ltd.goods.cloud.shopease.controller;

import com.alibaba.cloud.commons.lang.StringUtils;
import io.swagger.annotations.Api;
import ltd.common.cloud.shopease.dto.Result;
import ltd.common.cloud.shopease.dto.ResultGenerator;
import ltd.goods.cloud.shopease.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@CrossOrigin(origins = {"http://localhost:8080", "null"})
@RequestMapping("/goods/admin/upload")
@RestController
@Api(value = "v1", tags = "上传文件接口")
public class OssController {
    @Autowired
    private OssService ossService;
    @Autowired
    private StandardServletMultipartResolver standardServletMultipartResolver;


    /**
     * upload single file
     * @param file
     * @return
     */
    @PostMapping("/file")
    public Result uploadFile(MultipartFile file){
        String url = ossService.uploadFile(file);
        //如果url为空，就返回错误，如果 不为空就返回成功并返回数据
        return StringUtils.isEmpty(url) ? ResultGenerator.genFailResult("upload failed"):ResultGenerator.genSuccessResult((Object) url);
    }
    /**
     * upload multi files
     * @param
     * @return
     */
    @PostMapping("/files")
    public ResponseEntity<Map<String, Object>> uploadFiles(HttpServletRequest httpServletRequest){
        List<MultipartFile> multipartFiles = new ArrayList<>(8);
        Map<String, Object> response = new HashMap<>();
        if (standardServletMultipartResolver.isMultipart(httpServletRequest)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) httpServletRequest;
            Iterator<String> iter = multiRequest.getFileNames();
            int total = 0;
            while (iter.hasNext()) {
                if (total > 5) {
                    response.put("errno", 1); // 表示失败
                    response.put("message", "Maximum 5 Images");
                    return ResponseEntity.ok(response);
                }
                total += 1;
                MultipartFile file = multiRequest.getFile(iter.next());
                multipartFiles.add(file);
            }
        }
        if (CollectionUtils.isEmpty(multipartFiles)) {
            response.put("errno", 1); // 表示失败
            response.put("message", "Parameter Error");
            return ResponseEntity.ok(response);
        }
        List<String> urls = ossService.uploadFiles(multipartFiles.toArray(new MultipartFile[multipartFiles.size()]));
        // 创建返回的 Map
        if (urls.isEmpty()) {
            response.put("errno", 1); // 表示失败
            response.put("message", "upload failed");
        } else {
            response.put("errno", 0); // 表示成功

            // 创建 data 列表
            List<Map<String, String>> data = new ArrayList<>();
            for (String url : urls) {
                // 每个图片对象，alt 和 href 设置为空
                Map<String, String> imageObject = new HashMap<>();
                imageObject.put("url", url);
                imageObject.put("alt", "");
                imageObject.put("href", "");
                data.add(imageObject);
            }
            response.put("data", data);
        }

        return ResponseEntity.ok(response);
    }
}
