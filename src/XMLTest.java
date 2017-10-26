import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.FileOutputStream;

public class XMLTest {
    public void test(){
        try {
            Element element= DocumentHelper.createElement("roottest");
            Document document= DocumentHelper.createDocument(element);
            element.addElement("eltest");
            element.addAttribute("id","1");
            element.addNamespace("odc","http://www.opengis.net/sld");
            OutputFormat format = new OutputFormat("    ", true);
            format.setEncoding("utf-8");//设置编码格式
            XMLWriter xmlWriter = new XMLWriter(new FileOutputStream("E:\\test.xml"), format);

            xmlWriter.write(document);
            xmlWriter.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
