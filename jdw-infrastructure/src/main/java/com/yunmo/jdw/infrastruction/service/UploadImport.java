package com.yunmo.jdw.infrastruction.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yunmo.jdw.domian.*;
import com.yunmo.jdw.infrastruction.mapper.*;
import com.yunmo.jdw.infrastruction.mybatisService.*;
import com.yunmo.jdw.infrastruction.mapper.*;
import com.yunmo.jdw.infrastruction.mybatisService.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class UploadImport {
    @Autowired
    ConciliationStatementMapper conciliationStatementMapper;
    @Autowired
    EmploymentMapper employmentMapper;
    @Autowired
    SocialInsuranceMapper socialInsuranceMapper;
    @Autowired
    QccApi qccApi;
    @Autowired
    QccJsonService qccJsonService;

    @Autowired
    QccJsonMapper qccJsonMapper;
    @Autowired
    EmploymentService employmentService;

    @Autowired
    ConciliationStatementService conciliationStatementService;
    @Autowired
    SocialInsuranceService socialInsuranceService;
    @Autowired
    com.yunmo.jdw.infrastruction.mapper.fineMapper fineMapper;
    @Autowired
    com.yunmo.jdw.infrastruction.mybatisService.fineService fineService;

    public void excelToDtoInsert(int type, boolean xls, InputStream inputStream) throws IOException {
        Workbook workbook = workBookXsl(xls, inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        Row rowHead = sheet.getRow(0);

        CellReference cellReference = new CellReference("A4");
        boolean flag = false;
        for (int i = cellReference.getRow(); i <= sheet.getLastRowNum();) {
            Row r = sheet.getRow(i);
            if(r == null){
                // 如果是空行（即没有任何数据、格式），直接把它以下的数据往上移动
                sheet.shiftRows(i+1, sheet.getLastRowNum(),-1);
                continue;
            }
            flag = false;
            for(Cell c:r){
                if(c.getCellType() != Cell.CELL_TYPE_BLANK){
                    flag = true;
                    break;
                }
            }
            if(flag){
                i++;
                continue;
            }
            else{//如果是空白行（即可能没有数据，但是有一定格式）
                if(i == sheet.getLastRowNum())//如果到了最后一行，直接将那一行remove掉
                    sheet.removeRow(r);
                else//如果还没到最后一行，则数据往上移一行
                    sheet.shiftRows(i+1, sheet.getLastRowNum(),-1);
            }
        }
        int rowcount = sheet.getLastRowNum();
        //1、裁判文书网
        //2、用工表
        //3、社保表
        //4、企查查
        if (type == 1) {
            List<ConciliationStatement> conciliationStatementList = new ArrayList<>();
            rowHead.createCell((int) 'F' - 65).setCellValue("原告");
            rowHead.createCell((int) 'G' - 65).setCellValue("被告");
            log.info("start add row");
            for (int a = 1; a <= rowcount; a++) {
                log.info("start add row");
                try {
//                        DataFormatter formatter = new DataFormatter();
                    Row row = sheet.getRow(a);
                    Cell cell = row.getCell(0);
                    String date = cell.getStringCellValue();
                    String result = null;
                    if (row.getCell((int) 'F' - 65) != null && row.getCell((int) 'F' - 65).getCellType() != Cell.CELL_TYPE_BLANK) {
                        result = row.getCell((int) 'F' - 65).getStringCellValue();
                    }
                    String yuangao = null, beigao = null;
                    String y1 = "(.*)与";
                    String y2 = "(.*)诉";
                    String y3 = "原告(.*);";
                    List<String> yuangaoList = new ArrayList<>();
                    yuangaoList.add(y1);
                    yuangaoList.add(y2);
                    yuangaoList.add(y3);
                    for (String yg : yuangaoList) {
                        Matcher matcher = Pattern.compile(yg).matcher(date);
                        if (matcher.find()) {
                            yuangao = matcher.group(1);
                            break;
                        }
                    }
                    if (yuangao.contains("原告")) {
                        Matcher matcher = Pattern.compile("原告(.*)").matcher(yuangao);
                        if (matcher.find()) {
                            yuangao = matcher.group(1);
                        }
                    }
                    String b1 = "与(.*)追索";
                    String b2 = "与(.*)劳动";
                    String b3 = "与(.*)经济";
                    String b4 = "与(.*)工伤";
                    String b5 = "与(.*)社会";
                    String b6 = "与(.*)确认";
                    String b7 = "被告(.*公司)";
                    String b8 = "诉(.*公司)";
                    List<String> beigaoList = new ArrayList<>();
                    beigaoList.add(b1);
                    beigaoList.add(b2);
                    beigaoList.add(b3);
                    beigaoList.add(b4);
                    beigaoList.add(b5);
                    beigaoList.add(b6);
                    beigaoList.add(b7);
                    beigaoList.add(b8);
                    for (String bg : beigaoList) {
                        Matcher matcher = Pattern.compile(bg).matcher(date);
                        if (matcher.find()) {
                            beigao = matcher.group(1);
                            break;
                        }
                    }
                    if (beigao.contains("被告")) {
                        Matcher matcher = Pattern.compile("被告(.*)").matcher(beigao);
                        if (matcher.find()) {
                            beigao = matcher.group(1);
                        }
                    }
//                    row.createCell(5).setCellValue(yuangao);
//                    row.createCell(6).setCellValue(beigao);
                    ConciliationStatement conciliationStatement = new ConciliationStatement();
                    conciliationStatement.setTitle(date);
                    conciliationStatement.setPlaintiff(yuangao);
                    conciliationStatement.setDefendant(beigao);
                    conciliationStatement.setContent(result);
                    conciliationStatementList.add(conciliationStatement);
//                    UpdateWrapper<ConciliationStatement> updateWrapper = new UpdateWrapper<>();
//                    updateWrapper.eq("title", conciliationStatement.getTitle());
//                    conciliationStatementService.saveOrUpdate(conciliationStatement, updateWrapper);
                } catch (NullPointerException n) {
                    throw new RuntimeException("请完善表格");
                }
            }
            List<ConciliationStatement> conciliationStatementAllList=conciliationStatementMapper.selectList(null);
//            List<ConciliationStatement> conciliationStatementSaveList;
//            List<ConciliationStatement> conciliationStatementUpdateList;
            conciliationStatementAllList.stream().forEach(conciliationStatementAll -> {
                            conciliationStatementList.removeIf(conciliationStatement -> conciliationStatement.getTitle().equals(conciliationStatementAll.getTitle()));
                });
            conciliationStatementService.saveBatch(conciliationStatementList);
            log.info("end add row");
            log.info(JSON.toJSONString(conciliationStatementList));
        }
        if (type == 2) {
            List<Employment> employmentList = new ArrayList<>();
            for (int a = 1; a <= rowcount; a++) {
                try {
                    Row row = sheet.getRow(a);
                    String name = row.getCell((int) 'C' - 65).getStringCellValue();
                    String company = row.getCell((int) 'H' - 65).getStringCellValue();
                    String cardId = row.getCell((int) 'B' - 65).getStringCellValue();
                    String gender = row.getCell((int) 'D' - 65).getStringCellValue();
                    Employment employment = new Employment();
                    employment.setName(name);
                    employment.setCompany(company);
                    employment.setCardId(cardId);
                    employment.setGender(gender);
                    employmentList.add(employment);
                } catch (NullPointerException n) {
                    throw new RuntimeException("请完善劳动表格");
                }
            }
            employmentMapper.delete(null);
            employmentService.saveBatch(employmentList);
            log.info(JSON.toJSONString(employmentList));
        }
        if (type == 3) {
            List<SocialInsurance> socialInsuranceList = new ArrayList<>();
            for (int a = 1; a <= rowcount; a++) {
                try {
                    DataFormatter formatter = new DataFormatter();
                    Row row = sheet.getRow(a);
                    String name = row.getCell((int) 'C' - 65).getStringCellValue();
                    String company = row.getCell((int) 'O' - 65).getStringCellValue();
                    String socialSecurityNumber = row.getCell((int) 'B' - 65).getStringCellValue();
                    int companyId = Integer.parseInt(row.getCell((int) 'N' - 65).getStringCellValue());
                    SocialInsurance socialInsurance = new SocialInsurance();
                    socialInsurance.setName(name);
                    socialInsurance.setCompany(company);
                    socialInsurance.setSocialSecurityNumber(socialSecurityNumber);
                    socialInsurance.setCompanyId((long) companyId);
                    socialInsuranceList.add(socialInsurance);
                } catch (NullPointerException n) {
                    throw new RuntimeException("请完善参保表格");
                }
            }
            socialInsuranceMapper.delete(null);
            socialInsuranceService.saveBatch(socialInsuranceList);
            log.info(JSON.toJSONString(socialInsuranceList));
        }
    }

    public void companyToQccApi(HttpServletResponse response, String excelName, boolean xls, InputStream inputStream) throws IOException {
        List<fine> fineList = ToMysqlAndExcelConfig(xls, inputStream);
        List<QccJson> qccJsonList = qccApi.ExcelToQccInsert(fineList);
        Workbook workbook = new XSSFWorkbook();
        XSSFSheet xssfSheet = (XSSFSheet) workbook.createSheet("sheet1");
        XSSFRow sheetRow0 = xssfSheet.createRow(1);
        sheetRow0.createCell((int) 'C' - 65).setCellValue("公司名");
        sheetRow0.createCell((int) 'G' - 65).setCellValue("惩罚时间");
        sheetRow0.createCell((int) 'H' - 65).setCellValue("注销时间");
        sheetRow0.createCell((int) 'I' - 65).setCellValue("企查查查询结果");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");

        for (int row = 2; row <= qccJsonList.size() + 1; row++) {
            XSSFRow sheetRow = xssfSheet.createRow(row);
            sheetRow.createCell((int) 'C' - 65).setCellValue(qccJsonList.get(row - 2).getCompanyName());
            sheetRow.createCell((int) 'G' - 65).setCellValue(dateFormat.format(Date.from(qccJsonList.get(row - 2).getPenaltyTime())));
            if (qccJsonList.get(row - 2).getLogoutTime() != null) {
                sheetRow.createCell((int) 'H' - 65).setCellValue(dateFormat.format(Date.from(qccJsonList.get(row - 2).getLogoutTime())));
            }
            sheetRow.createCell((int) 'I' - 65).setCellValue(JSON.toJSONString(qccJsonList.get(row - 2).getResult(), SerializerFeature.WriteMapNullValue));
        }
//        String oldNameHead = null;
//        Matcher matcher = Pattern.compile("(.*)\\.").matcher(excelName);
//        if (matcher.find()) {
//            oldNameHead = matcher.group(1);
//        }
        Date date1 = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String string = df.format(date1);
        String finalName = "企查查" + string + ".xlsx";
        log.info(finalName);
        response.setHeader("content-Type", "application/vnd.ms-excel");
        // 下载文件的默认名称
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(finalName, "utf-8"));
        OutputStream os = null;
        try {
            os = response.getOutputStream();  // 文件夹名称叫text.xls
            workbook.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.info(JSON.toJSONString(qccJsonList));
    }

    public void companyToMysql(boolean xls, InputStream inputStream) throws IOException {
        List<fine> fineList = ToMysqlAndExcelConfig(xls, inputStream);
        log.info(JSON.toJSONString(fineList));
    }

    public List<fine> ToMysqlAndExcelConfig(boolean xls, InputStream inputStream) throws IOException {
      //  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        Workbook workbook = workBookXsl(xls, inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        int rowcount = sheet.getLastRowNum();
        List<fine> fineList = new ArrayList<>();
        Date penaltyTimeDate;
        for (int a = 2; a <= rowcount; a++) {
            try {
                fine fine = new fine();
                Row row = sheet.getRow(a);
                fine.setCompanyName(row.getCell((int) 'C' - 65).getStringCellValue());
                fine.setFineReason(row.getCell((int) 'D' - 65).getStringCellValue());
                fine.setFineMoney((float) row.getCell((int) 'E' - 65).getNumericCellValue());
                fine.setFineNo(row.getCell((int) 'F' - 65).getStringCellValue());
                String penaltyTime = row.getCell((int) 'G' - 65).getStringCellValue();
                penaltyTimeDate = parseDate(penaltyTime);
                Instant penaltyTimeDateInstant = penaltyTimeDate.toInstant();
                fine.setPenaltyTime(penaltyTimeDateInstant);
                fineList.add(fine);
                UpdateWrapper<fine> updateWrapperFine = new UpdateWrapper<>();
                updateWrapperFine.eq("company_name", fine.getCompanyName());
                fineService.saveOrUpdate(fine, updateWrapperFine);
            } catch (NullPointerException n) {
                throw new RuntimeException("请完善表格");
            }
        }
        return fineList;
    }

    public String onlineQccAndLoginToMysql(boolean xls,String oldName, InputStream inputStream) throws IOException {
        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        Workbook workbook = workBookXsl(xls, inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        int rowcount = sheet.getLastRowNum();
        List<QccJson> qccJsonList = new ArrayList<>();
        Date logoutTimeDate;
        List<QccJson> qccJsonListNew = new ArrayList<>();
        String finalName = oldName;
        for (int a = 2; a <= rowcount; a++) {
            try {
                Row row = sheet.getRow(a);
                String company = row.getCell((int) 'C' - 65).getStringCellValue();
                QccJson qccJson = new QccJson();
                qccJson.setCompanyName(company);
                Cell cell = row.getCell((int) 'H' - 65);
                Cell cellResult = row.getCell((int) 'I' - 65);
                if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
                    logoutTimeDate = parseDate(row.getCell((int) 'H' - 65).getStringCellValue());
                    Instant logoutTimeDateInstant = logoutTimeDate.toInstant();
                    qccJson.setLogoutTime(logoutTimeDateInstant);
                }
                if (cellResult != null && cellResult.getCellType() != Cell.CELL_TYPE_BLANK) {
                    Map<String, Object> result = JSONObject.parseObject(row.getCell((int) 'I' - 65).getStringCellValue(), Map.class);
                    qccJson.setResult(result);
                }
                qccJsonList.add(qccJson);
            } catch (NullPointerException n) {
                throw new RuntimeException("请完善表格");
            }
        }
        List<fine> fineList = fineMapper.findFineListByCompanyName(qccJsonList);
        fineList.stream().forEach(fine -> {
            qccJsonList.stream().forEach(qccJson -> {
                if (qccJson.getCompanyName().equals(fine.getCompanyName())) {
                    qccJson.setPenaltyTime(fine.getPenaltyTime()).setSign(finalName);
                    if (qccJson.getLogoutTime() != null && ChronoUnit.MONTHS.between(LocalDateTime.ofInstant(qccJson.getPenaltyTime(), ZoneId.systemDefault()), LocalDateTime.ofInstant(qccJson.getLogoutTime(), ZoneId.systemDefault())) <= 6) {
                        qccJson.setQccType(QccJson.QccType.malice);
                    } else {
                        qccJson.setQccType(QccJson.QccType.natural);
                    }
                    UpdateWrapper<QccJson> updateWrapperFine = new UpdateWrapper<>();
                    updateWrapperFine.eq("company_name", qccJson.getCompanyName());
                    qccJsonService.saveOrUpdate(qccJson, updateWrapperFine);
                    qccJsonListNew.add(qccJson);
                }
            });

        });
        log.info(JSON.toJSONString(qccJsonList));
        return finalName;
    }

    public Workbook workBookXsl(boolean xls, InputStream inputStream) throws IOException {
        Workbook workbook;
        if (xls) {
            workbook = new HSSFWorkbook(inputStream);
        } else {
            workbook = new XSSFWorkbook(inputStream);
        }
        return workbook;

    }

//    public List<Fine> QccAndLoginToMysql(int type, boolean xls, InputStream inputStream) throws IOException {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
//        Workbook workbook = workBookXsl(xls, inputStream);
//        Sheet sheet = workbook.getSheetAt(0);
//        int rowcount = sheet.getLastRowNum();
//        List<Fine> FineList = new ArrayList<>();
//        for (int a = 2; a <= rowcount; a++) {
//            Date penaltyTimeDate;
//            Date logoutTimeDate;
//            try {
//                Row row = sheet.getRow(a);
//                String company = row.getCell((int) 'C' - 65).getStringCellValue();
//                Fine Fine = new Fine();
//                Fine.setCompanyName(company);
//                String penaltyTime = row.getCell((int) 'G' - 65).getStringCellValue();
//                penaltyTimeDate = dateFormat.parse(penaltyTime);
//                Instant penaltyTimeDateInstant = penaltyTimeDate.toInstant();
//                Fine.setPenaltyTime(penaltyTimeDateInstant);
//                Cell cell = row.getCell((int) 'H' - 65);
//                Cell cellResult = row.getCell((int) 'I' - 65);
//                FineList.add(Fine);
//
//                if (type == 2 && cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
//                    logoutTimeDate = dateFormat.parse(row.getCell((int) 'H' - 65).getStringCellValue());
//                    Instant logoutTimeDateInstant = logoutTimeDate.toInstant();
//                    Fine.setLogoutTime(logoutTimeDateInstant);
//                }
//                if (type == 2 && cellResult != null && cellResult.getCellType() != Cell.CELL_TYPE_BLANK) {
//                    Map<String, Object> result = JSONObject.parseObject(row.getCell((int) 'I' - 65).getStringCellValue(), Map.class);
//                    Fine.setResult(result);
//                }
//                if (type == 2) {
//                    QccJson qccJson = new QccJson();
//                    qccJson.setCompanyName(Fine.getCompanyName())
//                            .setLogoutTime(Fine.getLogoutTime())
//                            .setPenaltyTime(Fine.getPenaltyTime())
//                            .setResult(Fine.getResult());
//                    if (Fine.getLogoutTime() != null && ChronoUnit.MONTHS.between(LocalDateTime.ofInstant(Fine.getPenaltyTime(), ZoneId.systemDefault()), LocalDateTime.ofInstant(Fine.getLogoutTime(), ZoneId.systemDefault())) <= 6) {
//                        qccJson.setQccType(QccJson.QccType.malice);
//                        Fine.setQccType(QccJson.QccType.malice);
//                    } else {
//                        qccJson.setQccType(QccJson.QccType.natural);
//                        Fine.setQccType(QccJson.QccType.natural);
//                    }
//                    UpdateWrapper<QccJson> updateWrapperQccJson = new UpdateWrapper<>();
//                    updateWrapperQccJson.eq("company_name", Fine.getCompanyName());
//                    qccJsonService.saveOrUpdate(qccJson, updateWrapperQccJson);
//                }
//                if (type == 1) {
//                    UpdateWrapper<Fine> updateWrapperFine = new UpdateWrapper<>();
//                    updateWrapperFine.eq("company_name", Fine.getCompanyName());
//                    FineService.saveOrUpdate(Fine, updateWrapperFine);
//                }
//            } catch (NullPointerException n) {
//                throw new RuntimeException("请完善表格");
//            } catch (ParseException e) {
//                logoutTimeDate = null;
//            }
//        }
//        if (type == 2) {
//            FineMapper.delete(null);
//            FineService.saveBatch(FineList);
//        }
//        return FineList;
//    }
public static Date parseDate(String inputDate) {
    Date outputDate = null;
    String[] possibleDateFormats =
            {
                    "yyyy-MM-dd",
                    "yyyy.MM.dd",
                    "yyyy/MM/dd",
                    "yyyy年MM月dd日",
                    "yyyy MM dd"
            };

    try {
        outputDate = DateUtils.parseDate(inputDate, possibleDateFormats);
    } catch (ParseException e) {
        e.printStackTrace();
    }
    return outputDate;
}
}


