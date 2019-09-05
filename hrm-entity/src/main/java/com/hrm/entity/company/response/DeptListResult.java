package com.hrm.entity.company.response;

import com.hrm.entity.company.Company;
import com.hrm.entity.company.Department;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DeptListResult {
    private String companyId;
    private String companyName;
    private String companyManage;
    private List<Department> departments;

    public DeptListResult(Company company, List<Department> list) {
        this.companyId = company.getId();
        this.companyName = company.getName();
        this.companyManage = company.getLegalRepresentative();
        this.departments = list;
    }

}
