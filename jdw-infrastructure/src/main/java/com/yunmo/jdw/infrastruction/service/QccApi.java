package com.yunmo.jdw.infrastruction.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunmo.jdw.domian.fine;
import com.yunmo.jdw.domian.QccJson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.client.methods.HttpHead;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.regex.Pattern;

import static java.lang.System.out;


@Service
@Slf4j
public class QccApi {
    private static final String appkey = "c8553feb801f458ea26918398a52935e";
    private static final String secretKey = "5C7ACA696AD2A7BE69FCF7C779E8B97B";

    public List<QccJson> ExcelToQccInsert(List<fine> fineList) {
        List<QccJson> qccJsonList = new ArrayList<>();
        for (fine fine : fineList) {
            String reqInterNme = "http://api.qichacha.com/ECIInfoVerify/GetInfo";
            String paramStr = "searchKey=" + fine.getCompanyName().trim();
            String status = "";
            try {
                HttpHead reqHeader = new HttpHead();
                String[] autherHeader = RandomAuthentHeader();
                reqHeader.setHeader("Token", autherHeader[0]);
                reqHeader.setHeader("Timespan", autherHeader[1]);
                final String reqUri = reqInterNme.concat("?key=").concat(appkey).concat("&").concat(paramStr);
                String tokenJson = HttpHelper.httpGet(reqUri, reqHeader.getAllHeaders());
//                String tokenJson = "{\"Status\":\"200\",\"Message\":\"【有效请求】查询成功\",\"OrderNumber\":\"ECIINFOVERIFY2022063016444296864406\",\"Result\":{\"Partners\":[],\"Employees\":[{\"KeyNo\":\"pbcdbaa2573f6a1e18894324a5785412\",\"Name\":\"许忠其\",\"Job\":\"负责人\"}],\"Branches\":[],\"ChangeRecords\":[],\"ContactInfo\":{\"WebSite\":null,\"PhoneNumber\":null,\"Email\":null},\"Industry\":{\"IndustryCode\":\"C\",\"Industry\":\"制造业\",\"SubIndustryCode\":\"24\",\"SubIndustry\":\"文教、工美、体育和娱乐用品制造业\",\"MiddleCategoryCode\":\"243\",\"MiddleCategory\":\"工艺美术及礼仪用品制造\",\"SmallCategoryCode\":null,\"SmallCategory\":null},\"EmergingIndustyList\":null,\"InsuredCount\":null,\"EnglishName\":\"Jiangyin Jianfeigang Ingot Mould Factory\",\"PersonScope\":\"\",\"IXCode\":null,\"TagList\":[{\"Type\":\"903\",\"Name\":\"注销\"}],\"ARContactList\":null,\"EconKindCodeList\":[\"90\"],\"KeyNo\":\"108bfbca05d1002cec515f3bf471541c\",\"Name\":\"江阴市健飞纲锭模制造厂\",\"No\":\"3202811101701\",\"BelongOrg\":\"江阴市市场监督管理局\",\"OperId\":\"pbcdbaa2573f6a1e18894324a5785412\",\"OperName\":\"许忠其\",\"StartDate\":\"1991-06-18 00:00:00\",\"EndDate\":\"\",\"Status\":\"注销\",\"Province\":\"JS\",\"UpdatedDate\":\"2022-05-27 16:34:43\",\"CreditCode\":\"\",\"RegistCapi\":\"\",\"EconKind\":\"集体所有制\",\"Address\":\"江阴市滨江开发区大河港新屋里33号\",\"Scope\":\"制造、加工钢锭模制造、钢结构件有色金属制造、加工金属材料、注销。\",\"TermStart\":\"\",\"TeamEnd\":\"\",\"CheckDate\":\"2001-05-08 00:00:00\",\"OrgNo\":null,\"IsOnStock\":\"0\",\"StockNumber\":null,\"StockType\":null,\"OriginalName\":[],\"ImageUrl\":\"https://image.qcc.com/auto/108bfbca05d1002cec515f3bf471541c.jpg?x-oss-process=style/logo_120\",\"EntType\":\"0\",\"RecCap\":null,\"RevokeInfo\":{\"CancelDate\":\"2020-07-05\",\"CancelReason\": \"其他原因\",\"RevokeDate\": \"\",\"RevokeReason\": \"\"},\"Area\":{\"Province\":\"江苏省\",\"City\":\"无锡市\",\"County\":\"江阴市\"},\"AreaCode\":\"320281\"}}";
  //              String tokenJson = HttpHelper.httpGet(reqUri, reqHeader.getAllHeaders());
                ObjectMapper mapper = new ObjectMapper();
                status = FormartJson(tokenJson, "Status");
                out.println(String.format("==========================>Status:{%s}", status));
                if (!HttpCodeRegex.isAbnornalRequest(status)) {
                    PrettyPrintJson(tokenJson);
                }
                Map<String, Object> tokenJsonMap = mapper.readValue(tokenJson, Map.class);
                Map qccJsonMap = (Map) tokenJsonMap.get("Result");
                Instant instant = toInstant(tokenJson);
                qccJsonList.add(QccJson.builder()
                        .companyName(fine.getCompanyName())
                        .penaltyTime(fine.getPenaltyTime())
                        .logoutTime(instant)
                        .result(qccJsonMap)
                        .build());
            } catch (Exception e1) {
                log.error(e1.getMessage(), e1);
            }
        }
        return qccJsonList;
    }

    // 获取返回码 Res Code
    static class HttpCodeRegex {
        private static final String ABNORMAL_REGIX = "(101)|(102)";
        private static final Pattern pattern = Pattern.compile(ABNORMAL_REGIX);

        protected static boolean isAbnornalRequest(final String status) {
            return pattern.matcher(status).matches();
        }
    }

    // 获取Auth Code
    protected final String[] RandomAuthentHeader() {
        String timeSpan = String.valueOf(System.currentTimeMillis() / 1000);
        String[] authentHeaders = new String[]{DigestUtils.md5Hex(appkey.concat(timeSpan).concat(secretKey)).toUpperCase(), timeSpan};
        return authentHeaders;
    }

    // 解析JSON
    protected String FormartJson(String jsonString, String key) throws JSONException {
        JSONObject jObject = new JSONObject(jsonString);
        return (String) jObject.get(key);
    }

    // pretty print 返回值
    protected static void PrettyPrintJson(String jsonString) throws JSONException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Object obj = mapper.readValue(jsonString, Object.class);
            String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
            out.println(indented);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public Instant toInstant(String jsonString) throws JsonProcessingException, ParseException {

        ObjectMapper mapper = new ObjectMapper();
        Instant instant=null;
        Map<String, Object> tmpMap = mapper.readValue(jsonString, new TypeReference<>() {
        });
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        try {
        Map CancelDate = (Map) tmpMap.get("Result");
        Map CancelDate1 = (Map) CancelDate.get("RevokeInfo");

        if (CancelDate1 != null) {
            Date date1 = dateFormat.parse((String) CancelDate1.get("CancelDate"));
            instant = date1.toInstant();
        }
        return instant;
    }

}

