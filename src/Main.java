import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String[] products = {"Хлеб", "Яблоки", "Молоко"};
        int[] prices = {50, 150, 100};
        int[] count = {0, 0, 0};
        Basket basket = new Basket(products, prices);
        basket.setCount(count);
        File file = new File("basket.txt");
        if (file.exists()) {
            basket = Basket.loadFromTxtFile(file);
            System.out.println("Существует сохраненная корзина");
            basket.printCart();
        } else {
            try {
                file.createNewFile();
                System.out.println("Нет сохраненной корзины");
            } catch (IOException ex){
                System.out.println(ex.getMessage());
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
       //     count[productNumber] += productCount;

            basket.saveTxt(file);
        }

        basket.printCart();
    }

}
