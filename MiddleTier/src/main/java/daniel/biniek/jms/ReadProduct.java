package daniel.biniek.jms;


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
}
