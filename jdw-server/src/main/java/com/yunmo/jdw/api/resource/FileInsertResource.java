package com.yunmo.jdw.api.resource;



import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yunmo.jdw.domian.ConciliationStatement;
import com.yunmo.jdw.domian.FalseAction;
import com.yunmo.jdw.infrastruction.mapper.ConciliationStatementMapper;
import com.yunmo.jdw.infrastruction.mybatisService.FalseActionService;
import com.yunmo.jdw.infrastruction.service.QccApi;
import com.yunmo.jdw.infrastruction.service.UploadImport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/public/upload")
@Transactional
@Tag(name = "文件导入相关")
public class FileInsertResource {
    //SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");
    //1、裁判文书网
    //2、用工表
    //3、社保表
    //4、企查查
    @Autowired
    QccApi qccApi;

    @Autowired
    UploadImport uploadImport;

    @Autowired
    CompanyListResource companyListResource;

    @Autowired
    ConciliationStatementMapper conciliationStatementMapper;
    @Autowired
    FalseActionService falseActionService;
    @PostMapping("/wenshu")
    public String upload1(MultipartFile file) throws IOException {
            String oldName = file.getOriginalFilename();
            checkEndFlag(oldName);
            checkExcelName(oldName, "(调解)");
            Boolean b = checkOldName(oldName);
            uploadImport.excelToDtoInsert(1, b, file.getInputStream());
            return ("上传成功");
    }

    @PostMapping("/shebaoyonggong")
    @Operation(description = "实时导入社保和用工数据")
    public String upload2(MultipartFile[] files) throws IOException {
        List<String> fileNameList = new ArrayList<>();
        for(MultipartFile file:files) {
            String oldName = file.getOriginalFilename();
            fileNameList.add(getFileBeginName(oldName));
            checkEndFlag(oldName);
            checkExcelName(oldName, "(参保|劳动)");
            Boolean b = checkOldName(oldName);
            if(oldName.contains("参保")) {
                 uploadImport.excelToDtoInsert(3, b, file.getInputStream());
            }else{
                 uploadImport.excelToDtoInsert(2, b, file.getInputStream());
            }
        }
        return  removeIfAndUpdateFalseAction(fileNameList);
    }

    @PostMapping("/companyToQccApi")
    @Operation(description = "调用企查查接口返回excel")
    public String upload4(HttpServletResponse response, MultipartFile file) throws IOException {
        String oldName = file.getOriginalFilename();
        checkEndFlag(oldName);
        checkExcelName(oldName, "(行政)");
        Boolean b = checkOldName(oldName);
        uploadImport.companyToQccApi(response,oldName, b, file.getInputStream());
        return ("上传成功");
    }

    @PostMapping("/companyToMysql")
    @Operation(description = "导入原始公司数据到mysql")
    public String upload5(MultipartFile file) throws IOException{
        String oldName = file.getOriginalFilename();
        checkEndFlag(oldName);
        checkExcelName(oldName, "(行政)");
        Boolean b = checkOldName(oldName);
        uploadImport.companyToMysql(b, file.getInputStream());
        return ("上传成功");
    }

    @PostMapping("/onlineQccAndLoginToMysql")
    @Operation(description = "实时导入企查查数据或者自己的注销数据到mysql")
    public String upload6(MultipartFile file) throws IOException {
        String oldName = file.getOriginalFilename();
        checkEndFlag(oldName);
        checkExcelName(oldName, "(企查查|注销)");
        Boolean b = checkOldName(oldName);
        return uploadImport.onlineQccAndLoginToMysql(b,oldName, file.getInputStream());
    }

    public void checkEndFlag(String oldName) {

        if (oldName.endsWith(".xls") && oldName.endsWith(".xlsx")) {
            throw new RuntimeException("上传文件格式有误");

        }
    }

    public Boolean checkOldName(String oldName) {
        Boolean b;
        if (oldName.endsWith(".xls")) {
            b = true;
        } else {
            b = false;
        }
        return b;
    }

    public String getFileBeginName(String fileName){
        if(fileName.lastIndexOf("（")==-1){
            return fileName.substring(0,fileName.lastIndexOf("("));
        }else{
            return fileName.substring(0,fileName.lastIndexOf("（"));
        }
    }

    public void checkExcelName(String name,String zhengze) {
        Matcher matcher = Pattern.compile(zhengze).matcher(name);
        if (!matcher.find()) {
                throw new RuntimeException("不是指定的文件");
        }
    }

    public String removeIfAndUpdateFalseAction(List<String> fileName) {

        List<ConciliationStatement> companyList=conciliationStatementMapper.findByRepeatCompanyList(fileName);
        if(companyList==null){
           throw new RuntimeException("没有对应公司");
        }
        List<ConciliationStatement> employConciliationStatements = conciliationStatementMapper.findByEmploymentRepeatList(companyList);
        List<ConciliationStatement> socialConciliationStatements = conciliationStatementMapper.findBySocialRepeatList(companyList);
        for (ConciliationStatement employConciliationStatement : employConciliationStatements) {
            companyList.removeIf(conciliationStatement -> conciliationStatement.getPlaintiff().equals(employConciliationStatement.getPlaintiff()));
        }
        for (ConciliationStatement socialConciliationStatement : socialConciliationStatements) {
            companyList.removeIf(conciliationStatement -> conciliationStatement.getPlaintiff().equals(socialConciliationStatement.getPlaintiff()));
        }
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String string = df.format(date);
        String name = "";
        for(String oneFileName:fileName){
            name+=oneFileName;
        }
        String finalName = name + string;
        System.out.println(finalName);
        for(ConciliationStatement conciliationStatement:companyList){
            FalseAction falseAction=new FalseAction();
            falseAction.setTitle(conciliationStatement.getTitle())
                    .setPlaintiff(conciliationStatement.getPlaintiff())
                    .setDefendant(conciliationStatement.getDefendant())
                    .setContent(conciliationStatement.getContent())
                    .setSign(finalName);
            UpdateWrapper<FalseAction> updateWrapper=new UpdateWrapper<>();
            Map<String,Object> params=new HashMap<>();
            params.put("title",conciliationStatement.getTitle());
            params.put("plaintiff",conciliationStatement.getPlaintiff());
            params.put("defendant",conciliationStatement.getDefendant());
            updateWrapper.allEq(params);
            falseActionService.saveOrUpdate(falseAction,updateWrapper);
        }
        return finalName;
    }
}
