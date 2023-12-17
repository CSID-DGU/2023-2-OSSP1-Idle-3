package com.almostThere.domain.meeting.controller;

import com.almostThere.domain.meeting.dto.ReceiptResponseDto;
import com.almostThere.domain.meeting.dto.create.CalculateDetailRequestDto;
import com.almostThere.domain.meeting.service.CalculateDetailService;
import com.almostThere.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.UUID;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/meeting-calculate")
public class MeetingCalculateApiController {
    @Value("${spring.clova.apiURL}")
    private String apiURL;
    @Value("${spring.clova.secretKey}")
    private String secretKey;

    private final CalculateDetailService calculateDetailService;

    /**
     * dyeon7310
     * @param receipt
     * @return 영수증 이미지 파일을 읽어 상호명과 총금액을 구한다.
     * CLOVA OCR Document API 이용
     * 허용 파일 형식: JPG, JPEG, PNG
     */
    @PostMapping("/receipt")
    public BaseResponse getReceiptInfo(Authentication authentication, @RequestParam MultipartFile receipt) {
        String contentType = receipt.getContentType();

        if (receipt==null || !(contentType.equals("image/jpg") || contentType.equals("image/jpeg") || contentType.equals("image/png"))){
            return BaseResponse.invalidFile();
        }

        try {
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            String type = contentType.substring(6);

            con.setUseCaches(false);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            con.setRequestProperty("X-OCR-SECRET", secretKey);

            System.out.println("파일 형식: "+type);
            JSONObject json = new JSONObject();
            json.put("version", "V2");
            json.put("requestId", UUID.randomUUID().toString());
            json.put("timestamp", System.currentTimeMillis());
            JSONObject image = new JSONObject();
            image.put("format", type);
            //image should be public, otherwise, should use data
            FileInputStream inputStream = (FileInputStream) receipt.getInputStream();
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            image.put("data", buffer);
            image.put("name", "demo");
            JSONArray images = new JSONArray();
            images.put(image);
            json.put("images", images);
            String postParams = json.toString();

            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            ReceiptResponseDto result = calculateDetailService.parseData(response);
            return BaseResponse.success(result);
        } catch (Exception e) {
            return BaseResponse.fail();
        }
    }

    @PostMapping("/detail")
    public BaseResponse addCalculateDetail(Authentication authentication, CalculateDetailRequestDto detailDto) throws IOException {
        calculateDetailService.saveCalculateDetail(detailDto);  //정산 내역 저장
        return BaseResponse.success(null);
    }

    @DeleteMapping("/{calculateDetailId}")
    public BaseResponse deleteCalculateDetail(Authentication authentication, @PathVariable Long calculateDetailId) throws UnknownHostException {
        calculateDetailService.deleteCalculateDetail(calculateDetailId);
        return BaseResponse.success(null);
    }
}

