package com.in.Hotel.restIml;

import com.in.Hotel.POJO.Category;
import com.in.Hotel.constants.HotelConstants;
import com.in.Hotel.rest.CategoryRest;
import com.in.Hotel.service.CategoryService;
import com.in.Hotel.utils.HotelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class CategoryRestImpl implements CategoryRest {

    @Autowired
    CategoryService categoryService;


    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
        try{

            return categoryService.addNewCategory(requestMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return HotelUtils.getResponseEntity(HotelConstants.SOMETHING_WANT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Override
    public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
        try{
            return categoryService.getAllCategory(filterValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
        try {
            return categoryService.updateCategory(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return HotelUtils.getResponseEntity(HotelConstants.SOMETHING_WANT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
