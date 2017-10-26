package service;

public class LogRecord {
    public static void infoMsg(Exception ex)
    {
    }
    public static void infoMsg(String msg)
    {
    }
    public static void errorMsg(Exception ex)
    {

    }
    public static void errorMsg(String msg)
    {

    }
    public static void showMsg(Exception ex)
    {
        ShowPrompt.show(ex.getMessage());
    }
    public static void showMsg(String msg)
    {
        ShowPrompt.show(msg);
    }
}
