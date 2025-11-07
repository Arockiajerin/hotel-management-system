package com.in.Hotel.restIml;

import com.in.Hotel.POJO.Bill;
import com.in.Hotel.constants.HotelConstants;
import com.in.Hotel.rest.BillRest;
import com.in.Hotel.service.BillService;
import com.in.Hotel.utils.HotelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class BillRestImpl implements BillRest {

    @Autowired
    BillService billService;

    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
        try {
            return billService.generateReport(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return HotelUtils.getResponseEntity(HotelConstants.SOMETHING_WANT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Override
    public ResponseEntity<List<Bill>> getBill() {
        try {

            return billService.getBills();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param requestMap
     * @return
     */
    @Override
    public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {
        try {
            return billService.getPdf(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseEntity<String> deleteBill(Integer id) {
        try {
            return billService.deleteBill(id);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return HotelUtils.getResponseEntity(HotelConstants.SOMETHING_WANT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
