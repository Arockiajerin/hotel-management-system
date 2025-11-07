package com.in.Hotel.utils;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Slf4j

public class HotelUtils {
    private HotelUtils(){

    }
    public static ResponseEntity<String>getResponseEntity(String responsemessage, HttpStatus httpStatus){
        return new ResponseEntity<String>("{\"message\":\""+responsemessage+"\"}" ,httpStatus);

    }

    public static String getUUID(){
        Date data = new Date();
        long time = data.getTime();
        return "BILL"+time;

    }

    public static JSONArray getJsonArrayFromString(String data)throws JSONException{
        JSONArray jsonArray = new JSONArray(data);


        return jsonArray;
    }
    public static Map<String ,Object > getMapFromJson(String data) {
        if (StringUtils.isNotBlank(data))
            return new Gson().fromJson(data, new TypeToken<Map<String, Object>>() {
            }.getType());

        return new HashMap<>();
    }

    public static Boolean isFileExist(String path){
        log.info("Inside isFileExist {}",path);
        try {
            File file=new File(path);
            return (file != null&& file.exists())?Boolean.TRUE:Boolean.FALSE;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false ;
    }
}
