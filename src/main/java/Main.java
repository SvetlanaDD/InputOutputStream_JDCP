import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException,
            ParserConfigurationException, SAXException {

        Scanner scanner = new Scanner(System.in);
        String[] products = {"Хлеб", "Яблоки", "Молоко"};
        int[] prices = {50, 150, 100};
        int[] count = {0, 0, 0};
        Basket basket = new Basket(products, prices);
        basket.setCount(count);

        ClientLog log = new ClientLog(new ArrayList<>(), new ArrayList<>());

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document docConfig = builder.parse(new File("shop.xml"));

        // проверка конфигурации загрузки корзины
        String fileNameBasket;
        NodeList nodeList = docConfig.getElementsByTagName("load");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (Node.ELEMENT_NODE == node.getNodeType()) {
                Element element = (Element) node;
                Node nodeEnabled = element.getElementsByTagName("enabled").item(0).getChildNodes().item(0);
                Node nodeFileName = element.getElementsByTagName("fileName").item(0).getChildNodes().item(0);
                Node nodeFormat = element.getElementsByTagName("format").item(0).getChildNodes().item(0);

                if (nodeEnabled.getNodeValue().equals("true")) {
                    fileNameBasket = nodeFileName.getNodeValue();
                    File file = new File(fileNameBasket);
                    if (file.exists()) {
                        if (nodeFormat.getNodeValue().equals("json")) {
                            basket = Basket.loadFromJSONFile(file);
                            System.out.println("Есть сохраненная корзина");
                            basket.printCart();
                        } else {
                            basket = Basket.loadFromTxtFile(file);
                        }
                    }
                }
            }
        }

        basket.printListProducts();
        while (true) {
            System.out.println("Введите товар и кол-во или введите 'end'");
            String input = scanner.nextLine();
            if ("end".equals(input)) {
                break;
            }
            String[] parts = input.split(" ");
            int productNum = Integer.parseInt(parts[0]) - 1;
            int amount = Integer.parseInt(parts[1]);
            basket.addToCart(productNum, amount);
            log.log(productNum, amount);

            //проверка конфигурации сохранения корзины
            NodeList nodeListSv = docConfig.getElementsByTagName("save");
            for (int i = 0; i < nodeListSv.getLength(); i++) {
                Node node = nodeListSv.item(i);
                if (Node.ELEMENT_NODE == node.getNodeType()) {
                    Element element = (Element) node;
                    Node nodeEnabled = element.getElementsByTagName("enabled").item(0).getChildNodes().item(0);
                    Node nodeFileName = element.getElementsByTagName("fileName").item(0).getChildNodes().item(0);
                    Node nodeFormat = element.getElementsByTagName("format").item(0).getChildNodes().item(0);
                    if (nodeEnabled.getNodeValue().equals("true")) {
                        fileNameBasket = nodeFileName.getNodeValue();
                        File file = new File(fileNameBasket);
                        if (nodeFormat.getNodeValue().equals("json")) {
                            basket.saveJSON(file);
                        } else {
                            basket.saveTxt(file);
                        }
                    }
                }
            }
        }
        basket.printCart();

        //проверка конфигурации сохранения лог-файла
        NodeList nodeListLog = docConfig.getElementsByTagName("log");
        for (int i = 0; i < nodeListLog.getLength(); i++) {
            Node node = nodeListLog.item(i);
            if (Node.ELEMENT_NODE == node.getNodeType()) {
                Element element = (Element) node;
                Node nodeEnabled = element.getElementsByTagName("enabled").item(0).getChildNodes().item(0);
                Node nodeFileName = element.getElementsByTagName("fileName").item(0).getChildNodes().item(0);
                if (nodeEnabled.getNodeValue().equals("true")) {
                    fileNameBasket = nodeFileName.getNodeValue();
                    File file = new File(fileNameBasket);
                    log.exportAsCSV(file);
                }
            }
        }


    }
}

