package com.vtit.utils;

import com.vtit.exception.IdInvalidException;

public class IdValidator {

    /**
     * Kiểm tra chuỗi ID có phải là số nguyên dương hay không
     * @param id chuỗi nhận từ @PathVariable
     * @return số nguyên hợp lệ
     * @throws IdInvalidException nếu không hợp lệ
     */
    public static Integer validateAndParse(String id) {
        try {
            Integer intId = Integer.parseInt(id);
            if (intId <= 0) {
                throw new IdInvalidException("ID phải là số nguyên dương");
            }
            return intId;
        } catch (NumberFormatException e) {
            throw new IdInvalidException("ID phải là số nguyên dương");
        }
    }
}
