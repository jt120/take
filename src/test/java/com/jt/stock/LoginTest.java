package com.jt.stock;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kevinsawicki.http.HttpRequest;
import com.jt.stock.model.Stock;
import org.junit.Test;
import org.nutz.dao.Dao;
import org.nutz.dao.impl.NutDao;
import org.nutz.dao.impl.SimpleDataSource;
import org.nutz.dao.util.Daos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by he on 2016/4/28.
 */
public class LoginTest {

    /**
     * Set-Cookie:xqat=93b9123bccf67168e3adb0c07d89b9e1f6cc8db6; domain=.xueqiu.com; path=/; expires=Mon, 23 May 2016 13:36:54 GMT; httpOnly
     Set-Cookie:xq_r_token=8cfa9fd958be66a0a6692ab80219e8eaecef6718; domain=.xueqiu.com; path=/; expires=Mon, 23 May 2016 13:36:54 GMT; httpOnly
     Set-Cookie:xq_is_login=1; domain=xueqiu.com; path=/; expires=Mon, 23 May 2016 13:36:54 GMT
     Set-Cookie:u=2970459786; domain=.xueqiu.com; path=/; expires=Mon, 23 May 2016 13:36:54 GMT; httpOnly
     Set-Cookie:xq_token_expire=Mon%20May%2023%202016%2021%3A36%3A54%20GMT%2B0800%20(CST); domain=.xueqiu.com; path=/; expires=Mon, 23 May 2016 13:36:54 GMT; httpOnly
     Set-Cookie:xq_a_token=93b9123bccf67168e3adb0c07d89b9e1f6cc8db6; domain=.xueqiu.com; path=/; expires=Mon, 23 May 2016 13:36:54 GMT; httpOnlyvv

     * @throws Exception
     */
    @Test
    public void test01() throws Exception {

        String base = "https://xueqiu.com/user/login";
        String param = "areacode=86&username=jt120lz%40gmail.com&password=3E0409E11A5756B971C54CC38035C198&remember_me=on";
        URL url = new URL(base+"?"+param);
        URLConnection conn = url.openConnection();
        conn.connect();
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String s = null;
        while ((s = br.readLine()) != null) {
            System.out.println(s);
        }
        br.close();
        Map<String, List<String>> headers = conn.getHeaderFields();
        headers.forEach((key,value)-> {
            if (key.startsWith("Set-Cookie")) {
                value.forEach(v->System.out.println(v));
            }
        });
    }

    static class MyHttp {
        HttpURLConnection conn;

        MyHttp(HttpURLConnection conn) {
            this.conn = conn;
        }

        public MyHttp build(String req) {
            MyHttp request = null;
            try {
                URL url = new URL(req);
                URLConnection conn = url.openConnection();
                conn.connect();
                request = new MyHttp((HttpURLConnection) conn);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return request;
        }

        public MyHttp addHeader(String key, String value) {
            return null;
        }

    }

