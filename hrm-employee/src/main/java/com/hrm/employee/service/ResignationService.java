package com.hrm.employee.service;

import com.hrm.employee.dao.EmployeeResignationDao;
import com.hrm.entity.employee.EmployeeResignation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ResignationService {
    @Autowired
    EmployeeResignationDao resignationDao;

    public void save(EmployeeResignation resignation) {
        resignation.setCreateTime(new Date());
        resignationDao.save(resignation);
    }

    public EmployeeResignation findById(String userId) {
        return resignationDao.findByUserId(userId);
    }
}
