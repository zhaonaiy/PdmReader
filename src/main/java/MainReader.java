import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.fusesource.jansi.Ansi;

import java.io.File;
import java.util.List;

import static org.fusesource.jansi.Ansi.Color.*;

public class MainReader {

    public static void main(String[] args) throws DocumentException {
        if (args.length < 1) {
            throw new IllegalArgumentException("第一个参数必须是pdm文件路径");
        }
        String fileName = args[0];
        System.out.println(Ansi.ansi().fg(YELLOW).a("pdm file path-->") + fileName);


        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(new File(fileName));
        Element rootElement = document.getRootElement();


        Namespace oNamespace = new Namespace("o", "object");
        Namespace cNamespace = new Namespace("c", "collection");
        Namespace aNamespace = new Namespace("a", "attribute");

        Element rootObject = rootElement.element(new QName("RootObject", oNamespace));

        Element children = rootObject.element(new QName("Children", cNamespace));
//        System.out.println(children);
        Element model = children.element(new QName("Model", oNamespace));
//        System.out.println(model);
        Element tables = model.element(new QName("Tables", cNamespace));
        List<Element> tablelist = tables.elements(new QName("Table", oNamespace));

        System.out.println(Ansi.ansi().fg(YELLOW).a("table size-->" + tablelist.size()));

        System.out.println(Ansi.ansi().fgDefault().a("---start---"));
        int i = 0;
        for (Element element : tablelist) {
            i++;
            Element name = element.element(new QName("Name", aNamespace));
            Element code = element.element(new QName("Code", aNamespace));
            System.out.println();
            System.out.println("--->" + Ansi.ansi().fg(BLUE).a("NO." + i) + Ansi.ansi().fg(RED).a(" " + name.getText() + " ") + Ansi.ansi().fg(YELLOW).a(code.getText()) + Ansi.ansi().fgDefault().a("<---"));
            ;

            List<Element> columns = element.element(new QName("Columns", cNamespace)).elements(new QName("Column", oNamespace));
            for (Element column : columns) {
                Element cname = column.element(new QName("Name", aNamespace));
                Element ccode = column.element(new QName("Code", aNamespace));
                Element cDataType = column.element(new QName("DataType", aNamespace));
                Element cLength = column.element(new QName("Length", aNamespace));

                System.out.print(getPadString(ccode.getText(), 20));
                System.out.print(getPadString(getTextFromEle(cDataType), 15));
                System.out.print(getPadString(getTextFromEle(cLength), 7));
                System.out.print(cname.getText());
                System.out.println();
            }

        }
    }

    static String getTextFromEle(Element element) {
        if (element == null) {
            return "";
        }
        return element.getText();
    }

    /**
     * like pringf()
     */
    static String getPadString(String str, int length) {
        int size = str.length();
        if (size < length) {
            str += getBlank(length - size);
            return str;
        } else
            return str + "  ";
    }


    static String getBlank(int length) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < length; i++) {
            s.append(" ");
        }
        return s.toString();
    }


}
