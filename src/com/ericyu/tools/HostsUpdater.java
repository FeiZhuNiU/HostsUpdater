package com.ericyu.tools;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HostsUpdater
{
    private static Map<String,String> urlMap;
    private static String url_hosts_github = "https://github.com/racaljk/hosts/blob/master/hosts";

    static {
        urlMap = new HashMap<String, String>();
        urlMap.put(url_hosts_github,"0.docs.google.com");
    }


    //private static String pattern = "/^(([1-9]|([1-9]\\d)|(1\\d\\d)|(2([0-4]\\d|5[0-5])))\\.)((d|([1-9]\\d)|(1\\d\\d)|(2([0-4]\\d|5[0-5])))\\.){2}([1-9]|([1-9]\\d)|(1\\d\\d)|(2([0-4]\\d|5[0-5])))$/";
    private static String pattern = "((2[0-4]\\d|25[0-5]|1?\\d?\\d)\\.){3}(2[0-4]\\d|25[0-5]|1?\\d?\\d)";
    public static void main(String[] args)
    {
        Iterator iterator = urlMap.keySet().iterator();
        while(iterator.hasNext())
        {
            String url = iterator.next().toString();
            String targetSting = urlMap.get(url);
            try
            {
                Document doc = Jsoup.parse(new URL(url).openConnection().getInputStream(), "UTF-8", url);
                List<String> addresses = getAddressFromDocument(doc);
                UpdateHosts(addresses);

            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private static void UpdateHosts(List<String> str)
    {
        FileWriter fileWriter = null;
        try
        {
            String os = System.getProperties().getProperty("os.name");
            String file  = null;
            if(os.equalsIgnoreCase("Linux"))
                file = "/etc/hosts";
            else
                file = "C:\\Windows\\System32\\drivers\\etc\\hosts";
            fileWriter = new FileWriter(file,false);   // true 表示续写
            for (String s : str) {
                fileWriter.write(s+"\n");
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(fileWriter!=null)
                try
                {
                    fileWriter.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
        }
    }

    private static List<String> getAddressFromDocument(Document doc)
    {
        //Elements eles = doc.getElementsByTag("pre");
        Elements eles = doc.getElementsByClass("blob-code");

        if(eles.size()==0)
        {
            System.out.println("can not find addresses.( " + eles.size()+" )" );
            return null;
        }
        List<String> ret = new ArrayList<String>();
        for (Element ele : eles) {
            ret.add(ele.html());
        }

        return ret;
    }
}
