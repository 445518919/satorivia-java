package com.satoriviacafe.common.utils.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import lombok.Getter;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;
import com.satoriviacafe.common.config.SatoriviacafeConfig;
import com.satoriviacafe.common.constant.Constants;
import com.satoriviacafe.common.exception.file.FileNameLengthLimitExceededException;
import com.satoriviacafe.common.exception.file.FileSizeLimitExceededException;
import com.satoriviacafe.common.exception.file.InvalidExtensionException;
import com.satoriviacafe.common.utils.DateUtils;
import com.satoriviacafe.common.utils.VStringUtils;
import com.satoriviacafe.common.utils.uuid.Seq;

/**
 * 檔案上傳工具類
 *
 * @author satoriviacafe
 */
@SuppressWarnings("unused")
public class FileUploadUtils {
    /**
     * 預設大小 50M
     */
    public static final long DEFAULT_MAX_SIZE = 50 * 1024 * 1024L;

    /**
     * 預設的檔案名最大長度 100
     */
    public static final int DEFAULT_FILE_NAME_LENGTH = 100;

    /**
     * 預設上傳的地址
     */
    @Getter
    private static String defaultBaseDir = SatoriviacafeConfig.getProfile();

    public static void setDefaultBaseDir(String defaultBaseDir) {
        FileUploadUtils.defaultBaseDir = defaultBaseDir;
    }

