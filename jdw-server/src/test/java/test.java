import com.yunmo.jdw.domian.fine;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {

    @Test
    public int aaa() {
//    List strList = new ArrayList();
//    strList.add("123");
//    strList.add("456");
////    for(String str:strList)
////    {
////        System.out.println("----------->>" + str); //可以直接取出
////    }
//        System.out.println("------asassasasasasasasa");
//    System.out.println("----------->>" + strList); //可以直接取出
//        fine fine =new fine();
//        String a= fine.getCompanyName();
//        System.out.println(a);
        // 如引入其他类，需在此自己添加import语句
        return solution( new int[]{1, 2, 3},
                new int[]{2, 2, 3});
    }
    public int solution(int[] hands1, int[] hands2) {
        // 在这⾥写代码
        sort(hands1);
        sort(hands2);
        int h1=0;
        int h2=0;
        int length=hands1.length;
        for(int t=0;t<3;t++){
            h1=h1+hands1[length-1-t];
            h2=h2+hands2[length-1-t];
        }
        if(h1>h2){
            return h1;
        }else{
            return h2;
        }

    }
    public void sort(int[] hands){
        boolean flag= true;
        int temp;
        for(int i=0;i<hands.length-1;i++){
            for(int j=0;j<hands.length-1-i;j++){
                if(hands[j]>hands[j+1]) {
                    temp=hands[j];
                    hands[j]=hands[j+1];
                    hands[j+1]=temp;
                    flag=false;
                }
            }
            if(flag){
                break;//没有交换则退出
            }
        }
    }
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//public class test {
//    //    public static void main(String[] args) {
////        String a="冷自建与被告无锡小天鹅股份有限公司劳动合同纠纷";
////        // String data1=a.substring(a.indexOf(""),a.indexOf("与"))!=null?a.substring(a.indexOf(""),a.indexOf("合同"))!=a.substring(a.indexOf(""),a.indexOf("合"));
////        String p="(.*)与";
////        Pattern r=Pattern.compile(p);
////        Matcher m=r.matcher(a);
////        String t=null,g=null,h=null,j=null;
////        j="ffff";
////        j="aaa";
////        String c=t!=null?t:g!=null?g:h!=null?h:j;
////        System.out.println(a.substring(a.indexOf("被告")+2,a.indexOf("公司")+2));
////        // while(m.find()){
////        // System.out.println(m.group(1));
////        // }
////        // System.out.println(data1);
////    }
//    public static void main(String[] args) {
//        //待解析的JSON字符串
//        String JSONString = "{'name':'卢本伟','age':24,'Hero':{'name':'Fizz','Position':'Mid','charactor':'killer'},'nickNames':['五五开','芦苇','white'],'Honors':[{'year':2011,'name':'TGA总决赛冠军'},{'year':2013,'name':'S3全球总决赛中国区冠军'},{'year':2013,'name':'S3全球总决赛亚军'}]}";
//
//        try {
//
//            JSONObject JSON = new JSONObject(JSONString);      // 第一步，将string变为JSON  这里最外层是{  所以是new JSONObject
//
//            /*
//             * 普通元素，getXxx()直接获取
//             */
//            String name = JSON.getString("name");
//            int age = JSON.getInt("age");
//            System.out.println("姓名：" + name);
//            System.out.println("年龄：" + age);
//            System.out.println("————————————————————————————————");
//
//            /*
//             * 属性大括号包括，先获取JSONObject对象  getJSONObject()  然后进一步getXxx()解析属性
//             */
//            JSONObject hero = JSON.getJSONObject("Hero");
//            String hero_name = hero.getString("name");
//            String hero_position = hero.getString("Position");
//            String hero_charactor = hero.getString("charactor");
//            System.out.println("擅长英雄：");
//            System.out.println("英雄名：" + hero_name);
//            System.out.println("位置：" + hero_position);
//            System.out.println("英雄定位：" + hero_charactor);
//            System.out.println("————————————————————————————————");
//
//            /*
//             * 属性被中括号包括，获取JSONArray对象,getJSONArray  ，然后进一步遍历即可
//             */
//            System.out.println("外号：");
//            JSONArray nickNames = JSON.getJSONArray("nickNames");
//            for (Object nickName : nickNames) {
//                System.out.println(nickName);
//            }
//            System.out.println("————————————————————————————————");
//
//            /*
//             * 属性中既有中括号包括，又嵌套了大括号，一层层获取即可   先getJSONArray，然后getJSONObject，到了属性，就是getXxx()了
//             */
//            JSONArray Honors = JSON.getJSONArray("Honors");
//            System.out.println("所获荣誉：");
//            for (int i = 0; i < Honors.length(); i++) {
//                JSONObject honor = Honors.getJSONObject(i);
//                int honor_year = honor.getInt("year");
//                String honor_name = honor.getString("name");
//                System.out.println(honor_year + " : " + honor_name);
//            }
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }
//}

    @Test
    public void aa1a(){
        String oldNameHead = null;
        Matcher matcher = Pattern.compile("(.*)\\.").matcher("aaaa.xlsx");
        if (matcher.find()) {
            oldNameHead = matcher.group(1);
        }
        System.out.println(oldNameHead);
    }

    @Test
    public void ccc(){
        String beigao=null;
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
            Matcher matcher = Pattern.compile(bg).matcher("梅锡芬与无锡电缆厂有限公司追索劳动报酬纠纷一审调解书");
            if (matcher.find()) {
                beigao = matcher.group(1);
            }
        }
    }
}
