package com.in.Hotel.serviceImpl;


import com.google.common.base.Strings;
import com.in.Hotel.JWT.JwtFilter;
import com.in.Hotel.POJO.Category;
import com.in.Hotel.constants.HotelConstants;
import com.in.Hotel.dao.CategoryDao;
import com.in.Hotel.service.CategoryService;
import com.in.Hotel.utils.HotelUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
        try {

            if(jwtFilter.isAdmin()){

                if (validateCategoryMap(requestMap, false)) {
                    categoryDao.save(getCategoryFromMap(requestMap, false));
                    return HotelUtils.getResponseEntity("Category Added Successfully", HttpStatus.OK);
                }

            }else {
                return HotelUtils.getResponseEntity(HotelConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return HotelUtils.getResponseEntity(HotelConstants.SOMETHING_WANT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }




    private boolean validateCategoryMap(Map<String, String> requestMap, boolean validateId) {
        if (requestMap.containsKey("name")) {
            if (requestMap.containsKey("id") && validateId) {
                return true;
            } else if (!validateId) {
                return true;
            }
        }
        return false;

    }
    private Category getCategoryFromMap(Map<String, String> requestMap, boolean isAdd) {
        Category category = new Category();
        if (isAdd) {
            category.setId(Integer.parseInt(requestMap.get("id")));
        }
        category.setName(requestMap.get("name"));
        return category;
    }

    @Override
    public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
        try{
            if (!Strings.isNullOrEmpty(filterValue)&&filterValue.equalsIgnoreCase("true")){

                log.info("Inside if");
                return new ResponseEntity<List<Category>>(categoryDao.getAllCategory(), HttpStatus.OK);
            }
            return new ResponseEntity<>(categoryDao.findAll(),HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  new ResponseEntity<List<Category>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {

        try{

            if (jwtFilter.isAdmin()){

                if (validateCategoryMap(requestMap, true)) {
                    Optional optional=categoryDao.findById(Integer.parseInt(requestMap.get("id")));
                    if (!optional.isEmpty()) {
                        categoryDao.save(getCategoryFromMap(requestMap, true));
                        return HotelUtils.getResponseEntity("Category Updated Successfully", HttpStatus.OK);
                    } else {
                        return HotelUtils.getResponseEntity("Category id does not exist", HttpStatus.OK);
                    }
                }
                return HotelUtils.getResponseEntity(HotelConstants.INVALID_DATA,HttpStatus.BAD_REQUEST);

            }else {
                return HotelUtils.getResponseEntity(HotelConstants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return HotelUtils.getResponseEntity(HotelConstants.SOMETHING_WANT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
