package com.hrm.employee.service;

import com.hrm.employee.dao.TransferPositionDao;
import com.hrm.entity.employee.EmployeeTransferPosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TransferPositionService {
    @Autowired
    private TransferPositionDao transferPositionDao;

    public EmployeeTransferPosition findById(String uid, Integer status) {
        EmployeeTransferPosition transferPosition = transferPositionDao.findByUserId(uid);
        if (status != null && transferPosition != null) {
            if (transferPosition.getEstatus() != status) {
                transferPosition = null;
            }
        }
        return transferPosition;
    }

    public void save(EmployeeTransferPosition transferPosition) {
        transferPosition.setCreateTime(new Date());
        transferPosition.setEstatus(1); //未执行
        transferPositionDao.save(transferPosition);
    }
}
