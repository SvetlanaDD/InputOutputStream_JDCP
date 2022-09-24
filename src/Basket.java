import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Basket implements Serializable {
    private static final long serialVersionUID = 123L;
    private String[] products;
    private int[] prices;
    private int[] count;

    public Basket(String[] products, int[] prices) {
        this.products = products;
        this.prices = prices;
    }

    public Basket() {
        this.products = new String[]{"1", "2", "3"};
        this.prices = new int[]{1, 2, 3};
        this.count = new int[]{0, 0, 0};
    }

    // метод добавления amount штук продукта номер productNum в корзину;
    public void addToCart(int productNum, int amount) {
        this.count[productNum] += amount;
    }

    // метод вывода на экран покупательской корзины.
    public void printCart() {
        System.out.printf("%3s|%17s|%6s|%12s|%7s\n", " № ", "Название продукта", "Кол-во", "Цена, руб/шт", "В сумме");
        int sum = 0;
        for (int i = 0; i < products.length; i++) {
            if (count[i] != 0) {
                System.out.printf("%-3s|%-17s|%-6s|%-12s|%-7s\n", i + 1, products[i], count[i], prices[i], count[i] * prices[i]);
                sum += count[i] * prices[i];
            }
        }
        System.out.println("Итого: " + sum + " руб.");
    }

    public void printListProducts() {
        System.out.println("Список возможных продуктов для покупки");
        System.out.printf("%3s|%17s|%6s\n", " № ", "Название продукта", "Цена");
        for (int i = 0; i < products.length; i++) {
            System.out.printf("%-3s|%-17s|%-5s руб/шт\n", i + 1, products[i], prices[i]);
        }
    }

    // метод сохранения корзины в текстовый файл; использовать встроенные сериализаторы нельзя;
    public void saveTxt(File textFile) throws IOException {
        try (FileWriter writer = new FileWriter(textFile, false)) {
            for (String product : products) {
                writer.write(product + " ");
            }
            writer.append('\n');
            for (int price : prices) {
                writer.write(price + " ");
            }
            writer.append('\n');
            for (int count : count) {
                writer.write(count + " ");
            }
            writer.append('\n');
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    // статический(!) метод восстановления объекта корзины из текстового файла, в который ранее была она сохранена;
    public static Basket loadFromTxtFile(File textFile) {
        Basket basket = new Basket();
        try (BufferedReader reader = new BufferedReader(new FileReader(textFile))) {
            String str;
            List<String> listStr = new ArrayList<>();
            while ((str = reader.readLine()) != null) {
                listStr.add(str);
            }

            basket.products = listStr.get(0).split(" ");
            String[] priceStr = listStr.get(1).split(" ");
            String[] countStr = listStr.get(2).split(" ");

            for (int i = 0; i < priceStr.length; i++) {
                basket.prices[i] = Integer.parseInt(priceStr[i]);
            }
            for (int i = 0; i < countStr.length; i++) {
                basket.count[i] = Integer.parseInt(countStr[i]);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return basket;
    }

    public void saveBin(File file) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
        out.writeObject(this);
        out.close();
    }

    public static Basket loadFromBinFile(File file) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
        Basket basket = (Basket) in.readObject();
        in.close();
        return basket;
    }

    public String[] getProducts() {
        return products;
    }

    public void setProducts(String[] products) {
        this.products = products;
    }

    public int[] getPrices() {
        return prices;
    }

    public void setPrices(int[] prices) {
        this.prices = prices;
    }

    public int[] getCount() {
        return count;
    }

    public void setCount(int[] count) {
        this.count = count;
    }
}
