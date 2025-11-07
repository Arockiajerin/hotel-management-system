package com.in.Hotel.serviceImpl;

import java.util.*;


import com.google.common.base.Strings;
import com.in.Hotel.JWT.JwtFilter;
import com.in.Hotel.JWT.JwtUtil;
import com.in.Hotel.POJO.User;
import com.in.Hotel.constants.HotelConstants;
import com.in.Hotel.dao.UserDao;
import com.in.Hotel.service.UserService;
import com.in.Hotel.utils.EmailUtil;
import com.in.Hotel.utils.HotelUtils;
import com.in.Hotel.utils.MockEmailUtil;
import com.in.Hotel.wrapper.UserWrapper;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Data

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    EmailUtil emailUtil;
//@Autowired
//MockEmailUtil emailUtil;


    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requstMap) {
        try {


            log.info("Inside signup{}", requstMap);
            if (ValidateSignUpMap(requstMap)) {
                User user = userDao.findByEmailId(requstMap.get("email"));
                if (Objects.isNull(user)) {
                    userDao.save(getUSerFromMap(requstMap));
                    return HotelUtils.getResponseEntity("Successfully Registered", HttpStatus.OK);

                } else {
                    return HotelUtils.getResponseEntity("Email  already exits", HttpStatus.BAD_REQUEST);
                }

            } else {
                return HotelUtils.getResponseEntity(HotelConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       return HotelUtils.getResponseEntity(HotelConstants.SOMETHING_WANT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }




    private boolean ValidateSignUpMap(Map<String,String>requestMap){
       if (requestMap.containsKey("name") && requestMap.containsKey("contactNumber")
                && requestMap.containsKey("email") && requestMap.containsKey("password")){
            return true;
        }
        return false;
    }
    private User getUSerFromMap(Map<String,String> requestMap){
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(passwordEncoder.encode(requestMap.get("password")));
        user.setStatus("false");
        user.setRole("user");
        return user;
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requstMap) {
       log.info("Inside login");
       try{
           Authentication auth = authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(requstMap.get("email"),requstMap.get("password"))
           );
           if (auth.isAuthenticated()){
               User user = userDao.findByEmailId(requstMap.get("email"));
               if(user != null && "true".equalsIgnoreCase(user.getStatus())){
                   return new ResponseEntity<String>("{\"token\":\""+jwtUtil.generateToken(user.getEmail(),
                           user.getRole())+"\"}",HttpStatus.OK);

               }
               else {
                   return new ResponseEntity<String>("{\"message\":\""+"Wait for admin approval"+"\"}",HttpStatus.BAD_REQUEST);
               }
           }
       } catch (Exception e) {
           log.error("{}",e);
       }
        return new ResponseEntity<String>("{\"message\":\""+"Bad Credentials."+"\"}",HttpStatus.BAD_REQUEST);
    }


    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try {
            if (jwtFilter.isAdmin()){

                return  new ResponseEntity<>(userDao.getAllUser(), HttpStatus.OK);

            }
            else {
                return new ResponseEntity<>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
       try {
           if (jwtFilter.isAdmin()) {
               Optional<User> optional  = userDao.findById(Long.parseLong(requestMap.get("id")));
                if (!optional.isEmpty()) {

                    Integer result = userDao.updateStatus(requestMap.get("status"), Long.parseLong(requestMap.get("id")));
                    sendMailToAllAdmin(requestMap.get("status"), optional.get().getEmail(), userDao.getAllAdmin());

                    return HotelUtils.getResponseEntity("User Status Updated Successfully", HttpStatus.OK);
                }else {
                    return HotelUtils.getResponseEntity("User id does not exist",HttpStatus.OK);
                }

           } else {
               return HotelUtils.getResponseEntity(HotelConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
           }
       } catch (Exception e) {
           e.printStackTrace();
       }
       return HotelUtils.getResponseEntity(HotelConstants.SOMETHING_WANT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }




    private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {
        try {
            allAdmin.remove(jwtFilter.getCurrentUser());


            log.info("Email notification simulated for user status update");
            log.info("User: {}, Status: {}, Admins to notify: {}", user, status, allAdmin);



        } catch (Exception e) {
            log.warn("Email notification failed, but user update was successful: {}", e.getMessage());
        }

    }
    @Override
    public ResponseEntity<String> checkToken() {
        return HotelUtils.getResponseEntity("true",HttpStatus.OK);
    }


    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
            User userObj = userDao.findByEmail(jwtFilter.getCurrentUser());
            if (userObj != null) {
                // Use passwordEncoder.matches() to compare raw password with encoded password
                if (passwordEncoder.matches(requestMap.get("oldPassword"), userObj.getPassword())) {
                    // Encode the new password before saving
                    userObj.setPassword(passwordEncoder.encode(requestMap.get("newPassword")));
                    userDao.save(userObj);
                    return HotelUtils.getResponseEntity("Password Updated Successfully", HttpStatus.OK);
                }
                return HotelUtils.getResponseEntity("Incorrect Old Password", HttpStatus.BAD_REQUEST);
            }
            return HotelUtils.getResponseEntity("User not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return HotelUtils.getResponseEntity(HotelConstants.SOMETHING_WANT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try{
            User user=userDao.findByEmail(requestMap.get("email"));
            if (!Objects.isNull(user)&&!Strings.isNullOrEmpty(user.getEmail())) {
                try {
                    emailUtil.forgotMail(user.getEmail(),"Credentials by Hotel Management System",user.getPassword());
                } catch (Exception e) {
                    log.error("Failed to send email to: {}", user.getEmail(), e);
                }
                return HotelUtils.getResponseEntity("Check your mail for Credentials",HttpStatus.OK);
            } else {
                return HotelUtils.getResponseEntity("Email not found",HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return HotelUtils.getResponseEntity(HotelConstants.SOMETHING_WANT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }


}



