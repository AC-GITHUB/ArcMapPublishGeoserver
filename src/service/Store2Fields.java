package service;

import java.util.ArrayList;
import java.util.List;

public class Store2Fields
{
    private List<String> al1,al2;
    public Store2Fields()
    {
        al1 = new ArrayList<>();
        al2 = new ArrayList<>();
    }

    public final void addStrings(String str1, String str2)
    {
        al1.add(str1);
        al2.add(str2);
    }

    public final String getString1ByIndex(int index)
    {
        if (al1.size()>index)
        {
            return al1.get(index);
        }
        else
        {
            return "";
        }
    }

    public final String getString2ByIndex(int index)
    {
        if (al2.size() > index)
        {
            return al2.get(index);
        }
        else
        {
            return "";
        }
    }

    public final String getString1ForString2(String str2)
    {
        int index= al2.indexOf(str2);
        if(index>0)
            return al1.get(index);
        return "";
    }

    public final String getString2ForString1(String str1)
    {
        int index= al1.indexOf(str1);
        if(index>0)
            return al2.get(index);
        return "";
    }

    public final List<String> getStringlist1()
    {
        return al1;
    }

    public final List<String> getStringlist2()
    {
        return al2;
    }

    public final int getCount()
    {
        return al1.size();
    }

    public final boolean ContainsString1(String str1)
    {
       return al1.contains(str1);
    }

    public final boolean ContainsString2(String str2)
    {
        return al2.contains(str2);
    }

}
