package ltd.goods.cloud.newbee.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface OssService {
    /**
     * upload single file
     * @param file
     * @return
     */
    String uploadFile(MultipartFile file);
    /**
     * upload multipart files
     *
     * @param files
     * @return
     */
    List<String> uploadFiles(MultipartFile[] files);
}
