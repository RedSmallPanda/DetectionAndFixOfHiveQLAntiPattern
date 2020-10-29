package bias_check;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.xml.xpath.XPath;
import java.io.IOException;

public class memorysetting_check {
    public static void main(String [] args){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String hadoop_url="http://202.120.40.28:50087/";
        HttpGet httpGet = new HttpGet("http://202.120.40.28:50087/jobhistory/job/job_1603857704538_0001");
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        String result="";
        HttpEntity entity = response.getEntity();

        try {
            if(entity != null)
                result = EntityUtils.toString(entity);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(response != null)
                    response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        System.out.println(result);
        String magic="class=\"logslink\" href=\"";
        int logurl_idx=result.indexOf(magic)+magic.length();
        String tmp=result.substring(logurl_idx);
        String logurl_raw=tmp.substring(0,tmp.indexOf("\""));
        System.out.println(logurl_raw);
        String logurl=hadoop_url+logurl_raw+"/syslog/?start=0";

        HttpGet log_httpGet = new HttpGet(logurl);
        CloseableHttpResponse log_response = null;
        try {
            log_response = httpClient.execute(log_httpGet);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        String logs="";
        HttpEntity log_entity = log_response.getEntity();

        try {
            if(entity != null)
                logs = EntityUtils.toString(log_entity);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(log_response != null)
                    log_response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(logs);
        if(logs.indexOf("running beyond physical memory limits")>0 | logs.indexOf("running beyond virtual memory limits")>0){
            System.out.println("mapreduce.map.memory.mb或mapreduce.reduce.memory.mb设置不合理");
        }
        else {
            System.out.println("内存设置合理");
        }
    }
}
