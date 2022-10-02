import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONObject;

public class ClientLog {
    private ArrayList<Integer> productNum;
    private ArrayList<Integer> amount;

    public ClientLog (){
    }

    public ClientLog(ArrayList<Integer> productNum, ArrayList<Integer> amount) {
        this.productNum = productNum;
        this.amount = amount;
    }

    //  Т.е. покупатель добавил покупку, то это действие должно быть там сохранено. Для этого создайте там метод
    public void log(int productNum, int amount) {
        this.productNum.add(productNum+1);
        this.amount.add(amount);
    }

    //      для сохранения всего журнала действия в файл в формате csv
//    productNum,amount
//          3,4
//          1,1
//          3,2
//          5,12

    public void exportAsCSV(File csvFile) {
        JSONObject obj = new JSONObject();
        for (int i = 0; i < this.productNum.size(); i++) {
            obj.put("productNum", this.productNum.get(i));
            obj.put("amount", this.amount.get(i));
        }
        try (FileWriter file = new FileWriter(csvFile)) {
            file.write(obj.toJSONString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
