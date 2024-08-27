package com.h2o.poppy.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


@Service
public class S3Service {

    private final AmazonS3 s3Client;

    public S3Service(@Value("${aws.access-key-id}") String accessKeyId,
                     @Value("${aws.secret-key}") String secretKey,
                     @Value("${aws.region}") String region) {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKeyId, secretKey);
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }

    public void createFolder(String folderName) {
        String bucketName = "poppys3";
        String folderKey = folderName.endsWith("/") ? folderName : folderName + "/";
        if (!s3Client.doesObjectExist(bucketName, folderKey)) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(0);
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, folderKey, new ByteArrayInputStream(new byte[0]), metadata);
            s3Client.putObject(putObjectRequest);
            System.out.println("Folder created successfully in bucket: " + bucketName);
        } else {
            System.out.println("Folder already exists in bucket: " + bucketName);
        }
    }

    @Async
    public void uploadFile(String folderName, MultipartFile file) throws IOException {
        String bucketName = "poppys3";
        String folderKey = folderName.endsWith("/") ? folderName : folderName + "/";
        String fileKey = folderKey + file.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileKey, file.getInputStream(), metadata);
        s3Client.putObject(putObjectRequest);
        System.out.println("File uploaded successfully to " + fileKey);
    }

    // 파일 모두 읽기
    public List<String> listObjectsInFolder(String folderPath) {
        String bucketName = "poppys3";
        // S3 버킷에서 객체 목록을 가져오기 위한 요청 생성
        ListObjectsV2Request request = new ListObjectsV2Request()
                .withBucketName(bucketName)
                .withPrefix(folderPath + "/"); // 폴더 내 모든 객체를 가져오기 위해 '/'를 추가합니다.

        // 객체 목록 가져오기
        ListObjectsV2Result result = s3Client.listObjectsV2(request);
        List<String> objectKeys = new ArrayList<>();

        // 가져온 객체들의 키(파일명)를 리스트에 추가
        for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
            objectKeys.add(objectSummary.getKey());
        }

        return objectKeys;
    }

    // 비디오 파일 업로드
    @Async
    public void videoUploadFile(String folderName, File originalFile, String originFileName, String videoType) throws IOException {
        String bucketName = "poppys3";
        String folderKey = folderName.endsWith("/") ? folderName : folderName + "/";

        File reencodedFile = null;

        try {
            //FFmpeg ffmpeg = new FFmpeg("C:\\Users\\SSAFY\\Desktop\\ffmpeg\\bin\\ffmpeg.exe");
            //FFprobe ffprobe = new FFprobe("C:\\Users\\SSAFY\\Desktop\\ffmpeg\\bin\\ffprobe.exe");

            FFmpeg ffmpeg = new FFmpeg("/usr/bin/ffmpeg");
            FFprobe ffprobe = new FFprobe("/usr/bin/ffprobe");

            reencodedFile = File.createTempFile("reencoded_", "_" + originFileName);

            FFmpegProbeResult probeResult = ffprobe.probe(originalFile.getAbsolutePath());

            FFmpegBuilder builder = new FFmpegBuilder()
                    .setInput(probeResult)  // Use FFmpegProbeResult here
                    .overrideOutputFiles(true) // Override the output if it exists
                    .addOutput(reencodedFile.getAbsolutePath())   // Filename for the destination
                    .setFormat("mp4")        // Format is inferred from filename, or can be set
                    .setTargetSize(2_000_000)  // Aim for a 2MB file
                    .disableSubtitle()       // No subtitles
                    .setAudioChannels(1)         // Mono audio
                    .setAudioCodec("aac")        // using the aac codec
                    .setAudioSampleRate(48_000)  // at 48KHz
                    .setAudioBitRate(32768)      // at 32 kbit/s
                    .setVideoCodec("libx264")     // Video using x264
                    .setVideoFrameRate(30, 1)     // at 30 frames per second
                    .setVideoResolution(1280, 720) // at 1280x720 resolution
                    .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL) // Allow FFmpeg to use experimental specs
                    .done();

            FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
            // Run a one-pass encode
            executor.createJob(builder).run();

            // Or run a two-pass encode (which is better quality at the cost of being slower)
            executor.createTwoPassJob(builder).run();

            String fileKey = folderKey + originFileName;
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(reencodedFile.length());
            metadata.setContentType(videoType);

            try (InputStream inputStream = new FileInputStream(reencodedFile)) {
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileKey, inputStream, metadata);
                s3Client.putObject(putObjectRequest);
                System.out.println("File uploaded successfully to " + fileKey);
            }
        } finally {
            // Delete the temporary files
            if (originalFile != null && originalFile.exists()) {
                if (!originalFile.delete()) {
                    System.err.println("Failed to delete original file: " + originalFile.getAbsolutePath());
                }
            }
            if (reencodedFile != null && reencodedFile.exists()) {
                if (!reencodedFile.delete()) {
                    System.err.println("Failed to delete reencoded file: " + reencodedFile.getAbsolutePath());
                }
            }
        }
    }
}