    public String request(String req) {
        StringBuilder sb = null;
        try {
            URL url = new URL(req);
            URLConnection conn = url.openConnection();
            conn.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String s = null;
            sb = new StringBuilder();
            while ((s = br.readLine()) != null) {
                sb.append(s);
                sb.append("\n");
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Test
    public void test02() throws Exception {
        //49
        ObjectMapper mapper = new ObjectMapper();
        List<String> ret = new ArrayList<>();
        String url = "https://xueqiu.com/stock/cata/stocklist.json?page=";
        String param = "&size=100&order=desc&orderby=percent&type=11%2C12&_=1461851096446";
        for (int i = 1; i <= 50; i++) {
            String dest = url + i + param;
            HttpRequest req = HttpRequest.get(dest);
            req.header("Cookie", "xq_a_token=93b9123bccf67168e3adb0c07d89b9e1f6cc8db6;");
            String body = req.body();
            ret.add(body);

            JsonNode jsonNode = mapper.readTree(body);
            JsonNode stocks = jsonNode.get("stocks");
            stocks.forEach(st->{
                Stock stock = null;
                try {
                    stock = mapper.readValue(st.toString(), Stock.class);
                    DaoUtil.dao.insert(stock);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
            Thread.sleep(500);
            System.out.println("fetch url " + dest + " result " + body);

        }
        System.out.println("final result---> " + mapper.writeValueAsString(ret));

    }

    @Test
    public void test03() throws Exception {
        String s = "{\"count\":{\"count\":4821},\"success\":\"true\",\"stocks\":[{\"symbol\":\"SZ300507\",\"code\":\"300507\",\"name\":\"N苏奥\",\"current\":\"35.88\",\"percent\":\"43.98\",\"change\":\"10.96\",\"high\":\"35.88\",\"low\":\"32.89\",\"high52w\":\"35.88\",\"low52w\":\"32.89\",\"marketcapital\":\"2.3921196E9\",\"amount\":\"327775.76\",\"pettm\":\"55.35\",\"volume\":\"9177\",\"hasexist\":\"false\"},{\"symbol\":\"SZ002629\",\"code\":\"002629\",\"name\":\"仁智油服\",\"current\":\"15.24\",\"percent\":\"10.04\",\"change\":\"1.39\",\"high\":\"15.24\",\"low\":\"13.77\",\"high52w\":\"20.53\",\"low52w\":\"5.8\",\"marketcapital\":\"6.27808752E9\",\"amount\":\"4.1995728123E8\",\"pettm\":\"\",\"volume\":\"28692259\",\"hasexist\":\"false\"},{\"symbol\":\"SH603029\",\"code\":\"603029\",\"name\":\"天鹅股份\",\"current\":\"15.57\",\"percent\":\"10.04\",\"change\":\"1.42\",\"high\":\"15.57\",\"low\":\"15.57\",\"high52w\":\"15.57\",\"low52w\":\"12.86\",\"marketcapital\":\"1.4533038E9\",\"amount\":\"56068.0\",\"pettm\":\"286.27\",\"volume\":\"3601\",\"hasexist\":\"false\"},{\"symbol\":\"SZ300277\",\"code\":\"300277\",\"name\":\"海联讯\",\"current\":\"35.81\",\"percent\":\"10.02\",\"change\":\"3.26\",\"high\":\"35.81\",\"low\":\"32.25\",\"high52w\":\"49.89\",\"low52w\":\"12.62\",\"marketcapital\":\"4.79854E9\",\"amount\":\"1.3366533866E8\",\"pettm\":\"740.54\",\"volume\":\"3800989\",\"hasexist\":\"false\"},{\"symbol\":\"SZ000099\",\"code\":\"000099\",\"name\":\"中信海直\",\"current\":\"13.51\",\"percent\":\"10.02\",\"change\":\"1.23\",\"high\":\"13.51\",\"low\":\"12.13\",\"high52w\":\"26.9\",\"low52w\":\"9.51\",\"marketcapital\":\"8.1880113742E9\",\"amount\":\"4.9739589024E8\",\"pettm\":\"53.97\",\"volume\":\"38139036\",\"hasexist\":\"false\"},{\"symbol\":\"SZ300508\",\"code\":\"300508\",\"name\":\"维宏股份\",\"current\":\"61.99\",\"percent\":\"10.01\",\"change\":\"5.64\",\"high\":\"61.99\",\"low\":\"61.99\",\"high52w\":\"61.99\",\"low52w\":\"26.51\",\"marketcapital\":\"3.5222718E9\",\"amount\":\"1181405.42\",\"pettm\":\"260.59\",\"volume\":\"19058\",\"hasexist\":\"false\"},{\"symbol\":\"SZ002795\",\"code\":\"002795\",\"name\":\"永和智控\",\"current\":\"23.52\",\"percent\":\"10.01\",\"change\":\"2.14\",\"high\":\"23.52\",\"low\":\"23.52\",\"high52w\":\"23.52\",\"low52w\":\"17.82\",\"marketcapital\":\"2.352E9\",\"amount\":\"136416.0\",\"pettm\":\"78.9\",\"volume\":\"5800\",\"hasexist\":\"false\"},{\"symbol\":\"SZ002684\",\"code\":\"002684\",\"name\":\"猛狮科技\",\"current\":\"27.36\",\"percent\":\"10.01\",\"change\":\"2.49\",\"high\":\"27.36\",\"low\":\"25.1\",\"high52w\":\"47.0\",\"low52w\":\"13.6\",\"marketcapital\":\"8.99290433664E9\",\"amount\":\"5.2692498123E8\",\"pettm\":\"3230.69\",\"volume\":\"19503423\",\"hasexist\":\"false\"},{\"symbol\":\"SZ002097\",\"code\":\"002097\",\"name\":\"山河智能\",\"current\":\"9.01\",\"percent\":\"10.01\",\"change\":\"0.82\",\"high\":\"9.01\",\"low\":\"8.01\",\"high52w\":\"20.5\",\"low52w\":\"6.18\",\"marketcapital\":\"6.80547825E9\",\"amount\":\"3.8944224334E8\",\"pettm\":\"\",\"volume\":\"44918891\",\"hasexist\":\"false\"},{\"symbol\":\"SH603868\",\"code\":\"603868\",\"name\":\"飞科电器\",\"current\":\"61.23\",\"percent\":\"10.01\",\"change\":\"5.57\",\"high\":\"61.23\",\"low\":\"58.35\",\"high52w\":\"61.23\",\"low52w\":\"25.96\",\"marketcapital\":\"2.6671788E10\",\"amount\":\"1.423326879E9\",\"pettm\":\"100.03\",\"volume\":\"23473068\",\"hasexist\":\"false\"},{\"symbol\":\"SH600538\",\"code\":\"600538\",\"name\":\"国发股份\",\"current\":\"11.32\",\"percent\":\"10.01\",\"change\":\"1.03\",\"high\":\"11.32\",\"low\":\"10.18\",\"high52w\":\"19.58\",\"low52w\":\"6.34\",\"marketcapital\":\"5.2570214142E9\",\"amount\":\"2.56642047E8\",\"pettm\":\"1297.05\",\"volume\":\"23892932\",\"hasexist\":\"false\"},{\"symbol\":\"SZ300481\",\"code\":\"300481\",\"name\":\"濮阳惠成\",\"current\":\"67.31\",\"percent\":\"10.0\",\"change\":\"6.12\",\"high\":\"67.31\",\"low\":\"59.9\",\"high52w\":\"68.0\",\"low52w\":\"12.06\",\"marketcapital\":\"5.3848E9\",\"amount\":\"5.1261821189E8\",\"pettm\":\"91.4\",\"volume\":\"7785000\",\"hasexist\":\"false\"},{\"symbol\":\"SZ300097\",\"code\":\"300097\",\"name\":\"智云股份\",\"current\":\"48.29\",\"percent\":\"10.0\",\"change\":\"4.39\",\"high\":\"48.29\",\"low\":\"43.0\",\"high52w\":\"50.0\",\"low52w\":\"21.06\",\"marketcapital\":\"7.13911126555E9\",\"amount\":\"4.6744147166E8\",\"pettm\":\"140.36\",\"volume\":\"10035826\",\"hasexist\":\"false\"},{\"symbol\":\"SZ300073\",\"code\":\"300073\",\"name\":\"当升科技\",\"current\":\"45.32\",\"percent\":\"10.0\",\"change\":\"4.12\",\"high\":\"45.32\",\"low\":\"40.17\",\"high52w\":\"45.32\",\"low52w\":\"15.08\",\"marketcapital\":\"8.2951017864E9\",\"amount\":\"8.7772275122E8\",\"pettm\":\"252.15\",\"volume\":\"20112212\",\"hasexist\":\"false\"},{\"symbol\":\"SZ002793\",\"code\":\"002793\",\"name\":\"东音股份\",\"current\":\"44.75\",\"percent\":\"10.0\",\"change\":\"4.07\",\"high\":\"44.75\",\"low\":\"44.75\",\"high52w\":\"44.75\",\"low52w\":\"14.36\",\"marketcapital\":\"4.475E9\",\"amount\":\"4152576.25\",\"pettm\":\"133.08\",\"volume\":\"92795\",\"hasexist\":\"false\"},{\"symbol\":\"SZ002729\",\"code\":\"002729\",\"name\":\"好利来\",\"current\":\"94.82\",\"percent\":\"10.0\",\"change\":\"8.62\",\"high\":\"94.82\",\"low\":\"85.09\",\"high52w\":\"94.82\",\"low52w\":\"42.0\",\"marketcapital\":\"6.3225976E9\",\"amount\":\"1.9470811214E8\",\"pettm\":\"248.08\",\"volume\":\"2102397\",\"hasexist\":\"false\"},{\"symbol\":\"SZ000665\",\"code\":\"000665\",\"name\":\"湖北广电\",\"current\":\"15.4\",\"percent\":\"10.0\",\"change\":\"1.4\",\"high\":\"15.4\",\"low\":\"15.4\",\"high52w\":\"29.5\",\"low52w\":\"11.75\",\"marketcapital\":\"9.7977486992E9\",\"amount\":\"3.07895434E7\",\"pettm\":\"26.33\",\"volume\":\"1999321\",\"hasexist\":\"false\"},{\"symbol\":\"SH603726\",\"code\":\"603726\",\"name\":\"朗迪集团\",\"current\":\"29.92\",\"percent\":\"10.0\",\"change\":\"2.72\",\"high\":\"29.92\",\"low\":\"29.92\",\"high52w\":\"29.92\",\"low52w\":\"16.89\",\"marketcapital\":\"2.8340224E9\",\"amount\":\"914684.0\",\"pettm\":\"135.57\",\"volume\":\"30571\",\"hasexist\":\"false\"},{\"symbol\":\"SH603701\",\"code\":\"603701\",\"name\":\"德宏股份\",\"current\":\"67.11\",\"percent\":\"10.0\",\"change\":\"6.1\",\"high\":\"67.11\",\"low\":\"65.18\",\"high52w\":\"67.11\",\"low52w\":\"19.44\",\"marketcapital\":\"5.261424E9\",\"amount\":\"5.46247644E8\",\"pettm\":\"131.19\",\"volume\":\"8164656\",\"hasexist\":\"false\"},{\"symbol\":\"SZ300509\",\"code\":\"300509\",\"name\":\"新美星\",\"current\":\"27.86\",\"percent\":\"9.99\",\"change\":\"2.53\",\"high\":\"27.86\",\"low\":\"27.86\",\"high52w\":\"27.86\",\"low52w\":\"17.45\",\"marketcapital\":\"2.2288E9\",\"amount\":\"195020.0\",\"pettm\":\"74.76\",\"volume\":\"7000\",\"hasexist\":\"false\"},{\"symbol\":\"SZ300466\",\"code\":\"300466\",\"name\":\"赛摩电气\",\"current\":\"30.16\",\"percent\":\"9.99\",\"change\":\"2.74\",\"high\":\"30.16\",\"low\":\"28.38\",\"high52w\":\"92.27\",\"low52w\":\"12.3\",\"marketcapital\":\"7.2384E9\",\"amount\":\"5.7877842145E8\",\"pettm\":\"221.74\",\"volume\":\"19550837\",\"hasexist\":\"false\"},{\"symbol\":\"SH600073\",\"code\":\"600073\",\"name\":\"上海梅林\",\"current\":\"11.45\",\"percent\":\"9.99\",\"change\":\"1.04\",\"high\":\"11.45\",\"low\":\"10.78\",\"high52w\":\"19.18\",\"low52w\":\"8.35\",\"marketcapital\":\"1.07370024544E10\",\"amount\":\"2.58606146E8\",\"pettm\":\"83.1\",\"volume\":\"22950735\",\"hasexist\":\"false\"},{\"symbol\":\"SZ300249\",\"code\":\"300249\",\"name\":\"依米康\",\"current\":\"10.58\",\"percent\":\"9.98\",\"change\":\"0.96\",\"high\":\"10.58\",\"low\":\"9.39\",\"high52w\":\"40.25\",\"low52w\":\"9.28\",\"marketcapital\":\"4.65447999926E9\",\"amount\":\"1.0817463345E8\",\"pettm\":\"226.02\",\"volume\":\"10396544\",\"hasexist\":\"false\"},{\"symbol\":\"SZ002106\",\"code\":\"002106\",\"name\":\"莱宝高科\",\"current\":\"10.36\",\"percent\":\"9.98\",\"change\":\"0.94\",\"high\":\"10.36\",\"low\":\"9.56\",\"high52w\":\"16.42\",\"low52w\":\"7.24\",\"marketcapital\":\"7.3122554176E9\",\"amount\":\"6.8684223953E8\",\"pettm\":\"\",\"volume\":\"68534933\",\"hasexist\":\"false\"},{\"symbol\":\"SH603822\",\"code\":\"603822\",\"name\":\"嘉澳环保\",\"current\":\"18.62\",\"percent\":\"9.98\",\"change\":\"1.69\",\"high\":\"18.62\",\"low\":\"18.62\",\"high52w\":\"18.62\",\"low52w\":\"0.0\",\"marketcapital\":\"1.365777E9\",\"amount\":\"40964.0\",\"pettm\":\"172.78\",\"volume\":\"2200\",\"hasexist\":\"false\"},{\"symbol\":\"SH600370\",\"code\":\"600370\",\"name\":\"三房巷\",\"current\":\"15.76\",\"percent\":\"9.98\",\"change\":\"1.43\",\"high\":\"15.76\",\"low\":\"15.0\",\"high52w\":\"22.0\",\"low52w\":\"6.59\",\"marketcapital\":\"5.02582762592E9\",\"amount\":\"1.33063527E8\",\"pettm\":\"236.88\",\"volume\":\"8540225\",\"hasexist\":\"false\"},{\"symbol\":\"SZ002094\",\"code\":\"002094\",\"name\":\"青岛金王\",\"current\":\"24.44\",\"percent\":\"9.69\",\"change\":\"2.16\",\"high\":\"24.5\",\"low\":\"22.0\",\"high52w\":\"32.15\",\"low52w\":\"13.14\",\"marketcapital\":\"7.8676421928E9\",\"amount\":\"2.3792038202E8\",\"pettm\":\"82.53\",\"volume\":\"10057227\",\"hasexist\":\"false\"},{\"symbol\":\"SH600259\",\"code\":\"600259\",\"name\":\"广晟有色\",\"current\":\"56.59\",\"percent\":\"9.39\",\"change\":\"4.86\",\"high\":\"56.9\",\"low\":\"51.91\",\"high52w\":\"73.08\",\"low52w\":\"26.51\",\"marketcapital\":\"1.483352053714E10\",\"amount\":\"6.80639191E8\",\"pettm\":\"\",\"volume\":\"12432920\",\"hasexist\":\"false\"},{\"symbol\":\"SZ300021\",\"code\":\"300021\",\"name\":\"大禹节水\",\"current\":\"18.55\",\"percent\":\"9.18\",\"change\":\"1.56\",\"high\":\"18.69\",\"low\":\"16.77\",\"high52w\":\"26.99\",\"low52w\":\"9.21\",\"marketcapital\":\"5.16803E9\",\"amount\":\"2.4752329366E8\",\"pettm\":\"73.47\",\"volume\":\"13857487\",\"hasexist\":\"false\"},{\"symbol\":\"SZ000881\",\"code\":\"000881\",\"name\":\"大连国际\",\"current\":\"19.27\",\"percent\":\"8.93\",\"change\":\"1.58\",\"high\":\"19.36\",\"low\":\"17.61\",\"high52w\":\"24.88\",\"low52w\":\"13.4\",\"marketcapital\":\"5.952857568E9\",\"amount\":\"3.8126635653E8\",\"pettm\":\"\",\"volume\":\"20375126\",\"hasexist\":\"false\"},{\"symbol\":\"SZ000710\",\"code\":\"000710\",\"name\":\"天兴仪表\",\"current\":\"30.45\",\"percent\":\"8.75\",\"change\":\"2.45\",\"high\":\"30.8\",\"low\":\"27.8\",\"high52w\":\"42.86\",\"low52w\":\"13.28\",\"marketcapital\":\"4.60404E9\",\"amount\":\"1.8860848701E8\",\"pettm\":\"\",\"volume\":\"6368918\",\"hasexist\":\"false\"},{\"symbol\":\"SZ300061\",\"code\":\"300061\",\"name\":\"康耐特\",\"current\":\"28.0\",\"percent\":\"8.4\",\"change\":\"2.17\",\"high\":\"28.0\",\"low\":\"25.0\",\"high52w\":\"37.31\",\"low52w\":\"7.91\",\"marketcapital\":\"6.997754148E9\",\"amount\":\"2.5532551211E8\",\"pettm\":\"127.86\",\"volume\":\"9477299\",\"hasexist\":\"false\"},{\"symbol\":\"SZ002260\",\"code\":\"002260\",\"name\":\"德奥通航\",\"current\":\"27.14\",\"percent\":\"8.13\",\"change\":\"2.04\",\"high\":\"27.61\",\"low\":\"24.7\",\"high52w\":\"63.99\",\"low52w\":\"15.7\",\"marketcapital\":\"7.197528E9\",\"amount\":\"3.217017596E8\",\"pettm\":\"\",\"volume\":\"12164369\",\"hasexist\":\"false\"},{\"symbol\":\"SZ300037\",\"code\":\"300037\",\"name\":\"新宙邦\",\"current\":\"52.11\",\"percent\":\"7.96\",\"change\":\"3.84\",\"high\":\"53.1\",\"low\":\"47.51\",\"high52w\":\"62.59\",\"low52w\":\"21.5\",\"marketcapital\":\"9.58932826524E9\",\"amount\":\"1.00065517896E9\",\"pettm\":\"56.83\",\"volume\":\"19738984\",\"hasexist\":\"false\"},{\"symbol\":\"SZ002618\",\"code\":\"002618\",\"name\":\"丹邦科技\",\"current\":\"42.95\",\"percent\":\"7.48\",\"change\":\"2.99\",\"high\":\"43.88\",\"low\":\"39.45\",\"high52w\":\"62.89\",\"low52w\":\"16.97\",\"marketcapital\":\"7.844388E9\",\"amount\":\"1.6846163184E8\",\"pettm\":\"117.31\",\"volume\":\"4057331\",\"hasexist\":\"false\"},{\"symbol\":\"SH600300\",\"code\":\"600300\",\"name\":\"维维股份\",\"current\":\"5.69\",\"percent\":\"6.95\",\"change\":\"0.37\",\"high\":\"5.7\",\"low\":\"5.2\",\"high52w\":\"15.27\",\"low52w\":\"4.31\",\"marketcapital\":\"9.51368E9\",\"amount\":\"2.53903246E8\",\"pettm\":\"131.17\",\"volume\":\"46177600\",\"hasexist\":\"false\"},{\"symbol\":\"SH603901\",\"code\":\"603901\",\"name\":\"永创智能\",\"current\":\"30.57\",\"percent\":\"6.74\",\"change\":\"1.93\",\"high\":\"31.5\",\"low\":\"28.03\",\"high52w\":\"77.0\",\"low52w\":\"21.7\",\"marketcapital\":\"6.114E9\",\"amount\":\"9.0819156E7\",\"pettm\":\"78.87\",\"volume\":\"3089343\",\"hasexist\":\"false\"},{\"symbol\":\"SH600280\",\"code\":\"600280\",\"name\":\"中央商场\",\"current\":\"7.28\",\"percent\":\"6.74\",\"change\":\"0.46\",\"high\":\"7.37\",\"low\":\"7.02\",\"high52w\":\"41.94\",\"low52w\":\"5.96\",\"marketcapital\":\"8.35987786816E9\",\"amount\":\"1.37586039E8\",\"pettm\":\"136.06\",\"volume\":\"19051467\",\"hasexist\":\"false\"},{\"symbol\":\"SZ002111\",\"code\":\"002111\",\"name\":\"威海广泰\",\"current\":\"25.6\",\"percent\":\"6.71\",\"change\":\"1.61\",\"high\":\"26.2\",\"low\":\"23.53\",\"high52w\":\"44.68\",\"low52w\":\"18.4\",\"marketcapital\":\"9.2439085312E9\",\"amount\":\"2.2538793374E8\",\"pettm\":\"55.42\",\"volume\":\"9010892\",\"hasexist\":\"false\"},{\"symbol\":\"SZ300341\",\"code\":\"300341\",\"name\":\"麦迪电气\",\"current\":\"31.23\",\"percent\":\"6.59\",\"change\":\"1.93\",\"high\":\"31.89\",\"low\":\"28.71\",\"high52w\":\"53.48\",\"low52w\":\"13.71\",\"marketcapital\":\"8.00823822651E9\",\"amount\":\"1.0852997278E8\",\"pettm\":\"82.23\",\"volume\":\"3544476\",\"hasexist\":\"false\"},{\"symbol\":\"SZ000573\",\"code\":\"000573\",\"name\":\"粤宏远A\",\"current\":\"8.35\",\"percent\":\"6.51\",\"change\":\"0.51\",\"high\":\"8.43\",\"low\":\"7.7\",\"high52w\":\"12.31\",\"low52w\":\"3.85\",\"marketcapital\":\"5.2000092934E9\",\"amount\":\"2.6434471165E8\",\"pettm\":\"\",\"volume\":\"32378943\",\"hasexist\":\"false\"},{\"symbol\":\"SZ300449\",\"code\":\"300449\",\"name\":\"汉邦高科\",\"current\":\"100.48\",\"percent\":\"6.44\",\"change\":\"6.08\",\"high\":\"101.68\",\"low\":\"91.3\",\"high52w\":\"276.99\",\"low52w\":\"34.03\",\"marketcapital\":\"7.103936E9\",\"amount\":\"1.7126930429E8\",\"pettm\":\"188.82\",\"volume\":\"1718724\",\"hasexist\":\"false\"},{\"symbol\":\"SZ002035\",\"code\":\"002035\",\"name\":\"华帝股份\",\"current\":\"23.12\",\"percent\":\"6.3\",\"change\":\"1.37\",\"high\":\"23.26\",\"low\":\"21.32\",\"high52w\":\"25.0\",\"low52w\":\"9.38\",\"marketcapital\":\"8.29687330224E9\",\"amount\":\"1.5500731876E8\",\"pettm\":\"37.86\",\"volume\":\"6909777\",\"hasexist\":\"false\"},{\"symbol\":\"SZ002589\",\"code\":\"002589\",\"name\":\"瑞康医药\",\"current\":\"32.63\",\"percent\":\"6.29\",\"change\":\"1.93\",\"high\":\"32.8\",\"low\":\"30.21\",\"high52w\":\"111.1\",\"low52w\":\"23.38\",\"marketcapital\":\"1.809475401344E10\",\"amount\":\"2.440272486E8\",\"pettm\":\"66.92\",\"volume\":\"7640139\",\"hasexist\":\"false\"},{\"symbol\":\"SH601607\",\"code\":\"601607\",\"name\":\"上海医药\",\"current\":\"18.1\",\"percent\":\"6.28\",\"change\":\"1.07\",\"high\":\"18.18\",\"low\":\"17.06\",\"high52w\":\"32.1\",\"low52w\":\"14.61\",\"marketcapital\":\"4.86692807378E10\",\"amount\":\"2.93708325E8\",\"pettm\":\"16.92\",\"volume\":\"16470547\",\"hasexist\":\"false\"},{\"symbol\":\"SZ002034\",\"code\":\"002034\",\"name\":\"美欣达\",\"current\":\"40.9\",\"percent\":\"6.26\",\"change\":\"2.41\",\"high\":\"41.09\",\"low\":\"37.71\",\"high52w\":\"68.53\",\"low52w\":\"19.53\",\"marketcapital\":\"3.432328E9\",\"amount\":\"1.0488365916E8\",\"pettm\":\"79.93\",\"volume\":\"2673385\",\"hasexist\":\"false\"},{\"symbol\":\"SH600549\",\"code\":\"600549\",\"name\":\"厦门钨业\",\"current\":\"23.62\",\"percent\":\"6.21\",\"change\":\"1.38\",\"high\":\"23.73\",\"low\":\"22.06\",\"high52w\":\"42.78\",\"low52w\":\"12.65\",\"marketcapital\":\"2.554677788E10\",\"amount\":\"6.14588729E8\",\"pettm\":\"\",\"volume\":\"26565476\",\"hasexist\":\"false\"},{\"symbol\":\"SZ002023\",\"code\":\"002023\",\"name\":\"海特高新\",\"current\":\"15.42\",\"percent\":\"6.2\",\"change\":\"0.9\",\"high\":\"15.75\",\"low\":\"14.43\",\"high52w\":\"36.45\",\"low52w\":\"11.95\",\"marketcapital\":\"1.166971726626E10\",\"amount\":\"3.3249131826E8\",\"pettm\":\"336.14\",\"volume\":\"21795397\",\"hasexist\":\"false\"},{\"symbol\":\"SH600386\",\"code\":\"600386\",\"name\":\"北巴传媒\",\"current\":\"18.72\",\"percent\":\"6.18\",\"change\":\"1.09\",\"high\":\"19.16\",\"low\":\"17.41\",\"high52w\":\"26.99\",\"low52w\":\"9.02\",\"marketcapital\":\"7.547904E9\",\"amount\":\"2.25513829E8\",\"pettm\":\"51.65\",\"volume\":\"12203482\",\"hasexist\":\"false\"},{\"symbol\":\"SH603369\",\"code\":\"603369\",\"name\":\"今世缘\",\"current\":\"35.81\",\"percent\":\"5.98\",\"change\":\"2.02\",\"high\":\"36.82\",\"low\":\"34.15\",\"high52w\":\"52.97\",\"low52w\":\"23.76\",\"marketcapital\":\"1.7969458E10\",\"amount\":\"2.16013936E8\",\"pettm\":\"25.14\",\"volume\":\"6073145\",\"hasexist\":\"false\"},{\"symbol\":\"SZ200152\",\"code\":\"200152\",\"name\":\"山航B\",\"current\":\"19.49\",\"percent\":\"5.92\",\"change\":\"1.09\",\"high\":\"20.2\",\"low\":\"18.2\",\"high52w\":\"31.2\",\"low52w\":\"12.96\",\"marketcapital\":\"7.796E9\",\"amount\":\"1.3591023E7\",\"pettm\":\"10.13\",\"volume\":\"716600\",\"hasexist\":\"false\"},{\"symbol\":\"SZ002596\",\"code\":\"002596\",\"name\":\"海南瑞泽\",\"current\":\"29.0\",\"percent\":\"5.92\",\"change\":\"1.62\",\"high\":\"29.08\",\"low\":\"27.0\",\"high52w\":\"47.0\",\"low52w\":\"14.09\",\"marketcapital\":\"9.409307694E9\",\"amount\":\"2.4529253559E8\",\"pettm\":\"141.91\",\"volume\":\"8620749\",\"hasexist\":\"false\"},{\"symbol\":\"SH603006\",\"code\":\"603006\",\"name\":\"联明股份\",\"current\":\"45.38\",\"percent\":\"5.88\",\"change\":\"2.52\",\"high\":\"46.29\",\"low\":\"42.06\",\"high52w\":\"66.71\",\"low52w\":\"23.95\",\"marketcapital\":\"4.36822402634E9\",\"amount\":\"1.88975708E8\",\"pettm\":\"37.35\",\"volume\":\"4264640\",\"hasexist\":\"false\"},{\"symbol\":\"SH600898\",\"code\":\"600898\",\"name\":\"三联商社\",\"current\":\"15.8\",\"percent\":\"5.76\",\"change\":\"0.86\",\"high\":\"16.34\",\"low\":\"14.75\",\"high52w\":\"27.85\",\"low52w\":\"8.44\",\"marketcapital\":\"3.989876356E9\",\"amount\":\"2.12276809E8\",\"pettm\":\"182.95\",\"volume\":\"13564877\",\"hasexist\":\"false\"},{\"symbol\":\"SZ300208\",\"code\":\"300208\",\"name\":\"恒顺众昇\",\"current\":\"15.07\",\"percent\":\"5.75\",\"change\":\"0.82\",\"high\":\"15.37\",\"low\":\"14.1\",\"high52w\":\"110.85\",\"low52w\":\"11.4\",\"marketcapital\":\"1.155153175E10\",\"amount\":\"1.8109350839E8\",\"pettm\":\"32.3\",\"volume\":\"12162460\",\"hasexist\":\"false\"},{\"symbol\":\"SZ000601\",\"code\":\"000601\",\"name\":\"韶能股份\",\"current\":\"8.96\",\"percent\":\"5.66\",\"change\":\"0.48\",\"high\":\"9.04\",\"low\":\"8.45\",\"high52w\":\"14.0\",\"low52w\":\"5.68\",\"marketcapital\":\"9.68174295424E9\",\"amount\":\"2.13075063E8\",\"pettm\":\"23.93\",\"volume\":\"24110877\",\"hasexist\":\"false\"},{\"symbol\":\"SZ002134\",\"code\":\"002134\",\"name\":\"天津普林\",\"current\":\"16.35\",\"percent\":\"5.62\",\"change\":\"0.87\",\"high\":\"16.75\",\"low\":\"15.15\",\"high52w\":\"20.98\",\"low52w\":\"7.19\",\"marketcapital\":\"4.0196437068E9\",\"amount\":\"2.187039603E8\",\"pettm\":\"\",\"volume\":\"13575686\",\"hasexist\":\"false\"},{\"symbol\":\"SH600597\",\"code\":\"600597\",\"name\":\"光明乳业\",\"current\":\"11.83\",\"percent\":\"5.53\",\"change\":\"0.62\",\"high\":\"12.08\",\"low\":\"11.11\",\"high52w\":\"28.7\",\"low52w\":\"9.9\",\"marketcapital\":\"1.455843262237E10\",\"amount\":\"1.96320213E8\",\"pettm\":\"34.8\",\"volume\":\"16786494\",\"hasexist\":\"false\"},{\"symbol\":\"SZ002490\",\"code\":\"002490\",\"name\":\"山东墨龙\",\"current\":\"9.27\",\"percent\":\"5.46\",\"change\":\"0.48\",\"high\":\"9.45\",\"low\":\"8.74\",\"high52w\":\"18.0\",\"low52w\":\"6.5\",\"marketcapital\":\"7.396054668E9\",\"amount\":\"2.2068422422E8\",\"pettm\":\"\",\"volume\":\"24109631\",\"hasexist\":\"false\"},{\"symbol\":\"SZ002234\",\"code\":\"002234\",\"name\":\"民和股份\",\"current\":\"26.91\",\"percent\":\"5.45\",\"change\":\"1.39\",\"high\":\"27.5\",\"low\":\"24.96\",\"high52w\":\"30.0\",\"low52w\":\"8.32\",\"marketcapital\":\"8.12807486712E9\",\"amount\":\"1.3656042755E8\",\"pettm\":\"\",\"volume\":\"5204508\",\"hasexist\":\"false\"},{\"symbol\":\"SH600338\",\"code\":\"600338\",\"name\":\"西藏珠峰\",\"current\":\"25.91\",\"percent\":\"5.45\",\"change\":\"1.34\",\"high\":\"26.1\",\"low\":\"24.28\",\"high52w\":\"43.2\",\"low52w\":\"11.25\",\"marketcapital\":\"1.691941818433E10\",\"amount\":\"2.15821285E8\",\"pettm\":\"56.63\",\"volume\":\"8415933\",\"hasexist\":\"false\"},{\"symbol\":\"SH600199\",\"code\":\"600199\",\"name\":\"金种子酒\",\"current\":\"8.83\",\"percent\":\"5.37\",\"change\":\"0.45\",\"high\":\"9.01\",\"low\":\"8.42\",\"high52w\":\"16.88\",\"low52w\":\"6.68\",\"marketcapital\":\"4.90749326766E9\",\"amount\":\"1.85506382E8\",\"pettm\":\"94.23\",\"volume\":\"21270223\",\"hasexist\":\"false\"},{\"symbol\":\"SZ300233\",\"code\":\"300233\",\"name\":\"金城医药\",\"current\":\"25.0\",\"percent\":\"5.35\",\"change\":\"1.27\",\"high\":\"26.09\",\"low\":\"24.1\",\"high52w\":\"119.65\",\"low52w\":\"18.0\",\"marketcapital\":\"6.33E9\",\"amount\":\"1.2914087217E8\",\"pettm\":\"36.47\",\"volume\":\"5122044\",\"hasexist\":\"false\"},{\"symbol\":\"SZ002443\",\"code\":\"002443\",\"name\":\"金洲管道\",\"current\":\"12.2\",\"percent\":\"5.35\",\"change\":\"0.62\",\"high\":\"12.22\",\"low\":\"11.45\",\"high52w\":\"20.35\",\"low52w\":\"8.43\",\"marketcapital\":\"6.350533344E9\",\"amount\":\"3.0048258938E8\",\"pettm\":\"66.4\",\"volume\":\"25260445\",\"hasexist\":\"false\"},{\"symbol\":\"SZ300127\",\"code\":\"300127\",\"name\":\"银河磁体\",\"current\":\"16.53\",\"percent\":\"5.29\",\"change\":\"0.83\",\"high\":\"17.27\",\"low\":\"15.6\",\"high52w\":\"26.2\",\"low52w\":\"9.3\",\"marketcapital\":\"5.3416093308E9\",\"amount\":\"2.9339375926E8\",\"pettm\":\"54.93\",\"volume\":\"17581495\",\"hasexist\":\"false\"},{\"symbol\":\"SZ300260\",\"code\":\"300260\",\"name\":\"新莱应材\",\"current\":\"29.57\",\"percent\":\"5.23\",\"change\":\"1.47\",\"high\":\"29.77\",\"low\":\"27.82\",\"high52w\":\"51.14\",\"low52w\":\"16.32\",\"marketcapital\":\"2.9584785E9\",\"amount\":\"6.038578638E7\",\"pettm\":\"4197.61\",\"volume\":\"2093115\",\"hasexist\":\"false\"},{\"symbol\":\"SZ002358\",\"code\":\"002358\",\"name\":\"森源电气\",\"current\":\"16.91\",\"percent\":\"5.23\",\"change\":\"0.84\",\"high\":\"17.1\",\"low\":\"15.84\",\"high52w\":\"72.3\",\"low52w\":\"12.05\",\"marketcapital\":\"1.345351970208E10\",\"amount\":\"3.4823082389E8\",\"pettm\":\"45.74\",\"volume\":\"20941268\",\"hasexist\":\"false\"},{\"symbol\":\"SZ000668\",\"code\":\"000668\",\"name\":\"荣丰控股\",\"current\":\"27.2\",\"percent\":\"5.18\",\"change\":\"1.34\",\"high\":\"28.28\",\"low\":\"25.55\",\"high52w\":\"34.79\",\"low52w\":\"16.97\",\"marketcapital\":\"3.994099408E9\",\"amount\":\"1.8438298547E8\",\"pettm\":\"139.93\",\"volume\":\"6842358\",\"hasexist\":\"false\"},{\"symbol\":\"SZ200596\",\"code\":\"200596\",\"name\":\"古井贡B\",\"current\":\"25.8\",\"percent\":\"5.13\",\"change\":\"1.26\",\"high\":\"25.86\",\"low\":\"24.6\",\"high52w\":\"32.98\",\"low52w\":\"19.02\",\"marketcapital\":\"1.299288E10\",\"amount\":\"1.677421152E7\",\"pettm\":\"14.32\",\"volume\":\"654386\",\"hasexist\":\"false\"},{\"symbol\":\"SZ002632\",\"code\":\"002632\",\"name\":\"道明光学\",\"current\":\"23.76\",\"percent\":\"5.13\",\"change\":\"1.16\",\"high\":\"24.17\",\"low\":\"22.22\",\"high52w\":\"35.66\",\"low52w\":\"10.84\",\"marketcapital\":\"7.02964586016E9\",\"amount\":\"1.9352032338E8\",\"pettm\":\"153.01\",\"volume\":\"8177396\",\"hasexist\":\"false\"},{\"symbol\":\"SZ000692\",\"code\":\"000692\",\"name\":\"惠天热电\",\"current\":\"5.99\",\"percent\":\"5.09\",\"change\":\"0.29\",\"high\":\"6.1\",\"low\":\"5.65\",\"high52w\":\"14.5\",\"low52w\":\"4.56\",\"marketcapital\":\"3.19166952624E9\",\"amount\":\"1.6773448251E8\",\"pettm\":\"97.16\",\"volume\":\"28402381\",\"hasexist\":\"false\"},{\"symbol\":\"SH600866\",\"code\":\"600866\",\"name\":\"*ST星湖\",\"current\":\"7.11\",\"percent\":\"5.02\",\"change\":\"0.34\",\"high\":\"7.11\",\"low\":\"6.65\",\"high52w\":\"9.65\",\"low52w\":\"4.81\",\"marketcapital\":\"4.58874753615E9\",\"amount\":\"1.05927403E8\",\"pettm\":\"\",\"volume\":\"15095061\",\"hasexist\":\"false\"},{\"symbol\":\"SH600793\",\"code\":\"600793\",\"name\":\"ST宜纸\",\"current\":\"31.65\",\"percent\":\"5.01\",\"change\":\"1.51\",\"high\":\"31.65\",\"low\":\"30.03\",\"high52w\":\"31.65\",\"low52w\":\"16.2\",\"marketcapital\":\"3.332745E9\",\"amount\":\"3.7229686E7\",\"pettm\":\"375.45\",\"volume\":\"1188289\",\"hasexist\":\"false\"},{\"symbol\":\"SZ000403\",\"code\":\"000403\",\"name\":\"ST生化\",\"current\":\"30.66\",\"percent\":\"5.0\",\"change\":\"1.46\",\"high\":\"30.66\",\"low\":\"28.73\",\"high52w\":\"32.94\",\"low52w\":\"20.5\",\"marketcapital\":\"8.35722918534E9\",\"amount\":\"1.5306020478E8\",\"pettm\":\"107.75\",\"volume\":\"5097485\",\"hasexist\":\"false\"},{\"symbol\":\"SH600773\",\"code\":\"600773\",\"name\":\"西藏城投\",\"current\":\"14.73\",\"percent\":\"4.99\",\"change\":\"0.7\",\"high\":\"14.97\",\"low\":\"13.75\",\"high52w\":\"25.2\",\"low52w\":\"9.68\",\"marketcapital\":\"1.074131725599E10\",\"amount\":\"5.5528298E8\",\"pettm\":\"172.06\",\"volume\":\"38505692\",\"hasexist\":\"false\"},{\"symbol\":\"SZ300346\",\"code\":\"300346\",\"name\":\"南大光电\",\"current\":\"25.68\",\"percent\":\"4.94\",\"change\":\"1.21\",\"high\":\"26.5\",\"low\":\"24.11\",\"high52w\":\"72.02\",\"low52w\":\"19.52\",\"marketcapital\":\"4.13098752E9\",\"amount\":\"1.084928699E8\",\"pettm\":\"114.47\",\"volume\":\"4275082\",\"hasexist\":\"false\"},{\"symbol\":\"SH600983\",\"code\":\"600983\",\"name\":\"惠而浦\",\"current\":\"10.7\",\"percent\":\"4.9\",\"change\":\"0.5\",\"high\":\"11.22\",\"low\":\"10.3\",\"high52w\":\"20.6\",\"low52w\":\"8.46\",\"marketcapital\":\"8.2008973E9\",\"amount\":\"1.36553584E8\",\"pettm\":\"21.39\",\"volume\":\"12628166\",\"hasexist\":\"false\"},{\"symbol\":\"SZ000860\",\"code\":\"000860\",\"name\":\"顺鑫农业\",\"current\":\"20.32\",\"percent\":\"4.85\",\"change\":\"0.94\",\"high\":\"20.65\",\"low\":\"19.11\",\"high52w\":\"30.75\",\"low52w\":\"13.77\",\"marketcapital\":\"1.159438863744E10\",\"amount\":\"4.2100364102E8\",\"pettm\":\"32.25\",\"volume\":\"20893416\",\"hasexist\":\"false\"},{\"symbol\":\"SH600854\",\"code\":\"600854\",\"name\":\"春兰股份\",\"current\":\"7.56\",\"percent\":\"4.85\",\"change\":\"0.35\",\"high\":\"7.66\",\"low\":\"7.08\",\"high52w\":\"14.0\",\"low52w\":\"5.18\",\"marketcapital\":\"3.92710654728E9\",\"amount\":\"1.21465783E8\",\"pettm\":\"116.83\",\"volume\":\"16300695\",\"hasexist\":\"false\"},{\"symbol\":\"SZ002027\",\"code\":\"002027\",\"name\":\"分众传媒\",\"current\":\"32.81\",\"percent\":\"4.82\",\"change\":\"1.51\",\"high\":\"33.0\",\"low\":\"30.71\",\"high52w\":\"62.34\",\"low52w\":\"11.82\",\"marketcapital\":\"1.433277535675E11\",\"amount\":\"3.691250426E8\",\"pettm\":\"42.29\",\"volume\":\"11525401\",\"hasexist\":\"false\"},{\"symbol\":\"SZ300438\",\"code\":\"300438\",\"name\":\"鹏辉能源\",\"current\":\"110.8\",\"percent\":\"4.75\",\"change\":\"5.02\",\"high\":\"114.88\",\"low\":\"103.81\",\"high52w\":\"191.7\",\"low52w\":\"23.55\",\"marketcapital\":\"9.3072E9\",\"amount\":\"2.737119085E8\",\"pettm\":\"106.63\",\"volume\":\"2491000\",\"hasexist\":\"false\"},{\"symbol\":\"SZ002553\",\"code\":\"002553\",\"name\":\"南方轴承\",\"current\":\"13.02\",\"percent\":\"4.75\",\"change\":\"0.59\",\"high\":\"13.25\",\"low\":\"12.5\",\"high52w\":\"28.65\",\"low52w\":\"7.87\",\"marketcapital\":\"4.53096E9\",\"amount\":\"2.1068533782E8\",\"pettm\":\"72.69\",\"volume\":\"16311646\",\"hasexist\":\"false\"},{\"symbol\":\"SZ002299\",\"code\":\"002299\",\"name\":\"圣农发展\",\"current\":\"24.02\",\"percent\":\"4.71\",\"change\":\"1.08\",\"high\":\"24.5\",\"low\":\"22.56\",\"high52w\":\"27.4\",\"low52w\":\"13.49\",\"marketcapital\":\"2.6683818E10\",\"amount\":\"1.5880726428E8\",\"pettm\":\"\",\"volume\":\"6721035\",\"hasexist\":\"false\"},{\"symbol\":\"SZ300241\",\"code\":\"300241\",\"name\":\"瑞丰光电\",\"current\":\"16.15\",\"percent\":\"4.6\",\"change\":\"0.71\",\"high\":\"16.17\",\"low\":\"15.14\",\"high52w\":\"21.89\",\"low52w\":\"8.81\",\"marketcapital\":\"4.0695189254E9\",\"amount\":\"8.481363555E7\",\"pettm\":\"98.32\",\"volume\":\"5348407\",\"hasexist\":\"false\"},{\"symbol\":\"SH600262\",\"code\":\"600262\",\"name\":\"北方股份\",\"current\":\"29.06\",\"percent\":\"4.57\",\"change\":\"1.27\",\"high\":\"29.68\",\"low\":\"27.4\",\"high52w\":\"56.45\",\"low52w\":\"18.59\",\"marketcapital\":\"4.9402E9\",\"amount\":\"6.9399159E7\",\"pettm\":\"\",\"volume\":\"2391608\",\"hasexist\":\"false\"},{\"symbol\":\"SZ000669\",\"code\":\"000669\",\"name\":\"金鸿能源\",\"current\":\"16.86\",\"percent\":\"4.53\",\"change\":\"0.73\",\"high\":\"17.49\",\"low\":\"15.93\",\"high52w\":\"37.98\",\"low52w\":\"14.86\",\"marketcapital\":\"8.19406594824E9\",\"amount\":\"2.2303666175E8\",\"pettm\":\"33.11\",\"volume\":\"13367813\",\"hasexist\":\"false\"},{\"symbol\":\"SH603026\",\"code\":\"603026\",\"name\":\"石大胜华\",\"current\":\"33.7\",\"percent\":\"4.53\",\"change\":\"1.46\",\"high\":\"34.92\",\"low\":\"31.52\",\"high52w\":\"46.78\",\"low52w\":\"7.81\",\"marketcapital\":\"6.830316E9\",\"amount\":\"4.24284649E8\",\"pettm\":\"112.93\",\"volume\":\"12600519\",\"hasexist\":\"false\"},{\"symbol\":\"SZ000958\",\"code\":\"000958\",\"name\":\"东方能源\",\"current\":\"15.5\",\"percent\":\"4.52\",\"change\":\"0.67\",\"high\":\"15.75\",\"low\":\"14.66\",\"high52w\":\"44.96\",\"low52w\":\"11.8\",\"marketcapital\":\"8.5426175015E9\",\"amount\":\"2.9284274469E8\",\"pettm\":\"19.18\",\"volume\":\"19076276\",\"hasexist\":\"false\"},{\"symbol\":\"SZ002769\",\"code\":\"002769\",\"name\":\"普路通\",\"current\":\"75.32\",\"percent\":\"4.5\",\"change\":\"3.24\",\"high\":\"76.42\",\"low\":\"71.18\",\"high52w\":\"141.44\",\"low52w\":\"37.61\",\"marketcapital\":\"1.134763271656E10\",\"amount\":\"3.5846009676E8\",\"pettm\":\"60.12\",\"volume\":\"4779527\",\"hasexist\":\"false\"},{\"symbol\":\"SZ002637\",\"code\":\"002637\",\"name\":\"赞宇科技\",\"current\":\"24.54\",\"percent\":\"4.47\",\"change\":\"1.05\",\"high\":\"24.85\",\"low\":\"22.99\",\"high52w\":\"37.9\",\"low52w\":\"13.01\",\"marketcapital\":\"3.9264E9\",\"amount\":\"1.3764552554E8\",\"pettm\":\"207.14\",\"volume\":\"5718663\",\"hasexist\":\"false\"},{\"symbol\":\"SZ000070\",\"code\":\"000070\",\"name\":\"特发信息\",\"current\":\"25.45\",\"percent\":\"4.47\",\"change\":\"1.09\",\"high\":\"25.78\",\"low\":\"24.25\",\"high52w\":\"36.8\",\"low52w\":\"15.35\",\"marketcapital\":\"7.97850814285E9\",\"amount\":\"1.4577585735E8\",\"pettm\":\"86.78\",\"volume\":\"5818047\",\"hasexist\":\"false\"},{\"symbol\":\"SZ002520\",\"code\":\"002520\",\"name\":\"日发精机\",\"current\":\"19.19\",\"percent\":\"4.41\",\"change\":\"0.81\",\"high\":\"19.78\",\"low\":\"18.36\",\"high52w\":\"50.07\",\"low52w\":\"14.48\",\"marketcapital\":\"7.08864487674E9\",\"amount\":\"9.864885131E7\",\"pettm\":\"174.33\",\"volume\":\"5135382\",\"hasexist\":\"false\"},{\"symbol\":\"SZ002643\",\"code\":\"002643\",\"name\":\"万润股份\",\"current\":\"47.71\",\"percent\":\"4.4\",\"change\":\"2.01\",\"high\":\"49.48\",\"low\":\"45.1\",\"high52w\":\"50.18\",\"low52w\":\"24.33\",\"marketcapital\":\"1.62152573375E10\",\"amount\":\"4.6792099884E8\",\"pettm\":\"50.06\",\"volume\":\"9935811\",\"hasexist\":\"false\"},{\"symbol\":\"SZ002108\",\"code\":\"002108\",\"name\":\"沧州明珠\",\"current\":\"19.58\",\"percent\":\"4.32\",\"change\":\"0.81\",\"high\":\"20.05\",\"low\":\"18.51\",\"high52w\":\"33.36\",\"low52w\":\"8.0\",\"marketcapital\":\"1.210940897144E10\",\"amount\":\"1.38566767046E9\",\"pettm\":\"42.2\",\"volume\":\"70911875\",\"hasexist\":\"false\"},{\"symbol\":\"SZ300394\",\"code\":\"300394\",\"name\":\"天孚通信\",\"current\":\"92.0\",\"percent\":\"4.31\",\"change\":\"3.8\",\"high\":\"93.6\",\"low\":\"87.1\",\"high52w\":\"142.77\",\"low52w\":\"42.35\",\"marketcapital\":\"6.83928E9\",\"amount\":\"1.7559033583E8\",\"pettm\":\"62.55\",\"volume\":\"1924519\",\"hasexist\":\"false\"},{\"symbol\":\"SH603998\",\"code\":\"603998\",\"name\":\"方盛制药\",\"current\":\"44.56\",\"percent\":\"4.31\",\"change\":\"1.84\",\"high\":\"45.21\",\"low\":\"42.38\",\"high52w\":\"65.11\",\"low52w\":\"28.0\",\"marketcapital\":\"6.3155886144E9\",\"amount\":\"1.73585946E8\",\"pettm\":\"73.65\",\"volume\":\"3933375\",\"hasexist\":\"false\"},{\"symbol\":\"SZ000333\",\"code\":\"000333\",\"name\":\"美的集团\",\"current\":\"32.1\",\"percent\":\"4.29\",\"change\":\"1.32\",\"high\":\"32.26\",\"low\":\"31.02\",\"high52w\":\"41.49\",\"low52w\":\"23.33\",\"marketcapital\":\"1.369832584188E11\",\"amount\":\"8.0980097431E8\",\"pettm\":\"10.78\",\"volume\":\"25404055\",\"hasexist\":\"false\"},{\"symbol\":\"SH603718\",\"code\":\"603718\",\"name\":\"海利生物\",\"current\":\"41.6\",\"percent\":\"4.29\",\"change\":\"1.71\",\"high\":\"42.2\",\"low\":\"39.8\",\"high52w\":\"66.03\",\"low52w\":\"9.81\",\"marketcapital\":\"1.1648E10\",\"amount\":\"1.35135279E8\",\"pettm\":\"123.16\",\"volume\":\"3252470\",\"hasexist\":\"false\"},{\"symbol\":\"SZ002688\",\"code\":\"002688\",\"name\":\"金河生物\",\"current\":\"24.61\",\"percent\":\"4.28\",\"change\":\"1.01\",\"high\":\"25.48\",\"low\":\"23.31\",\"high52w\":\"29.7\",\"low52w\":\"11.88\",\"marketcapital\":\"6.25379136382E9\",\"amount\":\"2.080958191E8\",\"pettm\":\"47.52\",\"volume\":\"8476095\",\"hasexist\":\"false\"},{\"symbol\":\"SZ002733\",\"code\":\"002733\",\"name\":\"雄韬股份\",\"current\":\"25.02\",\"percent\":\"4.25\",\"change\":\"1.02\",\"high\":\"25.48\",\"low\":\"23.5\",\"high52w\":\"46.68\",\"low52w\":\"11.68\",\"marketcapital\":\"7.65612E9\",\"amount\":\"3.7687139722E8\",\"pettm\":\"56.58\",\"volume\":\"15180027\",\"hasexist\":\"false\"}]}";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(s);
        List<Stock> list = new ArrayList<>();
        JsonNode stocks = node.get("stocks");
        stocks.forEach(st -> {
            try {
//                JsonNode symbol = st.get("symbol");
//                System.out.println(symbol.toString());
                Stock stock = mapper.readValue(st.toString(), Stock.class);
//                DaoUtil.dao.insert(stock);
                list.add(stock);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        System.out.println(list.size());
    }

    static class DaoUtil {
        static SimpleDataSource dataSource = new SimpleDataSource();
        static {
            dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/take");
            dataSource.setUsername("root");
            dataSource.setPassword("root");
        }
        static Dao dao = new NutDao(dataSource);
    }

    @Test
    public void test04() throws Exception {
        SimpleDataSource dataSource = new SimpleDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/take");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        Dao dao = new NutDao(dataSource);
        Daos.createTablesInPackage(dao, "com.jt.stock.model", false);

    }

    @Test
    public void test05() throws Exception {

        HttpRequest request = HttpRequest.get("http://push3.gtimg.cn/q=sz300507,sz002629,sh603029,sz000099,sz300277,sz002684,sh600538,sz002097,sz002795,sh603868,sz300508,sz002729,sz000665,sz300073,sz300481,sz300097,sz002793,sh603701,sh603726,sh600073,sz300466,sz300509,sz002106,sh603822,sz300249&m=push&r=864212903");
        String body = request.body();
        System.out.println(body);
    }

    /**
     * {
     "symbol": "SZ002688",
     "code": "002688",
     "name": "金河生物",
     "current": "24.61",
     "percent": "4.28",
     "change": "1.01",
     "high": "25.48",
     "low": "23.31",
     "high52w": "29.7",
     "low52w": "11.88",
     "marketcapital": "6.25379136382E9",
     "amount": "2.080958191E8",
     "pettm": "47.52",
     "volume": "8476095",
     "hasexist": "false"
     },
     */
}
