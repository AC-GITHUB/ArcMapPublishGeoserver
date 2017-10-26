package service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainFormProcess {
    ArcMapSymbols arcMapSymbols;
    String mSLDFilename;
    String mSLDPath;
    String mSLDFile;
    String mSLDTempFilename;
    String mXSDFilename;
    CommonXMLHandle  mobjCommonXML=new CommonXMLHandle();
    List<String> mScaleAl;
    String mHighScale;
    String mLowScale;
    boolean mLabel;
    boolean mTooltip;
    boolean mAllLayers;
    boolean mSeparateFiles;
    boolean chkScale;
    boolean isArcSDE;
    boolean isValidate;
    public boolean isValidate() {
        return isValidate;
    }

    public void setValidate(boolean validate) {
        isValidate = validate;
    }
    public boolean isArcSDE() {
        return isArcSDE;
    }

    public void setArcSDE(boolean arcSDE) {
        isArcSDE = arcSDE;
    }




    public ArcMapSymbols getArcMapSymbols() {
        return arcMapSymbols;
    }

    public void setArcMapSymbols(ArcMapSymbols arcMapSymbols) {
        this.arcMapSymbols = arcMapSymbols;
    }

    public String getmSLDFilename() {
        return mSLDFilename;
    }

    public void setmSLDFilename(String mSLDFilename) {
        this.mSLDFilename = mSLDFilename;
    }

    public String getmSLDPath() {
        return mSLDPath;
    }

    public void setmSLDPath(String mSLDPath) {
        this.mSLDPath = mSLDPath;
    }

    public String getmSLDFile() {
        return mSLDFile;
    }

    public void setmSLDFile(String mSLDFile) {
        this.mSLDFile = mSLDFile;
    }

    public String getmSLDTempFilename() {
        return mSLDTempFilename;
    }

    public void setmSLDTempFilename(String mSLDTempFilename) {
        this.mSLDTempFilename = mSLDTempFilename;
    }

    public String getmXSDFilename() {
        return mXSDFilename;
    }

    public void setmXSDFilename(String mXSDFilename) {
        this.mXSDFilename = mXSDFilename;
    }

    public CommonXMLHandle getMobjCommonXML() {
        return mobjCommonXML;
    }

    public void setMobjCommonXML(CommonXMLHandle mobjCommonXML) {
        this.mobjCommonXML = mobjCommonXML;
    }

    public List<String> getmScaleAl() {
        return mScaleAl;
    }

    public void setmScaleAl(List<String> mScaleAl) {
        this.mScaleAl = mScaleAl;
    }

    public String getmHighScale() {
        return mHighScale;
    }

    public void setmHighScale(String mHighScale) {
        this.mHighScale = mHighScale;
    }

    public String getmLowScale() {
        return mLowScale;
    }

    public void setmLowScale(String mLowScale) {
        this.mLowScale = mLowScale;
    }

    public boolean ismLabel() {
        return mLabel;
    }

    public void setmLabel(boolean mLabel) {
        this.mLabel = mLabel;
    }

    public boolean ismTooltip() {
        return mTooltip;
    }

    public void setmTooltip(boolean mTooltip) {
        this.mTooltip = mTooltip;
    }

    public boolean ismAllLayers() {
        return mAllLayers;
    }

    public void setmAllLayers(boolean mAllLayers) {
        this.mAllLayers = mAllLayers;
    }

    public boolean ismSeparateFiles() {
        return mSeparateFiles;
    }

    public void setmSeparateFiles(boolean mSeparateFiles) {
        this.mSeparateFiles = mSeparateFiles;
    }



    public enum Fileinfo
    {
        Name(0), //The pure name of the file
        Path(1); //the pure path to the file

        private int intValue;
        private static java.util.HashMap<Integer, Fileinfo> mappings;
        private synchronized static java.util.HashMap<Integer, Fileinfo> getMappings()
        {
            if (mappings == null)
            {
                mappings = new java.util.HashMap<Integer, Fileinfo>();
            }
            return mappings;
        }

        private Fileinfo(int value)
        {
            intValue = value;
            Fileinfo.getMappings().put(value, this);
        }

        public int getValue()
        {
            return intValue;
        }

        public static Fileinfo forValue(int value)
        {
            return getMappings().get(value);
        }
    }
    public List<String> SortMe(List<String> al)
    {
        List<String> al2 = new ArrayList<>();
        String cStoreString = "";
        short i = 0;
        short iTmpDigit = 0;
        try
        {
            while (!(al.size() == 1))
            {
                cStoreString = al.get(0);
                iTmpDigit = (short) 0;
                for (i = 1; i <= al.size() - 1; i++)
                {
                    if (Integer.parseInt(cStoreString) > Integer.parseInt(al.get(0)))
                    {
                        cStoreString = al.get(0);
                        iTmpDigit = i;
                    }
                }
                al2.add(cStoreString);
                al.remove(iTmpDigit);
            }
            al2.add(al.get(0));
            return al2;
        }
        catch (RuntimeException ex)
        {
            LogRecord.showMsg(ex);
        }
       return null;
    }
    public String GetFileInfo(String FileName, Fileinfo WhatDoIWant)
    {
        int iLastIndex = 0;
        String cWantedSubstring = "";
        cWantedSubstring = "";

        iLastIndex = FileName.lastIndexOf("\\");
        if (WhatDoIWant == Fileinfo.Name)
        {
            cWantedSubstring = FileName.substring(iLastIndex + 1);
        }
        else if (WhatDoIWant == Fileinfo.Path)
        {
            cWantedSubstring = FileName.substring(0, iLastIndex) + FileName.substring(iLastIndex + FileName.substring(iLastIndex).length());
        }
        return cWantedSubstring;
    }
    public void InitCommonXML()
    {

        try
        {
            mobjCommonXML.setXMLfilename("src\\resources\\Preconfigure_Converter.xml");
            if (mobjCommonXML.OpenDoc() == true)
            {

            }
            else
            {
                LogRecord.
                    infoMsg("The Configuration-file Preconfigure_Converter.xml wasn't found at the application home-directory." + " please copy a Preconfigure_Converter.xml to the application home-directory and restart the application." + "\r\n");

            }
        }
        catch (RuntimeException ex)
        {
           LogRecord.showMsg(new Exception("Fehler beim Einlesen der Vorkonfigurations-XML-Datei",ex));
        }
    }
    private void Preconfigure()
    {
        short i = 0;
        List<String> alTmp = new ArrayList<>();

        try
        {
            if (mobjCommonXML.FindRoot("preconfigure") == true)
            {
                //Ab hier die Ma遱t鋌e
                if (mobjCommonXML.FindSection("scales") == true)
                {
                    mScaleAl =mobjCommonXML.GetAllEntryValues();
                }
            }

            alTmp = SortMe(mScaleAl);
            mScaleAl = null;
            mScaleAl = alTmp;

            //Ab hier der SLD-Dateipfad/Name
            if (mobjCommonXML.FindSection("SLDFilename") == true)
            {
                mSLDTempFilename = mobjCommonXML.getSectionValue();
            }

            //Ab hier die Checkbox Scales
            if (mobjCommonXML.FindSection("Checkboxes") == true)
            {
                if (mobjCommonXML.FindEntry("chkScale") == true)
                {
                    if (mobjCommonXML.getEntryValue().toUpperCase() == "true".toUpperCase())
                    {
                        chkScale = true;
                    }
                    else if (mobjCommonXML.getEntryValue().toUpperCase() == "false".toUpperCase() )
                    {
                        chkScale = false;
                    }
                    else
                    {
                        chkScale = false;
                    }
                }
            }



        }
        catch (Exception ex)
        {
            LogRecord.showMsg(new Exception("Fehler beim Einlesen der Vorkonfigurations-XML-Datei",ex));
        }
    }
    void ReadBackValues()
    {

        try
        {
            //Ab hier der SLD-Dateipfad/Name
            if (mobjCommonXML.FindSection("SLDFilename") == true)
            {
                mobjCommonXML.SetSectionValue(getmSLDFilename());
            }
            //Ab hier die Checkbox Scales
            if (mobjCommonXML.FindSection("Checkboxes") == true)
            {
                if (mobjCommonXML.FindEntry("chkScale") == true)
                {
                    if (chkScale)
                    {
                        mobjCommonXML.SetEntryValue("true");
                    }
                    else
                    {
                        mobjCommonXML.SetEntryValue("false");
                    }
                }
            }
        }
        catch (Exception ex)
        {
            LogRecord.showMsg(new Exception("Fehler beim Auslesen in die Vorkonfigurations-XML-Datei",ex));

        }
    }
    private void LoadCboScalesNew()
    {
        String cHighScale = "";
        String cLowScale = "";
        short i = 0;
        List<String> alTmp = new ArrayList<>();
        cHighScale =getmHighScale();
        cLowScale =getmLowScale();

        try
        {
            if (mobjCommonXML.FindRoot("preconfigure") == true)
            {
                if (mobjCommonXML.FindSection("scales") == true)
                {
                    mScaleAl = mobjCommonXML.GetAllEntryValues();
                }
            }
            alTmp = SortMe(mScaleAl);
            mScaleAl = null;
            mScaleAl = alTmp;
        }
        catch (Exception ex)
        {
            LogRecord.showMsg(new Exception("Fehler beim Einlesen der Vorkonfigurations-XML-Datei",ex));

        }
    }
    public  boolean outputSLDFile(){
       ArcMapSymbols AnalizeArcMap = new ArcMapSymbols(this, mSLDFilename);
       return true;
    }

}
