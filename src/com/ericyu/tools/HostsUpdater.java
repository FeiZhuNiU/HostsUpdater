package com.ericyu.tools;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

/**
 * Created by 麟 on 2015/2/27.
 */
public class HostsUpdater
{
    private static String url_hosts = "http://www.360kb.com/kb/2_122.html";

    public static void main(String[] args)
    {
        try
        {
            Document doc = Jsoup.parse(new URL(url_hosts).openConnection().getInputStream(), "UTF-8", url_hosts);
            String addresses = getAddressFromDocument(doc);
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
            fileWriter = new FileWriter("C:\\Windows\\System32\\drivers\\etc\\hosts");
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
        Elements eles = doc.getElementsByTag("pre");
        if(eles.size()!=1)
        {
            System.out.println("the number of elements with tag 'pre' is not current.( " + eles.size()+" )" );
            return null;
        }
        String ret = eles.first().html();
        ret= ret.substring(ret.indexOf("#"));
        return ret;
    }
}
