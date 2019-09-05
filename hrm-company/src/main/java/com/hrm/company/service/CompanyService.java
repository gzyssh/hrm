package com.hrm.company.service;

import com.hrm.common.utils.IdWorker;
import com.hrm.company.dao.CompanyDao;
import com.hrm.entity.company.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 *
 * </p>
 *
 * @author guozy
 * @create 2019/08/07
 */
@Service
public class CompanyService {

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private IdWorker idWorker;

    public void add(Company company) {
        company.setId(String.valueOf(idWorker.nextId()));
        company.setCreateTime(new Date());
        company.setState(1); //启用
        company.setAuditState("0"); //待审核
        company.setBalance(0d);
        companyDao.save(company);
    }
    public void update(Company company) {
        Company temp = companyDao.findById(company.getId()).get();
        temp.setAuditState("1");
        companyDao.save(company);
    }
    public Company findById(String id) {
        Optional<Company> optional = companyDao.findById(id);
        return optional.isPresent()?optional.get():null;
    }
    public void deleteById(String id) {
        companyDao.deleteById(id);
    }
    public List<Company> findAll() {
        return companyDao.findAll();
    }
}
