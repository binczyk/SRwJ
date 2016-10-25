package daniel.biniek.Controller;


public class ReadProduct {

    public String read(String object){
        StringBuilder stringBuilder = new StringBuilder();
        String [] value = object.split(";");
        stringBuilder.append("Prodcut: ");
        stringBuilder.append(value[0]);
        stringBuilder.append(" Buy: ");
        stringBuilder.append(value[1]);
        stringBuilder.append(" Sell: ");
        stringBuilder.append(value[2]);
        stringBuilder.append(" backend: ");
        stringBuilder.append(value[3]);

        return stringBuilder.toString();
    }

    public String readName(String prod) {
        return prod.split(";")[0];
    }
    public String readBuy(String prod) {
        return prod.split(";")[1];
    }
    public String readSell(String prod) {
        return prod.split(";")[2];
    }
    public String readBack(String prod) {
        return prod.split(";")[3];
    }
}
