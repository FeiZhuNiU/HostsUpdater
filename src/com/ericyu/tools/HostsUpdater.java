package com.ericyu.tools;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 麟 on 2015/2/27.
 */
public class HostsUpdater
{
    private static String url_hosts = "http://www.360kb.com/kb/2_122.html";
    //private static String pattern = "/^(([1-9]|([1-9]\\d)|(1\\d\\d)|(2([0-4]\\d|5[0-5])))\\.)((d|([1-9]\\d)|(1\\d\\d)|(2([0-4]\\d|5[0-5])))\\.){2}([1-9]|([1-9]\\d)|(1\\d\\d)|(2([0-4]\\d|5[0-5])))$/";
    private static String pattern = "((2[0-4]\\d|25[0-5]|1?\\d?\\d)\\.){3}(2[0-4]\\d|25[0-5]|1?\\d?\\d)";
    public static void main(String[] args)
    {
        try
        {
            Document doc = Jsoup.parse(new URL(url_hosts).openConnection().getInputStream(), "UTF-8", url_hosts);
            String addresses = getAddressFromDocument(doc);
            if(addresses == null)
            {
                return;
            }
            UpdateHosts(addresses);

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static void UpdateHosts(String str)
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
            fileWriter = new FileWriter(file);
            fileWriter.write(str);

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

    private static String getAddressFromDocument(Document doc)
    {
        //Elements eles = doc.getElementsByTag("pre");
        Elements eles = doc.getElementsContainingText("www.google.com");
        if(eles.size()==0)
        {
            System.out.println("can not find addresses.( " + eles.size()+" )" );
            return null;
        }
        String ret = eles.last().toString();
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(ret);
        String temp = null;
        if(m.find())
        {
            temp = m.group();
        }
        if(temp == null)
        {
            System.out.println("can not find ip address");
            return null;
        }

        ret= ret.substring(ret.indexOf(temp));
        ret = ret.replace("<br>", " ");
        ret = ret.replace("&nbsp;", " ");

        return ret;
    }
}
