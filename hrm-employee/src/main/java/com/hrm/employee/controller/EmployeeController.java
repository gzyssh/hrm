package com.hrm.employee.controller;

import com.hrm.common.controller.BaseController;
import com.hrm.common.entity.PageResult;
import com.hrm.common.entity.Result;
import com.hrm.common.entity.ResultCode;
import com.hrm.common.utils.BeanMapUtils;
import com.hrm.common.utils.DownloadUtils;
import com.hrm.employee.service.*;
import com.hrm.entity.employee.*;
import com.hrm.entity.employee.response.EmployeeReportResult;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/employees")
public class EmployeeController extends BaseController {
    @Autowired
    private UserCompanyPersonalService userCompanyPersonalService;
    @Autowired
    private UserCompanyJobsService userCompanyJobsService;
    @Autowired
    private ResignationService resignationService;
    @Autowired
    private TransferPositionService transferPositionService;
    @Autowired
    private PositiveService positiveService;
    @Autowired
    private ArchiveService archiveService;


    /**
     * 员工个人信息保存
     */
    @PutMapping("/{id}/personalInfo")
    public Result savePersonalInfo(@PathVariable(name = "id") String uid, @RequestBody Map map) throws Exception {
        UserCompanyPersonal sourceInfo = BeanMapUtils.mapToBean(map, UserCompanyPersonal.class);
        if (sourceInfo == null) {
            sourceInfo = new UserCompanyPersonal();
        }
        sourceInfo.setUserId(uid);
        sourceInfo.setCompanyId(super.companyId);
        userCompanyPersonalService.save(sourceInfo);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 员工个人信息读取
     */
    @GetMapping("/{id}/personalInfo")
    public Result findPersonalInfo(@PathVariable(name = "id") String uid) throws Exception {
        UserCompanyPersonal info = userCompanyPersonalService.findById(uid);
        if(info == null) {
            info = new UserCompanyPersonal();
            info.setUserId(uid);
        }
        return new Result(ResultCode.SUCCESS,info);
    }

    /**
     * 员工岗位信息保存
     */
    @PutMapping("/{id}/jobs")
    public Result saveJobsInfo(@PathVariable(name = "id") String uid, @RequestBody UserCompanyJobs sourceInfo) throws Exception {
        //更新员工岗位信息
        if (sourceInfo == null) {
            sourceInfo = new UserCompanyJobs();
            sourceInfo.setUserId(uid);
            sourceInfo.setCompanyId(super.companyId);
        }
        userCompanyJobsService.save(sourceInfo);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 员工岗位信息读取
     */
    @GetMapping("/{id}/jobs")
    public Result findJobsInfo(@PathVariable(name = "id") String uid) throws Exception {
        UserCompanyJobs info = userCompanyJobsService.findById(uid);
        if(info == null) {
            info = new UserCompanyJobs();
            info.setUserId(uid);
            info.setCompanyId(companyId);
        }
        return new Result(ResultCode.SUCCESS,info);
    }

    /**
     * 离职表单保存
     */
    @PutMapping(value = "/{id}/leave")
    public Result saveLeave(@PathVariable(name = "id") String uid, @RequestBody EmployeeResignation resignation) throws Exception {
        resignation.setUserId(uid);
        resignationService.save(resignation);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 离职表单读取
     */
    @GetMapping("/{id}/leave")
    public Result findLeave(@PathVariable(name = "id") String uid) throws Exception {
        EmployeeResignation resignation = resignationService.findById(uid);
        if(resignation == null) {
            resignation = new EmployeeResignation();
            resignation.setUserId(uid);
        }
        return new Result(ResultCode.SUCCESS,resignation);
    }

    /**
     * 导入员工
     */
    @PostMapping("/import")
    public Result importDatas(@RequestParam(name = "file") MultipartFile attachment) throws Exception {
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 调岗表单保存
     */
    @PutMapping("/{id}/transferPosition")
    public Result saveTransferPosition(@PathVariable(name = "id") String uid, @RequestBody EmployeeTransferPosition transferPosition) throws Exception {
        transferPosition.setUserId(uid);
        transferPositionService.save(transferPosition);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 调岗表单读取
     */
    @GetMapping("/{id}/transferPosition")
    public Result findTransferPosition(@PathVariable(name = "id") String uid) throws Exception {
        UserCompanyJobs jobsInfo = userCompanyJobsService.findById(uid);
        if(jobsInfo == null) {
            jobsInfo = new UserCompanyJobs();
            jobsInfo.setUserId(uid);
        }
        return new Result(ResultCode.SUCCESS,jobsInfo);
    }

    /**
     * 转正表单保存
     */
    @PutMapping("/{id}/positive")
    public Result savePositive(@PathVariable(name = "id") String uid, @RequestBody EmployeePositive positive) throws Exception {
        positiveService.save(positive);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 转正表单读取
     */
    @GetMapping(value = "/{id}/positive")
    public Result findPositive(@PathVariable(name = "id") String uid) throws Exception {
        EmployeePositive positive = positiveService.findById(uid);
        if(positive == null) {
            positive = new EmployeePositive();
            positive.setUserId(uid);
        }
        return new Result(ResultCode.SUCCESS,positive);
    }

    /**
     * 历史归档详情列表
     */
    @GetMapping(value = "/archives/{month}")
    public Result archives(@PathVariable(name = "month") String month, @RequestParam(name = "type") Integer type) throws Exception {
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 归档更新
     */
    @PutMapping("/archives/{month}")
    public Result saveArchives(@PathVariable(name = "month") String month) throws Exception {
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 历史归档列表
     */
    @GetMapping("/archives")
    public Result findArchives(@RequestParam(name = "pagesize") Integer pagesize, @RequestParam(name = "page") Integer page, @RequestParam(name = "year") String year) throws Exception {
        Map map = new HashMap();
        map.put("year",year);
        map.put("companyId",companyId);
        Page<EmployeeArchive> searchPage = archiveService.findSearch(map, page, pagesize);
        PageResult<EmployeeArchive> pr = new PageResult(searchPage.getTotalElements(),searchPage.getContent());
        return new Result(ResultCode.SUCCESS,pr);
    }

    /**
     * 人事报表导出
     */
    @GetMapping("/export/{month}")
    public void export(@PathVariable String month) throws Exception {
        //获取报表数据
        List<EmployeeReportResult> list=userCompanyPersonalService.findByReport(companyId,month);
        //构造Excel--》创造工作簿
        Workbook wb=new XSSFWorkbook();
        //构造Sheet
        Sheet sheet = wb.createSheet();
        //创建行
        Row row = sheet.createRow(0);
        //标题
        String[] title="编号,姓名,手机,最高学历,国家地区,护照号,籍贯,生日,属相,入职时间,离职类型,离职原因,离职时间".split(",");
        int titleIndex=0;
        for (String t : title) {
            Cell cell = row.createCell(titleIndex++);
            cell.setCellValue(t);
        }
        int rowIndex = 1;
        Cell cell=null;
        for (EmployeeReportResult employeeReportResult : list) {
            row = sheet.createRow(rowIndex++);
            // 编号
            cell = row.createCell(0);
            cell.setCellValue(employeeReportResult.getUserId());
            // 姓名
            cell = row.createCell(1);
            cell.setCellValue(employeeReportResult.getUsername());
            // 手机
            cell = row.createCell(2);
            cell.setCellValue(employeeReportResult.getMobile());
            // 最高学历
            cell = row.createCell(3);
            cell.setCellValue(employeeReportResult.getTheHighestDegreeOfEducation());
            // 国家地区
            cell = row.createCell(4);
            cell.setCellValue(employeeReportResult.getNationalArea());
            // 护照号
            cell = row.createCell(5);
            cell.setCellValue(employeeReportResult.getPassportNo());
            // 籍贯
            cell = row.createCell(6);
            cell.setCellValue(employeeReportResult.getNativePlace());
            // 生日
            cell = row.createCell(7);
            cell.setCellValue(employeeReportResult.getBirthday());
            // 属相
            cell = row.createCell(8);
            cell.setCellValue(employeeReportResult.getZodiac());
            // 入职时间
            cell = row.createCell(9);
            cell.setCellValue(employeeReportResult.getTimeOfEntry());
            // 离职类型
            cell = row.createCell(10);
            cell.setCellValue(employeeReportResult.getTypeOfTurnover());
            // 离职原因
            cell = row.createCell(11);
            cell.setCellValue(employeeReportResult.getReasonsForLeaving());
            // 离职时间
            cell = row.createCell(12);
            cell.setCellValue(employeeReportResult.getResignationTime());
        }
        //完成下载
        ByteArrayOutputStream bo=new ByteArrayOutputStream();
        wb.write(bo);
        new DownloadUtils().download(bo,response,month+"人事报表.xlsx");
    }
}