    /**
     * 以預設配置進行檔案上傳
     *
     * @param file 上傳的檔案
     * @return 檔案名稱
     * @throws IOException 如果發生IO異常
     */
    public static String upload(MultipartFile file) throws IOException {
        try {
            return upload(getDefaultBaseDir(), file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    /**
     * 根據檔案路徑上傳
     *
     * @param baseDir 相對應用的基目錄
     * @param file    上傳的檔案
     * @return 檔案名稱
     * @throws IOException 如果發生IO異常
     */
    public static String upload(String baseDir, MultipartFile file) throws IOException {
        try {
            return upload(baseDir, file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    /**
     * 檔案上傳
     *
     * @param baseDir          相對應用的基目錄
     * @param file             上傳的檔案
     * @param allowedExtension 允許的上傳檔案類型
     * @return 返回上傳成功的檔案名 (相對路徑)
     * @throws FileSizeLimitExceededException       如果超出最大大小
     * @throws FileNameLengthLimitExceededException 檔案名太長
     * @throws IOException                          比如讀寫檔案出錯時
     * @throws InvalidExtensionException            檔案校驗異常 (副檔名不允許)
     */
    public static String upload(String baseDir, MultipartFile file, String[] allowedExtension)
            throws FileSizeLimitExceededException, IOException, FileNameLengthLimitExceededException,
            InvalidExtensionException {
        int fileNamelength = Objects.requireNonNull(file.getOriginalFilename()).length();
        if (fileNamelength > FileUploadUtils.DEFAULT_FILE_NAME_LENGTH) {
            throw new FileNameLengthLimitExceededException(FileUploadUtils.DEFAULT_FILE_NAME_LENGTH);
        }

        assertAllowed(file, allowedExtension); // 檢查檔案大小和副檔名

        String fileName = extractFilename(file); // 提取並清理檔名，生成相對路徑結構

        // 獲取經過驗證的、規範化的絕對檔案物件
        File targetFile = getAbsoluteFile(baseDir, fileName);

        // 使用驗證後的 File 物件的路徑來傳輸檔案
        file.transferTo(targetFile.toPath());
        return getPathFileName(baseDir, fileName); // fileName 是相對路徑部分
    }

    /**
     * 提取並編碼（清理）檔案名。
     * 生成的檔名格式為：日期路徑/清理後的原始檔名基礎部分_序列號.副檔名
     * @param file MultipartFile物件
     * @return 清理和格式化後的相對檔案路徑 (例如 "2023/05/29/sanitizedName_seq123.jpg")
     */
    public static String extractFilename(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        String nameOnly; // 存儲清理後的檔名（不含路徑，不含副檔名）

        if (originalFileName == null || originalFileName.isEmpty()) {
            nameOnly = "uploaded_file"; // 如果原始檔名為空，則使用預設名稱
        } else {
            // 使用 FilenameUtils.getName 獲取檔名部分（去除可能包含的路徑）
            nameOnly = FilenameUtils.getName(originalFileName);
            // 清理檔名：只允許字母、數字、點、底線、減號。其他字元替換為底線。
            nameOnly = nameOnly.replaceAll("[^a-zA-Z0-9.\\-_]", "_");
            // 防止清理後檔名為空或只包含點 (例如 "...")
            if (nameOnly.isEmpty() || nameOnly.matches("^\\.+$")) {
                nameOnly = "uploaded_file";
            }
        }

        // 確保清理後的檔名部分不會太長 (可選，因為前面有全局長度檢查)
        // 預留約20個字元給日期路徑、序列號和副檔名
        int maxNameOnlyLength = DEFAULT_FILE_NAME_LENGTH - 20;
        if (nameOnly.length() > maxNameOnlyLength) {
            nameOnly = nameOnly.substring(0, maxNameOnlyLength);
        }

        // 格式化最終的相對檔案路徑： 日期路徑/清理後名稱_序列號.副檔名
        return VStringUtils.format("{}/{}_{}.{}", DateUtils.datePath(),
                nameOnly, Seq.getId(Seq.uploadSeqType), getExtension(file));
    }

    /**
     * 構建並驗證絕對檔案路徑。 (已修訂以增強安全性)
     *
     * @param uploadDir 基礎上傳目錄。
     * @param fileName  相對檔案名 (包含日期路徑、清理後的名稱、序列號和副檔名)。
     * @return 一個規範化的、經過驗證的 File 物件。
     * @throws IOException 如果路徑無效或試圖寫入到指定目錄之外。
     */
    public static File getAbsoluteFile(String uploadDir, String fileName) throws IOException {
        // 將預期的基礎上傳目錄路徑轉換為絕對路徑並規範化。
        Path intendedBaseDirPath = Paths.get(uploadDir).toAbsolutePath().normalize();

        // 將相對檔名 (fileName, 例如 "2023/05/29/sanitizedname_seq.ext")
        // 解析為相對於基礎目錄的路徑，並規範化。
        Path targetFilePath = intendedBaseDirPath.resolve(fileName).normalize();

        // **至關重要的安全檢查**: 驗證規範化後的目標路徑是否仍然
        // 位於 (或等於) 規範化後的預期基礎目錄之內。
        // 這可以防止路徑遍歷攻擊 (例如，如果 fileName 中包含 "..")。
        if (!targetFilePath.startsWith(intendedBaseDirPath)) {
            throw new IOException("無效的檔案路徑。試圖將檔案寫入指定上傳目錄之外。目標: '" + targetFilePath + "', 基礎目錄: '" + intendedBaseDirPath + "'");
        }

        File targetFile = targetFilePath.toFile();
        File parentDir = targetFile.getParentFile();

        // 如果目標檔案的父目錄不存在，則創建它。
        // 這一步必須在路徑驗證之後進行。
        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                // 再次檢查，因為其他執行緒可能已經創建了它。
                if (!parentDir.isDirectory()) {
                    throw new IOException("無法為以下路徑創建父目錄: " + targetFile.getAbsolutePath());
                }
            }
        }
        // 額外檢查以確保父路徑確實是一個目錄
        if (parentDir == null || !parentDir.isDirectory()) {
            throw new IOException("父路徑不是一個有效的目錄: " + (parentDir != null ? parentDir.getAbsolutePath() : "null"));
        }

        return targetFile; // 返回經過驗證的規範化 File 物件
    }

    /**
     * 獲取用於資源訪問的相對路徑檔案名 (例如，用於URL)。
     *
     * @param uploadDir 上傳時使用的基礎目錄 (通常是 SatoriviacafeConfig.getProfile() 的結果)
     * @param fileName  extractFilename 方法返回的相對檔案路徑 (例如 "2023/05/29/cleaned_name_seq.ext")
     * @return 組合後的資源路徑，例如 "/profile/uploads/2023/05/29/cleaned_name_seq.ext"
     */
    public static String getPathFileName(String uploadDir, String fileName)  {
        // 此方法假定 SatoriviacafeConfig.getProfile() 是 uploadDir 的一部分，
        // 且 fileName 是相對路徑。
        // 應確保 SatoriviacafeConfig.getProfile() 是一個安全的絕對路徑。
        int dirLastIndex = getDirLastIndex(uploadDir);

        String currentDir = ""; // uploadDir 中相對於 profilePath 的部分
        if (uploadDir.length() > dirLastIndex) {
            currentDir = VStringUtils.substring(uploadDir, dirLastIndex);
        }

        // 確保路徑分隔符統一為Unix風格('/')，並移除前後多餘的空格和斜杠，以便URL拼接
        currentDir = FilenameUtils.separatorsToUnix(currentDir.trim());
        fileName = FilenameUtils.separatorsToUnix(fileName.trim());

        if (currentDir.startsWith("/")) currentDir = currentDir.substring(1);
        if (currentDir.endsWith("/")) currentDir = currentDir.substring(0, currentDir.length() - 1);
        if (fileName.startsWith("/")) fileName = fileName.substring(1); // fileName 通常不應該以 / 開頭

        // 拼接最終的資源訪問路徑
        if (!currentDir.isEmpty()) {
            return Constants.RESOURCE_PREFIX + "/" + currentDir + "/" + fileName;
        } else {
            // 如果 currentDir 為空 (即 uploadDir 就是 profilePath)
            return Constants.RESOURCE_PREFIX + "/" + fileName;
        }
    }

    private static int getDirLastIndex(String uploadDir) {
        String profilePath = SatoriviacafeConfig.getProfile(); // 基礎配置路徑，例如 "/var/www/uploads" 或 "D:/uploads"

        // 驗證 uploadDir 是否以 profilePath 開頭，如果不是，可能存在配置問題
        if (profilePath == null || !uploadDir.startsWith(profilePath)) {
            // 此處應處理配置錯誤，例如拋出異常或使用預設值
            return 0; // 如果 profilePath 為 null 或 uploadDir 不以 profilePath 開頭，則返回 0
            // 目前假設 uploadDir 正確地以 profilePath 為前綴
        }

        // 計算 profilePath 在 uploadDir 中的結束索引
        int dirLastIndex = profilePath.length();
        // 如果 profilePath 後面緊跟著一個路徑分隔符，則索引加1
        if (uploadDir.length() > dirLastIndex &&
                (uploadDir.charAt(dirLastIndex) == File.separatorChar || uploadDir.charAt(dirLastIndex) == '/')) {
            dirLastIndex++;
        }
        return dirLastIndex;
    }


    /**
     * 檔案大小和副檔名校驗
     *
     * @param file             上傳的檔案
     * @param allowedExtension 允許的副檔名陣列
     * @throws FileSizeLimitExceededException 如果超出最大大小
     * @throws InvalidExtensionException      如果副檔名不允許
     */
    public static void assertAllowed(MultipartFile file, String[] allowedExtension)
            throws FileSizeLimitExceededException, InvalidExtensionException {
        long size = file.getSize();
        if (size > DEFAULT_MAX_SIZE) {
            throw new FileSizeLimitExceededException(DEFAULT_MAX_SIZE / 1024 / 1024);
        }

        String originalFileName = file.getOriginalFilename(); // 用於錯誤訊息的原始檔名
        String extension = getExtension(file); // 獲取檔案副檔名

        // 如果設定了允許的副檔名列表，並且當前檔案的副檔名不在允許列表中
        if (allowedExtension != null && !isAllowedExtension(extension, allowedExtension)) {
            // 根據不同的MIME類型拋出更具體的異常
            if (allowedExtension == MimeTypeUtils.IMAGE_EXTENSION) {
                throw new InvalidExtensionException.InvalidImageExtensionException(allowedExtension, extension,
                        originalFileName);
            } else if (allowedExtension == MimeTypeUtils.FLASH_EXTENSION) {
                throw new InvalidExtensionException.InvalidFlashExtensionException(allowedExtension, extension,
                        originalFileName);
            } else if (allowedExtension == MimeTypeUtils.MEDIA_EXTENSION) {
                throw new InvalidExtensionException.InvalidMediaExtensionException(allowedExtension, extension,
                        originalFileName);
            } else if (allowedExtension == MimeTypeUtils.VIDEO_EXTENSION) {
                throw new InvalidExtensionException.InvalidVideoExtensionException(allowedExtension, extension,
                        originalFileName);
            } else {
                // 通用無效副檔名異常
                throw new InvalidExtensionException(allowedExtension, extension, originalFileName);
            }
        }
    }

    /**
     * 判斷副檔名是否是允許的副檔名
     *
     * @param extension        檔案副檔名
     * @param allowedExtension 允許的副檔名陣列
     * @return 如果允許返回true，否則返回false
     */
    public static boolean isAllowedExtension(String extension, String[] allowedExtension) {
        if (extension == null) return false; // 處理副檔名為null的情況
        for (String str : allowedExtension) {
            if (str.equalsIgnoreCase(extension)) { // 忽略大小寫比較
                return true;
            }
        }
        return false;
    }

    /**
     * 獲取檔案名的副檔名
     *
     * @param file 表單檔案
     * @return 副檔名 (例如 "jpg", "png")，如果沒有則返回空字串
     */
    public static String getExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String extension = null;
        if (originalFilename != null) {
            // 從原始檔名中提取副檔名
            extension = FilenameUtils.getExtension(originalFilename);
        }

        // 如果從檔名中無法獲取副檔名 (例如檔名中沒有'.')，則嘗試從 ContentType 中獲取
        if (VStringUtils.isEmpty(extension)) {
            String contentType = file.getContentType();
            if (contentType != null) {
                extension = MimeTypeUtils.getExtension(contentType);
            }
        }
        return (extension == null) ? "" : extension; // 如果最終仍未獲取到，返回空字串
    }
}
